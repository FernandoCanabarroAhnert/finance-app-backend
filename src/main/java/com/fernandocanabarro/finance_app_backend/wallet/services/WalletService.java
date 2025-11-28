package com.fernandocanabarro.finance_app_backend.wallet.services;

import org.springframework.data.domain.Page;

import com.fernandocanabarro.finance_app_backend.shared.dtos.ReportDto;
import com.fernandocanabarro.finance_app_backend.wallet.dtos.WalletRequestDto;
import com.fernandocanabarro.finance_app_backend.wallet.dtos.WalletResponseDto;
import com.fernandocanabarro.finance_app_backend.wallet.dtos.WalletUpdateDto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface WalletService {

    Mono<Page<WalletResponseDto>> findAll(String page, String size, String sort, String direction);
    Mono<WalletResponseDto> findById(Long id);
    Mono<WalletResponseDto> create(WalletRequestDto dto);
    Mono<WalletResponseDto> update(Long id, WalletUpdateDto dto);
    Mono<Void> delete(Long id);
    Flux<ReportDto> getWalletReport();

}
