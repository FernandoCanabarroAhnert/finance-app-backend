package com.fernandocanabarro.finance_app_backend.wallet.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WalletResponseDto {
    private Long id;
    private String name;
    private String color;
    private BigDecimal balance;
    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private LocalDate createdAt;
}
