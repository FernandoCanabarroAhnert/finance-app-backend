package com.fernandocanabarro.finance_app_backend.wallet.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.fernandocanabarro.finance_app_backend.wallet.entities.Wallet;
import com.fernandocanabarro.finance_app_backend.wallet.projections.WalletSelectProjection;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface WalletRepository extends ReactiveCrudRepository<Wallet, Long>, WalletReportRepository {
    Flux<Wallet> findByUserId(String userId, Pageable pageable);
    Mono<Long> countByUserId(String userId);
    @Query(value = """
        SELECT id, name
        FROM wallets
        WHERE user_id = :userId        
    """)
    Flux<WalletSelectProjection> findWalletSelect(String userId);
}
