package com.asdc.dalexchange.controller;

import com.asdc.dalexchange.dto.PaymentDTO;
import com.asdc.dalexchange.model.ShippingAddress;
import com.asdc.dalexchange.service.OrderService;
import com.asdc.dalexchange.service.PaymentService;
import com.asdc.dalexchange.service.ProductRatingService;
import com.asdc.dalexchange.service.ProductService;
import com.asdc.dalexchange.service.ShippingAddressService;
import com.asdc.dalexchange.service.SoldItemService;
import com.asdc.dalexchange.service.TradeRequestService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller for managing payment.
 */
@RestController
@RequestMapping("/payment")
@AllArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;
    private final ProductService productService;
    private final SoldItemService soldItemService;
    private final ProductRatingService productRatingService;
    private final OrderService orderService;
    private final TradeRequestService tradeRequestService;
    private final ShippingAddressService shippingAddressService;

    /**
     * Saves the details of a new order, including the shipping address.
     *
     * @param requestBody a {@link Map} containing order details including product ID and shipping address.
     * @return a {@link ResponseEntity} containing the order ID if successful, or an error status if an exception occurs.
     */
    @PostMapping("/save_order_details")
    public ResponseEntity<Long> saveOrderDetails(@RequestBody Map<String, Object> requestBody) {
        try {
            Long productId = Long.parseLong(requestBody.get("productId").toString());
            log.info("Saving order details for productId: {}", productId);

            ShippingAddress savedShippingAddress = shippingAddressService.saveShippingAddress(requestBody);
            Long orderId = orderService.saveNewOrder(savedShippingAddress, productId);

            log.info("Order created with orderId: {}", orderId);
            return ResponseEntity.status(HttpStatus.CREATED).body(orderId);
        } catch (Exception e) {
            log.error("Error saving order details: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(-1L);
        }
    }

    /**
     * Saves payment details for an order.
     *
     * @param requestBody a {@link Map} containing payment details.
     * @return a {@link ResponseEntity} containing the {@link PaymentDTO} if successful.
     */
    @PostMapping("/save_payment")
    public ResponseEntity<PaymentDTO> savePayment(@RequestBody Map<String, Object> requestBody) {
        log.info("Saving payment details");

        PaymentDTO payment = paymentService.savePayment(requestBody);
        log.info("Payment saved with ID: {}", payment.getPaymentId());

        return ResponseEntity.ok(payment);
    }

    /**
     * Creates a new payment intent for a transaction.
     *
     * @param requestBody a {@link Map} containing payment intent details.
     * @return a {@link ResponseEntity} containing a {@link Map} with the payment session ID.
     */
    @PostMapping("/create_payment_intent")
    public ResponseEntity<Map<String, Object>> createPaymentIntent(@RequestBody Map<String, Object> requestBody) {
        try {
            log.info("Creating payment intent");

            String sessionId = paymentService.createPaymentIntent(requestBody);
            Map<String, Object> response = new HashMap<>();
            response.put("sessionId", sessionId);

            log.info("Payment intent created with sessionId: {}", sessionId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            log.error("Error creating payment intent: {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Processes the success of a payment and updates related entities.
     *
     * @param requestBody a {@link Map} containing payment success details.
     * @return a {@link ResponseEntity} with a success message if successful.
     */
    @PutMapping("/payment_success")
    public ResponseEntity<String> changeRequestStatus(@RequestBody Map<String, Object> requestBody) {
        try {
            log.info("Processing payment success for requestBody: {}", requestBody);

            productService.markProductAsSold(requestBody);
            tradeRequestService.updateStatusByProduct(requestBody);
            soldItemService.saveSoldItem(requestBody);

            log.info("Payment success processed successfully");
            return ResponseEntity.status(HttpStatus.OK).body("success");
        } catch (Exception e) {
            log.error("Error processing payment success: {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Saves a rating for a product.
     *
     * @param requestBody a {@link Map} containing product rating details.
     * @return a {@link ResponseEntity} with a success message if the rating is saved successfully.
     */
    @PostMapping("/save_rating")
    public ResponseEntity<String> saveProductRating(@RequestBody Map<String, Object> requestBody) {
        log.info("Saving product rating");

        productRatingService.saveRating(requestBody);
        log.info("Product rating saved successfully");

        return ResponseEntity.status(HttpStatus.OK).body("saved successfully");
    }
}
