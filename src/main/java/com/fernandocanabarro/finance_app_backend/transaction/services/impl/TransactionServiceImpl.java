package com.fernandocanabarro.finance_app_backend.transaction.services.impl;

import java.math.BigDecimal;
import java.util.function.Function;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fernandocanabarro.finance_app_backend.category.repositories.CategoryRepository;
import com.fernandocanabarro.finance_app_backend.shared.exceptions.ForbiddenException;
import com.fernandocanabarro.finance_app_backend.shared.exceptions.NotFoundException;
import com.fernandocanabarro.finance_app_backend.shared.services.AuthService;
import com.fernandocanabarro.finance_app_backend.transaction.dtos.TransactionRequestDto;
import com.fernandocanabarro.finance_app_backend.transaction.dtos.TransactionResponseDto;
import com.fernandocanabarro.finance_app_backend.transaction.entities.Transaction;
import com.fernandocanabarro.finance_app_backend.transaction.enums.TransactionType;
import com.fernandocanabarro.finance_app_backend.transaction.mappers.TransactionMapper;
import com.fernandocanabarro.finance_app_backend.transaction.repositories.TransactionRepository;
import com.fernandocanabarro.finance_app_backend.transaction.services.TransactionService;
import com.fernandocanabarro.finance_app_backend.wallet.repositories.WalletRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    
    private final TransactionRepository transactionRepository;
    private final AuthService authService;
    private final WalletRepository walletRepository;
    private final CategoryRepository categoryRepository;
    
    @Override
    @Transactional(readOnly = true)
    public Mono<Page<TransactionResponseDto>> findAll(String page, String size, String sort, String direction) {
        return withUserId(userId -> {
            Sort sortOrder = direction.equalsIgnoreCase("asc") ? Sort.by(sort).ascending() : Sort.by(sort).descending();
            Pageable pageable = PageRequest.of(Integer.parseInt(page), Integer.parseInt(size)).withSort(sortOrder);
            return this.transactionRepository.findByUserId(userId, pageable)
                .collectList()
                .zipWith(this.transactionRepository.countByUserId(userId))
                .map(content -> {
                    return new PageImpl<>(
                        content.getT1().stream().map(TransactionMapper::toDto).toList(),
                        pageable,
                        content.getT2()
                    );
                });
        });
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<TransactionResponseDto> findById(Long id) {
        return withUserId(userId -> {
            return this.transactionRepository.findById(id)
                .switchIfEmpty(Mono.error(() -> new NotFoundException("Transaction with id " + id + " not found")))
                .filter(transaction -> transaction.getUserId().equals(userId))
                .switchIfEmpty(Mono.error(() -> new ForbiddenException("You do not have permission to access this transaction")))
                .map(TransactionMapper::toDto);
        });
    }

    @Override
    @Transactional
    public Mono<TransactionResponseDto> create(TransactionRequestDto dto) {
        return withUserId(userId -> {
            return Mono.zip(this.walletRepository.existsById(dto.getWalletId()), this.categoryRepository.existsById(dto.getCategoryId()))
                .flatMap(result -> {
                    boolean walletExists = result.getT1();
                    boolean categoryExists = result.getT2();
                    if (!walletExists) {
                        throw new NotFoundException("Wallet with id " + dto.getWalletId() + " not found");
                    }
                    if (!categoryExists) {
                        throw new NotFoundException("Category with id " + dto.getCategoryId() + " not found");
                    }
                    return this.transactionRepository.save(TransactionMapper.toEntity(dto, userId))
                        .flatMap(transaction -> 
                            this.walletRepository.findById(transaction.getWalletId())
                                .flatMap(wallet -> {
                                    BigDecimal amount = transaction.getAmount();
                                    if (transaction.getType().equals(TransactionType.INCOME.toString())) {
                                        wallet.increaseBalance(amount);
                                        wallet.increaseTotalIncome(amount);
                                    } else {
                                        wallet.decreaseBalance(amount);
                                        wallet.increaseTotalExpense(amount);
                                    }
                                    return this.walletRepository.save(wallet)
                                        .thenReturn(transaction);
                                })
                        ).map(TransactionMapper::toDto);
            });
        });
    }

    @Override
    @Transactional
    public Mono<TransactionResponseDto> update(Long id, TransactionRequestDto dto) {
        return withUserId(userId -> {
            return this.transactionRepository.findById(id)
                .switchIfEmpty(Mono.error(() -> new NotFoundException("Transaction with id " + id + " not found")))
                .filter(transaction -> transaction.getUserId().equals(userId))
                .switchIfEmpty(Mono.error(() -> new ForbiddenException("You do not have permission to access this transaction")))
                .flatMap(transaction -> {
                    BigDecimal oldAmount = transaction.getAmount();
                    String oldType = transaction.getType();
                    Long oldWalletId = transaction.getWalletId();
                    return Mono.zip(this.walletRepository.existsById(dto.getWalletId()), this.categoryRepository.existsById(dto.getCategoryId()))
                        .flatMap(result -> {
                            boolean walletExists = result.getT1();
                            boolean categoryExists = result.getT2();
                            if (!walletExists) {
                                throw new NotFoundException("Wallet with id " + dto.getWalletId() + " not found");
                            }
                            if (!categoryExists) {
                                throw new NotFoundException("Category with id " + dto.getCategoryId() + " not found");
                            }

                            Mono<Void> restoreCurrentWalletValue = this.walletRepository.findById(oldWalletId)
                                .flatMap(oldWallet -> {
                                    if (oldType.equals(TransactionType.INCOME.toString())) {
                                        oldWallet.decreaseBalance(oldAmount);
                                        oldWallet.decreaseTotalIncome(oldAmount);
                                    } else {
                                        oldWallet.increaseBalance(oldAmount);
                                        oldWallet.decreaseTotalExpense(oldAmount);
                                    }
                                    return this.walletRepository.save(oldWallet).then(Mono.empty());
                                });

                            transaction.setWalletId(dto.getWalletId());
                            transaction.setCategoryId(dto.getCategoryId());
                            transaction.setAmount(dto.getAmount());
                            transaction.setType(TransactionType.valueOf(dto.getType()).toString());
                            Mono<Transaction> update = this.transactionRepository.save(transaction)
                                .flatMap(updateTransaction -> {
                                    return this.walletRepository.findById(updateTransaction.getWalletId())
                                        .flatMap(wallet -> {
                                            BigDecimal amount = updateTransaction.getAmount();
                                            if (updateTransaction.getType().equals(TransactionType.INCOME.toString())) {
                                                wallet.increaseBalance(amount);
                                                wallet.increaseTotalIncome(amount);
                                            } else {
                                                wallet.decreaseBalance(amount);
                                                wallet.increaseTotalExpense(amount);
                                            }
                                            return this.walletRepository.save(wallet)
                                                .thenReturn(updateTransaction);
                                        });
                                });
                            return restoreCurrentWalletValue.then(update);
                        })
                        .map(TransactionMapper::toDto);
                });
            });
    }

    @Override
    @Transactional
    public Mono<Void> delete(Long id) {
        return withUserId(userId -> {
            return this.transactionRepository.findById(id)
                .switchIfEmpty(Mono.error(() -> new NotFoundException("Transaction with id " + id + " not found")))
                .filter(transaction -> transaction.getUserId().equals(userId))
                .switchIfEmpty(Mono.error(() -> new ForbiddenException("You do not have permission to access this transaction")))
                .then(this.transactionRepository.deleteById(id));
        });
    }

    private <T> Mono<T> withUserId(Function<String, Mono<T>> function) {
        return this.authService.getCurrentUserId().flatMap(function);
    }


}
