package com.asdc.dalexchange.mappers.impl;

import com.asdc.dalexchange.dto.ProductModerationDTO;
import com.asdc.dalexchange.mappers.Mapper;
import com.asdc.dalexchange.model.Product;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ProductModerationMapperImpl implements Mapper<Product, ProductModerationDTO> {

    private final ModelMapper modelMapper;

    @Override
    public ProductModerationDTO mapTo(Product product) {
        return this.modelMapper.map(product, ProductModerationDTO.class);
    }

    @Override
    public Product mapFrom(ProductModerationDTO productModerationDTO) {
        return this.modelMapper.map(productModerationDTO, Product.class);
    }
}
