package com.softi.productservice.service;

import com.softi.productservice.dto.EditProductDto;
import com.softi.productservice.dto.ProductDto;
import com.softi.productservice.dto.ProductSearchCriteria;

import java.util.List;

public interface ProductService {

    ProductDto findById(String productId);

    ProductDto createProduct(EditProductDto productDto);

    ProductDto updateProduct(String productId, EditProductDto productDto);

    void deleteProduct(String productId);

    List<ProductDto> findByCriteria(ProductSearchCriteria productSearchCriteria);

    List<ProductDto> findByKeyword(String keyword);
}
