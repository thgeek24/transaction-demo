package com.thgeek.banking.transaction.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation for validating transaction accounts
 * in {@link com.thgeek.banking.transaction.dto.CreateTransactionReq}
 *
 * @author Tao Hong
 * @version 1.0
 * @since 2025/03/19 12:32
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TransactionAccountValidator.class)
public @interface ValidTransactionAccounts {
    String message() default "Invalid account numbers for transaction type";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
