package com.thgeek.banking.transaction.exception;

/**
 * A custom Exception for Resource Not Found
 *
 * @author Tao Hong
 * @version 1.0
 * @since 2025/03/18 15:17
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
