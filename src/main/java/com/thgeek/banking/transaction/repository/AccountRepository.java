package com.thgeek.banking.transaction.repository;

import com.thgeek.banking.transaction.domain.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Account repository
 *
 * @author Tao Hong
 * @version 1.0
 * @since 2025/03/19 08:02
 */
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountNo(String accountNo);

    Page<Account> findAll(Specification<Account> spec, Pageable pageable);
}
