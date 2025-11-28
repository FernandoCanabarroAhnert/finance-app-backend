package com.fernandocanabarro.finance_app_backend.wallet.repositories.impl;

import java.math.BigDecimal;

import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;

import com.fernandocanabarro.finance_app_backend.shared.dtos.ReportDto;
import com.fernandocanabarro.finance_app_backend.wallet.repositories.WalletReportRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@Repository
@RequiredArgsConstructor
public class WalletReportRepositoryImpl implements WalletReportRepository {

    private final DatabaseClient databaseClient;

    @Override
    public Flux<ReportDto> getWalletReport(String userId) {
        return databaseClient.sql("""
                    SELECT id, name, color,
                        ROUND(
                            (SELECT COUNT(*) FROM transactions t WHERE t.wallet_id = w.id)::numeric 
                            / (SELECT COUNT(*) FROM transactions)::numeric * 100,
                            2
                        ) AS percentage
                    FROM wallets w
                    WHERE w.user_id = :userId
                """)
                .bind("userId", userId)
                .map((row, meta) -> new ReportDto(
                        row.get("id", Long.class),
                        row.get("name", String.class),
                        row.get("color", String.class),
                        row.get("percentage", BigDecimal.class)
                ))
                .all();
    }

    

}
