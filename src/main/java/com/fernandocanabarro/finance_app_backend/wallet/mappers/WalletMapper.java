package com.fernandocanabarro.finance_app_backend.wallet.mappers;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fernandocanabarro.finance_app_backend.wallet.dtos.WalletRequestDto;
import com.fernandocanabarro.finance_app_backend.wallet.dtos.WalletResponseDto;
import com.fernandocanabarro.finance_app_backend.wallet.entities.Wallet;

public class WalletMapper {

    public static WalletResponseDto toDto(Wallet wallet) {
        return new WalletResponseDto(wallet.getId(), wallet.getName(), wallet.getColor(), wallet.getBalance(), wallet.getCreatedAt(), wallet.getLastIncome(), wallet.getLastExpense());
    }

    public static Wallet toEntity(WalletRequestDto dto) {
        return new Wallet(null, null, dto.getName(), dto.getColor(), dto.getInitialBalance(), BigDecimal.ZERO, BigDecimal.ZERO, LocalDate.now());
    }

}
