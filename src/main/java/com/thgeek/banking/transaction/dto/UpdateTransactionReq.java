package com.thgeek.banking.transaction.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for updating a transaction
 *
 * @author Tao Hong
 * @version 1.0
 * @since 2025/03/18 15:30
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTransactionReq {
    @Size(max = 255)
    private String description;

    @Size(max = 255)
    private String failedReason;
}
