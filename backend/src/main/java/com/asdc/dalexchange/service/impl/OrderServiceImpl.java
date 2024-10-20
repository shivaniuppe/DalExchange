package com.asdc.dalexchange.service.impl;

import com.asdc.dalexchange.dto.*;
import com.asdc.dalexchange.enums.OrderStatus;
import com.asdc.dalexchange.enums.PaymentStatus;
import com.asdc.dalexchange.mappers.Mapper;
import com.asdc.dalexchange.model.*;
import com.asdc.dalexchange.repository.*;
import com.asdc.dalexchange.service.OrderService;
import com.asdc.dalexchange.service.TradeRequestService;
import com.asdc.dalexchange.util.AuthUtil;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of the OrderService interface.
 * Provides methods to manage orders.
 */
@Service
@Slf4j
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;
    private UserRepository userRepository;
    private PaymentRepository paymentRepository;
    private ProductRepository productRepository;
    private TradeRequestService tradeRequestService;
    private ShippingRepository shippingRepository;
    private Mapper<OrderDetails, OrderDTO> orderMapper;

    /**
     * Calculates the percentage change in sales over the last 30 days.
     *
     * @return The percentage change in sales.
     */
    public double salesChange() {
        log.info("Calculating sales change for the last 30 days");
        LocalDateTime now = getCurrentDateTime();
        LocalDateTime startOfCurrentPeriod = now.minusDays(30);
        LocalDateTime startOfPreviousPeriod = startOfCurrentPeriod.minusDays(30);

        Double totalSalesLast30Days = orderRepository.getTotalSalesSince(startOfCurrentPeriod);
        Double totalSalesPrevious30Days = orderRepository.getTotalSalesBetween(startOfPreviousPeriod, startOfCurrentPeriod);

        if (totalSalesLast30Days == null) {
            totalSalesLast30Days = 0.0;
        }
        if (totalSalesPrevious30Days == null) {
            totalSalesPrevious30Days = 0.0;
        }

        double percentageIncrease = calculatePercentageIncrease(totalSalesLast30Days, totalSalesPrevious30Days);

        log.info("Sales change calculated: {}%", percentageIncrease);
        return percentageIncrease;
    }

    /**
     * Calculates the percentage change in the number of orders over the last 30 days.
     *
     * @return The percentage change in the number of orders.
     */
    public double ordersChange() {
        log.info("Calculating orders change for the last 30 days");
        LocalDateTime now = getCurrentDateTime();
        LocalDateTime startOfCurrentPeriod = now.minusDays(30);
        LocalDateTime startOfPreviousPeriod = startOfCurrentPeriod.minusDays(30);

        Long totalOrdersLast30Days = orderRepository.countOrdersSince(startOfCurrentPeriod);
        Long totalOrdersPrevious30Days = orderRepository.countOrdersBetween(startOfPreviousPeriod, startOfCurrentPeriod);

        if (totalOrdersLast30Days == null) {
            totalOrdersLast30Days = 0L;
        }
        if (totalOrdersPrevious30Days == null) {
            totalOrdersPrevious30Days = 0L;
        }

        double percentageIncrease = calculatePercentageIncrease(totalOrdersLast30Days.doubleValue(), totalOrdersPrevious30Days.doubleValue());

        log.info("Orders change calculated: {}%", percentageIncrease);
        return percentageIncrease;
    }

    /**
     * Calculates the percentage increase.
     *
     * @param currentSales  The current sales.
     * @param previousSales The previous sales.
     * @return The percentage increase.
     */
    private double calculatePercentageIncrease(Double currentSales, Double previousSales) {
        if (previousSales == 0) {
            return currentSales > 0 ? 100.0 : 0.0;
        }
        return ((currentSales - previousSales) / previousSales) * 100;
    }

    /**
     * Calculates the percentage change in the average order value over the last 30 days.
     *
     * @return The percentage change in the average order value.
     */
    public double avgOrderValueChange() {
        log.info("Calculating average order value change for the last 30 days");
        LocalDateTime now = getCurrentDateTime();
        LocalDateTime startOfCurrentPeriod = now.minusDays(30);
        LocalDateTime startOfPreviousPeriod = startOfCurrentPeriod.minusDays(30);

        Double avgOrderValueLast30Days = orderRepository.getAvgOrderValueSince(startOfCurrentPeriod);
        Double avgOrderValuePrevious30Days = orderRepository.getAvgOrderValueBetween(startOfPreviousPeriod, startOfCurrentPeriod);

        if (avgOrderValueLast30Days == null) {
            avgOrderValueLast30Days = 0.0;
        }
        if (avgOrderValuePrevious30Days == null) {
            avgOrderValuePrevious30Days = 0.0;
        }

        double percentageIncrease = calculatePercentageIncrease(avgOrderValueLast30Days, avgOrderValuePrevious30Days);

        log.info("Average order value change calculated: {}%", percentageIncrease);
        return percentageIncrease;
    }

    /**
     * Gets the number of new orders in the last 30 days.
     *
     * @return The number of new orders.
     */
    public long newOrders() {
        long count = orderRepository.countOrdersInLast30Days();
        log.info("New orders in the last 30 days: {}", count);
        return count;
    }

    /**
     * Gets the total sales in the last 30 days.
     *
     * @return The total sales.
     */
    public double totalSales() {
        double total = orderRepository.totalSalesInLast30Days();
        log.info("Total sales in the last 30 days: {}", total);
        return total;
    }

    /**
     * Gets the average sales in the last 30 days.
     *
     * @return The average sales.
     */
    public double avgSales() {
        double avg = orderRepository.avgOrderValueInLast30Days();
        log.info("Average sales in the last 30 days: {}", avg);
        return avg;
    }

    /**
     * Gets the current date and time.
     *
     * @return The current date and time.
     */
    public LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }

    /**
     * Gets all orders.
     *
     * @return A list of all orders.
     */
    @Override
    public List<OrderDTO> getAllOrders() {
        log.info("Fetching all orders");
        return orderRepository.findAll().stream()
                .map(orderMapper::mapTo)
                .collect(Collectors.toList());
    }

    /**
     * Gets an order by ID.
     *
     * @param orderId The ID of the order.
     * @return The order details.
     */
    public OrderDTO getOrderById(int orderId) {
        log.info("Fetching order with ID: {}", orderId);
        OrderDetails order = orderRepository.findById((long) orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return orderMapper.mapTo(order);
    }

    /**
     * Updates an order.
     *
     * @param orderId              The ID of the order.
     * @param updatedOrderDetails The updated order details.
     * @return The updated order.
     */
    @Transactional
    public OrderDTO updateOrder(int orderId, OrderDetails updatedOrderDetails) {
        log.info("Updating order with ID: {}", orderId);
        OrderDetails order = orderRepository.findById((long) orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (updatedOrderDetails.getTotalAmount() != 0) {
            order.setTotalAmount(updatedOrderDetails.getTotalAmount());
        }
        if (updatedOrderDetails.getOrderStatus() != null) {
            order.setOrderStatus(updatedOrderDetails.getOrderStatus());
        }
        if (updatedOrderDetails.getAdminComments() != null) {
            order.setAdminComments(updatedOrderDetails.getAdminComments());
        }
        if (updatedOrderDetails.getShippingAddress() != null) {
            updateShippingAddress(order.getShippingAddress().getAddressId(), updatedOrderDetails.getShippingAddress());
        }
        if (updatedOrderDetails.getPayment() != null) {
            order.setPayment(updatedOrderDetails.getPayment());
        }
        OrderDetails updatedOrder = orderRepository.save(order);
        log.info("Order with ID: {} updated successfully", orderId);
        return orderMapper.mapTo(updatedOrder);
    }

    /**
     * Cancels an order.
     *
     * @param orderId       The ID of the order.
     * @param adminComments Comments from the admin.
     */
    @Transactional
    public void cancelOrder(int orderId, String adminComments) {
        log.info("Cancelling order with ID: {}", orderId);
        OrderDetails order = orderRepository.findById((long) orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setOrderStatus(OrderStatus.Cancelled);
        order.setAdminComments(adminComments);
        orderRepository.save(order);
        log.info("Order with ID: {} cancelled successfully", orderId);
    }

    /**
     * Processes a refund for an order.
     *
     * @param orderId     The ID of the order.
     * @param refundAmount The amount to be refunded.
     * @return The updated order details.
     */
    @Transactional
    public OrderDTO processRefund(int orderId, double refundAmount) {
        log.info("Processing refund for order with ID: {}", orderId);
        OrderDetails order = orderRepository.findById((long) orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setTotalAmount(order.getTotalAmount() - refundAmount);
        OrderDetails updatedOrder = orderRepository.save(order);
        log.info("Refund processed for order with ID: {}", orderId);
        return orderMapper.mapTo(updatedOrder);
    }

    /**
     * Saves a new order.
     *
     * @param getSavedShippingAddress The shipping address.
     * @param productId               The ID of the product.
     * @return The ID of the saved order.
     */
    @Transactional
    @Override
    public Long saveNewOrder(ShippingAddress getSavedShippingAddress, Long productId) {
        log.info("Saving new order for productId: {}", productId);

        Long userId = AuthUtil.getCurrentUserId(userRepository);
        Double amount = tradeRequestService.getApprovedTradeRequestAmount(productId);

        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (!optionalProduct.isPresent()) {
            log.error("Product with id {} not found", productId);
            throw new RuntimeException("Product not found");
        }

        Product product = optionalProduct.get();

        User user = userRepository.findByUserId(userId);
        if (user == null) {
            log.error("User with id {} not found", userId);
            throw new RuntimeException("User not found");
        }

        Payment payment = new Payment();
        payment.setPaymentMethod("Card");
        payment.setPaymentStatus(PaymentStatus.pending);
        payment.setAmount(amount);
        payment.setPaymentDate(LocalDateTime.now());
        Payment savedPayment = paymentRepository.save(payment);
        log.info("Payment saved with id: {}", savedPayment.getPaymentId());

        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setBuyer(user);
        orderDetails.setProductId(product);
        orderDetails.setTotalAmount(amount);
        orderDetails.setOrderStatus(OrderStatus.Pending);
        orderDetails.setTransactionDatetime(LocalDateTime.now());
        orderDetails.setShippingAddress(getSavedShippingAddress);
        orderDetails.setPayment(savedPayment);

        OrderDetails savedOrder = orderRepository.save(orderDetails);
        log.info("Order saved with id: {}", savedOrder.getOrderId());

        return savedOrder.getOrderId();
    }


    /**
     * Updates the shipping address of an order.
     *
     * @param addressId              The ID of the address.
     * @param updatedShippingAddress The updated shipping address details.
     */
    @Transactional
    public void updateShippingAddress(Long addressId, ShippingAddress updatedShippingAddress) {
        log.info("Updating shipping address with ID: {}", addressId);
        ShippingAddress existingAddress = shippingRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Shipping Address not found"));

        existingAddress.setBillingName(updatedShippingAddress.getBillingName());
        existingAddress.setCountry(updatedShippingAddress.getCountry());
        existingAddress.setLine1(updatedShippingAddress.getLine1());
        existingAddress.setLine2(updatedShippingAddress.getLine2());
        existingAddress.setCity(updatedShippingAddress.getCity());
        existingAddress.setState(updatedShippingAddress.getState());
        existingAddress.setPostalCode(updatedShippingAddress.getPostalCode());

        shippingRepository.save(existingAddress);
        log.info("Shipping address with ID: {} updated successfully", addressId);
    }

    /**
     * Gets the number of items sold per month.
     *
     * @return A list of items sold per month.
     */
    @Override
    public List<ItemsSoldDTO> getItemsSold() {
        log.info("Fetching items sold per month");
        List<Object[]> results = orderRepository.findItemsSoldPerMonth();
        return results.stream().map(result -> {
            ItemsSoldDTO dto = new ItemsSoldDTO();
            dto.setMonth((String) result[0]);
            dto.setItemsSold(((Number) result[1]).intValue());
            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * Gets the top-selling categories.
     *
     * @return A list of top-selling categories.
     */
    @Override
    public List<TopSellingCategoriesDTO> getTopSellingCategories() {
        log.info("Fetching top-selling categories");
        List<Object[]> results = orderRepository.findTopSellingCategories();
        return results.stream().map(result -> {
            TopSellingCategoriesDTO dto = new TopSellingCategoriesDTO();
            dto.setCategory((String) result[0]);
            dto.setSales(((Number) result[1]).intValue());
            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * Gets the best-selling products.
     *
     * @return A list of best-selling products.
     */
    @Override
    public List<BestSellingProductsDTO> getBestSellingProducts() {
        log.info("Fetching best-selling products");
        List<Object[]> results = orderRepository.findBestSellingProducts();
        return results.stream().map(result -> {
            BestSellingProductsDTO dto = new BestSellingProductsDTO();
            dto.setProductName((String) result[0]);
            dto.setItemsSold(((Number) result[1]).intValue());
            return dto;
        }).collect(Collectors.toList());
    }

}
