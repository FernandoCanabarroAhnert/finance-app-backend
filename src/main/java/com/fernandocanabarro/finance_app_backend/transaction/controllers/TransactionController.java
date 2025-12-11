package com.fernandocanabarro.finance_app_backend.transaction.controllers;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fernandocanabarro.finance_app_backend.transaction.dtos.BalanceReportDto;
import com.fernandocanabarro.finance_app_backend.transaction.dtos.MonthBalanceReportDto;
import com.fernandocanabarro.finance_app_backend.transaction.dtos.TransactionRequestDto;
import com.fernandocanabarro.finance_app_backend.transaction.dtos.TransactionResponseDto;
import com.fernandocanabarro.finance_app_backend.transaction.services.TransactionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    public Mono<ResponseEntity<Page<TransactionResponseDto>>> findAll(
        @RequestParam(defaultValue = "0") String page,
        @RequestParam(defaultValue = "10") String size,
        @RequestParam(defaultValue = "id") String sort,
        @RequestParam(defaultValue = "asc") String direction
    ) {
        return this.transactionService.findAll(page, size, sort, direction)
            .map(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<TransactionResponseDto>> findById(@PathVariable Long id) {
        return this.transactionService.findById(id)
            .map(ResponseEntity::ok);
    }

    @PostMapping
    public Mono<ResponseEntity<TransactionResponseDto>> create(@Valid @RequestBody TransactionRequestDto dto) {
        return this.transactionService.create(dto)
            .map(response -> ResponseEntity.status(201).body(response));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<TransactionResponseDto>> update(@PathVariable Long id, @Valid @RequestBody TransactionRequestDto dto) {
        return this.transactionService.update(id, dto)
            .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable Long id) {
        return this.transactionService.delete(id)
            .thenReturn(ResponseEntity.noContent().build());
    }

    @GetMapping("/balance-report")
    public Mono<ResponseEntity<BalanceReportDto>> getBalanceReport() {
        return this.transactionService.getBalanceReport()
            .map(ResponseEntity::ok);
    }

    @GetMapping("/monthly-report")
    public Flux<MonthBalanceReportDto> getMonthlyBalanceReport() {
        return this.transactionService.getMonthlyBalanceReport();
    }

}
