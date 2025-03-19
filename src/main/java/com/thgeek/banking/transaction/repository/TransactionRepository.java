package com.thgeek.banking.transaction.repository;

import com.thgeek.banking.transaction.domain.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Transaction repository
 *
 * @author Tao Hong
 * @version 1.0
 * @since 2025/03/18 15:15
 */
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findByTrxReferenceNo(String trxReferenceNo);

    Page<Transaction> findAll(Specification<Transaction> spec, Pageable pageable);
}
