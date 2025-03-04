package id.ac.ui.cs.advprog.eshop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.HashMap;
import java.util.Map;

public class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private OrderService orderService;

    private PaymentServiceImpl paymentServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        paymentServiceImpl = new PaymentServiceImpl(paymentRepository, orderService);
    }

    @Test
    void testAddPayment() {
        Order order = new Order("orderId1", "PENDING");
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("key1", "value1");

        Payment newPayment = new Payment("paymentId1", "Credit Card", "PENDING", paymentData);
        doReturn(newPayment).when(paymentRepository).save(any(Payment.class));

        Payment payment = paymentServiceImpl.addPayment(order, "Credit Card", paymentData);
        assertNotNull(payment);
        assertEquals("paymentId1", payment.getId());
        assertEquals("Credit Card", payment.getMethod());
    }

    // Same tests as PaymentService.java can be used here for other methods...
}
