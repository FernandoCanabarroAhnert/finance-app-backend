package com.fernandocanabarro.finance_app_backend.transaction.repositories.impl;

import java.math.BigDecimal;

import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;

import com.fernandocanabarro.finance_app_backend.transaction.dtos.BalanceReportDto;
import com.fernandocanabarro.finance_app_backend.transaction.dtos.MonthBalanceReportDto;
import com.fernandocanabarro.finance_app_backend.transaction.repositories.TransactionReportRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class TransactionReportRepositoryImpl implements TransactionReportRepository {

    private final DatabaseClient databaseClient;

    @Override
    public Mono<BalanceReportDto> getBalanceReport(String userId) {
        return databaseClient.sql("""
                    SELECT
                        COALESCE(
                            (SELECT SUM(w.balance)
                            FROM wallets w
                            WHERE w.user_id = :userId),
                        0) AS total_balance,
                        COALESCE(SUM(CASE WHEN t.type = 'INCOME' THEN t.amount ELSE 0 END), 0) AS total_incomes,
                        COALESCE(SUM(CASE WHEN t.type = 'EXPENSE' THEN t.amount ELSE 0 END), 0) AS total_expenses,
                        COALESCE(SUM(CASE WHEN t.type = 'INCOME' THEN t.amount ELSE 0 END), 0) -
                        COALESCE(SUM(CASE WHEN t.type = 'EXPENSE' THEN t.amount ELSE 0 END), 0) AS total_economy
                    FROM transactions t
                    WHERE t.user_id = :userId
                """)
                .bind("userId", userId)
                .map((row, meta) -> new BalanceReportDto(
                        row.get("total_balance", BigDecimal.class),
                        row.get("total_incomes", BigDecimal.class),
                        row.get("total_expenses", BigDecimal.class),
                        row.get("total_economy", BigDecimal.class)))
                .one();
    }

    @Override
    public Flux<MonthBalanceReportDto> getMonthlyBalanceReport(String userId) {
        return databaseClient.sql("""
                    SELECT
                        CASE 
                            WHEN EXTRACT(MONTH FROM date) = 1 THEN 'Jan'
                            WHEN EXTRACT(MONTH FROM date) = 2 THEN 'Fev'
                            WHEN EXTRACT(MONTH FROM date) = 3 THEN 'Mar'
                            WHEN EXTRACT(MONTH FROM date) = 4 THEN 'Abr'
                            WHEN EXTRACT(MONTH FROM date) = 5 THEN 'Mai'
                            WHEN EXTRACT(MONTH FROM date) = 6 THEN 'Jun'
                            WHEN EXTRACT(MONTH FROM date) = 7 THEN 'Jul'
                            WHEN EXTRACT(MONTH FROM date) = 8 THEN 'Ago'
                            WHEN EXTRACT(MONTH FROM date) = 9 THEN 'Set'
                            WHEN EXTRACT(MONTH FROM date) = 10 THEN 'Out'
                            WHEN EXTRACT(MONTH FROM date) = 11 THEN 'Nov'
                            WHEN EXTRACT(MONTH FROM date) = 12 THEN 'Dez'
                        END AS "month",
                        EXTRACT(MONTH FROM date) AS month_index,
                        SUM(
                            CASE 
                                WHEN type = 'INCOME' THEN amount
                                WHEN type = 'EXPENSE' THEN -amount
                                ELSE 0
                            END
                        ) AS balance
                    FROM transactions
                    WHERE user_id = :userId
                    GROUP BY 1, 2
                    ORDER BY month_index;
                """)
                .bind("userId", userId)
                .map((row, meta) -> new MonthBalanceReportDto(
                        row.get("month", String.class),
                        row.get("month_index", Integer.class),
                        row.get("balance", BigDecimal.class)))
                .all();
    }

}
