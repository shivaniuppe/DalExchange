package com.asdc.dalexchange.mappers.impl;

import com.asdc.dalexchange.dto.ProductRatingDTO;
import com.asdc.dalexchange.mappers.Mapper;
import com.asdc.dalexchange.model.ProductRating;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductRatingMapperImpl implements Mapper<ProductRating, ProductRatingDTO> {

    private final ModelMapper modelMapper;

    @Autowired
    public ProductRatingMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @PostConstruct
    public void init() {
        modelMapper.addMappings(new PropertyMap<ProductRating, ProductRatingDTO>() {
            @Override
            protected void configure() {
                map().setTitle(source.getProduct().getTitle());
            }
        });
    }

    @Override
    public ProductRatingDTO mapTo(ProductRating productRating) {
        return modelMapper.map(productRating, ProductRatingDTO.class);
    }

    @Override
    public ProductRating mapFrom(ProductRatingDTO productRatingDTO) {
        throw new UnsupportedOperationException("Mapping from ProductRatingDTO to ProductRating is not supported.");
    }
}
