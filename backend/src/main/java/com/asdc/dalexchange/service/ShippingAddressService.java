package com.asdc.dalexchange.service;

import com.asdc.dalexchange.model.ShippingAddress;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface ShippingAddressService {
    ShippingAddress saveShippingAddress(Map<String,Object> requestBody);
}
