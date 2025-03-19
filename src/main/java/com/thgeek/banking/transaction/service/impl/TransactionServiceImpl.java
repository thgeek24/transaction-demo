package com.thgeek.banking.transaction.service.impl;

import com.thgeek.banking.transaction.constant.TransactionStatus;
import com.thgeek.banking.transaction.domain.Account;
import com.thgeek.banking.transaction.domain.Transaction;
import com.thgeek.banking.transaction.dto.CreateTransactionReq;
import com.thgeek.banking.transaction.dto.TransactionQuery;
import com.thgeek.banking.transaction.dto.UpdateTransactionReq;
import com.thgeek.banking.transaction.exception.DuplicateTransactionException;
import com.thgeek.banking.transaction.exception.ResourceNotFoundException;
import com.thgeek.banking.transaction.repository.AccountRepository;
import com.thgeek.banking.transaction.repository.TransactionRepository;
import com.thgeek.banking.transaction.service.TransactionService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Implementation of Transaction Service
 *
 * @author Tao Hong
 * @version 1.0
 * @since 2025/03/18 15:36
 */
@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final EntityManager entityManager;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository,
                                  AccountRepository accountRepository,
                                  EntityManager entityManager) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.entityManager = entityManager;
    }

    @Override
    @Cacheable("transactions")
    public Page<Transaction> query(TransactionQuery query) {
        Transaction probe = Transaction.builder().trxReferenceNo(query.getTrxReferenceNo()).build();
        Example<Transaction> example = Example.of(probe);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));
        return transactionRepository.findAll(example, pageable);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Transaction create(CreateTransactionReq req) {
        log.info("Creating transaction with reference number: {}", req.getTrxReferenceNo());
        Optional<Transaction> existingTrx = transactionRepository.findByTrxReferenceNo(req.getTrxReferenceNo());
        if (existingTrx.isPresent()) {
            throw new DuplicateTransactionException("Transaction reference number "
                    + req.getTrxReferenceNo() + " already exists");
        }

        // Save initial PENDING transaction in a new transaction
        Transaction transaction = Transaction.builder()
                .trxReferenceNo(req.getTrxReferenceNo())
                .fromAccountNo(req.getFromAccountNo())
                .toAccountNo(req.getToAccountNo())
                .amount(req.getAmount())
                .status(TransactionStatus.PENDING)
                .type(req.getType())
                .description(req.getDescription())
                .build();
        saveWithRequiresNew(transaction);

        try {
            // Handle different transaction types
            switch (req.getType()) {
                case TRANSFER -> updateBalanceOnTransfer(transaction);
                case WITHDRAW -> updateBalanceOnWithdraw(transaction);
                case DEPOSIT -> updateBalanceOnDeposit(transaction);
                default -> throw new IllegalArgumentException("Unsupported transaction type: " + req.getType());
            }
            // If successful, mark as COMPLETED
            transaction.setStatus(TransactionStatus.COMPLETED);
            return transactionRepository.save(transaction);
        } catch (Exception e) {
            transaction.setStatus(TransactionStatus.FAILED);
            transaction.setFailedReason(e.getMessage());
            transactionRepository.save(transaction);
            log.error("Transaction with reference number {} failed: {}", req.getTrxReferenceNo(), e.getMessage());
            throw e;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Transaction update(Long id, UpdateTransactionReq req) {
        log.info("Updating transaction with id: {}", id);
        Transaction transaction = transactionRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Transaction not found with id: " + id));
        if (StringUtils.isNotBlank(req.getDescription())) {
            transaction.setDescription(req.getDescription());
        }
        if (StringUtils.isNotBlank(req.getFailedReason())) {
            transaction.setFailedReason(req.getFailedReason());
        }
        return transactionRepository.save(transaction);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        log.info("Deleting transaction with id: {}", id);
        Optional<Transaction> existingTrx = transactionRepository.findById(id);
        if (existingTrx.isEmpty()) {
            log.warn("Transaction not found with id: {}", id);
            return;
        }

        Transaction transaction = existingTrx.get();
        if (transaction.isDeleted()) {
            log.warn("Transaction already deleted with id: {}", id);
        } else {
            transaction.setDeleted(true);
            transactionRepository.save(transaction);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Transaction saveWithRequiresNew(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    private void updateBalanceOnTransfer(Transaction transaction) {
        // Fetch accounts
        Account fromAccount = accountRepository.findByAccountNo(transaction.getFromAccountNo()).orElseThrow(() ->
                new ResourceNotFoundException("From account not found"));
        Account toAccount = accountRepository.findByAccountNo(transaction.getToAccountNo()).orElseThrow(() ->
                new RuntimeException("To account not found"));

        // Lock accounts to prevent concurrent modifications
        Long fromAccountId = fromAccount.getId();
        Long toAccountId = toAccount.getId();
        // Lock smaller id first to avoid deadlock
        Long firstLockId = Math.min(fromAccountId, toAccountId);
        Long secondLockId = Math.max(fromAccountId, toAccountId);
        Account firstAccount = entityManager.find(Account.class, firstLockId, LockModeType.PESSIMISTIC_WRITE);
        Account secondAccount = entityManager.find(Account.class, secondLockId, LockModeType.PESSIMISTIC_WRITE);

        Account lockedFromAccount = (firstLockId.equals(fromAccountId)) ? firstAccount : secondAccount;
        Account lockedToAccount = (firstLockId.equals(toAccountId)) ? firstAccount : secondAccount;

        // Check balance
        if (lockedFromAccount.getBalance().compareTo(transaction.getAmount()) < 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }
        // Update balances
        lockedFromAccount.setBalance(lockedFromAccount.getBalance().subtract(transaction.getAmount()));
        lockedToAccount.setBalance(lockedToAccount.getBalance().add(transaction.getAmount()));
        accountRepository.save(lockedFromAccount);
        accountRepository.save(lockedToAccount);
    }

    private void updateBalanceOnWithdraw(Transaction transaction) {

    }

    private void updateBalanceOnDeposit(Transaction transaction) {

    }
}
