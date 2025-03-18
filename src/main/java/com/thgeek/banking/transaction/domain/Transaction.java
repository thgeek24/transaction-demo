package com.thgeek.banking.transaction.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

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
@Entity
@Table(name = "transaction")
public class Transaction {
    @Id
    private Long id;

    private String referenceNo;

    private String type;

    private BigDecimal amount;

    private LocalDateTime timestamp;
}
