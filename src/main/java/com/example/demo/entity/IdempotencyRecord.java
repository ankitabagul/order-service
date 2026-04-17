package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class IdempotencyRecord {

    @Id
    private String idempotencyKey;

    private String orderId;

    public IdempotencyRecord() {}

    public IdempotencyRecord(String idempotencyKey, String orderId) {
        this.idempotencyKey = idempotencyKey;
        this.orderId = orderId;
    }

    public String getIdempotencyKey() { return idempotencyKey; }
    public String getOrderId() { return orderId; }
}