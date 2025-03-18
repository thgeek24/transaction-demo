package com.thgeek.banking.transaction.exception;

/**
 * A custom Exception for Duplicate Transaction
 *
 * @author Tao Hong
 * @version 1.0
 * @since 2025/03/18 15:19
 */
public class DuplicateTransactionException extends RuntimeException {
    public DuplicateTransactionException(String message) {
        super(message);
    }
}
