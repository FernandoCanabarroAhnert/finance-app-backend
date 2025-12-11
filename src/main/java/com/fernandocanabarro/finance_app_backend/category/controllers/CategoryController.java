package com.fernandocanabarro.finance_app_backend.category.controllers;

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

import com.fernandocanabarro.finance_app_backend.category.dtos.CategoryRequestDto;
import com.fernandocanabarro.finance_app_backend.category.dtos.CategoryResponseDto;
import com.fernandocanabarro.finance_app_backend.category.services.CategoryService;
import com.fernandocanabarro.finance_app_backend.shared.dtos.ReportDto;
import com.fernandocanabarro.finance_app_backend.shared.dtos.SelectDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public Mono<ResponseEntity<Page<CategoryResponseDto>>> findAll(
        @RequestParam(defaultValue = "0") String page,
        @RequestParam(defaultValue = "10") String size,
        @RequestParam(defaultValue = "id") String sort,
        @RequestParam(defaultValue = "asc") String direction
    ) {
        return this.categoryService.findAll(page, size, sort, direction)
            .map(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<CategoryResponseDto>> findById(@PathVariable Long id) {
        return this.categoryService.findById(id)
            .map(ResponseEntity::ok);
    }

    @PostMapping
    public Mono<ResponseEntity<CategoryResponseDto>> create(@Valid @RequestBody CategoryRequestDto dto) {
        return this.categoryService.create(dto)
            .map(response -> ResponseEntity.status(201).body(response));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<CategoryResponseDto>> update(@PathVariable Long id, @Valid @RequestBody CategoryRequestDto dto) {
        return this.categoryService.update(id, dto)
            .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable Long id) {
        return this.categoryService.delete(id)
            .thenReturn(ResponseEntity.noContent().build());
    }

    @GetMapping("/report")
    public Flux<ReportDto> getCategoryReport() {
        return this.categoryService.getCategoryReport();
    }

    @GetMapping("/select")
    public Flux<SelectDto> findCategorySelect() {
        return this.categoryService.findCategorySelect();
    }

}
