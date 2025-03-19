package com.thgeek.banking.transaction.dto;

import com.thgeek.banking.transaction.constant.TransactionType;
import com.thgeek.banking.transaction.validation.ValidTransactionAccounts;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author Tao Hong
 * @version 1.0
 * @since 2025/03/18 15:30
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ValidTransactionAccounts
public class CreateTransactionReq {
    /**
     * Transaction Reference Number (TRN) - the unique identifier for each transaction
     */
    @NotBlank
    private String trxReferenceNo;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal amount;

    private String fromAccountNo;

    private String toAccountNo;

    @NotNull
    private TransactionType type;

    @Size(max = 255)
    private String description;
}
