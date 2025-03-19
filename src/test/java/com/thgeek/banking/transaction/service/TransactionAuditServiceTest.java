package com.thgeek.banking.transaction.service;

import com.thgeek.banking.transaction.constant.TransactionStatus;
import com.thgeek.banking.transaction.domain.Transaction;
import com.thgeek.banking.transaction.repository.TransactionRepository;
import com.thgeek.banking.transaction.service.impl.TransactionAuditServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for TransactionAuditService implementation
 *
 * @author Tao Hong
 * @version 1.0
 * @since 2025/03/19 16:49
 */
@ExtendWith(MockitoExtension.class)
class TransactionAuditServiceTest {
    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionAuditServiceImpl transactionAuditService;

    @Test
    void recordTransaction_ShouldSaveTransaction() {
        Transaction transaction = Transaction.builder()
                .trxReferenceNo("TRX001")
                .fromAccountNo("ACC001")
                .amount(BigDecimal.valueOf(100))
                .status(TransactionStatus.PENDING)
                .build();

        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        Transaction result = transactionAuditService.recordTransaction(transaction);

        assertNotNull(result);
        assertEquals("TRX001", result.getTrxReferenceNo());
        verify(transactionRepository).save(transaction);
    }
}