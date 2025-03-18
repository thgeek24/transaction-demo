package com.thgeek.banking.transaction.domain;

import com.thgeek.banking.transaction.constant.TransactionStatus;
import com.thgeek.banking.transaction.constant.TransactionType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Transaction entity
 *
 * @author Tao Hong
 * @version 1.0
 * @since 2025/03/18 12:46
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "transaction", uniqueConstraints = {@UniqueConstraint(columnNames = "trx_reference_no")})
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "trx_reference_no", unique = true, nullable = false)
    private String trxReferenceNo;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Column
    private String description;
}
