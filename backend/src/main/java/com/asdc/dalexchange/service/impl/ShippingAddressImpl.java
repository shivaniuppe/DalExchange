package com.asdc.dalexchange.service.impl;

import com.asdc.dalexchange.model.ShippingAddress;
import com.asdc.dalexchange.repository.ShippingRepository;
import com.asdc.dalexchange.service.ShippingAddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Implementation of the {@link ShippingAddressService} interface for managing shipping addresses.
 * Provides functionality for saving shipping address details.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ShippingAddressImpl implements ShippingAddressService {

    private final ShippingRepository shippingRepository;

    /**
     * Saves a new shipping address based on the provided request body.
     *
     * @param requestBody a map containing shipping address details with keys:
     *                    "billingName", "country", "line1", "line2", "city", "state", and "postalCode".
     * @return the saved {@link ShippingAddress} object with generated ID.
     */
    @Override
    public ShippingAddress saveShippingAddress(Map<String, Object> requestBody) {
        log.info("Saving shipping address with requestBody: {}", requestBody);

        ShippingAddress address = new ShippingAddress();
        address.setBillingName(requestBody.get("billingName").toString());
        address.setCountry(requestBody.get("country").toString());
        address.setLine1(requestBody.get("line1").toString());
        address.setLine2(requestBody.get("line2") != null ? requestBody.get("line2").toString() : null);
        address.setCity(requestBody.get("city").toString());
        address.setState(requestBody.get("state").toString());
        address.setPostalCode(requestBody.get("postalCode").toString());

        ShippingAddress savedAddress = shippingRepository.save(address);

        log.info("Shipping address saved with ID: {}", savedAddress.getAddressId());
        return savedAddress;
    }
}
