package com.softi.productservice.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "products")
@CompoundIndexes({
        @CompoundIndex(def = "{'name': 'text', 'description': 'text'}")
})
public class Product {

    @Id
    private String id;

    private String name;

    private String description;

    private Double price;

    private Map<String, String> attributes;

    private List<String> categoryIds;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Boolean isActive;

}
