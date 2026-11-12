package com.ecommerce.payment.controller;
import com.ecommerce.payment.dto.PaymentRequest;
import com.ecommerce.payment.entity.Order;
import com.ecommerce.payment.repository.OrderRepository;
import com.ecommerce.payment.service.StripeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = "*")
public class PaymentController {
    @Autowired
    private StripeService stripeService;
    @Autowired
    private OrderRepository orderRepository;
    @PostMapping("/create-payment-intent")
    public ResponseEntity<?> createPaymentIntent(@RequestBody PaymentRequest request) {
        try {
            com.stripe.model.PaymentIntent intent = stripeService.createPaymentIntent(request.getAmount(), request.getCurrency());
            Order newOrder = new Order();
            newOrder.setAmount(request.getAmount());
            newOrder.setCurrency(request.getCurrency());
            newOrder.setStatus("PENDING");
            newOrder.setPaymentIntentId(intent.getId());
            orderRepository.save(newOrder);
            Map<String, String> response = new HashMap<>();
            response.put("clientSecret", intent.getClientSecret());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}