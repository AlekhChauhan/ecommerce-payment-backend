package com.ecommerce.payment.repository;

import com.ecommerce.payment.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findByPaymentIntentId(String paymentIntentId);
}