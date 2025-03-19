package com.thgeek.banking.transaction.exception.handler;

import com.thgeek.banking.transaction.dto.JsonResponse;
import com.thgeek.banking.transaction.exception.DuplicateTransactionException;
import com.thgeek.banking.transaction.exception.InsufficientBalanceException;
import com.thgeek.banking.transaction.exception.ResourceNotFoundException;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.List;

/**
 * Global Exception Handler
 *
 * @author Tao Hong
 * @version 1.0
 * @since 2025/03/18 22:35
 */
@RestControllerAdvice
public class GlobalAdviceHandler implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {
        if (body instanceof JsonResponse) {
            return body;
        }
        if (body instanceof ResponseEntity) {
            return body;
        }
        if (body instanceof Page) {
            return JsonResponse.successPage((Page<?>) body);
        }
        if (body instanceof List) {
            return JsonResponse.successMany((List<?>) body);
        }
        if (body == null) {
            return JsonResponse.success();
        }
        return JsonResponse.successOne(body);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<JsonResponse<?>> handleTransactionNotFoundException(ResourceNotFoundException ex) {
        JsonResponse<?> errorResponse = JsonResponse.error(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateTransactionException.class)
    public ResponseEntity<JsonResponse<?>> handleDuplicateTransactionException(DuplicateTransactionException ex) {
        JsonResponse<?> errorResponse = JsonResponse.error(HttpStatus.CONFLICT.value(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<JsonResponse<?>> handleInsufficientBalanceException(InsufficientBalanceException ex) {
        JsonResponse<?> errorResponse = JsonResponse.error(HttpStatus.UNPROCESSABLE_ENTITY.value(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<JsonResponse<?>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder builder = new StringBuilder();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName;
            if (error instanceof FieldError) {
                fieldName = ((FieldError) error).getField();
            } else {
                fieldName = error.getObjectName();
            }
            String errorMessage = error.getDefaultMessage();
            builder.append(fieldName).append(": ").append(errorMessage);
        });

        JsonResponse<?> errorResponse = JsonResponse.error(HttpStatus.BAD_REQUEST.value(), builder.toString());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<JsonResponse<?>> handleGenericException(Exception ex) {
        JsonResponse<?> errorResponse = JsonResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}