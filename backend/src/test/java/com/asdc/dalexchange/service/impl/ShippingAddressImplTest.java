package com.asdc.dalexchange.service.impl;
import com.asdc.dalexchange.model.ShippingAddress;
import com.asdc.dalexchange.repository.ShippingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


public class ShippingAddressImplTest {

    @InjectMocks
    private ShippingAddressImpl shippingAddressService;

    @Mock
    private ShippingRepository shippingRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveShippingAddress_withLine2() {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("billingName", "John Doe");
        requestBody.put("country", "USA");
        requestBody.put("line1", "123 Main St");
        requestBody.put("line2", "Apt 4B");
        requestBody.put("city", "Anytown");
        requestBody.put("state", "CA");
        requestBody.put("postalCode", "12345");

        ShippingAddress address = new ShippingAddress();
        address.setBillingName("John Doe");
        address.setCountry("USA");
        address.setLine1("123 Main St");
        address.setLine2("Apt 4B");
        address.setCity("Anytown");
        address.setState("CA");
        address.setPostalCode("12345");

        when(shippingRepository.save(address)).thenReturn(address);

        ShippingAddress result = shippingAddressService.saveShippingAddress(requestBody);
        assertEquals(address, result);
    }

    @Test
    public void testSaveShippingAddress_withoutLine2() {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("billingName", "John Doe");
        requestBody.put("country", "USA");
        requestBody.put("line1", "123 Main St");
        requestBody.put("city", "Anytown");
        requestBody.put("state", "CA");
        requestBody.put("postalCode", "12345");

        ShippingAddress address = new ShippingAddress();
        address.setBillingName("John Doe");
        address.setCountry("USA");
        address.setLine1("123 Main St");
        address.setLine2(null);
        address.setCity("Anytown");
        address.setState("CA");
        address.setPostalCode("12345");

        when(shippingRepository.save(address)).thenReturn(address);

        ShippingAddress result = shippingAddressService.saveShippingAddress(requestBody);
        assertEquals(address, result);
    }
}
