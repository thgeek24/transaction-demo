package com.thgeek.banking.transaction.service.impl;

import com.thgeek.banking.transaction.domain.Transaction;
import com.thgeek.banking.transaction.repository.TransactionRepository;
import com.thgeek.banking.transaction.service.TransactionAuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of TransactionAuditService
 *
 * @author Tao Hong
 * @version 1.0
 * @since 2025/03/19 14:47
 */
@Service
@Slf4j
public class TransactionAuditServiceImpl implements TransactionAuditService {
    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionAuditServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Transaction recordTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }
}