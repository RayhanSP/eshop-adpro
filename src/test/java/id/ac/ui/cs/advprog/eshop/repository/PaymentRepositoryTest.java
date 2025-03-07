package id.ac.ui.cs.advprog.eshop.repository;

import static org.junit.jupiter.api.Assertions.*;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class PaymentRepositoryTest {

    private PaymentRepository paymentRepository;

    @BeforeEach
    void setUp() {
        paymentRepository = new PaymentRepository();
    }

    @Test
    void testSavePayment() {
        Payment payment = new Payment("paymentId1", "Credit Card", "PENDING", null);
        paymentRepository.save(payment);

        Payment fetchedPayment = paymentRepository.findById("paymentId1");
        assertNotNull(fetchedPayment);
        assertEquals("paymentId1", fetchedPayment.getId());
    }

    @Test
    void testFindById() {
        Payment payment = new Payment("paymentId1", "Credit Card", "PENDING", null);
        paymentRepository.save(payment);

        Payment fetchedPayment = paymentRepository.findById("paymentId1");
        assertNotNull(fetchedPayment);
        assertEquals("paymentId1", fetchedPayment.getId());
    }

    @Test
    void testFindAllPayments() {
        Payment payment1 = new Payment("paymentId1", "Credit Card", "PENDING", null);
        Payment payment2 = new Payment("paymentId2", "Debit Card", "PENDING", null);
        paymentRepository.save(payment1);
        paymentRepository.save(payment2);

        List<Payment> fetchedPayments = paymentRepository.findAll();
        assertEquals(2, fetchedPayments.size());
    }

    @Test
    void testDeleteById() {
        // Arrange: Simpan dua pembayaran
        Payment payment1 = new Payment("paymentId1", "Credit Card", "PENDING", null);
        Payment payment2 = new Payment("paymentId2", "Debit Card", "PENDING", null);
        paymentRepository.save(payment1);
        paymentRepository.save(payment2);

        // Act: Hapus payment dengan id "paymentId1"
        boolean result = paymentRepository.deleteById("paymentId1");

        // Assert
        assertTrue(result, "deleteById seharusnya mengembalikan true jika penghapusan berhasil");
        // Verifikasi bahwa payment dengan id "paymentId1" tidak ada lagi
        Payment fetchedPayment = paymentRepository.findById("paymentId1");
        assertNull(fetchedPayment, "Payment dengan id paymentId1 seharusnya tidak ditemukan");
        // Sisa pembayaran seharusnya hanya 1
        List<Payment> remainingPayments = paymentRepository.findAll();
        assertEquals(1, remainingPayments.size(), "Hanya harus ada 1 payment tersisa");
    }
}
