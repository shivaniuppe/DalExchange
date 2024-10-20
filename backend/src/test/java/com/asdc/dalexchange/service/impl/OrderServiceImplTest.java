package com.asdc.dalexchange.service.impl;

import com.asdc.dalexchange.dto.*;
import com.asdc.dalexchange.enums.OrderStatus;
import com.asdc.dalexchange.enums.PaymentStatus;
import com.asdc.dalexchange.mappers.Mapper;
import com.asdc.dalexchange.model.*;
import com.asdc.dalexchange.repository.*;
import com.asdc.dalexchange.service.TradeRequestService;
import com.asdc.dalexchange.util.AuthUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TradeRequestService tradeRequestService;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private ShippingRepository shippingRepository;

    @Mock
    private Mapper<OrderDetails, OrderDTO> orderMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSalesChange() {
        LocalDateTime now = LocalDateTime.now();
        when(orderRepository.getTotalSalesSince(any(LocalDateTime.class))).thenReturn(1000.0);
        when(orderRepository.getTotalSalesBetween(any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(800.0);

        double result = orderService.salesChange();

        assertEquals(25.0, result);

        when(orderRepository.getTotalSalesSince(any(LocalDateTime.class))).thenReturn(null);
        when(orderRepository.getTotalSalesBetween(any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(null);

        result = orderService.salesChange();
        assertEquals(0.0, result);
    }

    @Test
    void testOrdersChange() {
        LocalDateTime now = LocalDateTime.now();
        when(orderRepository.countOrdersSince(any(LocalDateTime.class))).thenReturn(50L);
        when(orderRepository.countOrdersBetween(any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(40L);

        double result = orderService.ordersChange();

        assertEquals(25.0, result);

        when(orderRepository.countOrdersSince(any(LocalDateTime.class))).thenReturn(null);
        when(orderRepository.countOrdersBetween(any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(null);

        result = orderService.ordersChange();
        assertEquals(0.0, result);
    }

    @Test
    void testAvgOrderValueChange() {
        LocalDateTime now = LocalDateTime.now();
        when(orderRepository.getAvgOrderValueSince(any(LocalDateTime.class))).thenReturn(20.0);
        when(orderRepository.getAvgOrderValueBetween(any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(16.0);

        double result = orderService.avgOrderValueChange();

        assertEquals(25.0, result);

        when(orderRepository.getAvgOrderValueSince(any(LocalDateTime.class))).thenReturn(null);
        when(orderRepository.getAvgOrderValueBetween(any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(null);

        result = orderService.avgOrderValueChange();
        assertEquals(0.0, result);
    }

    @Test
    void testNewOrders() {
        when(orderRepository.countOrdersInLast30Days()).thenReturn(100L);

        long result = orderService.newOrders();

        assertEquals(100, result);
    }

    @Test
    void testTotalSales() {
        when(orderRepository.totalSalesInLast30Days()).thenReturn(5000.0);

        double result = orderService.totalSales();

        assertEquals(5000.0, result);
    }

    @Test
    void testAvgSales() {
        when(orderRepository.avgOrderValueInLast30Days()).thenReturn(50.0);

        double result = orderService.avgSales();

        assertEquals(50.0, result);
    }
    @Test
    public void testUpdateOrder_AdminComments() {
        long orderId = 1L;
        OrderDetails existingOrder = new OrderDetails();
        existingOrder.setOrderId(orderId);

        OrderDetails updatedOrderDetails = new OrderDetails();
        updatedOrderDetails.setAdminComments("This is a comment");

        OrderDTO updatedOrderDTO = new OrderDTO();
        updatedOrderDTO.setOrderId(orderId);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));
        when(orderRepository.save(existingOrder)).thenReturn(existingOrder);
        when(orderMapper.mapTo(existingOrder)).thenReturn(updatedOrderDTO);

        OrderDTO result = orderService.updateOrder((int) orderId, updatedOrderDetails);

        assertEquals("This is a comment", existingOrder.getAdminComments());
        assertEquals(updatedOrderDTO, result);
    }

    @Test
    public void testUpdateOrder_Payment() {
        long orderId = 1L;
        OrderDetails existingOrder = new OrderDetails();
        existingOrder.setOrderId(orderId);

        Payment newPayment = new Payment();
        newPayment.setPaymentMethod("Card");
        newPayment.setPaymentStatus(PaymentStatus.completed);

        OrderDetails updatedOrderDetails = new OrderDetails();
        updatedOrderDetails.setPayment(newPayment);

        OrderDTO updatedOrderDTO = new OrderDTO();
        updatedOrderDTO.setOrderId(orderId);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));
        when(orderRepository.save(existingOrder)).thenReturn(existingOrder);
        when(orderMapper.mapTo(existingOrder)).thenReturn(updatedOrderDTO);

        OrderDTO result = orderService.updateOrder((int) orderId, updatedOrderDetails);

        assertEquals(newPayment, existingOrder.getPayment());
        assertEquals(updatedOrderDTO, result);
    }


    @Test
    public void testGetAllOrders() {
        OrderDetails order1 = new OrderDetails();
        order1.setOrderId(1L);
        order1.setTotalAmount(100.0);

        OrderDetails order2 = new OrderDetails();
        order2.setOrderId(2L);
        order2.setTotalAmount(200.0);

        List<OrderDetails> orders = Arrays.asList(order1, order2);
        List<OrderDTO> orderDTOs = orders.stream().map(order -> {
            OrderDTO dto = new OrderDTO();
            dto.setOrderId(order.getOrderId());
            dto.setTotalAmount(order.getTotalAmount());
            return dto;
        }).collect(Collectors.toList());

        when(orderRepository.findAll()).thenReturn(orders);
        when(orderMapper.mapTo(order1)).thenReturn(orderDTOs.get(0));
        when(orderMapper.mapTo(order2)).thenReturn(orderDTOs.get(1));

        List<OrderDTO> result = orderService.getAllOrders();

        assertEquals(2, result.size());
        assertEquals(100.0, result.get(0).getTotalAmount());
        assertEquals(200.0, result.get(1).getTotalAmount());
    }

    @Test
    public void testGetOrderById() {
        long orderId = 1L;
        OrderDetails order = new OrderDetails();
        order.setOrderId(orderId);
        order.setTotalAmount(100.0);

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderId(orderId);
        orderDTO.setTotalAmount(100.0);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderMapper.mapTo(order)).thenReturn(orderDTO);

        OrderDTO result = orderService.getOrderById((int) orderId);

        assertEquals(orderDTO, result);
    }

    @Test
    public void testUpdateOrder() {

        long orderId = 1L;
        OrderDetails existingOrder = new OrderDetails();
        existingOrder.setOrderId(orderId);
        existingOrder.setTotalAmount(100.0);

        OrderDetails updatedOrderDetails = new OrderDetails();
        updatedOrderDetails.setTotalAmount(150.0);
        updatedOrderDetails.setOrderStatus(OrderStatus.Shipped);

        OrderDTO updatedOrderDTO = new OrderDTO();
        updatedOrderDTO.setOrderId(orderId);
        updatedOrderDTO.setTotalAmount(150.0);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));
        when(orderRepository.save(existingOrder)).thenReturn(existingOrder);
        when(orderMapper.mapTo(existingOrder)).thenReturn(updatedOrderDTO);

        OrderDTO result = orderService.updateOrder((int) orderId, updatedOrderDetails);

        assertEquals(150.0, existingOrder.getTotalAmount());
        assertEquals(OrderStatus.Shipped, existingOrder.getOrderStatus());
        assertEquals(updatedOrderDTO, result);
    }

    @Test
    public void testCancelOrder() {

        long orderId = 1L;
        OrderDetails order = new OrderDetails();
        order.setOrderId(orderId);
        order.setOrderStatus(OrderStatus.Shipped);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        orderService.cancelOrder((int) orderId, "Cancelled by admin");

        assertEquals(OrderStatus.Cancelled, order.getOrderStatus());
        assertEquals("Cancelled by admin", order.getAdminComments());
        verify(orderRepository).save(order);
    }

    @Test
    public void testProcessRefund() {
        long orderId = 1L;
        OrderDetails order = new OrderDetails();
        order.setOrderId(orderId);
        order.setTotalAmount(200.0);

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderId(orderId);
        orderDTO.setTotalAmount(150.0);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);
        when(orderMapper.mapTo(order)).thenReturn(orderDTO);

        OrderDTO result = orderService.processRefund((int) orderId, 50.0);

        assertEquals(150.0, order.getTotalAmount());
        assertEquals(orderDTO, result);
    }

    @Test
    public void testUpdateShippingAddress() {
        long addressId = 1L;
        ShippingAddress existingAddress = new ShippingAddress();
        existingAddress.setAddressId(addressId);
        existingAddress.setBillingName("Old Name");

        ShippingAddress updatedAddress = new ShippingAddress();
        updatedAddress.setBillingName("New Name");

        when(shippingRepository.findById(addressId)).thenReturn(Optional.of(existingAddress));

        orderService.updateShippingAddress(addressId, updatedAddress);

        assertEquals("New Name", existingAddress.getBillingName());
        verify(shippingRepository).save(existingAddress);
    }

    @Test
    public void testUpdateOrder_ShippingAddress() {
        long orderId = 1L;
        OrderDetails existingOrder = new OrderDetails();
        existingOrder.setOrderId(orderId);

        ShippingAddress existingAddress = new ShippingAddress();
        existingAddress.setAddressId(1L);
        existingAddress.setBillingName("Old Name");

        existingOrder.setShippingAddress(existingAddress);

        ShippingAddress updatedAddress = new ShippingAddress();
        updatedAddress.setAddressId(1L);
        updatedAddress.setBillingName("New Name");

        OrderDetails updatedOrderDetails = new OrderDetails();
        updatedOrderDetails.setShippingAddress(updatedAddress);

        OrderDTO updatedOrderDTO = new OrderDTO();
        updatedOrderDTO.setOrderId(orderId);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));
        when(shippingRepository.findById(existingAddress.getAddressId())).thenReturn(Optional.of(existingAddress));
        when(orderRepository.save(existingOrder)).thenReturn(existingOrder);
        when(orderMapper.mapTo(existingOrder)).thenReturn(updatedOrderDTO);

        OrderDTO result = orderService.updateOrder((int) orderId, updatedOrderDetails);

        assertEquals("New Name", existingAddress.getBillingName());
        assertEquals(updatedOrderDTO, result);
    }

    @Test
    public void testUpdateShippingAddress_NotFound() {

        long addressId = 1L;
        ShippingAddress updatedAddress = new ShippingAddress();

        when(shippingRepository.findById(addressId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            orderService.updateShippingAddress(addressId, updatedAddress);
        });

        assertEquals("Shipping Address not found", exception.getMessage());
    }

    @Test
    void testGetItemsSold() {

        List<Object[]> mockData = Arrays.asList(
                new Object[]{"2024-07", 10},
                new Object[]{"2024-06", 20}
        );

        when(orderRepository.findItemsSoldPerMonth()).thenReturn(mockData);

        List<ItemsSoldDTO> result = orderService.getItemsSold();

        assertEquals(2, result.size());
        assertEquals("2024-07", result.get(0).getMonth());
        assertEquals(10, result.get(0).getItemsSold());
        assertEquals("2024-06", result.get(1).getMonth());
        assertEquals(20, result.get(1).getItemsSold());
    }

    @Test
    void testGetTopSellingCategories() {

        List<Object[]> mockData = Arrays.asList(
                new Object[]{"Electronics", 50},
                new Object[]{"Books", 30}
        );

        when(orderRepository.findTopSellingCategories()).thenReturn(mockData);

        List<TopSellingCategoriesDTO> result = orderService.getTopSellingCategories();

        assertEquals(2, result.size());
        assertEquals("Electronics", result.get(0).getCategory());
        assertEquals(50, result.get(0).getSales());
        assertEquals("Books", result.get(1).getCategory());
        assertEquals(30, result.get(1).getSales());
    }

    @Test
    void testGetBestSellingProducts() {

        List<Object[]> mockData = Arrays.asList(
                new Object[]{"Product A", 100},
                new Object[]{"Product B", 80}
        );

        when(orderRepository.findBestSellingProducts()).thenReturn(mockData);

        List<BestSellingProductsDTO> result = orderService.getBestSellingProducts();

        assertEquals(2, result.size());
        assertEquals("Product A", result.get(0).getProductName());
        assertEquals(100, result.get(0).getItemsSold());
        assertEquals("Product B", result.get(1).getProductName());
        assertEquals(80, result.get(1).getItemsSold());
    }


    @Test
    void testSaveNewOrder_Success() {
        Long userId = 1L;
        Long productId = 2L;
        Double amount = 100.0;

        ShippingAddress shippingAddress = new ShippingAddress();
        shippingAddress.setAddressId(1L);

        Product product = new Product();
        product.setProductId(productId);

        User user = new User();
        user.setUserId(userId);

        Payment payment = new Payment();
        payment.setPaymentMethod("Card");
        payment.setPaymentStatus(PaymentStatus.completed);
        payment.setAmount(amount);
        payment.setPaymentDate(LocalDateTime.now());

        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setOrderId(1L);

        try (MockedStatic<AuthUtil> authUtilMock = mockStatic(AuthUtil.class)) {
            authUtilMock.when(() -> AuthUtil.getCurrentUserId(userRepository)).thenReturn(userId);
            when(tradeRequestService.getApprovedTradeRequestAmount(productId)).thenReturn(amount);
            when(productRepository.findById(productId)).thenReturn(Optional.of(product));
            when(userRepository.findByUserId(userId)).thenReturn(user);
            when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
            when(orderRepository.save(any(OrderDetails.class))).thenReturn(orderDetails);

            Long resultOrderId = orderService.saveNewOrder(shippingAddress, productId);

            assertEquals(1L, resultOrderId);
            verify(tradeRequestService, times(1)).getApprovedTradeRequestAmount(productId);
            verify(productRepository, times(1)).findById(productId);
            verify(userRepository, times(1)).findByUserId(userId);
            verify(paymentRepository, times(1)).save(any(Payment.class));
            verify(orderRepository, times(1)).save(any(OrderDetails.class));
        }
    }


    @Test
    void testSaveNewOrder_ProductNotFound() {
        Long userId = 1L;
        Long productId = 2L;
        ShippingAddress shippingAddress = new ShippingAddress();
        shippingAddress.setAddressId(1L);

        try (MockedStatic<AuthUtil> authUtilMock = mockStatic(AuthUtil.class)) {
            authUtilMock.when(() -> AuthUtil.getCurrentUserId(userRepository)).thenReturn(userId);
            when(tradeRequestService.getApprovedTradeRequestAmount(productId)).thenReturn(100.0);
            when(productRepository.findById(productId)).thenReturn(Optional.empty());

            Exception exception = assertThrows(RuntimeException.class, () -> {
                orderService.saveNewOrder(shippingAddress, productId);
            });

            assertEquals("Product not found", exception.getMessage());
        }
    }

    @Test
    void testSaveNewOrder_UserNotFound() {
        Long userId = 1L;
        Long productId = 2L;
        Product product = new Product();
        product.setProductId(productId);
        product.setSold(false);
        ShippingAddress shippingAddress = new ShippingAddress();
        shippingAddress.setAddressId(1L);

        try (MockedStatic<AuthUtil> authUtilMock = mockStatic(AuthUtil.class)) {
            authUtilMock.when(() -> AuthUtil.getCurrentUserId(userRepository)).thenReturn(userId);
            when(tradeRequestService.getApprovedTradeRequestAmount(productId)).thenReturn(100.0);
            when(productRepository.findById(productId)).thenReturn(Optional.of(product));
            when(userRepository.findByUserId(userId)).thenReturn(null);

            Exception exception = assertThrows(RuntimeException.class, () -> {
                orderService.saveNewOrder(shippingAddress, productId);
            });

            assertEquals("User not found", exception.getMessage());
        }
    }

    @Test
    void testSaveNewOrder_FailedPaymentSave() {
        Long userId = 1L;
        Long productId = 2L;
        Double amount = 100.0;
        ShippingAddress shippingAddress = new ShippingAddress();
        shippingAddress.setAddressId(1L);

        Product product = new Product();
        product.setProductId(productId);
        product.setSold(false);

        User user = new User();
        user.setUserId(userId);

        Payment payment = new Payment();
        payment.setPaymentMethod("Card");
        payment.setPaymentStatus(PaymentStatus.completed);
        payment.setAmount(amount);
        payment.setPaymentDate(LocalDateTime.now());

        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setOrderId(1L);

        try (MockedStatic<AuthUtil> authUtilMock = mockStatic(AuthUtil.class)) {
            authUtilMock.when(() -> AuthUtil.getCurrentUserId(userRepository)).thenReturn(userId);
            when(tradeRequestService.getApprovedTradeRequestAmount(productId)).thenReturn(amount);
            when(productRepository.findById(productId)).thenReturn(Optional.of(product));
            when(userRepository.findByUserId(userId)).thenReturn(user);
            when(paymentRepository.save(any(Payment.class))).thenThrow(new RuntimeException("Failed to save payment"));

            Exception exception = assertThrows(RuntimeException.class, () -> {
                orderService.saveNewOrder(shippingAddress, productId);
            });

            assertEquals("Failed to save payment", exception.getMessage());
        }
    }
}
