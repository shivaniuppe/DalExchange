package com.asdc.dalexchange.mappers.impl;

import com.asdc.dalexchange.dto.ProductDetailsDTO;
import com.asdc.dalexchange.mappers.Mapper;
import com.asdc.dalexchange.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Implementation of the Mapper interface for mapping between Product and ProductDetailsDTO.
 */
@Component
@Slf4j
public class ProductDetailsMapperImpl implements Mapper<Product, ProductDetailsDTO> {

    private final ModelMapper modelMapper;

    /**
     * Constructor to initialize the ProductDetailsMapperImpl with a ModelMapper.
     *
     * @param modelMapper the ModelMapper to be used for mapping.
     */
    @Autowired
    public ProductDetailsMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        log.info("Initialized ProductDetailsMapperImpl with ModelMapper.");
    }

    /**
     * Maps a Product entity to a ProductDetailsDTO.
     *
     * @param product the Product entity to map from.
     * @return the mapped ProductDetailsDTO with additional fields set.
     */
    @Override
    public ProductDetailsDTO mapTo(Product product) {
        log.info("Mapping Product with id: {} to ProductDetailsDTO.", product.getProductId());
        ProductDetailsDTO productDetailsDTO = modelMapper.map(product, ProductDetailsDTO.class);

        productDetailsDTO.setSellerJoiningDate(product.getSeller().getJoinedAt());
        if (product.getCategory() != null) {
            productDetailsDTO.setCategory(product.getCategory().getName());
        }

        log.debug("Mapped ProductDetailsDTO: {}", productDetailsDTO);
        return productDetailsDTO;
    }

    /**
     * Maps a ProductDetailsDTO to a Product entity.
     *
     * @param productDetailsDTO the ProductDetailsDTO to map from.
     * @return the mapped Product entity.
     * @throws UnsupportedOperationException if the operation is not supported.
     */
    @Override
    public Product mapFrom(ProductDetailsDTO productDetailsDTO) {
        log.error("Mapping from ProductDetailsDTO to Product is not supported.");
        throw new UnsupportedOperationException("Mapping from ProductDetailsDTO to Product is not supported.");
    }
}
