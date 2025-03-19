package com.thgeek.banking.transaction.service;

import com.thgeek.banking.transaction.constant.TransactionStatus;
import com.thgeek.banking.transaction.constant.TransactionType;
import com.thgeek.banking.transaction.domain.Account;
import com.thgeek.banking.transaction.domain.Transaction;
import com.thgeek.banking.transaction.dto.CreateTransactionReq;
import com.thgeek.banking.transaction.dto.TransactionQuery;
import com.thgeek.banking.transaction.dto.UpdateTransactionReq;
import com.thgeek.banking.transaction.exception.DuplicateTransactionException;
import com.thgeek.banking.transaction.exception.ResourceNotFoundException;
import com.thgeek.banking.transaction.repository.AccountRepository;
import com.thgeek.banking.transaction.repository.TransactionRepository;
import com.thgeek.banking.transaction.service.impl.TransactionServiceImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Example;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for TransactionService implementation.
 * Tests CRUD operations and edge cases for transaction management.
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
    private Account fromAccount;
    private Account toAccount;

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

        fromAccount = Account.builder()
                .id(1L)
                .accountNo("ACC001")
                .balance(BigDecimal.valueOf(1000))
                .build();

        toAccount = Account.builder()
                .id(2L)
                .accountNo("ACC002")
                .balance(BigDecimal.valueOf(500))
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
    void saveWithRequiresNew_ShouldSaveTransaction() {
        when(transactionRepository.save(any(Transaction.class)))
                .thenReturn(transaction);

        Transaction result = transactionService.saveWithRequiresNew(transaction);

        assertNotNull(result);
        verify(transactionRepository).save(transaction);
    }

    @Test
    void create_ShouldHandleTransferTransaction() {
        createReq.setType(TransactionType.TRANSFER);
        createReq.setFromAccountNo("ACC001");
        createReq.setToAccountNo("ACC002");
        createReq.setAmount(BigDecimal.valueOf(100));

        when(transactionRepository.findByTrxReferenceNo(anyString()))
                .thenReturn(Optional.empty());
        when(accountRepository.findByAccountNo("ACC001"))
                .thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByAccountNo("ACC002"))
                .thenReturn(Optional.of(toAccount));
        when(entityManager.find(eq(Account.class), anyLong(), eq(LockModeType.PESSIMISTIC_WRITE)))
                .thenReturn(fromAccount, toAccount);
        when(transactionRepository.save(any(Transaction.class)))
                .thenReturn(transaction);
        when(accountRepository.save(any(Account.class)))
                .thenReturn(fromAccount, toAccount);

        Transaction result = transactionService.create(createReq);

        assertNotNull(result);
        assertEquals(TransactionStatus.COMPLETED, result.getStatus());
        verify(accountRepository, times(2)).save(any(Account.class));
    }

    @Test
    void create_ShouldFailTransferOnInsufficientBalance() {
        createReq.setType(TransactionType.TRANSFER);
        createReq.setFromAccountNo("ACC001");
        createReq.setToAccountNo("ACC002");
        createReq.setAmount(BigDecimal.valueOf(2000));

        fromAccount.setBalance(BigDecimal.valueOf(100));

        when(transactionRepository.findByTrxReferenceNo(anyString()))
                .thenReturn(Optional.empty());
        when(accountRepository.findByAccountNo("ACC001"))
                .thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByAccountNo("ACC002"))
                .thenReturn(Optional.of(toAccount));
        when(entityManager.find(eq(Account.class), anyLong(), eq(LockModeType.PESSIMISTIC_WRITE)))
                .thenReturn(fromAccount, toAccount);

        assertThrows(IllegalArgumentException.class,
                () -> transactionService.create(createReq));

        verify(transactionRepository, times(2)).save(argThat(t ->
                t.getStatus() == TransactionStatus.FAILED));
    }

    @Test
    void query_ShouldReturnPageOfTransactions() {
        TransactionQuery query = TransactionQuery.builder()
                .trxReferenceNo("TRX123")
                .build();
        Page<Transaction> page = new PageImpl<>(Collections.singletonList(transaction));

        when(transactionRepository.findAll(any(Example.class), any(Pageable.class)))
                .thenReturn(page);

        Page<Transaction> result = transactionService.query(query);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(transactionRepository).findAll(any(Example.class), any(Pageable.class));
    }

    @Test
    void create_ShouldCreateTransaction() {
        when(transactionRepository.findByTrxReferenceNo(anyString()))
                .thenReturn(Optional.empty());
        when(transactionRepository.save(any(Transaction.class)))
                .thenReturn(transaction);

        Transaction result = transactionService.create(createReq);

        assertNotNull(result);
        assertEquals(transaction.getTrxReferenceNo(), result.getTrxReferenceNo());
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void create_ShouldThrowDuplicateException() {
        when(transactionRepository.findByTrxReferenceNo(anyString()))
                .thenReturn(Optional.of(transaction));

        assertThrows(DuplicateTransactionException.class,
                () -> transactionService.create(createReq));
    }

    @Test
    void update_ShouldUpdateTransaction() {
        when(transactionRepository.findById(anyLong()))
                .thenReturn(Optional.of(transaction));
        when(transactionRepository.save(any(Transaction.class)))
                .thenReturn(transaction);

        Transaction result = transactionService.update(1L, updateReq);

        assertNotNull(result);
        assertEquals(TransactionStatus.COMPLETED, result.getStatus());
        assertEquals("Updated description", result.getDescription());
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void update_ShouldThrowNotFoundException() {
        when(transactionRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> transactionService.update(1L, updateReq));
    }

    @Test
    void delete_ShouldDeleteTransaction() {
        when(transactionRepository.existsById(anyLong()))
                .thenReturn(true);
        doNothing().when(transactionRepository).deleteById(anyLong());

        transactionService.delete(1L);

        verify(transactionRepository).deleteById(1L);
    }

    @Test
    void delete_ShouldThrowNotFoundException() {
        when(transactionRepository.existsById(anyLong()))
                .thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> transactionService.delete(1L));
    }
}