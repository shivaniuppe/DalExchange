package com.asdc.dalexchange.mappers.impl;

import com.asdc.dalexchange.dto.EditProfileDTO;
import com.asdc.dalexchange.mappers.Mapper;
import com.asdc.dalexchange.model.User;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Implementation of the Mapper interface for mapping between User and EditProfileDTO.
 */
@Component
@Slf4j
public class EditProfileMapperImpl implements Mapper<User, EditProfileDTO> {

    private final ModelMapper modelMapper;

    /**
     * Constructor to initialize the EditProfileMapperImpl with a ModelMapper.
     *
     * @param modelMapper the ModelMapper to be used for mapping.
     */
    @Autowired
    public EditProfileMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        log.info("Initialized EditProfileMapperImpl with ModelMapper.");
    }

    /**
     * Maps a User entity to an EditProfileDTO.
     *
     * @param user the User entity to map from.
     * @return the mapped EditProfileDTO.
     */
    @Override
    public EditProfileDTO mapTo(User user) {
        log.info("Mapping User with id: {} to EditProfileDTO.", user.getUserId());
        EditProfileDTO editProfileDTO = modelMapper.map(user, EditProfileDTO.class);
        log.debug("Mapped EditProfileDTO: {}", editProfileDTO);
        return editProfileDTO;
    }

    /**
     * Maps an EditProfileDTO to a User entity.
     *
     * @param editProfileDTO the EditProfileDTO to map from.
     * @return the mapped User entity.
     */
    @Override
    public User mapFrom(EditProfileDTO editProfileDTO) {
        log.info("Mapping EditProfileDTO with id: {} to User.", editProfileDTO.getUserId());
        User user = modelMapper.map(editProfileDTO, User.class);
        log.debug("Mapped User: {}", user);
        return user;
    }
}
