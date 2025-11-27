package com.fernandocanabarro.finance_app_backend.transaction.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponseDto(Long id, Long walletId, Long categoryId, Integer type, String description, BigDecimal amount, LocalDateTime date) {

}
