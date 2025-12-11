package com.fernandocanabarro.finance_app_backend.transaction.repositories;

import com.fernandocanabarro.finance_app_backend.transaction.dtos.BalanceReportDto;
import com.fernandocanabarro.finance_app_backend.transaction.dtos.MonthBalanceReportDto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TransactionReportRepository {
    Mono<BalanceReportDto> getBalanceReport(String userId);
    Flux<MonthBalanceReportDto> getMonthlyBalanceReport(String userId);
}
