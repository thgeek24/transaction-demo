package com.thgeek.banking.transaction.service.impl;

import com.thgeek.banking.transaction.constant.TransactionStatus;
import com.thgeek.banking.transaction.constant.TransactionType;
import com.thgeek.banking.transaction.domain.Transaction;
import com.thgeek.banking.transaction.dto.CreateTransactionReq;
import com.thgeek.banking.transaction.dto.TransactionQuery;
import com.thgeek.banking.transaction.dto.UpdateTransactionReq;
import com.thgeek.banking.transaction.exception.DuplicateTransactionException;
import com.thgeek.banking.transaction.exception.ResourceNotFoundException;
import com.thgeek.banking.transaction.repository.TransactionRepository;
import com.thgeek.banking.transaction.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Page<Transaction> query(TransactionQuery query) {
        Transaction probe = Transaction.builder().trxReferenceNo(query.getTrxReferenceNo()).build();
        Example<Transaction> example = Example.of(probe);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));
        return transactionRepository.findAll(example, pageable);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Transaction create(CreateTransactionReq req) {
        Optional<Transaction> existingTrx = transactionRepository.findByTrxReferenceNo(req.getTrxReferenceNo());
        if (existingTrx.isPresent()) {
            throw new DuplicateTransactionException("Transaction reference number "
                    + req.getTrxReferenceNo() + " already exists");
        }
        Transaction transaction = Transaction.builder()
                .trxReferenceNo(req.getTrxReferenceNo())
                .amount(req.getAmount())
                .status(TransactionStatus.valueOf(req.getStatus()))
                .type(TransactionType.valueOf(req.getType()))
                .description(req.getDescription())
                .build();
        return transactionRepository.save(transaction);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Transaction update(Long id, UpdateTransactionReq req) {
        Transaction transaction = transactionRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Transaction not found with id: " + id));
        if (req.getStatus() != null) {
            transaction.setStatus(TransactionStatus.valueOf(req.getStatus()));
        }
        if (req.getDescription() != null) {
            transaction.setDescription(req.getDescription());
        }
        return transactionRepository.save(transaction);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        if (!transactionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Transaction not found with id " + id);
        }
        transactionRepository.deleteById(id);
    }
}
