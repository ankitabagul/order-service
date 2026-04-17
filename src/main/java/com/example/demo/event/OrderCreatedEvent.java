package com.example.demo.event;

import java.io.Serializable;

public class OrderCreatedEvent implements Serializable {

    private String orderId;
    private String customerName;
    private double amount;

    // Default constructor (required for Kafka deserialization)
    public OrderCreatedEvent() {
    }

    public OrderCreatedEvent(String orderId, String customerName, double amount) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.amount = amount;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "OrderCreatedEvent{" +
                "orderId='" + orderId + '\'' +
                ", customerName='" + customerName + '\'' +
                ", amount=" + amount +
                '}';
    }
}