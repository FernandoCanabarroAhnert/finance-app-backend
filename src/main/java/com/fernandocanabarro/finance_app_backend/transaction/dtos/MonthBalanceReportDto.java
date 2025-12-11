package com.fernandocanabarro.finance_app_backend.transaction.dtos;

import java.math.BigDecimal;

public record MonthBalanceReportDto(String month, Integer monthIndex, BigDecimal balance) {

}
