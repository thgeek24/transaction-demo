package com.thgeek.banking.transaction.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Query DTO for accounts
 *
 * @author Tao Hong
 * @version 1.0
 * @since 2025/03/19 15:32
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountQuery {
    private String accountNo;

    @Min(0)
    private Integer page;

    @Min(1)
    @Max(5000)
    private Integer size;
}
