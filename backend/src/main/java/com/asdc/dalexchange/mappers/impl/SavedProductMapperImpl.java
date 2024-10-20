package com.asdc.dalexchange.mappers.impl;

import com.asdc.dalexchange.dto.SavedProductDTO;
import com.asdc.dalexchange.mappers.Mapper;
import com.asdc.dalexchange.model.Product;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SavedProductMapperImpl implements Mapper<Product, SavedProductDTO> {

    private final ModelMapper modelMapper;

    /**
     * Constructor to initialize the SavedProductMapperImpl with a ModelMapper.
     *
     * @param modelMapper the ModelMapper to be used for mapping.
     */
    @Autowired
    public SavedProductMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    /**
     * Initializes the mappings between Product and SavedProductDTO.
     * This method is called after the constructor is called to set up custom mappings.
     */
    @PostConstruct
    public void init() {
        log.info("Initializing custom mappings between Product and SavedProductDTO.");
        modelMapper.addMappings(new PropertyMap<Product, SavedProductDTO>() {
            @Override
            protected void configure() {
                map().setTitle(source.getTitle());
                map().setPrice(source.getPrice());
                map().setCategory(source.getCategory().getName());
                map().setProductCondition(source.getProductCondition());
                map().setUseDuration(source.getUseDuration());
                map().setQuantityAvailable(source.getQuantityAvailable());
            }
        });
        log.info("Custom mappings between Product and SavedProductDTO initialized.");
    }

    /**
     * Maps a Product entity to a SavedProductDTO.
     *
     * @param product the Product entity to map from.
     * @return the mapped SavedProductDTO.
     */
    @Override
    public SavedProductDTO mapTo(Product product) {
        log.info("Mapping Product to SavedProductDTO with id: {}", product.getProductId());
        SavedProductDTO savedProductDTO = modelMapper.map(product, SavedProductDTO.class);
        log.debug("Mapped SavedProductDTO: {}", savedProductDTO);
        return savedProductDTO;
    }

    /**
     * Mapping from SavedProductDTO to Product is not supported in this implementation.
     *
     * @param savedProductDTO the SavedProductDTO to map from.
     * @return nothing, always throws UnsupportedOperationException.
     * @throws UnsupportedOperationException as this operation is not supported.
     */
    @Override
    public Product mapFrom(SavedProductDTO savedProductDTO) {
        log.warn("Attempted to map SavedProductDTO to Product, which is not supported.");
        throw new UnsupportedOperationException("Mapping from SavedProductDTO to Product is not supported.");
    }
}
