package com.softi.productservice.rest;

import com.softi.productservice.dto.CategoryDto;
import com.softi.productservice.exception.EntityNotFoundException;
import com.softi.productservice.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoryRestController {

    private final CategoryService categoryService;

    @GetMapping("/api/categories")
    public ResponseEntity<List<CategoryDto>> findAll() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    @GetMapping("/api/categories/{id}")
    public ResponseEntity<CategoryDto> findCategoryById(@PathVariable("id") String id) {
        CategoryDto categoryDto = categoryService.findById(id);
        if (categoryDto == null) {
            throw new EntityNotFoundException("Category with id %s not found.".formatted(id));
        }
        return ResponseEntity.ok(categoryDto);
    }

    @PostMapping("/api/categories")
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto) {
        CategoryDto savedCategory = categoryService.save(categoryDto);
        return ResponseEntity.ok(savedCategory);
    }

    @PutMapping("/api/categories")
    public ResponseEntity<CategoryDto> updateCategory(@RequestBody CategoryDto categoryDto) {
        CategoryDto updatedCategory = categoryService.save(categoryDto);
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/api/categories/{id}")
    public ResponseEntity<String> deleteCategoryById(@PathVariable("id") String id) {
        categoryService.delete(id);
        return ResponseEntity.ok("Deleted Category with id %s.".formatted(id));
    }
}
