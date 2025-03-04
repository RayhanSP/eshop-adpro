package id.ac.ui.cs.advprog.eshop.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.HashMap;
import java.util.Map;

public class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private OrderService orderService;

    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        paymentService = new PaymentService(paymentRepository, orderService);
    }

    @Test
    void testAddPayment() {
        Order order = new Order("orderId1", "PENDING");
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("key1", "value1");

        // Create mock behavior for the repository
        Payment newPayment = new Payment("paymentId1", "Credit Card", "PENDING", paymentData);
        doReturn(newPayment).when(paymentRepository).save(any(Payment.class));

        Payment payment = paymentService.addPayment(order, "Credit Card", paymentData);
        assertNotNull(payment);
        assertEquals("paymentId1", payment.getId());
        assertEquals("Credit Card", payment.getMethod());
    }

    @Test
    void testSetStatusSuccess() {
        Order order = new Order("orderId1", "PENDING");
        Payment payment = new Payment("paymentId1", "Credit Card", "PENDING", null);
        doReturn(order).when(orderService).findById("orderId1");

        // Call setStatus to change the status
        Payment updatedPayment = paymentService.setStatus(payment, "SUCCESS");

        assertEquals("SUCCESS", updatedPayment.getStatus());
        assertEquals("SUCCESS", order.getStatus());
    }

    @Test
    void testSetStatusRejected() {
        Order order = new Order("orderId1", "PENDING");
        Payment payment = new Payment("paymentId1", "Credit Card", "PENDING", null);
        doReturn(order).when(orderService).findById("orderId1");

        // Call setStatus to change the status to REJECTED
        Payment updatedPayment = paymentService.setStatus(payment, "REJECTED");

        assertEquals("REJECTED", updatedPayment.getStatus());
        assertEquals("FAILED", order.getStatus());
    }
}

