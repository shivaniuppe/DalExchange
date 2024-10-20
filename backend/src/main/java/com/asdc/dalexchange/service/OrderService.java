package com.asdc.dalexchange.service;

import com.asdc.dalexchange.dto.BestSellingProductsDTO;
import com.asdc.dalexchange.dto.ItemsSoldDTO;
import com.asdc.dalexchange.dto.OrderDTO;
import com.asdc.dalexchange.dto.TopSellingCategoriesDTO;
import com.asdc.dalexchange.model.OrderDetails;
import com.asdc.dalexchange.model.ShippingAddress;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {

    double salesChange();
    double ordersChange();
    double avgOrderValueChange();
    long newOrders();
    double totalSales();
    double avgSales();
    LocalDateTime getCurrentDateTime();
    List<ItemsSoldDTO> getItemsSold();
    List<TopSellingCategoriesDTO> getTopSellingCategories();
    List<BestSellingProductsDTO> getBestSellingProducts();

    // Order Moderation
    List<OrderDTO> getAllOrders();
    OrderDTO getOrderById(int orderId);
    OrderDTO updateOrder(int orderId, OrderDetails updatedOrderDetails);
    void cancelOrder(int orderId, String adminComments);
    OrderDTO processRefund(int orderId, double refundAmount);
    Long saveNewOrder(ShippingAddress getSavedShippingAddress, Long productId);
}
