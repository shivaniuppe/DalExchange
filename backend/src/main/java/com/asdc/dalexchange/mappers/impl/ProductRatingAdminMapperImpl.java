package com.asdc.dalexchange.mappers.impl;

import com.asdc.dalexchange.dto.ProductRatingAdminDTO;
import com.asdc.dalexchange.mappers.Mapper;
import com.asdc.dalexchange.model.ProductRating;
import com.asdc.dalexchange.model.ProductRatingID;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductRatingAdminMapperImpl implements Mapper<ProductRating, ProductRatingAdminDTO> {

    @Autowired
    private ModelMapper modelMapper;

    public ProductRatingAdminMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public ProductRatingAdminDTO mapTo(ProductRating productRating) {
        ProductRatingAdminDTO dto = new ProductRatingAdminDTO();
        dto.setProductId(productRating.getId().getProductId());
        dto.setUserId(productRating.getId().getUserId());
        dto.setReview(productRating.getReview());
        dto.setRating(productRating.getRating());
        return dto;
    }

    @Override
    public ProductRating mapFrom(ProductRatingAdminDTO productRatingAdminDTO) {
        ProductRating productRating = new ProductRating();
        ProductRatingID id = new ProductRatingID(productRatingAdminDTO.getProductId(), productRatingAdminDTO.getUserId());
        productRating.setId(id);
        productRating.setReview(productRatingAdminDTO.getReview());
        productRating.setRating(productRatingAdminDTO.getRating());
        return productRating;
    }
}
