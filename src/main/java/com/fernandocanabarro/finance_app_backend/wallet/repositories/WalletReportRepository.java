package com.fernandocanabarro.finance_app_backend.wallet.repositories;

import com.fernandocanabarro.finance_app_backend.shared.dtos.ReportDto;

import reactor.core.publisher.Flux;

public interface WalletReportRepository {
    Flux<ReportDto> getWalletReport(String userId);
}
