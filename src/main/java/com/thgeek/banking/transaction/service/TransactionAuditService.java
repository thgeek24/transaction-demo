package com.thgeek.banking.transaction.service;

import com.thgeek.banking.transaction.domain.Transaction;

/**
 * @author Tao Hong
 * @version 1.0
 * @since 2025/03/19 14:56
 */
public interface TransactionAuditService {
    /**
     * Saves a transaction record in a separate database transaction context
     *
     * @param transaction the transaction to save
     * @return the saved transaction
     * @apiNote uses {@link org.springframework.transaction.annotation.Propagation#REQUIRES_NEW}
     */
    Transaction recordTransaction(Transaction transaction);
}
