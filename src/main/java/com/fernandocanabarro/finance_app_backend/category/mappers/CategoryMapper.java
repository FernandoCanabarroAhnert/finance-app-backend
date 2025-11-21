package com.fernandocanabarro.finance_app_backend.category.mappers;

import com.fernandocanabarro.finance_app_backend.category.dtos.CategoryRequestDto;
import com.fernandocanabarro.finance_app_backend.category.dtos.CategoryResponseDto;
import com.fernandocanabarro.finance_app_backend.category.entities.Category;

public class CategoryMapper {

    public static CategoryResponseDto toDto(Category category) {
        return new CategoryResponseDto(category.getId(), category.getName(), category.getColor());
    }

    public static Category toEntity(CategoryRequestDto dto) {
        return new Category(null, dto.getName(), dto.getColor());
    }

}
