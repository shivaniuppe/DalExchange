package com.asdc.dalexchange.mappers.impl;

import com.asdc.dalexchange.dto.PurchaseProductDTO;
import com.asdc.dalexchange.mappers.Mapper;
import com.asdc.dalexchange.model.OrderDetails;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Implementation of the  Mapper interface for mapping between OrderDetails and PurchaseProductDTO.
 */
@Component
public class PurchaseProductMapperImpl implements Mapper<OrderDetails, PurchaseProductDTO> {

    private static final Logger logger = LoggerFactory.getLogger(PurchaseProductMapperImpl.class);

    private final ModelMapper modelMapper;

    /**
     * Constructs a {@link PurchaseProductMapperImpl} with the specified {@link ModelMapper}.
     *
     * @param modelMapper the {@link ModelMapper} used for mapping between {@link OrderDetails} and {@link PurchaseProductDTO}
     */
    @Autowired
    public PurchaseProductMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    /**
     * Initializes the mapper by setting up custom mappings between {@link OrderDetails} and {@link PurchaseProductDTO}.
     * This method is called after the bean is constructed and is used to configure the {@link ModelMapper} with
     * specific property mappings.
     */
    @PostConstruct
    public void init() {
        logger.info("Initializing PurchaseProductMapperImpl with custom property mappings.");
        modelMapper.addMappings(new PropertyMap<OrderDetails, PurchaseProductDTO>() {
            @Override
            protected void configure() {
                map().setTitle(source.getProductId().getTitle());
                map().setCategory(source.getProductId().getCategory().getName());
            }
        });
        logger.info("Custom property mappings configured successfully.");
    }

    /**
     * Maps an {@link OrderDetails} object to a {@link PurchaseProductDTO}.
     *
     * @param orderDetails the {@link OrderDetails} object to be mapped
     * @return the resulting {@link PurchaseProductDTO}
     */
    @Override
    public PurchaseProductDTO mapTo(OrderDetails orderDetails) {
        logger.debug("Mapping OrderDetails to PurchaseProductDTO: {}", orderDetails);
        PurchaseProductDTO dto = modelMapper.map(orderDetails, PurchaseProductDTO.class);
        logger.debug("Mapped OrderDetails to PurchaseProductDTO: {}", dto);
        return dto;
    }

    /**
     * Mapping from {@link PurchaseProductDTO} to {@link OrderDetails} is not supported by this implementation.
     *
     * @param purchaseProductDTO the {@link PurchaseProductDTO} to be mapped
     * @throws UnsupportedOperationException if called
     */
    @Override
    public OrderDetails mapFrom(PurchaseProductDTO purchaseProductDTO) {
        logger.error("Attempted to map from PurchaseProductDTO to OrderDetails, which is not supported.");
        throw new UnsupportedOperationException("Mapping from PurchaseProductDTO to OrderDetails is not supported.");
    }
}
