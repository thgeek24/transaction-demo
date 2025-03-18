package com.thgeek.banking.transaction.service;

import com.thgeek.banking.transaction.domain.Transaction;
import com.thgeek.banking.transaction.dto.CreateTransactionReq;
import com.thgeek.banking.transaction.dto.TransactionQuery;
import com.thgeek.banking.transaction.dto.UpdateTransactionReq;
import org.springframework.data.domain.Page;

/**
 * Transaction Service
 *
 * @author Tao Hong
 * @version 1.0
 * @since 2025/03/17 23:22
 */
public interface TransactionService {
    /**
     * Query transactions with pagination
     *
     * @param query the query parameters
     * @return paginated list of transactions
     */
    Page<Transaction> query(TransactionQuery query);

    /**
     * Create a new transaction
     *
     * @param req the creation request
     * @return the created transaction
     */
    Transaction create(CreateTransactionReq req);

    /**
     * Update an existing transaction
     *
     * @param id  the transaction ID
     * @param req the update request
     * @return the updated transaction
     */
    Transaction update(Long id, UpdateTransactionReq req);

    /**
     * Delete a transaction by its ID
     *
     * @param id the transaction ID
     */
    void delete(Long id);
}