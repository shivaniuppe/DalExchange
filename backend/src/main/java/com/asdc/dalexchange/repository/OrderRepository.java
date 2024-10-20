package com.asdc.dalexchange.repository;

import com.asdc.dalexchange.model.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;

@Repository
public interface OrderRepository extends JpaRepository<OrderDetails, Long>, JpaSpecificationExecutor<OrderDetails> {

    @Query(value = "SELECT COUNT(*) FROM order_details WHERE transaction_datetime >= CURDATE() - INTERVAL 30 DAY", nativeQuery = true)
    Long countOrdersInLast30Days();

    @Query(value = "SELECT COALESCE(SUM(total_amount), 0) FROM order_details WHERE transaction_datetime >= CURDATE() - INTERVAL 30 DAY", nativeQuery = true)
    double totalSalesInLast30Days();

    @Query(value = "SELECT COALESCE(AVG(total_amount), 0) FROM order_details WHERE transaction_datetime >= CURDATE() - INTERVAL 30 DAY", nativeQuery = true)
    double avgOrderValueInLast30Days();

    List<OrderDetails> findByBuyerUserId(Long userId);

    @Query("SELECT SUM(od.totalAmount) FROM OrderDetails od WHERE od.transactionDatetime >= :startDate")
    Double getTotalSalesSince(@Param("startDate") LocalDateTime startDate);

    @Query("SELECT SUM(od.totalAmount) FROM OrderDetails od WHERE od.transactionDatetime >= :startDate AND od.transactionDatetime < :endDate")
    Double getTotalSalesBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    // order
    @Query("SELECT COUNT(o) FROM OrderDetails o WHERE o.transactionDatetime >= :startDate")
    Long countOrdersSince(LocalDateTime startDate);

    @Query("SELECT COUNT(o) FROM OrderDetails o WHERE o.transactionDatetime >= :startDate AND o.transactionDatetime < :endDate")
    Long countOrdersBetween(LocalDateTime startDate, LocalDateTime endDate);

    // order value
    @Query("SELECT AVG(o.totalAmount) FROM OrderDetails o WHERE o.transactionDatetime >= :startDate")
    Double getAvgOrderValueSince(@Param("startDate") LocalDateTime startDate);

    @Query("SELECT AVG(o.totalAmount) FROM OrderDetails o WHERE o.transactionDatetime >= :startDate AND o.transactionDatetime < :endDate")
    Double getAvgOrderValueBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query(value = "SELECT DATE_FORMAT(transaction_datetime, '%Y-%m') AS month, COUNT(*) AS items_sold " +
            "FROM order_details " +
            "GROUP BY month " +
            "ORDER BY month", nativeQuery = true)
    List<Object[]> findItemsSoldPerMonth();

    @Query(value = "SELECT pc.name AS category_name, COUNT(od.product_id) AS items_sold " +
            "FROM order_details od " +
            "JOIN product p ON od.product_id = p.product_id " +
            "JOIN product_category pc ON p.category_id = pc.category_id " +
            "GROUP BY pc.name " +
            "ORDER BY items_sold DESC", nativeQuery = true)
    List<Object[]> findTopSellingCategories();

    @Query(value = "SELECT p.title AS product_name, COUNT(od.product_id) AS items_sold " +
            "FROM order_details od " +
            "JOIN product p ON od.product_id = p.product_id " +
            "GROUP BY p.title " +
            "ORDER BY items_sold DESC", nativeQuery = true)
    List<Object[]> findBestSellingProducts();
}
