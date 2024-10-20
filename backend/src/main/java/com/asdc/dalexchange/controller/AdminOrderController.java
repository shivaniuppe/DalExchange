package com.asdc.dalexchange.controller;

import com.asdc.dalexchange.dto.OrderDTO;
import com.asdc.dalexchange.model.OrderDetails;
import com.asdc.dalexchange.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Controller class to handle admin order related requests.
 */
@RestController
@RequestMapping("/admin/orders")
@Slf4j
@AllArgsConstructor
public class AdminOrderController {

    private final OrderService orderService;

    /**
     * Retrieves all orders.
     *
     * @return List of OrderDTO containing order details.
     */
    @GetMapping("/all")
    public List<OrderDTO> getOrders() {
        log.info("Fetching all orders");
        List<OrderDTO> orders = orderService.getAllOrders();
        log.info("All orders fetched successfully");
        return orders;
    }

    /**
     * Retrieves order details by ID.
     *
     * @param orderId the ID of the order to retrieve.
     * @return OrderDTO containing order details.
     */
    @GetMapping("/orderDetails/{orderId}")
    public OrderDTO getOrderById(@PathVariable int orderId) {
        log.info("Fetching order details for order ID: {}", orderId);
        OrderDTO order = orderService.getOrderById(orderId);
        log.info("Order details fetched successfully for order ID: {}", orderId);
        return order;
    }

    /**
     * Updates an order.
     *
     * @param orderId the ID of the order to update.
     * @param updatedOrderDetails the updated order details.
     * @return OrderDTO containing updated order details.
     */
    @PutMapping("/update/{orderId}")
    public OrderDTO updateOrder(
            @PathVariable int orderId,
            @RequestBody OrderDetails updatedOrderDetails) {
        log.info("Updating order ID: {}", orderId);
        OrderDTO updatedOrder = orderService.updateOrder(orderId, updatedOrderDetails);
        log.info("Order ID: {} updated successfully", orderId);
        return updatedOrder;
    }

    /**
     * Cancels an order.
     *
     * @param orderId the ID of the order to cancel.
     * @param adminComments comments from the admin regarding the cancellation.
     */
    @PutMapping("/cancel/{orderId}")
    public void cancelOrder(
            @PathVariable int orderId,
            @RequestBody String adminComments) {
        log.info("Cancelling order ID: {}", orderId);
        orderService.cancelOrder(orderId, adminComments);
        log.info("Order ID: {} cancelled successfully", orderId);
    }

    /**
     * Processes a refund for an order.
     *
     * @param orderId the ID of the order to refund.
     * @param refundAmountStr the amount to refund.
     * @return OrderDTO containing updated order details after refund.
     */
    @PutMapping("/refund/{orderId}")
    public OrderDTO processRefund(
            @PathVariable int orderId,
            @RequestBody String refundAmountStr) {
        double refundAmount = Double.parseDouble(refundAmountStr);
        log.info("Processing refund for order ID: {} with amount: {}", orderId, refundAmount);
        OrderDTO refundedOrder = orderService.processRefund(orderId, refundAmount);
        log.info("Refund processed successfully for order ID: {}", orderId);
        return refundedOrder;
    }
}
