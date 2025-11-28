package com.fernandocanabarro.finance_app_backend.category.repositories;

import com.fernandocanabarro.finance_app_backend.shared.dtos.ReportDto;

import reactor.core.publisher.Flux;

public interface CategoryReportRepository {
    Flux<ReportDto> getCategoryReport(String userId);
}
