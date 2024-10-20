package com.asdc.dalexchange.mappers.impl;

import com.asdc.dalexchange.dto.ProductDTO;
import com.asdc.dalexchange.mappers.Mapper;
import com.asdc.dalexchange.model.Product;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Implementation of the Mapper interface for mapping between Product and ProductDTO.
 */
@Component
@Slf4j
@AllArgsConstructor
public class ProductMapperImpl implements Mapper<Product, ProductDTO> {

    private final ModelMapper modelMapper;


    /**
     * Maps a Product entity to a ProductDTO.
     *
     * @param product the Product entity to map from.
     * @return the mapped ProductDTO.
     */
    @Override
    public ProductDTO mapTo(Product product) {
        log.info("Mapping Product with id: {} to ProductDTO.", product.getProductId());
        ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
        log.debug("Mapped ProductDTO: {}", productDTO);
        return productDTO;
    }

    /**
     * Maps a ProductDTO to a Product entity.
     *
     * @param productDTO the ProductDTO to map from.
     * @return the mapped Product entity.
     */
    @Override
    public Product mapFrom(ProductDTO productDTO) {
        log.info("Mapping ProductDTO with id: {} to Product.", productDTO.getProductId());
        Product product = modelMapper.map(productDTO, Product.class);
        log.debug("Mapped Product: {}", product);
        return product;
    }
}
