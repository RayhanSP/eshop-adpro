package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;

import java.util.List;
import java.util.Map;

public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderService orderService;

    // Constructor
    public PaymentServiceImpl(PaymentRepository paymentRepository, OrderService orderService) {
        this.paymentRepository = paymentRepository;
        this.orderService = orderService;
    }

    @Override
    public Payment addPayment(Order order, String method, Map<String, String> paymentData) {
        // Create a new Payment object
        String paymentId = generatePaymentId(); // Use private helper method to generate ID
        Payment payment = new Payment(paymentId, method, "PENDING", paymentData);

        // Save the payment to the repository
        paymentRepository.save(payment);

        return payment;
    }

    @Override
    public Payment setStatus(Payment payment, String status) {
        // Update the status of the payment
        payment.setStatus(status);

        // If status is "SUCCESS", update the related Order status to "SUCCESS"
        if ("SUCCESS".equals(status)) {
            Order relatedOrder = orderService.findById(payment.getId());
            if (relatedOrder != null) {
                relatedOrder.setStatus("SUCCESS");
                orderService.updateStatus(relatedOrder.getId(), "SUCCESS");
            }
        }
        // If status is "REJECTED", update the related Order status to "FAILED"
        else if ("REJECTED".equals(status)) {
            Order relatedOrder = orderService.findById(payment.getId());
            if (relatedOrder != null) {
                relatedOrder.setStatus("FAILED");
                orderService.updateStatus(relatedOrder.getId(), "FAILED");
            }
        }

        return payment;
    }

    @Override
    public Payment getPayment(String paymentId) {
        // Fetch the payment from the repository
        return paymentRepository.findById(paymentId);
    }

    @Override
    public List<Payment> getAllPayments() {
        // Fetch all payments from the repository
        return paymentRepository.findAll();
    }

    // Private helper method to generate paymentId (could be UUID or another mechanism)
    private String generatePaymentId() {
        return "PAY" + System.currentTimeMillis(); // Example implementation
    }
}
