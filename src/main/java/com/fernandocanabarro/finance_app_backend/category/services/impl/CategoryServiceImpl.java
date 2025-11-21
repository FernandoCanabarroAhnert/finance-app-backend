package com.fernandocanabarro.finance_app_backend.category.services.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.fernandocanabarro.finance_app_backend.category.dtos.CategoryRequestDto;
import com.fernandocanabarro.finance_app_backend.category.dtos.CategoryResponseDto;
import com.fernandocanabarro.finance_app_backend.category.mappers.CategoryMapper;
import com.fernandocanabarro.finance_app_backend.category.repositories.CategoryRepository;
import com.fernandocanabarro.finance_app_backend.category.services.CategoryService;
import com.fernandocanabarro.finance_app_backend.shared.exceptions.NotFoundException;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public Mono<Page<CategoryResponseDto>> findAll(String page, String size, String sort, String direction) {
        Sort sortOrder = direction.equalsIgnoreCase("asc") ? Sort.by(sort).ascending() : Sort.by(sort).descending();
        Pageable pageable = PageRequest.of(Integer.parseInt(page), Integer.parseInt(size)).withSort(sortOrder);
        return this.categoryRepository.findAllBy(pageable)
            .collectList()
            .zipWith(this.categoryRepository.count())
            .map(content -> {
                return new PageImpl<>(
                    content.getT1().stream()
                        .map(CategoryMapper::toDto)
                        .toList(),
                    pageable,
                    content.getT2()
                );
            });
    }

    @Override
    public Mono<CategoryResponseDto> findById(Long id) {
        return this.categoryRepository.findById(id)
            .switchIfEmpty(Mono.error(() -> new NotFoundException("Category with id " + id + " not found")))
            .map(CategoryMapper::toDto);
    }

    @Override
    public Mono<CategoryResponseDto> create(CategoryRequestDto dto) {
        return this.categoryRepository.save(CategoryMapper.toEntity(dto))
            .map(CategoryMapper::toDto);
    }

    @Override
    public Mono<CategoryResponseDto> update(Long id, CategoryRequestDto dto) {
        return this.categoryRepository.findById(id)
            .switchIfEmpty(Mono.error(() -> new NotFoundException("Category with id " + id + " not found")))
            .flatMap(existingCategory -> {
                existingCategory.setName(dto.getName());
                existingCategory.setColor(dto.getColor());
                return this.categoryRepository.save(existingCategory);
            })
            .map(CategoryMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        return this.categoryRepository.findById(id)
            .switchIfEmpty(Mono.error(() -> new NotFoundException("Category with id " + id + " not found")))
            .flatMap(existingCategory -> this.categoryRepository.delete(existingCategory));
    }

    

}
