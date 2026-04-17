package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    private String id;

    private String customerName;

    private double amount;

    public Order() {}

    public Order(String id, String customerName, double amount) {
        this.id = id;
        this.customerName = customerName;
        this.amount = amount;
    }

    public String getId() { return id; }
    public String getCustomerName() { return customerName; }
    public double getAmount() { return amount; }
}