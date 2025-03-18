package com.thgeek.banking.transaction.exception.handler;

import com.thgeek.banking.transaction.exception.DuplicateTransactionException;
import com.thgeek.banking.transaction.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global Exception Handler
 *
 * @author Tao Hong
 * @version 1.0
 * @since 2025/03/18 22:35
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(DuplicateTransactionException.class)
    public ResponseEntity<String> handleDuplicateTransaction(DuplicateTransactionException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleNotFound(ResourceNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}