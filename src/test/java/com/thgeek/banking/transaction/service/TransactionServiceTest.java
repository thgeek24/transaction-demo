package com.thgeek.banking.transaction.service;

import com.thgeek.banking.transaction.constant.TransactionStatus;
import com.thgeek.banking.transaction.constant.TransactionType;
import com.thgeek.banking.transaction.domain.Transaction;
import com.thgeek.banking.transaction.dto.CreateTransactionReq;
import com.thgeek.banking.transaction.dto.UpdateTransactionReq;
import com.thgeek.banking.transaction.exception.DuplicateTransactionException;
import com.thgeek.banking.transaction.exception.ResourceNotFoundException;
import com.thgeek.banking.transaction.repository.AccountRepository;
import com.thgeek.banking.transaction.repository.TransactionRepository;
import com.thgeek.banking.transaction.service.impl.TransactionServiceImpl;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for TransactionService implementation
 *
 * @author Tao Hong
 * @version 1.0
 * @since 2025/03/18 23:28
 */
@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private Transaction transaction;
    private CreateTransactionReq createReq;
    private UpdateTransactionReq updateReq;

    @BeforeEach
    void setUp() {
        transaction = Transaction.builder()
                .id(1L)
                .trxReferenceNo("TRX123")
                .amount(BigDecimal.valueOf(100))
                .status(TransactionStatus.PENDING)
                .type(TransactionType.DEPOSIT)
                .description("Test transaction")
                .build();

        createReq = CreateTransactionReq.builder()
                .trxReferenceNo("TRX123")
                .amount(BigDecimal.valueOf(100))
                .type(TransactionType.DEPOSIT)
                .description("Test transaction")
                .build();

        updateReq = UpdateTransactionReq.builder()
                .description("Updated description")
                .failedReason("Updated failed reason")
                .build();
    }

    @Test
    void delete_ShouldSoftDeleteTransaction() {
        Transaction existingTransaction = Transaction.builder()
                .id(1L)
                .deleted(false)
                .build();

        when(transactionRepository.findById(1L))
                .thenReturn(Optional.of(existingTransaction));
        when(transactionRepository.save(any(Transaction.class)))
                .thenReturn(existingTransaction);

        transactionService.delete(1L);

        verify(transactionRepository).save(argThat(Transaction::isDeleted));
    }

    @Test
    void delete_ShouldIgnoreAlreadyDeletedTransaction() {
        Transaction existingTransaction = Transaction.builder()
                .id(1L)
                .deleted(true)
                .build();

        when(transactionRepository.findById(1L))
                .thenReturn(Optional.of(existingTransaction));

        transactionService.delete(1L);

        verify(transactionRepository, never()).save(any());
    }

    @Test
    void create_ShouldThrowDuplicateException() {
        when(transactionRepository.findByTrxReferenceNo(anyString()))
                .thenReturn(Optional.of(transaction));

        assertThrows(DuplicateTransactionException.class,
                () -> transactionService.create(createReq));
    }

    @Test
    void update_ShouldThrowNotFoundException() {
        when(transactionRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> transactionService.update(1L, updateReq));
    }
}