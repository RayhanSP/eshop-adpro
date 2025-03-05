package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;

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
        String paymentId = generatePaymentId(); // Generate payment ID
        Payment payment = new Payment(paymentId, method, "PENDING", paymentData);

        paymentRepository.save(payment);
        return payment;
    }

    @Override
    public Payment setStatus(Payment payment, String status) {
        payment.setStatus(status);

        // Update the order status based on the payment status
        if ("SUCCESS".equals(status)) {
            Order relatedOrder = orderService.findById(payment.getId());
            if (relatedOrder != null) {
                relatedOrder.setStatus("SUCCESS");
                orderService.updateStatus(relatedOrder.getId(), "SUCCESS");
            }
        } else if ("REJECTED".equals(status)) {
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
        return paymentRepository.findById(paymentId);
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    // Helper method to generate payment ID
    String generatePaymentId() {
        return "PAY" + System.currentTimeMillis(); // Example of generating a payment ID
    }
}

