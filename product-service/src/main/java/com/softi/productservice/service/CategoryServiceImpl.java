package com.softi.productservice.service;

import com.softi.productservice.dto.CategoryDto;
import com.softi.productservice.mapper.CategoryMapper;
import com.softi.productservice.models.Category;
import com.softi.productservice.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryDto> findAll() {
        List<Category> categories = categoryRepository.findAll();
        return categoryMapper.toDtoList(categories);
    }

    @Override
    public CategoryDto findById(String id) {
        return categoryMapper.toDto(categoryRepository.findById(id).orElse(null));
    }

    @Override
    public CategoryDto save(CategoryDto categoryDto) {
        Category parent = resolveParentCategory(categoryDto);
        Category category = categoryMapper.toEntity(categoryDto, parent);
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toDto(savedCategory);
    }

    private Category resolveParentCategory(CategoryDto categoryDto) {
        Category parent = null;
        if (categoryDto.getParentId() != null) {
            Optional<Category> optional = categoryRepository.findById(categoryDto.getParentId());
            if (optional.isPresent()) {
                parent = optional.get();
            }
        }
        return parent;
    }

    @Override
    public void delete(String id) {
        categoryRepository.deleteById(id);
    }
}
