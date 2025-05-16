package com.softi.productservice.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ProductDto {

    private String id;

    private String name;

    private String description;

    private Double price;

    private Map<String, String> attributes;

    private List<String> categoryIds;

}
