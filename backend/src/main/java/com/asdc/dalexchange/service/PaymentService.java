package com.asdc.dalexchange.service;

import com.asdc.dalexchange.dto.PaymentDTO;

import java.util.Map;

public interface PaymentService {
         String createPaymentIntent(Map<String,Object> requestBody);
         PaymentDTO savePayment(Map<String,Object> requestBody);
}
