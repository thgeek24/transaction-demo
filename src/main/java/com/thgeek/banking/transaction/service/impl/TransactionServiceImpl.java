package com.thgeek.banking.transaction.service.impl;

import com.thgeek.banking.transaction.domain.Transaction;
import com.thgeek.banking.transaction.dto.CreateTransactionReq;
import com.thgeek.banking.transaction.dto.TransactionQuery;
import com.thgeek.banking.transaction.dto.UpdateTransactionReq;
import com.thgeek.banking.transaction.service.TransactionService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 * Implementation of Transaction Service
 *
 * @author Tao Hong
 * @version 1.0
 * @since 2025/03/18 15:36
 */
@Service
public class TransactionServiceImpl implements TransactionService {
    @Override
    public Page<Transaction> query(TransactionQuery query) {
        return null;
    }

    @Override
    public Transaction create(CreateTransactionReq req) {
        return null;
    }

    @Override
    public Transaction update(Long id, UpdateTransactionReq req) {
        return null;
    }


    @Override
    public void delete(Long id) {

    }
}
