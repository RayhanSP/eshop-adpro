package id.ac.ui.cs.advprog.eshop.repository;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.ArrayList;
import java.util.List;

public class PaymentRepositoryTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Test
    void testSavePayment() {
        Payment payment = new Payment("paymentId1", "Credit Card", "PENDING", null);
        paymentRepository.save(payment);

        verify(paymentRepository, times(1)).save(payment); // Verifies that the save method was called once
    }

    @Test
    void testFindById() {
        Payment payment = new Payment("paymentId1", "Credit Card", "PENDING", null);
        doReturn(payment).when(paymentRepository).findById("paymentId1");

        Payment fetchedPayment = paymentRepository.findById("paymentId1");
        assertNotNull(fetchedPayment);
        assertEquals("paymentId1", fetchedPayment.getId());
    }

    @Test
    void testFindAllPayments() {
        List<Payment> payments = new ArrayList<>();
        payments.add(new Payment("paymentId1", "Credit Card", "PENDING", null));
        payments.add(new Payment("paymentId2", "Debit Card", "PENDING", null));

        doReturn(payments).when(paymentRepository).findAll();

        List<Payment> fetchedPayments = paymentRepository.findAll();
        assertEquals(2, fetchedPayments.size());
    }
}
