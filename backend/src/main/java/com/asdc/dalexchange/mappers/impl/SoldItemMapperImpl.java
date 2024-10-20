package com.asdc.dalexchange.mappers.impl;

import com.asdc.dalexchange.dto.SoldItemDTO;
import com.asdc.dalexchange.mappers.Mapper;
import com.asdc.dalexchange.model.SoldItem;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SoldItemMapperImpl implements Mapper<SoldItem, SoldItemDTO> {

    private final ModelMapper modelMapper;

    /**
     * Constructor to initialize the SoldItemMapperImpl with a ModelMapper.
     *
     * @param modelMapper the ModelMapper to be used for mapping.
     */
    @Autowired
    public SoldItemMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    /**
     * Initializes the mappings between SoldItem and SoldItemDTO.
     * This method is called after the constructor is called to set up custom mappings.
     */
    @PostConstruct
    public void init() {
        log.info("Initializing custom mappings between SoldItem and SoldItemDTO.");
        modelMapper.addMappings(new PropertyMap<SoldItem, SoldItemDTO>() {
            @Override
            protected void configure() {
                map().setTitle(source.getProduct().getTitle());
                map().setPrice(source.getProduct().getPrice());
            }
        });
        log.info("Custom mappings between SoldItem and SoldItemDTO initialized.");
    }

    /**
     * Maps a SoldItem entity to a SoldItemDTO.
     *
     * @param soldItem the SoldItem entity to map from.
     * @return the mapped SoldItemDTO.
     */
    @Override
    public SoldItemDTO mapTo(SoldItem soldItem) {
        log.info("Mapping SoldItem to SoldItemDTO with id: {}", soldItem.getSoldItemId());
        SoldItemDTO soldItemDTO = modelMapper.map(soldItem, SoldItemDTO.class);
        log.debug("Mapped SoldItemDTO: {}", soldItemDTO);
        return soldItemDTO;
    }

    /**
     * Mapping from SoldItemDTO to SoldItem is not supported in this implementation.
     *
     * @param soldItemDTO the SoldItemDTO to map from.
     * @return nothing, always throws UnsupportedOperationException.
     * @throws UnsupportedOperationException as this operation is not supported.
     */
    @Override
    public SoldItem mapFrom(SoldItemDTO soldItemDTO) {
        log.warn("Attempted to map SoldItemDTO to SoldItem, which is not supported.");
        throw new UnsupportedOperationException("Mapping from SoldItemDTO to SoldItem is not supported.");
    }
}
