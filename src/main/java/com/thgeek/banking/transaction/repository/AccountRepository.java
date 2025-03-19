package com.thgeek.banking.transaction.repository;

import com.thgeek.banking.transaction.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Account repository
 *
 * @author Tao Hong
 * @version 1.0
 * @since 2025/03/19 08:02
 */
public interface AccountRepository extends JpaRepository<Account, Long> {
}
