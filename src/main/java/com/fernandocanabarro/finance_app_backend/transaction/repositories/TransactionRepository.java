package com.fernandocanabarro.finance_app_backend.transaction.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.fernandocanabarro.finance_app_backend.transaction.entities.Transaction;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TransactionRepository extends ReactiveCrudRepository<Transaction, Long> {
    Flux<Transaction> findByUserId(String userId, Pageable pageable);
    Mono<Long> countByUserId(String userId);
}
