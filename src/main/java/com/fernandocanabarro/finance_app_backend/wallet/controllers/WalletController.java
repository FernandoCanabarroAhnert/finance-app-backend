package com.fernandocanabarro.finance_app_backend.wallet.controllers;

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

import com.fernandocanabarro.finance_app_backend.wallet.dtos.WalletRequestDto;
import com.fernandocanabarro.finance_app_backend.wallet.dtos.WalletResponseDto;
import com.fernandocanabarro.finance_app_backend.wallet.dtos.WalletUpdateDto;
import com.fernandocanabarro.finance_app_backend.wallet.services.WalletService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/wallets")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @GetMapping
    public Mono<ResponseEntity<Page<WalletResponseDto>>> findAll(
        @RequestParam(defaultValue = "0") String page,
        @RequestParam(defaultValue = "10") String size,
        @RequestParam(defaultValue = "id") String sort,
        @RequestParam(defaultValue = "asc") String direction
    ) {
        return this.walletService.findAll(page, size, sort, direction)
            .map(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<WalletResponseDto>> findById(@PathVariable Long id) {
        return this.walletService.findById(id)
            .map(ResponseEntity::ok);
    }

    @PostMapping
    public Mono<ResponseEntity<WalletResponseDto>> create(@Valid @RequestBody WalletRequestDto dto) {
        return this.walletService.create(dto)
            .map(response -> ResponseEntity.status(201).body(response));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<WalletResponseDto>> update(@PathVariable Long id, @Valid @RequestBody WalletUpdateDto dto) {
        return this.walletService.update(id, dto)
            .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable Long id) {
        return this.walletService.delete(id)
            .thenReturn(ResponseEntity.noContent().build());
    }

}
