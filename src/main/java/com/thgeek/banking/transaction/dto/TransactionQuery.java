package com.thgeek.banking.transaction.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Query DTO for transactions
 *
 * @author Tao Hong
 * @version 1.0
 * @since 2025/03/18 15:23
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionQuery {
    private String trxReferenceNo;

    private String fromAccountNo;

    private String toAccountNo;

    @Min(0)
    private Integer page;

    @Min(1)
    @Max(5000)
    private Integer size;
}