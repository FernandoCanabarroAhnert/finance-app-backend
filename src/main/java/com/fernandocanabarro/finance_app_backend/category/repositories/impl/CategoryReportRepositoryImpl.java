package com.fernandocanabarro.finance_app_backend.category.repositories.impl;

import java.math.BigDecimal;

import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;

import com.fernandocanabarro.finance_app_backend.category.repositories.CategoryReportRepository;
import com.fernandocanabarro.finance_app_backend.shared.dtos.ReportDto;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@Repository
@RequiredArgsConstructor
public class CategoryReportRepositoryImpl implements CategoryReportRepository {

    private final DatabaseClient databaseClient;

    @Override
    public Flux<ReportDto> getCategoryReport(String userId) {
        return databaseClient.sql("""
                    SELECT id, name, color,
                        (SELECT COUNT(*) FROM transactions t WHERE t.category_id = c.id) AS count,
                        ROUND(
                            (SELECT COUNT(*) FROM transactions t WHERE t.category_id = c.id)::numeric 
                            / (SELECT COUNT(*) FROM transactions)::numeric * 100,
                            2
                        ) AS percentage
                    FROM categories c
                    WHERE c.user_id = :userId
                    AND (SELECT COUNT(*) FROM transactions t WHERE t.category_id = c.id) > 0
                """)
                .bind("userId", userId)
                .map((row, meta) -> new ReportDto(
                        row.get("id", Long.class),
                        row.get("name", String.class),
                        row.get("color", String.class),
                        row.get("count", Long.class),
                        row.get("percentage", BigDecimal.class)
                ))
                .all();
    }

    

}
