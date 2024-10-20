package com.asdc.dalexchange.mappers.impl;

import com.asdc.dalexchange.dto.UserDTO;
import com.asdc.dalexchange.mappers.Mapper;
import com.asdc.dalexchange.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements Mapper<User, UserDTO> {

    @Autowired
    private ModelMapper modelMapper;

    public UserMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDTO mapTo(User user) {
        return this.modelMapper.map(user, UserDTO.class);
    }

    @Override
    public User mapFrom(UserDTO userDTO) {
        return this.modelMapper.map(userDTO, User.class);
    }
}
