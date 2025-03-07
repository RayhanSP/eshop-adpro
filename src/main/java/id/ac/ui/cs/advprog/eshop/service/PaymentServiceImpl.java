package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;

import java.util.HashMap;
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

    @Override
    public Payment addPaymentByVoucher(Order order, String voucherCode) {
        // Siapkan data pembayaran dengan voucher code
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", voucherCode);

        String status;
        // Validasi voucher code:
        // - Panjang 16 karakter,
        // - Dimulai dengan "ESHOP",
        // - Mengandung tepat 8 digit numerik.
        if (voucherCode != null
                && voucherCode.length() == 16
                && voucherCode.startsWith("ESHOP")) {
            int digitCount = 0;
            for (char c : voucherCode.toCharArray()) {
                if (Character.isDigit(c)) {
                    digitCount++;
                }
            }
            status = (digitCount == 8) ? "SUCCESS" : "REJECTED";
        } else {
            status = "REJECTED";
        }

        String paymentId = generatePaymentId();
        // Metode pembayaran disini adalah "Voucher" (atau bisa disesuaikan)
        Payment payment = new Payment(paymentId, "Voucher", status, paymentData);
        paymentRepository.save(payment);
        return payment;
    }

    @Override
    public Payment addPaymentByCashOnDelivery(Order order, String address, String deliveryFee) {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("address", address);
        paymentData.put("deliveryFee", deliveryFee);

        // Jika salah satu informasi kosong (null atau empty), status menjadi REJECTED.
        boolean invalid = (address == null || address.trim().isEmpty())
                || (deliveryFee == null || deliveryFee.trim().isEmpty());
        String status = invalid ? "REJECTED" : "PENDING";

        String paymentId = generatePaymentId();
        Payment payment = new Payment(paymentId, "Cash On Delivery", status, paymentData);
        paymentRepository.save(payment);
        return payment;
    }

    // Helper method to generate payment ID
    String generatePaymentId() {
        return "PAY" + System.currentTimeMillis(); // Example of generating a payment ID
    }
}

