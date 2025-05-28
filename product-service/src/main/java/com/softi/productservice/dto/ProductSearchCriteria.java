package com.softi.productservice.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductSearchCriteria {

    private String name;
    private String description;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private List<String> categories;

}
