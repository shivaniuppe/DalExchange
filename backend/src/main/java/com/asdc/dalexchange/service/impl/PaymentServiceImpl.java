package com.asdc.dalexchange.service.impl;

import com.asdc.dalexchange.dto.PaymentDTO;
import com.asdc.dalexchange.enums.OrderStatus;
import com.asdc.dalexchange.enums.PaymentStatus;
import com.asdc.dalexchange.mappers.impl.PaymentMapperImpl;
import com.asdc.dalexchange.model.OrderDetails;
import com.asdc.dalexchange.model.Payment;
import com.asdc.dalexchange.repository.OrderRepository;
import com.asdc.dalexchange.repository.PaymentRepository;
import com.asdc.dalexchange.service.PaymentService;
import com.asdc.dalexchange.service.TradeRequestService;
import com.asdc.dalexchange.specifications.PaymentSpecification;
import com.asdc.dalexchange.util.ResourceNotFoundException;
import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Implementation of the {@link PaymentService} interface for managing payments.
 * Provides functionality for creating payment intents with Stripe and saving payment details.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentMapperImpl paymentMapper;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final TradeRequestService tradeRequestService;

    @Value("${frontend.url}")
    private String frontendURL;

    @Value("${stripe.api.key}")
    private String stripeSecretKey;

    /**
     * Creates a payment intent using Stripe's checkout session API.
     * Generates a Stripe checkout session URL for the specified product and order.
     *
     * @param requestBody a map containing the product ID and order ID.
     * @return the Stripe checkout session ID.
     * @throws RuntimeException if an error occurs during session creation.
     */
    @Override
    public String createPaymentIntent(Map<String, Object> requestBody) {
        try {
            Long productId = Long.parseLong(requestBody.get("productId").toString());
            Long orderId = Long.parseLong(requestBody.get("orderId").toString());
            double productPrice = tradeRequestService.getApprovedTradeRequestAmount(productId);

            String successURL = frontendURL + "/payment/success?amount=" + productPrice
                    + "&productId=" + productId + "&paymentIntentId={CHECKOUT_SESSION_ID}&orderId=" + orderId;
            String failureURL = frontendURL + "/payment/fail";

            Stripe.apiKey = stripeSecretKey;

            SessionCreateParams params = SessionCreateParams.builder()
                    .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setCancelUrl(failureURL)
                    .setSuccessUrl(successURL)
                    .addLineItem(SessionCreateParams.LineItem.builder()
                            .setQuantity(1L)
                            .setPriceData(
                                    SessionCreateParams.LineItem.PriceData.builder()
                                            .setCurrency("cad")
                                            .setUnitAmount((long) (productPrice * 100))
                                            .setProductData(
                                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                            .setName("Dal Exchange")
                                                            .build())
                                            .build())
                            .build())
                    .build();

            log.info("Creating payment session with parameters: {}", params);
            Session session = Session.create(params);
            log.info("Payment session created successfully with ID: {}", session.getId());

            return session.getId();
        } catch (Exception e) {
            log.error("Failed to create payment intent: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create payment intent: " + e.getMessage(), e);
        }
    }

    /**
     * Saves the payment details for the specified order and updates the order status.
     * Updates the payment status to completed and the order status to delivered.
     *
     * @param requestBody a map containing the order ID.
     * @return a {@link PaymentDTO} containing the saved payment details.
     * @throws ResourceNotFoundException if the order with the specified ID is not found.
     */
    @Override
    public PaymentDTO savePayment(Map<String, Object> requestBody) {
        Long orderId = Long.parseLong(requestBody.get("orderId").toString());
        log.info("Saving payment for order ID: {}", orderId);

        Optional<OrderDetails> orderOptional = orderRepository.findById(orderId);
        if (!orderOptional.isPresent()) {
            log.error("Order with ID {} not found", orderId);
            throw new ResourceNotFoundException("Order not found");
        }

        OrderDetails order = orderOptional.get();
        order.setOrderStatus(OrderStatus.Delivered);
        orderRepository.save(order);
        log.info("Order status updated to DELIVERED for order ID: {}", orderId);

        Specification<Payment> spec = PaymentSpecification.hasPaymentId(orderId);
        List<Payment> payments = paymentRepository.findAll(spec);
        if (payments.isEmpty()) {
            log.warn("No payments found for order ID: {}", orderId);
            return new PaymentDTO();
        }

        Long PaymentId = order.getPayment().getPaymentId();
        Payment payment = paymentRepository.findById(PaymentId).get();
        payment.setPaymentStatus(PaymentStatus.completed);
        payment.setPaymentDate(LocalDateTime.now());
        Payment savedPayment = paymentRepository.save(payment);
        log.info("Payment status updated to COMPLETED for payment ID: {}", savedPayment.getPaymentId());

        return paymentMapper.mapTo(savedPayment);
    }
}
