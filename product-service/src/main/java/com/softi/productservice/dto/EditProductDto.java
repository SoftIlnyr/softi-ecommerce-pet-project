package com.softi.productservice.dto;

import com.softi.productservice.models.Category;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class EditProductDto {

    private String id;

    private String name;

    private String description;

    private Double price;

    private Map<String, String> attributes;

    private List<String> categories;

}
