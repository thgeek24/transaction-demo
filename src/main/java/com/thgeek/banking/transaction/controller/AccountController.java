package com.thgeek.banking.transaction.controller;

import com.thgeek.banking.transaction.domain.Account;
import com.thgeek.banking.transaction.dto.AccountQuery;
import com.thgeek.banking.transaction.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Account Controller
 *
 * @author Tao Hong
 * @version 1.0
 * @since 2025/03/19 15:40
 */
@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public Page<Account> query(@Valid AccountQuery query) {
        return accountService.query(query);
    }
}
