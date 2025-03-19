package com.thgeek.banking.transaction.service.impl;

import com.thgeek.banking.transaction.constant.TransactionStatus;
import com.thgeek.banking.transaction.domain.Transaction;
import com.thgeek.banking.transaction.dto.CreateTransactionReq;
import com.thgeek.banking.transaction.dto.DepositTransactionReq;
import com.thgeek.banking.transaction.dto.TransactionQuery;
import com.thgeek.banking.transaction.dto.TransferTransactionReq;
import com.thgeek.banking.transaction.dto.UpdateTransactionReq;
import com.thgeek.banking.transaction.dto.WithdrawTransactionReq;
import com.thgeek.banking.transaction.exception.DuplicateTransactionException;
import com.thgeek.banking.transaction.exception.ResourceNotFoundException;
import com.thgeek.banking.transaction.repository.AccountRepository;
import com.thgeek.banking.transaction.repository.TransactionRepository;
import com.thgeek.banking.transaction.service.TransactionService;
import jakarta.persistence.EntityManager;
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

        // Handle different transaction types
        return switch (req.getType()) {
            case TRANSFER -> handleTransfer(TransferTransactionReq.builder()
                    .trxReferenceNo(req.getTrxReferenceNo())
                    .fromAccountNo(req.getFromAccountNo())
                    .toAccountNo(req.getToAccountNo())
                    .amount(req.getAmount())
                    .description(req.getDescription())
                    .build());
            case WITHDRAW -> handleWithdraw(WithdrawTransactionReq.builder()
                    .trxReferenceNo(req.getTrxReferenceNo())
                    .fromAccountNo(req.getFromAccountNo())
                    .amount(req.getAmount())
                    .description(req.getDescription())
                    .build());
            case DEPOSIT -> handleDeposit(DepositTransactionReq.builder()
                    .trxReferenceNo(req.getTrxReferenceNo())
                    .toAccountNo(req.getToAccountNo())
                    .amount(req.getAmount())
                    .description(req.getDescription())
                    .build());
        };
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

    private Transaction handleTransfer(TransferTransactionReq req) {
        if (StringUtils.isBlank(req.getFromAccountNo()) || StringUtils.isBlank(req.getToAccountNo())) {
            throw new IllegalArgumentException("From account number and to account number cannot be blank");
        }
        return null;
    }

    private Transaction handleWithdraw(WithdrawTransactionReq req) {
        if (StringUtils.isBlank(req.getFromAccountNo())) {
            throw new IllegalArgumentException("From account number cannot be blank");
        }
        return null;
    }

    private Transaction handleDeposit(DepositTransactionReq req) {
        if (StringUtils.isBlank(req.getToAccountNo())) {
            throw new IllegalArgumentException("To account number cannot be blank");
        }
        return null;
    }

    private Transaction saveTransaction(CreateTransactionReq req, TransactionStatus status) {
        Transaction transaction = Transaction.builder()
                .trxReferenceNo(req.getTrxReferenceNo())
                .fromAccountNo(req.getFromAccountNo())
                .toAccountNo(req.getToAccountNo())
                .amount(req.getAmount())
                .status(status)
                .type(req.getType())
                .description(req.getDescription())
                .build();
        return transactionRepository.save(transaction);
    }
}
