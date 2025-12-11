package com.fernandocanabarro.finance_app_backend.transaction.dtos;

import java.math.BigDecimal;

public record BalanceReportDto(BigDecimal totalBalance, BigDecimal totalIncomes, BigDecimal totalExpenses, BigDecimal totalEconomy) {

}
