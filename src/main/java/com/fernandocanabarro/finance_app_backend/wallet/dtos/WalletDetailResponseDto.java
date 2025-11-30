package com.fernandocanabarro.finance_app_backend.wallet.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fernandocanabarro.finance_app_backend.transaction.dtos.TransactionResponseDto;

public record WalletDetailResponseDto(Long id, String name, String color, BigDecimal balance, LocalDate createdAt, TransactionResponseDto lastIncomeTransaction, TransactionResponseDto lastExpenseTransaction) {
    
}
