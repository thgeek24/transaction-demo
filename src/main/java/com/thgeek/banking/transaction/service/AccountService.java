package com.thgeek.banking.transaction.service;

import com.thgeek.banking.transaction.domain.Account;
import com.thgeek.banking.transaction.dto.AccountQuery;
import org.springframework.data.domain.Page;

/**
 * Account Service
 *
 * @author Tao Hong
 * @version 1.0
 * @since 2025/03/19 15:34
 */
public interface AccountService {
    /**
     * Query accounts
     *
     * @param query the query parameters
     * @return paginated list of accounts
     */
    Page<Account> query(AccountQuery query);
}
