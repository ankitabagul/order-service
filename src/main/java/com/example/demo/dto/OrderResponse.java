package com.example.demo.dto;

public class OrderResponse {

    private String id;
    private String customerName;
    private double amount;

    public OrderResponse(String id, String customerName, double amount) {
        this.id = id;
        this.customerName = customerName;
        this.amount = amount;
    }

    public String getId() { return id; }
    public String getCustomerName() { return customerName; }
    public double getAmount() { return amount; }
}