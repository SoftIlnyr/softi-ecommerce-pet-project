package com.softi.productservice.mapper;

import com.softi.productservice.dto.EditProductDto;
import com.softi.productservice.dto.ProductDto;
import com.softi.productservice.models.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductDto toDto(Product product);

    Product toEntity(EditProductDto productDto);

    @Mapping(target = "id", ignore = true)
    void copyProductDtoToProduct(EditProductDto productDto, @MappingTarget Product product);

    List<ProductDto> toDtoList(List<Product> products);
}
