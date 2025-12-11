package com.fernandocanabarro.finance_app_backend.category.services;

import org.springframework.data.domain.Page;

import com.fernandocanabarro.finance_app_backend.category.dtos.CategoryRequestDto;
import com.fernandocanabarro.finance_app_backend.category.dtos.CategoryResponseDto;
import com.fernandocanabarro.finance_app_backend.shared.dtos.ReportDto;
import com.fernandocanabarro.finance_app_backend.shared.dtos.SelectDto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CategoryService {
    Mono<Page<CategoryResponseDto>> findAll(String page, String size, String sort, String direction);
    Mono<CategoryResponseDto> findById(Long id);
    Mono<CategoryResponseDto> create(CategoryRequestDto dto);
    Mono<CategoryResponseDto> update(Long id, CategoryRequestDto dto);
    Mono<Void> delete(Long id);
    Flux<ReportDto> getCategoryReport();
    Flux<SelectDto> findCategorySelect();
}
