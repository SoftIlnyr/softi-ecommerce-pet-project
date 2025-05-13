package com.softi.productservice.models;

import com.mongodb.lang.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "categories")
public class Category {

    @Id
    private String id;

    private String name;

    private Category parentCategory;

}
