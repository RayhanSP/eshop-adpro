package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private OrderService orderService;

    private PaymentService paymentService;

    private List<Product> products;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        paymentService = new PaymentServiceImpl(paymentRepository, orderService);

        // Setup non-empty products list
        products = new ArrayList<>();
        Product product1 = new Product();
        product1.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(2);
        Product product2 = new Product();
        product2.setProductId("a2c62328-4a37-4664-83c7-32db8620155");
        product2.setProductName("Sabun Cap Usep");
        product2.setProductQuantity(1);
        products.add(product1);
        products.add(product2);
    }

    @Test
    void testAddPayment() {
        // Arrange
        Order order = new Order(
                "13652556-012a-4c07-b546-54eb1396d79b",
                products,
                1708560000L,
                "Safira Sudrajat"
        );
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("key1", "value1");

        // Act
        Payment payment = paymentService.addPayment(order, "Credit Card", paymentData);

        // Assert
        assertNotNull(payment);
        // Karena generatePaymentId() menghasilkan id dinamis, periksa id tidak null dan diawali "PAY"
        assertNotNull(payment.getId());
        assertTrue(payment.getId().startsWith("PAY"));
        assertEquals("Credit Card", payment.getMethod());
        assertEquals("PENDING", payment.getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testSetStatusSuccess() {
        // Arrange
        // Pastikan Payment.id sama dengan Order.id untuk simulasi hubungan
        Order order = new Order(
                "13652556-012a-4c07-b546-54eb1396d79b",
                products,
                1708560000L,
                "Safira Sudrajat"
        );
        Payment payment = new Payment("13652556-012a-4c07-b546-54eb1396d79b", "Credit Card", "PENDING", null);
        when(orderService.findById("13652556-012a-4c07-b546-54eb1396d79b")).thenReturn(order);

        // Act
        Payment updatedPayment = paymentService.setStatus(payment, "SUCCESS");

        // Assert
        assertEquals("SUCCESS", updatedPayment.getStatus());
        assertEquals("SUCCESS", order.getStatus());
    }

    @Test
    void testSetStatusRejected() {
        // Arrange
        Order order = new Order(
                "13652556-012a-4c07-b546-54eb1396d79b",
                products,
                1708560000L,
                "Safira Sudrajat"
        );
        Payment payment = new Payment("13652556-012a-4c07-b546-54eb1396d79b", "Credit Card", "PENDING", null);
        when(orderService.findById("13652556-012a-4c07-b546-54eb1396d79b")).thenReturn(order);

        // Act
        Payment updatedPayment = paymentService.setStatus(payment, "REJECTED");

        // Assert
        assertEquals("REJECTED", updatedPayment.getStatus());
        assertEquals("FAILED", order.getStatus());
    }

    @Test
    void testGetPayment() {
        // Arrange
        Payment payment = new Payment("paymentId1", "Credit Card", "PENDING", null);
        when(paymentRepository.findById("paymentId1")).thenReturn(payment);

        // Act
        Payment fetchedPayment = paymentService.getPayment("paymentId1");

        // Assert
        assertEquals("paymentId1", fetchedPayment.getId());
        assertEquals("Credit Card", fetchedPayment.getMethod());
    }

    @Test
    void testGetAllPayments() {
        // Arrange
        Payment payment1 = new Payment("paymentId1", "Credit Card", "PENDING", null);
        Payment payment2 = new Payment("paymentId2", "Debit Card", "SUCCESS", null);
        List<Payment> payments = List.of(payment1, payment2);
        when(paymentRepository.findAll()).thenReturn(payments);

        // Act
        List<Payment> allPayments = paymentService.getAllPayments();

        // Assert
        assertEquals(2, allPayments.size());
        assertEquals("paymentId1", allPayments.get(0).getId());
        assertEquals("paymentId2", allPayments.get(1).getId());
    }

    @Test
    void testSetStatusSuccessNoRelatedOrder() {
        // Arrange
        // Payment dengan id "noOrder" sehingga orderService.findById akan mengembalikan null
        Payment payment = new Payment("noOrder", "Credit Card", "PENDING", null);
        when(orderService.findById("noOrder")).thenReturn(null);

        // Act
        Payment updatedPayment = paymentService.setStatus(payment, "SUCCESS");

        // Assert
        // Karena tidak ada Order terkait, Payment hanya diupdate statusnya
        assertEquals("SUCCESS", updatedPayment.getStatus());
        // Verifikasi bahwa updateStatus tidak pernah dipanggil
        verify(orderService, never()).updateStatus(anyString(), anyString());
    }

    @Test
    void testSetStatusRejectedNoRelatedOrder() {
        // Arrange
        // Payment dengan id "noOrder" sehingga orderService.findById mengembalikan null
        Payment payment = new Payment("noOrder", "Credit Card", "PENDING", null);
        when(orderService.findById("noOrder")).thenReturn(null);

        // Act
        Payment updatedPayment = paymentService.setStatus(payment, "REJECTED");

        // Assert
        assertEquals("REJECTED", updatedPayment.getStatus());
        // Verifikasi bahwa updateStatus tidak pernah dipanggil karena relatedOrder null
        verify(orderService, never()).updateStatus(anyString(), anyString());
    }

    @Test
    void testSetStatusOther() {
        // Arrange
        Payment payment = new Payment("somePaymentId", "Credit Card", "PENDING", null);
        // Tidak perlu stub orderService.findById karena branch if tidak terpenuhi

        // Act
        Payment updatedPayment = paymentService.setStatus(payment, "CANCELED");

        // Assert
        // Pastikan status hanya diperbarui menjadi "CANCELED"
        assertEquals("CANCELED", updatedPayment.getStatus());
        // Verifikasi bahwa orderService.findById dan updateStatus tidak pernah dipanggil
        verify(orderService, never()).findById(anyString());
        verify(orderService, never()).updateStatus(anyString(), anyString());
    }

    @Test
    void testAddPaymentByVoucherValid() {
        Order order = new Order("order1", products, 1708560000L, "Test Author");
        // Voucher valid: 16 karakter, diawali "ESHOP", dan memiliki tepat 8 digit numerik.
        String validVoucher = "ESHOP1234ABC5678";

        Payment payment = ((PaymentServiceImpl) paymentService).addPaymentByVoucher(order, validVoucher);

        assertNotNull(payment);
        assertNotNull(payment.getId());
        assertTrue(payment.getId().startsWith("PAY"));
        assertEquals("Voucher", payment.getMethod());
        assertEquals("SUCCESS", payment.getStatus());
        assertEquals(validVoucher, payment.getPaymentData().get("voucherCode"));
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testAddPaymentByVoucherInvalidDigits() {
        Order order = new Order("order1", products, 1708560000L, "Test Author");
        // Voucher valid format (16 karakter dan diawali "ESHOP") tetapi hanya memiliki 4 digit numerik.
        String invalidVoucher = "ESHOP1234ABCDEF";

        Payment payment = ((PaymentServiceImpl) paymentService).addPaymentByVoucher(order, invalidVoucher);

        assertNotNull(payment);
        assertNotNull(payment.getId());
        assertTrue(payment.getId().startsWith("PAY"));
        assertEquals("Voucher", payment.getMethod());
        assertEquals("REJECTED", payment.getStatus());
        assertEquals(invalidVoucher, payment.getPaymentData().get("voucherCode"));
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testAddPaymentByVoucherInvalidLength() {
        Order order = new Order("order1", products, 1708560000L, "Test Author");
        // Voucher dengan panjang tidak tepat (misalnya 14 karakter)
        String invalidVoucher = "ESHOP12345678";

        Payment payment = ((PaymentServiceImpl) paymentService).addPaymentByVoucher(order, invalidVoucher);

        assertNotNull(payment);
        assertNotNull(payment.getId());
        assertTrue(payment.getId().startsWith("PAY"));
        assertEquals("Voucher", payment.getMethod());
        assertEquals("REJECTED", payment.getStatus());
        assertEquals(invalidVoucher, payment.getPaymentData().get("voucherCode"));
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testAddPaymentByVoucherInvalidPrefix() {
        Order order = new Order("order1", products, 1708560000L, "Test Author");
        // Voucher yang tidak diawali dengan "ESHOP"
        String invalidVoucher = "SHOP123456789012";

        Payment payment = ((PaymentServiceImpl) paymentService).addPaymentByVoucher(order, invalidVoucher);

        assertNotNull(payment);
        assertNotNull(payment.getId());
        assertTrue(payment.getId().startsWith("PAY"));
        assertEquals("Voucher", payment.getMethod());
        assertEquals("REJECTED", payment.getStatus());
        assertEquals(invalidVoucher, payment.getPaymentData().get("voucherCode"));
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testAddPaymentByVoucherNullVoucher() {
        // Uji branch ketika voucherCode null (tidak masuk ke if)
        Order order = new Order("order1", products, 1708560000L, "Test Author");
        String nullVoucher = null;

        Payment payment = ((PaymentServiceImpl) paymentService).addPaymentByVoucher(order, nullVoucher);

        assertNotNull(payment);
        assertNotNull(payment.getId());
        assertTrue(payment.getId().startsWith("PAY"));
        assertEquals("Voucher", payment.getMethod());
        assertEquals("REJECTED", payment.getStatus());
        // Karena voucherCode null, value di map adalah null
        assertNull(payment.getPaymentData().get("voucherCode"));
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testAddPaymentByVoucherTooManyDigits() {
        Order order = new Order("order1", products, 1708560000L, "Test Author");
        // Voucher dengan 16 karakter, dimulai "ESHOP", tetapi mengandung 10 digit (bukan tepat 8 digit)
        String voucherTooManyDigits = "ESHOP9876543210X";
        // Periksa: "ESHOP" (5 char) + "9876543210" (10 digit) + "X" (1 char) = 16 karakter

        Payment payment = ((PaymentServiceImpl) paymentService).addPaymentByVoucher(order, voucherTooManyDigits);

        assertNotNull(payment);
        assertNotNull(payment.getId());
        assertTrue(payment.getId().startsWith("PAY"));
        assertEquals("Voucher", payment.getMethod());
        // Karena digit count bukan 8 (dalam voucher ini digit count = 10), status harus REJECTED
        assertEquals("REJECTED", payment.getStatus());
        assertEquals(voucherTooManyDigits, payment.getPaymentData().get("voucherCode"));
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testAddPaymentByCashOnDeliveryValid() {
        Order order = new Order("order1", products, 1708560000L, "Test Author");
        String address = "123 Main Street";
        String deliveryFee = "5000";

        Payment payment = ((PaymentServiceImpl) paymentService).addPaymentByCashOnDelivery(order, address, deliveryFee);

        assertNotNull(payment);
        assertNotNull(payment.getId());
        assertTrue(payment.getId().startsWith("PAY"));
        assertEquals("Cash On Delivery", payment.getMethod());
        // Data lengkap, status harus "PENDING"
        assertEquals("PENDING", payment.getStatus());
        assertEquals(address, payment.getPaymentData().get("address"));
        assertEquals(deliveryFee, payment.getPaymentData().get("deliveryFee"));
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testAddPaymentByCashOnDeliveryEmptyAddress() {
        Order order = new Order("order1", products, 1708560000L, "Test Author");
        String address = "";
        String deliveryFee = "5000";

        Payment payment = ((PaymentServiceImpl) paymentService).addPaymentByCashOnDelivery(order, address, deliveryFee);

        assertNotNull(payment);
        assertEquals("REJECTED", payment.getStatus());
        assertEquals(address, payment.getPaymentData().get("address"));
        assertEquals(deliveryFee, payment.getPaymentData().get("deliveryFee"));
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testAddPaymentByCashOnDeliveryNullAddress() {
        Order order = new Order("order1", products, 1708560000L, "Test Author");
        String address = null;
        String deliveryFee = "5000";

        Payment payment = ((PaymentServiceImpl) paymentService).addPaymentByCashOnDelivery(order, address, deliveryFee);

        assertNotNull(payment);
        assertEquals("REJECTED", payment.getStatus());
        // Jika address null, maka nilai pada map harus null
        assertNull(payment.getPaymentData().get("address"));
        assertEquals(deliveryFee, payment.getPaymentData().get("deliveryFee"));
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testAddPaymentByCashOnDeliveryEmptyDeliveryFee() {
        Order order = new Order("order1", products, 1708560000L, "Test Author");
        String address = "123 Main Street";
        String deliveryFee = "";

        Payment payment = ((PaymentServiceImpl) paymentService).addPaymentByCashOnDelivery(order, address, deliveryFee);

        assertNotNull(payment);
        assertEquals("REJECTED", payment.getStatus());
        assertEquals(address, payment.getPaymentData().get("address"));
        assertEquals(deliveryFee, payment.getPaymentData().get("deliveryFee"));
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testAddPaymentByCashOnDeliveryNullDeliveryFee() {
        Order order = new Order("order1", products, 1708560000L, "Test Author");
        String address = "123 Main Street";
        String deliveryFee = null;

        Payment payment = ((PaymentServiceImpl) paymentService).addPaymentByCashOnDelivery(order, address, deliveryFee);

        assertNotNull(payment);
        assertEquals("REJECTED", payment.getStatus());
        assertEquals(address, payment.getPaymentData().get("address"));
        assertNull(payment.getPaymentData().get("deliveryFee"));
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }
}
