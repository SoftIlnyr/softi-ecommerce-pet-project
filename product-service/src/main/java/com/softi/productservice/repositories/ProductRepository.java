package com.softi.productservice.repositories;

import com.softi.productservice.models.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {
}
