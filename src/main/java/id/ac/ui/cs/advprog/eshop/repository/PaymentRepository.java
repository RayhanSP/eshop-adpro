package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Payment;

import java.util.ArrayList;
import java.util.List;

public class PaymentRepository {

    private List<Payment> payments = new ArrayList<>();

    // Method to save a payment
    public void save(Payment payment) {
        payments.add(payment);
    }

    // Method to find a payment by its ID
    public Payment findById(String paymentId) {
        return payments.stream()
                .filter(payment -> payment.getId().equals(paymentId))
                .findFirst()
                .orElse(null); // Return null if no payment is found
    }

    // Method to get all payments
    public List<Payment> findAll() {
        return new ArrayList<>(payments); // Return a copy of the list
    }

    // Method to delete a payment by its ID
    public boolean deleteById(String paymentId) {
        return payments.removeIf(payment -> payment.getId().equals(paymentId));
    }
}
