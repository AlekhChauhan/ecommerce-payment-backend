package com.ecommerce.payment.controller;
import com.ecommerce.payment.entity.Order;
import com.ecommerce.payment.repository.OrderRepository;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api")
public class WebhookController {
    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    @Autowired
    private OrderRepository orderRepository;
    @PostMapping("/webhook")
    public ResponseEntity<String> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {
        Event event;
        try {
            // 1. Verify the message actually came from Stripe (Security check)
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
        } catch (SignatureVerificationException e) {
            System.out.println("Webhook signature verification failed!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
        }
        // 2. If the payment was successful...
        if ("payment_intent.succeeded".equals(event.getType())) {
            EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();

            if (dataObjectDeserializer.getObject().isPresent()) {
                PaymentIntent paymentIntent=(PaymentIntent) dataObjectDeserializer.getObject().get();

                Order order=orderRepository.findByPaymentIntentId(paymentIntent.getId());

                if (order!=null) {
                    order.setStatus("PAID");
                    orderRepository.save(order);
                    System.out.println("SUCCESS! Order ID " + order.getId() + " has been marked as PAID!");
                } else {
                    System.out.println("ERROR: Could not find DB order for PaymentIntent: " + paymentIntent.getId());
                }
            } else {
                System.out.println("CRITICAL ERROR: Stripe object deserialization failed.");
            }
        } else {
            System.out.println("Received unhandled event type: " +event.getType());
        }
        return ResponseEntity.ok("Success");
    }

}
