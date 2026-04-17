package com.example.demo.service;

import com.example.demo.dto.OrderResponse;
import com.example.demo.entity.IdempotencyRecord;
import com.example.demo.entity.Order;
import com.example.demo.event.OrderCreatedEvent;
import com.example.demo.exception.OrderNotFoundException;
import com.example.demo.repository.IdempotencyRepository;
import com.example.demo.repository.OrderRepository;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepo;
    private final IdempotencyRepository idemRepo;
    private final KafkaProducerService kafkaProducer;

    public OrderService(OrderRepository orderRepo,
                        IdempotencyRepository idemRepo,
                        KafkaProducerService kafkaProducer) {
        this.orderRepo = orderRepo;
        this.idemRepo = idemRepo;
        this.kafkaProducer = kafkaProducer;
    }

    /**
     * Create Order with Idempotency + Kafka Event
     */
    @Transactional
    public String createOrder(String key, String customerName, double amount) {

        // ✅ Check if already processed
        return idemRepo.findById(key)
                .map(IdempotencyRecord::getOrderId)
                .orElseGet(() -> createNewOrder(key, customerName, amount));
    }

    private String createNewOrder(String key, String customerName, double amount) {
        try {
            String orderId = UUID.randomUUID().toString();

            // Save Order
            Order order = new Order(orderId, customerName, amount);
            orderRepo.save(order);

            // Save Idempotency Record
            idemRepo.save(new IdempotencyRecord(key, orderId));

            // Publish Kafka Event
            OrderCreatedEvent event =
                    new OrderCreatedEvent(orderId, customerName, amount);

            kafkaProducer.send(event);

            return orderId;

        } catch (DataIntegrityViolationException ex) {
            // 🔥 Handle race condition (duplicate key)
            return idemRepo.findById(key)
                    .map(IdempotencyRecord::getOrderId)
                    .orElseThrow(() -> ex);
        }
    }

    /**
     * Get Order by ID
     */
    public OrderResponse getOrder(String id) {
        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found: " + id));

        return mapToResponse(order);
    }

    /**
     * Get All Orders
     */
    public List<OrderResponse> getAllOrders() {
        return orderRepo.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * Entity → DTO Mapping
     */
    private OrderResponse mapToResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getCustomerName(),
                order.getAmount()
        );
    }
}