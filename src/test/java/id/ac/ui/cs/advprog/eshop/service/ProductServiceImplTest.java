package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;

    @BeforeEach
    public void setUp() {
        product = new Product();
        product.setProductId(UUID.randomUUID().toString());
        product.setProductName("Laptop");
        product.setProductQuantity(10);
    }

    @Test
    public void testCreateProduct_WithNullProductId() {
        Product newProduct = new Product();
        newProduct.setProductId(null); // Simulasi produk tanpa ID
        newProduct.setProductName("Tablet");
        newProduct.setProductQuantity(5);

        when(productRepository.create(any(Product.class))).thenReturn(newProduct);

        Product createdProduct = productService.create(newProduct);

        assertNotNull(createdProduct.getProductId()); // Pastikan ID di-generate
        verify(productRepository, times(1)).create(any(Product.class));
    }

    @Test
    public void testCreateProduct_WithEmptyProductId() {
        Product newProduct = new Product();
        newProduct.setProductId(""); // Simulasi produk dengan ID kosong
        newProduct.setProductName("Tablet");
        newProduct.setProductQuantity(5);

        when(productRepository.create(any(Product.class))).thenReturn(newProduct);

        Product createdProduct = productService.create(newProduct);

        assertNotNull(createdProduct.getProductId()); // Pastikan ID di-generate
        verify(productRepository, times(1)).create(any(Product.class));
    }

    @Test
    public void testDeleteProduct_WithNullId() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> productService.deleteById(null));

        assertEquals("Product ID cannot be null or empty", thrown.getMessage());
        verify(productRepository, never()).deleteById(anyString());
    }

    @Test
    public void testDeleteProduct_WithEmptyId() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> productService.deleteById(""));

        assertEquals("Product ID cannot be null or empty", thrown.getMessage());
        verify(productRepository, never()).deleteById(anyString());
    }
}
