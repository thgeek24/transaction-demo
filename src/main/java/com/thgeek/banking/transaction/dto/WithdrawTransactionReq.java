package com.thgeek.banking.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Withdraw transaction DTO
 *
 * @author Tao Hong
 * @version 1.0
 * @since 2025/03/19 10:31
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawTransactionReq {
    private String trxReferenceNo;

    private String fromAccountNo;

    private BigDecimal amount;

    private String description;
}
