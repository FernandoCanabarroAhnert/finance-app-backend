package com.fernandocanabarro.finance_app_backend.category.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.fernandocanabarro.finance_app_backend.category.entities.Category;

import reactor.core.publisher.Flux;

@Repository
public interface CategoryRepository extends ReactiveCrudRepository<Category, Long> {
    Flux<Category> findAllBy(Pageable pageable);
}
