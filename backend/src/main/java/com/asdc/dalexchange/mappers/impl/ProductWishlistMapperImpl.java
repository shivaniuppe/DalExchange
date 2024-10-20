package com.asdc.dalexchange.mappers.impl;

import com.asdc.dalexchange.dto.ProductWishlistDTO;
import com.asdc.dalexchange.mappers.Mapper;
import com.asdc.dalexchange.model.ProductWishlist;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Implementation of the Mapper interface for mapping between ProductWishlist and ProductWishlistDTO.
 */
@Component
@Slf4j
public class ProductWishlistMapperImpl implements Mapper<ProductWishlist, ProductWishlistDTO> {

    private final ModelMapper modelMapper;

    /**
     * Constructor to initialize the ProductWishlistMapperImpl with a ModelMapper.
     *
     * @param modelMapper the ModelMapper to be used for mapping.
     */
    @Autowired
    public ProductWishlistMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        log.info("Initialized ProductWishlistMapperImpl with ModelMapper.");
    }

    /**
     * Maps a ProductWishlist entity to a ProductWishlistDTO.
     *
     * @param productWishlist the ProductWishlist entity to map from.
     * @return the mapped ProductWishlistDTO.
     */
    @Override
    public ProductWishlistDTO mapTo(ProductWishlist productWishlist) {
        log.info("Mapping ProductWishlist with id: {} to ProductWishlistDTO.", productWishlist.getWishlistId());
        ProductWishlistDTO productWishlistDTO = modelMapper.map(productWishlist, ProductWishlistDTO.class);
        log.debug("Mapped ProductWishlistDTO: {}", productWishlistDTO);
        return productWishlistDTO;
    }

    /**
     * Maps a ProductWishlistDTO to a ProductWishlist entity.
     *
     * @param productWishlistDTO the ProductWishlistDTO to map from.
     * @return the mapped ProductWishlist entity.
     */
    @Override
    public ProductWishlist mapFrom(ProductWishlistDTO productWishlistDTO) {
        log.info("Mapping ProductWishlistDTO with id: {} to ProductWishlist.", productWishlistDTO.getProductId());
        ProductWishlist productWishlist = modelMapper.map(productWishlistDTO, ProductWishlist.class);
        log.debug("Mapped ProductWishlist: {}", productWishlist);
        return productWishlist;
    }
}
