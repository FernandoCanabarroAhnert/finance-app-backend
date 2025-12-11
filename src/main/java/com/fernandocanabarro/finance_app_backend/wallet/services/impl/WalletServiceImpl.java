package com.fernandocanabarro.finance_app_backend.wallet.services.impl;

import java.util.Optional;
import java.util.function.Function;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fernandocanabarro.finance_app_backend.wallet.entities.Wallet;
import com.fernandocanabarro.finance_app_backend.wallet.mappers.WalletMapper;
import com.fernandocanabarro.finance_app_backend.shared.dtos.ReportDto;
import com.fernandocanabarro.finance_app_backend.shared.dtos.SelectDto;
import com.fernandocanabarro.finance_app_backend.shared.exceptions.BadRequestException;
import com.fernandocanabarro.finance_app_backend.shared.exceptions.ForbiddenException;
import com.fernandocanabarro.finance_app_backend.shared.exceptions.NotFoundException;
import com.fernandocanabarro.finance_app_backend.shared.services.AuthService;
import com.fernandocanabarro.finance_app_backend.transaction.enums.TransactionType;
import com.fernandocanabarro.finance_app_backend.transaction.repositories.TransactionRepository;
import com.fernandocanabarro.finance_app_backend.wallet.dtos.WalletDetailResponseDto;
import com.fernandocanabarro.finance_app_backend.wallet.dtos.WalletRequestDto;
import com.fernandocanabarro.finance_app_backend.wallet.dtos.WalletResponseDto;
import com.fernandocanabarro.finance_app_backend.wallet.dtos.WalletUpdateDto;
import com.fernandocanabarro.finance_app_backend.wallet.repositories.WalletRepository;
import com.fernandocanabarro.finance_app_backend.wallet.services.WalletService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {
    
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final AuthService authService;

    @Override
    @Transactional(readOnly = true)
    public Mono<Page<WalletResponseDto>> findAll(String page, String size, String sort, String direction) {
        return withUserId(userId -> {
                Sort sortOrder = direction.equalsIgnoreCase("asc") ? Sort.by(sort).ascending() : Sort.by(sort).descending();
                Pageable pageable = PageRequest.of(Integer.parseInt(page), Integer.parseInt(size)).withSort(sortOrder);
                return this.walletRepository.findByUserId(userId, pageable)
                    .collectList()
                    .zipWith(this.walletRepository.countByUserId(userId))
                    .map(content -> {
                        return new PageImpl<>(
                            content.getT1().stream()
                                .map(WalletMapper::toDto)
                                .toList(),
                            pageable,
                            content.getT2()
                        );
                    });
            });
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<WalletDetailResponseDto> findById(Long id) {
        return withUserId(userId -> {
            return this.walletRepository.findById(id)
                .switchIfEmpty(Mono.error(() -> new NotFoundException("Wallet with id " + id + " not found")))
                .filter(wallet -> wallet.getUserId().equals(userId))
                .switchIfEmpty(Mono.error(() -> new ForbiddenException("You do not have permission to access this wallet")))
                .flatMap(wallet -> {
                    return Mono.zip(
                        this.transactionRepository
                            .findWalletLastTransaction(TransactionType.INCOME.toString(), id)
                            .map(Optional::of)
                            .switchIfEmpty(Mono.just(Optional.empty())),
                        this.transactionRepository
                            .findWalletLastTransaction(TransactionType.EXPENSE.toString(), id)
                            .map(Optional::of)
                            .switchIfEmpty(Mono.just(Optional.empty()))
                    ).map(tuple -> {
                        return WalletMapper.toDetailDto(
                            wallet, 
                            tuple.getT1().orElse(null), 
                            tuple.getT2().orElse(null)
                        );
                    });
                });
        });
    };

    @Override
    @Transactional
    public Mono<WalletResponseDto> create(WalletRequestDto dto) {
        return withUserId(userId -> {
                Wallet wallet = WalletMapper.toEntity(dto);
                wallet.setUserId(userId);
                return this.walletRepository.save(wallet)
                    .map(WalletMapper::toDto);
            });
    }

    @Override
    @Transactional
    public Mono<WalletResponseDto> update(Long id, WalletUpdateDto dto) {
        return withUserId(userId -> {
                return this.walletRepository.findById(id)
                .switchIfEmpty(Mono.error(() -> new NotFoundException("Wallet with id " + id + " not found")))
                .filter(wallet -> wallet.getUserId().equals(userId))
                .switchIfEmpty(Mono.error(() -> new ForbiddenException("You do not have permission to update this wallet")))
                .flatMap(existingWallet -> {
                    existingWallet.setName(dto.getName());
                    existingWallet.setColor(dto.getColor());
                    return this.walletRepository.save(existingWallet);
                })
                .map(WalletMapper::toDto);
            });
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Mono<Void> delete(Long id) {
        return withUserId(userId -> {
            return this.walletRepository.findById(id)
                .switchIfEmpty(Mono.error(() -> new NotFoundException("Wallet with id " + id + " not found")))
                .filter(wallet -> wallet.getUserId().equals(userId))
                .switchIfEmpty(Mono.error(() -> new ForbiddenException("You do not have permission to delete this wallet")))
                .flatMap(existingWallet -> this.walletRepository.delete(existingWallet)
                    .onErrorMap(DataIntegrityViolationException.class, ex -> new BadRequestException(
                        "Wallet can't be deleted while it is associated with transactions"
                    ))
                );
        });
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<ReportDto> getWalletReport() {
        return withUserIdFlux(userId -> this.walletRepository.getWalletReport(userId));
    }

    @Override
    public Flux<SelectDto> findWalletSelect() {
        return withUserIdFlux(userId -> this.walletRepository.findWalletSelect(userId).map(proj -> new SelectDto(proj.getId(), proj.getName())));
    }

    private <T> Mono<T> withUserId(Function<String, Mono<T>> function) {
        return authService.getCurrentUserId().flatMap(function);
    }

    private <T> Flux<T> withUserIdFlux(Function<String, Flux<T>> function) {
        return authService.getCurrentUserId().flatMapMany(function);
    }

}
