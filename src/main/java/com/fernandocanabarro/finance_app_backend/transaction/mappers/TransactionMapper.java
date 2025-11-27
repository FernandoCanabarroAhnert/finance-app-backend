package com.fernandocanabarro.finance_app_backend.transaction.mappers;

import java.time.LocalDateTime;

import com.fernandocanabarro.finance_app_backend.transaction.dtos.TransactionRequestDto;
import com.fernandocanabarro.finance_app_backend.transaction.dtos.TransactionResponseDto;
import com.fernandocanabarro.finance_app_backend.transaction.entities.Transaction;
import com.fernandocanabarro.finance_app_backend.transaction.enums.TransactionType;

public class TransactionMapper {

    public static TransactionResponseDto toDto(Transaction transaction) {
        return new TransactionResponseDto(
            transaction.getId(),
            transaction.getWalletId(),
            transaction.getCategoryId(),
            TransactionType.valueOf(transaction.getType()).getCode(),
            transaction.getDescription(),
            transaction.getAmount(),
            transaction.getDate()
        );
    }

    public static Transaction toEntity(TransactionRequestDto dto, String userId) {
        return new Transaction(
            null, 
            dto.getWalletId(), 
            dto.getCategoryId(), 
            userId, 
            TransactionType.valueOf(dto.getType()).toString(), 
            dto.getDescription(), 
            dto.getAmount(), 
            LocalDateTime.now());
    }

}
