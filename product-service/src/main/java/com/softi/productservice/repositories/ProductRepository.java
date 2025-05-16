package com.softi.productservice.repositories;

import com.softi.productservice.models.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {

    @Query("{ '$text': { '$search': ?0 }, 'isActive': true }")
    List<Product> fullTextSearch(String keyword);

}
