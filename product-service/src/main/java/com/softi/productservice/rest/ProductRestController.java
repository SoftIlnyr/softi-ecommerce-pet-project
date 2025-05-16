package com.softi.productservice.rest;

import com.softi.productservice.dto.EditProductDto;
import com.softi.productservice.dto.ProductDto;
import com.softi.productservice.dto.ProductSearchCriteria;
import com.softi.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductRestController {

    private final ProductService productService;

    @GetMapping("/api/products")
    public ResponseEntity<List<ProductDto>> findByCriteria(
            @ModelAttribute ProductSearchCriteria productSearchCriteria) {
        List<ProductDto> productDtoList = productService.findByCriteria(productSearchCriteria);
        return ResponseEntity.ok(productDtoList);
    }

    @GetMapping("/api/products/{productId}")
    public ResponseEntity<ProductDto> findById(@PathVariable("productId") String productId) {
        ProductDto productDto = productService.findById(productId);
        return ResponseEntity.ok(productDto);
    }

    @PostMapping("/api/products")
    public ResponseEntity<ProductDto> createProduct(@RequestBody EditProductDto productDto) {
        ProductDto product = productService.createProduct(productDto);
        return ResponseEntity.ok(product);
    }

    @PutMapping("/api/products/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable("productId") String productId,
                                                    @RequestBody EditProductDto productDto) {
        ProductDto product = productService.updateProduct(productId, productDto);
        return ResponseEntity.ok(product);
    }

    @DeleteMapping("/api/products/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable("productId") String productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok("Product deleted successfully");
    }
}
