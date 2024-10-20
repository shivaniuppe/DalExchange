package com.asdc.dalexchange.mappers.impl;

import com.asdc.dalexchange.dto.ProductListingDTO;
import com.asdc.dalexchange.dto.TradeRequestDTO;
import com.asdc.dalexchange.mappers.Mapper;
import com.asdc.dalexchange.model.Product;
import com.asdc.dalexchange.model.TradeRequest;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Implementation of the Mapper interface for mapping between TradeRequest and TradeRequestDTO.
 */
@Component
@Slf4j
public class TradeRequestMapperImpl implements Mapper<TradeRequest, TradeRequestDTO> {

    private final ModelMapper modelMapper;
    private final Mapper<Product, ProductListingDTO> productListingMapper;

    /**
     * Constructor to initialize the TradeRequestMapperImpl with a ModelMapper and a ProductListingMapper.
     *
     * @param modelMapper the ModelMapper to be used for mapping.
     * @param productListingMapper the ProductListingMapper to be used for mapping product information.
     */
    public TradeRequestMapperImpl(ModelMapper modelMapper, Mapper<Product, ProductListingDTO> productListingMapper) {
        this.modelMapper = modelMapper;
        this.productListingMapper = productListingMapper;
        setupMappings();
    }

    /**
     * Sets up the custom mappings between TradeRequest and TradeRequestDTO.
     */
    private void setupMappings() {
        log.info("Setting up custom mappings between TradeRequest and TradeRequestDTO.");
        modelMapper.typeMap(TradeRequest.class, TradeRequestDTO.class).addMappings(mapper -> {
            mapper.map(src -> src.getSeller().getFullName(), TradeRequestDTO::setSellerName);
            mapper.map(src -> src.getSeller().getProfilePicture(), TradeRequestDTO::setSellerImage);
            mapper.map(src -> src.getSeller().getJoinedAt(), TradeRequestDTO::setSellerJoiningDate);
            mapper.map(src -> src.getSeller().getSellerRating(), TradeRequestDTO::setSellerRating);

            mapper.map(src -> src.getBuyer().getFullName(), TradeRequestDTO::setBuyerName);
            mapper.map(src -> src.getBuyer().getProfilePicture(), TradeRequestDTO::setBuyerImage);
            mapper.map(src -> src.getBuyer().getJoinedAt(), TradeRequestDTO::setBuyerJoiningDate);
            mapper.map(src -> src.getBuyer().getSellerRating(), TradeRequestDTO::setBuyerRating);
        });
    }

    /**
     * Maps a TradeRequest entity to a TradeRequestDTO.
     *
     * @param tradeRequest the TradeRequest entity to map from.
     * @return the mapped TradeRequestDTO.
     */
    @Override
    public TradeRequestDTO mapTo(TradeRequest tradeRequest) {
        log.info("Mapping TradeRequest to TradeRequestDTO with id: {}", tradeRequest.getRequestId());
        TradeRequestDTO tradeRequestDTO = modelMapper.map(tradeRequest, TradeRequestDTO.class);
        tradeRequestDTO.setProduct(productListingMapper.mapTo(tradeRequest.getProduct()));
        log.debug("Mapped TradeRequestDTO: {}", tradeRequestDTO);
        return tradeRequestDTO;
    }

    /**
     * Maps a TradeRequestDTO to a TradeRequest entity.
     *
     * @param tradeRequestDTO the TradeRequestDTO to map from.
     * @return the mapped TradeRequest entity.
     */
    @Override
    public TradeRequest mapFrom(TradeRequestDTO tradeRequestDTO) {
        log.info("Mapping TradeRequestDTO to TradeRequest with id: {}", tradeRequestDTO.getRequestId());
        TradeRequest tradeRequest = modelMapper.map(tradeRequestDTO, TradeRequest.class);
        log.debug("Mapped TradeRequest: {}", tradeRequest);
        return tradeRequest;
    }
}
