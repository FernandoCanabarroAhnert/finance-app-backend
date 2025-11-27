package com.fernandocanabarro.finance_app_backend.transaction.dtos;

import java.math.BigDecimal;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class TransactionRequestDto {

    @NotNull(message = "Wallet ID cannot be null")
    private Long walletId;
    @NotNull(message = "Category ID cannot be null")
    private Long categoryId;
    @Min(value = 1, message = "Type must be 1 (INCOME) or 2 (EXPENSE)")
    @Max(value = 2, message = "Type must be 1 (INCOME) or 2 (EXPENSE)")
    private Integer type;
    @NotBlank(message = "Description cannot be blank")
    private String description;
    @NotNull(message = "Amount cannot be null")
    private BigDecimal amount;

}
