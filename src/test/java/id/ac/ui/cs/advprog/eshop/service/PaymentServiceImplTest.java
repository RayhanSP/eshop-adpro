package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private OrderService orderService;

    private PaymentServiceImpl paymentServiceImpl;
    private List<Product> products;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Gunakan spy agar kita bisa stub method generatePaymentId()
        paymentServiceImpl = spy(new PaymentServiceImpl(paymentRepository, orderService));

        // Setup Products
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
                this.products,
                1708560000L,
                "Safira Sudrajat"
        );
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("key1", "value1");

        // Stub generatePaymentId untuk mengembalikan "paymentId1"
        doReturn("paymentId1").when(paymentServiceImpl).generatePaymentId();

        // Karena save() adalah void, kita hanya verifikasi pemanggilan nanti

        // Act
        Payment payment = paymentServiceImpl.addPayment(order, "Credit Card", paymentData);

        // Assert
        assertNotNull(payment);
        assertEquals("paymentId1", payment.getId());
        assertEquals("Credit Card", payment.getMethod());
        assertEquals("PENDING", payment.getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testSetStatusSuccess() {
        // Arrange
        // Agar relasi antara Order dan Payment valid, gunakan id yang sama
        Order order = new Order(
                "13652556-012a-4c07-b546-54eb1396d79b",
                this.products,
                1708560000L,
                "Safira Sudrajat"
        );
        Payment payment = new Payment("13652556-012a-4c07-b546-54eb1396d79b", "Credit Card", "PENDING", null);
        doReturn(order).when(orderService).findById("13652556-012a-4c07-b546-54eb1396d79b");

        // Act
        Payment updatedPayment = paymentServiceImpl.setStatus(payment, "SUCCESS");

        // Assert
        assertEquals("SUCCESS", updatedPayment.getStatus());
        assertEquals("SUCCESS", order.getStatus());
    }

    @Test
    void testSetStatusRejected() {
        // Arrange
        Order order = new Order(
                "13652556-012a-4c07-b546-54eb1396d79b",
                this.products,
                1708560000L,
                "Safira Sudrajat"
        );
        Payment payment = new Payment("13652556-012a-4c07-b546-54eb1396d79b", "Credit Card", "PENDING", null);
        doReturn(order).when(orderService).findById("13652556-012a-4c07-b546-54eb1396d79b");

        // Act
        Payment updatedPayment = paymentServiceImpl.setStatus(payment, "REJECTED");

        // Assert
        assertEquals("REJECTED", updatedPayment.getStatus());
        assertEquals("FAILED", order.getStatus());
    }

    @Test
    void testGetPayment() {
        // Arrange
        Payment payment = new Payment("paymentId1", "Credit Card", "PENDING", null);
        doReturn(payment).when(paymentRepository).findById("paymentId1");

        // Act
        Payment fetchedPayment = paymentServiceImpl.getPayment("paymentId1");

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
        doReturn(payments).when(paymentRepository).findAll();

        // Act
        List<Payment> allPayments = paymentServiceImpl.getAllPayments();

        // Assert
        assertEquals(2, allPayments.size());
        assertEquals("paymentId1", allPayments.get(0).getId());
        assertEquals("paymentId2", allPayments.get(1).getId());
    }

    @Test
    void testAddPaymentByVoucherValid() {
        Order order = new Order("order1", products, 1708560000L, "Test Author");
        String validVoucher = "ESHOP1234ABC5678";

        Payment payment = paymentServiceImpl.addPaymentByVoucher(order, validVoucher);

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
        String invalidVoucher = "ESHOP1234ABCDEF";

        Payment payment = paymentServiceImpl.addPaymentByVoucher(order, invalidVoucher);

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
        String invalidVoucher = "ESHOP12345678";

        Payment payment = paymentServiceImpl.addPaymentByVoucher(order, invalidVoucher);

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
        String invalidVoucher = "SHOP123456789012";

        Payment payment = paymentServiceImpl.addPaymentByVoucher(order, invalidVoucher);

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
        Order order = new Order("order1", products, 1708560000L, "Test Author");
        String nullVoucher = null;

        Payment payment = paymentServiceImpl.addPaymentByVoucher(order, nullVoucher);

        assertNotNull(payment);
        assertNotNull(payment.getId());
        assertTrue(payment.getId().startsWith("PAY"));
        assertEquals("Voucher", payment.getMethod());
        assertEquals("REJECTED", payment.getStatus());
        // Karena voucher null, value pada map akan null
        assertNull(payment.getPaymentData().get("voucherCode"));
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testAddPaymentByVoucherTooManyDigits() {
        Order order = new Order("order1", products, 1708560000L, "Test Author");
        // Voucher dengan 16 karakter, diawali "ESHOP", tetapi mengandung 10 digit (bukan tepat 8)
        String voucherTooManyDigits = "ESHOP9876543210X";

        Payment payment = paymentServiceImpl.addPaymentByVoucher(order, voucherTooManyDigits);

        assertNotNull(payment);
        assertNotNull(payment.getId());
        assertTrue(payment.getId().startsWith("PAY"));
        assertEquals("Voucher", payment.getMethod());
        // Karena digit count tidak sama dengan 8 (pada voucher ini digit count = 10), status harus REJECTED
        assertEquals("REJECTED", payment.getStatus());
        assertEquals(voucherTooManyDigits, payment.getPaymentData().get("voucherCode"));
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testAddPaymentByCashOnDeliveryValid() {
        Order order = new Order("order1", products, 1708560000L, "Test Author");
        String address = "456 Elm Street";
        String deliveryFee = "7500";

        Payment payment = paymentServiceImpl.addPaymentByCashOnDelivery(order, address, deliveryFee);

        assertNotNull(payment);
        assertNotNull(payment.getId());
        assertTrue(payment.getId().startsWith("PAY"));
        assertEquals("Cash On Delivery", payment.getMethod());
        assertEquals("PENDING", payment.getStatus());
        assertEquals(address, payment.getPaymentData().get("address"));
        assertEquals(deliveryFee, payment.getPaymentData().get("deliveryFee"));
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testAddPaymentByCashOnDeliveryEmptyAddress() {
        Order order = new Order("order1", products, 1708560000L, "Test Author");
        String address = "";
        String deliveryFee = "7500";

        Payment payment = paymentServiceImpl.addPaymentByCashOnDelivery(order, address, deliveryFee);

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
        String deliveryFee = "7500";

        Payment payment = paymentServiceImpl.addPaymentByCashOnDelivery(order, address, deliveryFee);

        assertNotNull(payment);
        assertEquals("REJECTED", payment.getStatus());
        assertNull(payment.getPaymentData().get("address"));
        assertEquals(deliveryFee, payment.getPaymentData().get("deliveryFee"));
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testAddPaymentByCashOnDeliveryEmptyDeliveryFee() {
        Order order = new Order("order1", products, 1708560000L, "Test Author");
        String address = "456 Elm Street";
        String deliveryFee = "";

        Payment payment = paymentServiceImpl.addPaymentByCashOnDelivery(order, address, deliveryFee);

        assertNotNull(payment);
        assertEquals("REJECTED", payment.getStatus());
        assertEquals(address, payment.getPaymentData().get("address"));
        assertEquals(deliveryFee, payment.getPaymentData().get("deliveryFee"));
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testAddPaymentByCashOnDeliveryNullDeliveryFee() {
        Order order = new Order("order1", products, 1708560000L, "Test Author");
        String address = "456 Elm Street";
        String deliveryFee = null;

        Payment payment = paymentServiceImpl.addPaymentByCashOnDelivery(order, address, deliveryFee);

        assertNotNull(payment);
        assertEquals("REJECTED", payment.getStatus());
        assertEquals(address, payment.getPaymentData().get("address"));
        assertNull(payment.getPaymentData().get("deliveryFee"));
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }
}
