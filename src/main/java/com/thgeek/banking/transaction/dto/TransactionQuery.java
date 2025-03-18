package com.thgeek.banking.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
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
}