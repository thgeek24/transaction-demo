package com.thgeek.banking.transaction.exception;

/**
 * A custom Exception for Insufficient Balance
 *
 * @author Tao Hong
 * @version 1.0
 * @since 2025/03/19 16:14
 */
public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(String message) {
        super(message);
    }
}
