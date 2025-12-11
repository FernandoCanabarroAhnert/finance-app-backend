package com.fernandocanabarro.finance_app_backend.category.services.impl;

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

import com.fernandocanabarro.finance_app_backend.category.dtos.CategoryRequestDto;
import com.fernandocanabarro.finance_app_backend.category.dtos.CategoryResponseDto;
import com.fernandocanabarro.finance_app_backend.category.entities.Category;
import com.fernandocanabarro.finance_app_backend.category.mappers.CategoryMapper;
import com.fernandocanabarro.finance_app_backend.category.repositories.CategoryRepository;
import com.fernandocanabarro.finance_app_backend.category.services.CategoryService;
import com.fernandocanabarro.finance_app_backend.shared.dtos.ReportDto;
import com.fernandocanabarro.finance_app_backend.shared.dtos.SelectDto;
import com.fernandocanabarro.finance_app_backend.shared.exceptions.BadRequestException;
import com.fernandocanabarro.finance_app_backend.shared.exceptions.ForbiddenException;
import com.fernandocanabarro.finance_app_backend.shared.exceptions.NotFoundException;
import com.fernandocanabarro.finance_app_backend.shared.services.AuthService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final AuthService authService;

    @Override
    @Transactional(readOnly = true)
    public Mono<Page<CategoryResponseDto>> findAll(String page, String size, String sort, String direction) {
        return withUserId(userId -> {
                Sort sortOrder = direction.equalsIgnoreCase("asc") ? Sort.by(sort).ascending() : Sort.by(sort).descending();
                Pageable pageable = PageRequest.of(Integer.parseInt(page), Integer.parseInt(size)).withSort(sortOrder);
                return this.categoryRepository.findByUserId(userId, pageable)
                    .collectList()
                    .zipWith(this.categoryRepository.countByUserId(userId))
                    .map(content -> {
                        return new PageImpl<>(
                            content.getT1().stream()
                                .map(CategoryMapper::toDto)
                                .toList(),
                            pageable,
                            content.getT2()
                        );
                    });
            });
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<CategoryResponseDto> findById(Long id) {
        return withUserId(userId -> {
            return this.categoryRepository.findById(id)
                .switchIfEmpty(Mono.error(() -> new NotFoundException("Category with id " + id + " not found")))
                .filter(category -> category.getUserId().equals(userId))
                .switchIfEmpty(Mono.error(() -> new ForbiddenException("You do not have permission to access this category")))
                .map(CategoryMapper::toDto);
        });
    }

    @Override
    @Transactional
    public Mono<CategoryResponseDto> create(CategoryRequestDto dto) {
        return withUserId(userId -> {
                Category category = CategoryMapper.toEntity(dto);
                category.setUserId(userId);
                return this.categoryRepository.save(category)
                    .map(CategoryMapper::toDto);
            });
    }

    @Override
    @Transactional
    public Mono<CategoryResponseDto> update(Long id, CategoryRequestDto dto) {
        return withUserId(userId -> {
                return this.categoryRepository.findById(id)
                .switchIfEmpty(Mono.error(() -> new NotFoundException("Category with id " + id + " not found")))
                .filter(category -> category.getUserId().equals(userId))
                .switchIfEmpty(Mono.error(() -> new ForbiddenException("You do not have permission to update this category")))
                .flatMap(existingCategory -> {
                    existingCategory.setName(dto.getName());
                    existingCategory.setColor(dto.getColor());
                    return this.categoryRepository.save(existingCategory);
                })
                .map(CategoryMapper::toDto);
            });
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Mono<Void> delete(Long id) {
        return withUserId(userId -> {
            return this.categoryRepository.findById(id)
                .switchIfEmpty(Mono.error(() -> new NotFoundException("Category with id " + id + " not found")))
                .filter(category -> category.getUserId().equals(userId))
                .switchIfEmpty(Mono.error(() -> new ForbiddenException("You do not have permission to delete this category")))
                .flatMap(existingCategory -> this.categoryRepository.delete(existingCategory)
                    .onErrorMap(DataIntegrityViolationException.class, ex -> new BadRequestException(
                        "Category can't be deleted while it is associated with transactions"
                    ))
                );
        });
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<ReportDto> getCategoryReport() {
        return withUserIdFlux(userId -> this.categoryRepository.getCategoryReport(userId));
    }

    @Override
    public Flux<SelectDto> findCategorySelect() {
        return withUserIdFlux(userId -> this.categoryRepository.findCategorySelect(userId).map(proj -> new SelectDto(proj.getId(), proj.getName())));
    }

    private <T> Mono<T> withUserId(Function<String, Mono<T>> function) {
        return authService.getCurrentUserId().flatMap(function);
    }

    private <T> Flux<T> withUserIdFlux(Function<String, Flux<T>> function) {
        return authService.getCurrentUserId().flatMapMany(function);
    }

}
