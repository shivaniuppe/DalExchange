package com.asdc.dalexchange.mappers.impl;

import com.asdc.dalexchange.dto.ProfilePageDTO;
import com.asdc.dalexchange.mappers.Mapper;
import com.asdc.dalexchange.model.User;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Implementation of the  Mapper interface for mapping between User and ProfilePageDTO.
 */
@Component
public class ProfilePageMapperImpl implements Mapper<User, ProfilePageDTO> {

    private static final Logger logger = LoggerFactory.getLogger(ProfilePageMapperImpl.class);

    private final ModelMapper modelMapper;

    /**
     * Constructs a {@link ProfilePageMapperImpl} with the specified {@link ModelMapper}.
     *
     * @param modelMapper the {@link ModelMapper} used for mapping between {@link User} and {@link ProfilePageDTO}
     */
    @Autowired
    public ProfilePageMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    /**
     * Maps a {@link User} to a {@link ProfilePageDTO}.
     *
     * @param user the {@link User} to be mapped
     * @return the resulting {@link ProfilePageDTO}
     */
    @Override
    public ProfilePageDTO mapTo(User user) {
        logger.debug("Mapping User to ProfilePageDTO: {}", user);
        ProfilePageDTO dto = modelMapper.map(user, ProfilePageDTO.class);
        logger.debug("Mapped User to ProfilePageDTO: {}", dto);
        return dto;
    }

    /**
     * Mapping from {@link ProfilePageDTO} to {@link User} is not supported by this implementation.
     *
     * @param profilePageDTO the {@link ProfilePageDTO} to be mapped
     * @throws UnsupportedOperationException if called
     */
    @Override
    public User mapFrom(ProfilePageDTO profilePageDTO) {
        logger.error("Attempted to map from ProfilePageDTO to User, which is not supported.");
        throw new UnsupportedOperationException("Mapping from ProfilePageDTO to User is not supported.");
    }
}
