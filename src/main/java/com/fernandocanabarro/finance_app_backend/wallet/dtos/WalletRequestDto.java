package com.fernandocanabarro.finance_app_backend.wallet.dtos;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;

@Getter
public class WalletRequestDto {

    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Color is required")
    private String color;
    @NotNull(message = "Initial balance is required")
    @PositiveOrZero(message = "Initial balance must be zero or positive")
    private BigDecimal initialBalance;

}
