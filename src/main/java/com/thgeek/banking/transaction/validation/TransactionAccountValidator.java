package com.thgeek.banking.transaction.validation;

import com.thgeek.banking.transaction.dto.CreateTransactionReq;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

/**
 * Create validator implementation
 *
 * @author Tao Hong
 * @version 1.0
 * @since 2025/03/19 12:33
 */
public class TransactionAccountValidator implements ConstraintValidator<ValidTransactionAccounts, CreateTransactionReq> {
    @Override
    public boolean isValid(CreateTransactionReq req, ConstraintValidatorContext context) {
        if (req.getType() == null) {
            return false;
        }

        return switch (req.getType()) {
            case TRANSFER -> StringUtils.isNotBlank(req.getFromAccountNo())
                    && StringUtils.isNotBlank(req.getToAccountNo());
            case WITHDRAW -> StringUtils.isNotBlank(req.getFromAccountNo())
                    && StringUtils.isBlank(req.getToAccountNo());
            case DEPOSIT -> StringUtils.isBlank(req.getFromAccountNo())
                    && StringUtils.isNotBlank(req.getToAccountNo());
        };
    }
}