package com.fernandocanabarro.finance_app_backend.transaction.services;

import org.springframework.data.domain.Page;

import com.fernandocanabarro.finance_app_backend.transaction.dtos.BalanceReportDto;
import com.fernandocanabarro.finance_app_backend.transaction.dtos.MonthBalanceReportDto;
import com.fernandocanabarro.finance_app_backend.transaction.dtos.TransactionRequestDto;
import com.fernandocanabarro.finance_app_backend.transaction.dtos.TransactionResponseDto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TransactionService {
    Mono<Page<TransactionResponseDto>> findAll(String page, String size, String sort, String direction);
    Mono<TransactionResponseDto> findById(Long id);
    Mono<TransactionResponseDto> create(TransactionRequestDto dto);
    Mono<TransactionResponseDto> update(Long id, TransactionRequestDto dto);
    Mono<Void> delete(Long id);
    Mono<BalanceReportDto> getBalanceReport();
    Flux<MonthBalanceReportDto> getMonthlyBalanceReport();
}
