package com.thgeek.banking.transaction.service.impl;

import com.thgeek.banking.transaction.domain.Account;
import com.thgeek.banking.transaction.dto.AccountQuery;
import com.thgeek.banking.transaction.repository.AccountRepository;
import com.thgeek.banking.transaction.service.AccountService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 * Implementation of AccountService
 *
 * @author Tao Hong
 * @version 1.0
 * @since 2025/03/19 15:38
 */
@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Page<Account> query(AccountQuery query) {
        int page = query.getPage() == null ? 0 : query.getPage();
        int size = query.getSize() == null ? 20 : query.getSize();
        Pageable pageable = PageRequest.of(page, size);

        Specification<Account> spec = (root, criteriaQuery, criteriaBuilder) -> {
            if (StringUtils.isNotBlank(query.getAccountNo())) {
                return criteriaBuilder.equal(root.get("accountNo"), query.getAccountNo());
            }
            return null;
        };

        return accountRepository.findAll(spec, pageable);
    }
}
