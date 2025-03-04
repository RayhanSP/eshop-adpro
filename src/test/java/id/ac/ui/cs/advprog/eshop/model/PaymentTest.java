package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

public class PaymentTest {

    @Test
    void testPaymentConstructorAndGetterSetter() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("key1", "value1");

        Payment payment = new Payment("paymentId1", "Credit Card", "PENDING", paymentData);

        // Check getter
        assertEquals("paymentId1", payment.getId());
        assertEquals("Credit Card", payment.getMethod());
        assertEquals("PENDING", payment.getStatus());
        assertEquals(paymentData, payment.getPaymentData());

        // Set new values using setter
        payment.setId("newPaymentId");
        payment.setMethod("Debit Card");
        payment.setStatus("SUCCESS");
        Map<String, String> newPaymentData = new HashMap<>();
        newPaymentData.put("key2", "value2");
        payment.setPaymentData(newPaymentData);

        // Check setter values
        assertEquals("newPaymentId", payment.getId());
        assertEquals("Debit Card", payment.getMethod());
        assertEquals("SUCCESS", payment.getStatus());
        assertEquals(newPaymentData, payment.getPaymentData());
    }
}

