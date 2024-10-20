package com.asdc.dalexchange.dto;

import lombok.Data;

@Data
public class AdminDashboardDTO {
    private long customers;
    private long orders;
    private double sales;
    private double avgOrderValue;
    private double salesChange;
    private double ordersChange;
    private double customersChange;
    private double avgOrderValueChange;
}
