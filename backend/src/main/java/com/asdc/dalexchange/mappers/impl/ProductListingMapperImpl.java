package com.asdc.dalexchange.mappers.impl;

import com.asdc.dalexchange.dto.ProductListingDTO;
import com.asdc.dalexchange.mappers.Mapper;
import com.asdc.dalexchange.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

/**
 * Implementation of the Mapper interface for mapping between Product and ProductListingDTO.
 */
@Component
@Slf4j
public class ProductListingMapperImpl implements Mapper<Product, ProductListingDTO> {

    private final ModelMapper modelMapper;

    /**
     * Constructor to initialize the ProductListingMapperImpl with a ModelMapper.
     *
     * @param modelMapper the ModelMapper to be used for mapping.
     */
    public ProductListingMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        setupMappings();
    }

    /**
     * Sets up the custom mappings between Product and ProductListingDTO.
     */
    private void setupMappings() {
        log.info("Setting up custom mappings between Product and ProductListingDTO.");
        TypeMap<Product, ProductListingDTO> propertyMapper = this.modelMapper.createTypeMap(Product.class, ProductListingDTO.class);
        propertyMapper.addMappings(mapper -> mapper.map(
                src -> src.getCategory().getName(), ProductListingDTO::setCategoryName
        ));
    }

    /**
     * Maps a Product entity to a ProductListingDTO.
     *
     * @param product the Product entity to map from.
     * @return the mapped ProductListingDTO.
     */
    @Override
    public ProductListingDTO mapTo(Product product) {
        log.info("Mapping Product to ProductListingDTO with id: {}", product.getProductId());
        ProductListingDTO productListingDTO = this.modelMapper.map(product, ProductListingDTO.class);
        log.debug("Mapped ProductListingDTO: {}", productListingDTO);
        return productListingDTO;
    }

    /**
     * Maps a ProductListingDTO to a Product entity.
     *
     * @param productListingDTO the ProductListingDTO to map from.
     * @return the mapped Product entity.
     */
    @Override
    public Product mapFrom(ProductListingDTO productListingDTO) {
        log.info("Mapping ProductListingDTO to Product with id: {}", productListingDTO.getProductId());
        Product product = this.modelMapper.map(productListingDTO, Product.class);
        log.debug("Mapped Product: {}", product);
        return product;
    }
}
