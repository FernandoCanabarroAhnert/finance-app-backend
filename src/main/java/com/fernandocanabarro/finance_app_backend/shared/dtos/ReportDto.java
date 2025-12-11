package com.fernandocanabarro.finance_app_backend.shared.dtos;

import java.math.BigDecimal;

public record ReportDto(Long id, String name, String color, Long count, BigDecimal percentage) {

}
