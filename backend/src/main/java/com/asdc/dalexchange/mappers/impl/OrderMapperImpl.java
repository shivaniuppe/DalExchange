package com.asdc.dalexchange.mappers.impl;

import com.asdc.dalexchange.dto.OrderDTO;
import com.asdc.dalexchange.mappers.Mapper;
import com.asdc.dalexchange.model.OrderDetails;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderMapperImpl implements Mapper<OrderDetails, OrderDTO> {

    @Autowired
    private ModelMapper modelMapper;
    private final TypeMap<OrderDetails, OrderDTO> propertyMapper;

    public OrderMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.propertyMapper = this.modelMapper.createTypeMap(OrderDetails.class, OrderDTO.class);
        this.propertyMapper.addMappings(
                mapper -> {
                    mapper.map(src -> src.getBuyer().getFullName(), OrderDTO::setFullName);
                    mapper.map(src -> src.getProductId().getTitle(), OrderDTO::setProductTitle);
                }
        );
    }

    @Override
    public OrderDTO mapTo(OrderDetails orderDetails) {
        return this.modelMapper.map(orderDetails, OrderDTO.class);
    }

    @Override
    public OrderDetails mapFrom(OrderDTO orderDetailsDTO) {
        return this.modelMapper.map(orderDetailsDTO, OrderDetails.class);
    }
}
