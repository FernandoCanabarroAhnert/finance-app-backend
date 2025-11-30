package com.fernandocanabarro.finance_app_backend.wallet.mappers;

import java.time.LocalDate;

import com.fernandocanabarro.finance_app_backend.transaction.entities.Transaction;
import com.fernandocanabarro.finance_app_backend.transaction.mappers.TransactionMapper;
import com.fernandocanabarro.finance_app_backend.wallet.dtos.WalletDetailResponseDto;
import com.fernandocanabarro.finance_app_backend.wallet.dtos.WalletRequestDto;
import com.fernandocanabarro.finance_app_backend.wallet.dtos.WalletResponseDto;
import com.fernandocanabarro.finance_app_backend.wallet.entities.Wallet;

public class WalletMapper {

    public static WalletResponseDto toDto(Wallet wallet) {
        return new WalletResponseDto(wallet.getId(), wallet.getName(), wallet.getColor(), wallet.getBalance(), wallet.getCreatedAt());
    }

    public static Wallet toEntity(WalletRequestDto dto) {
        return new Wallet(null, null, dto.getName(), dto.getColor(), dto.getInitialBalance(), LocalDate.now());
    }

    public static WalletDetailResponseDto toDetailDto(Wallet wallet, Transaction lastIncomeTransaction, Transaction lastExpenseTransaction) {
        return new WalletDetailResponseDto(
            wallet.getId(),
            wallet.getName(), wallet.getColor(), wallet.getBalance(), wallet.getCreatedAt(),
            lastIncomeTransaction != null ? TransactionMapper.toDto(lastIncomeTransaction) : null,
            lastExpenseTransaction != null ? TransactionMapper.toDto(lastExpenseTransaction) : null);
    }

}
