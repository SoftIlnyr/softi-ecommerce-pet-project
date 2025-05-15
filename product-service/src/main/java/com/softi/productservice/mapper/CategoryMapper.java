package com.softi.productservice.mapper;

import com.softi.productservice.dto.CategoryDto;
import com.softi.productservice.models.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    List<CategoryDto> toDtoList(List<Category> categoryList);

    @Mapping(source = "category", target = "parentId", qualifiedByName = "mapParentCategory")
    CategoryDto toDto(Category category);

    @Named("mapParentCategory")
    static String mapParentCategory(Category category) {
        if (category == null) {
            return null;
        }
        if (category.getParentCategory() == null) {
            return null;
        }
        return category.getParentCategory().getId();
    }

    @Mapping(target = "parentCategory", ignore = true)
    Category toEntity(CategoryDto categoryDto);

    default Category toEntity(CategoryDto categoryDto, Category parentCategory) {
        if (categoryDto == null) {
            return null;
        }
        Category category = toEntity(categoryDto);
        category.setParentCategory(parentCategory);
        return category;
    }

}
