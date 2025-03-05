package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;

import java.util.Map;
import java.util.List;

public interface PaymentService {

    // Method to add a new payment for a given order
    Payment addPayment(Order order, String method, Map<String, String> paymentData);

    // Method to set the status of a payment
    Payment setStatus(Payment payment, String status);

    // Method to get a payment by its ID
    Payment getPayment(String paymentId);

    // Method to get all payments
    List<Payment> getAllPayments();
}
