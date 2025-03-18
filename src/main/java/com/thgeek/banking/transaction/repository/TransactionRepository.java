package com.thgeek.banking.transaction.repository;

import com.thgeek.banking.transaction.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Transaction repository
 *
 * @author Tao Hong
 * @version 1.0
 * @since 2025/03/18 15:15
 */
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
