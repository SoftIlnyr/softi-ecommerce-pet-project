package com.softi.productservice.service;

import com.softi.common.exception.EntityNotFoundException;
import com.softi.productservice.dto.EditProductDto;
import com.softi.productservice.dto.ProductDto;
import com.softi.productservice.dto.ProductSearchCriteria;
import com.softi.productservice.kafka.ProductsKafkaProducerService;
import com.softi.productservice.mapper.ProductMapper;
import com.softi.productservice.models.Category;
import com.softi.productservice.models.Product;
import com.softi.productservice.repositories.CategoryRepository;
import com.softi.productservice.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;
    private final MongoTemplate mongoTemplate;
    private final ProductsKafkaProducerService kafkaProducerService;

    @Override
    public ProductDto findById(String productId) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new EntityNotFoundException("Product not found (id %s)".formatted(productId)));
        return productMapper.toDto(product);
    }

    @Override
    public ProductDto createProduct(EditProductDto productDto) {
        Product product = productMapper.toEntity(productDto);
        List<Category> categories = categoryRepository.findAllById(product.getCategoryIds());
        if (categories.isEmpty()) {
            throw new EntityNotFoundException("Categories not found (id %s)".formatted(productDto.getId()));
        }
        LocalDateTime currentDateTime = LocalDateTime.now();
        product.setCreatedAt(currentDateTime);
        product.setUpdatedAt(currentDateTime);
        product.setIsActive(true);
        product.setCategoryIds(categories.stream().map(Category::getId).toList());
        Product savedEntity = productRepository.save(product);
        kafkaProducerService.createEventProductCreated(product.getId(), currentDateTime);
        return productMapper.toDto(savedEntity);
    }

    @Override
    public ProductDto updateProduct(String productId, EditProductDto productDto) {
        Product productToUpdate = productRepository.findById(productId).orElseThrow(
                () -> new EntityNotFoundException("Product not found (id %s)".formatted(productId)));
        productMapper.copyProductDtoToProduct(productDto, productToUpdate);
        productToUpdate.setUpdatedAt(LocalDateTime.now());
        Product updatedEntity = productRepository.save(productToUpdate);
        return productMapper.toDto(updatedEntity);
    }

    @Override
    public void deleteProduct(String productId) {
        Product productToDelete = productRepository.findById(productId).orElseThrow(
                () -> new EntityNotFoundException("Product not found (id %s)".formatted(productId)));
        productToDelete.setIsActive(false);
        productRepository.save(productToDelete);
    }

    @Override
    public List<ProductDto> findByCriteria(ProductSearchCriteria criteria) {
        Query query = new Query();

        if (criteria.getName() != null) {
            query.addCriteria(Criteria.where("name").regex(criteria.getName(), "i"));
        }
        if (criteria.getCategories() != null && !criteria.getCategories().isEmpty())  {
            query.addCriteria(Criteria.where("category").in(criteria.getCategories()));
        }
        if (criteria.getMinPrice() != null) {
            query.addCriteria(Criteria.where("price").gte(criteria.getMinPrice()));
        }
        if (criteria.getMaxPrice() != null) {
            query.addCriteria(Criteria.where("price").lte(criteria.getMaxPrice()));
        }
        query.addCriteria(Criteria.where("isActive").is(true));

        List<Product> products = mongoTemplate.find(query, Product.class);
        return productMapper.toDtoList(products);
    }

    @Override
    public List<ProductDto> findByKeyword(String keyword) {
        List<Product> products = productRepository.fullTextSearch(keyword);
        return productMapper.toDtoList(products);
    }
}
