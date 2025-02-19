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
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService; // Harus menggunakan implementasi dari ProductService

    private Product product;

    @BeforeEach
    public void setUp() {
        product = new Product();
        product.setProductId(UUID.randomUUID().toString());
        product.setProductName("Laptop");
        product.setProductQuantity(10);
    }

    @Test
    public void testCreateProduct() {
        when(productRepository.create(any(Product.class))).thenReturn(product);

        Product createdProduct = productService.create(product);

        assertNotNull(createdProduct);
        assertEquals("Laptop", createdProduct.getProductName());
        verify(productRepository, times(1)).create(product);
    }

    @Test
    public void testFindAllProducts() {
        List<Product> products = List.of(product);
        Iterator<Product> productIterator = products.iterator();

        when(productRepository.findAll()).thenReturn(productIterator);

        List<Product> result = productService.findAll();

        assertEquals(1, result.size());
        assertEquals("Laptop", result.get(0).getProductName());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    public void testUpdateProduct_Success() {
        when(productRepository.update(any(Product.class))).thenReturn(product);

        product.setProductName("Updated Laptop");
        product.setProductQuantity(15);
        Product updatedProduct = productService.update(product);

        assertNotNull(updatedProduct);
        assertEquals("Updated Laptop", updatedProduct.getProductName());
        assertEquals(15, updatedProduct.getProductQuantity());
        verify(productRepository, times(1)).update(product);
    }

    @Test
    public void testUpdateProduct_NotFound() {
        when(productRepository.update(any(Product.class))).thenThrow(new RuntimeException("Product not found"));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> productService.update(product));

        assertEquals("Product not found", thrown.getMessage());
        verify(productRepository, times(1)).update(product);
    }

    @Test
    public void testDeleteProduct_Success() {
        doNothing().when(productRepository).deleteById(product.getProductId());

        assertDoesNotThrow(() -> productService.deleteById(product.getProductId()));
        verify(productRepository, times(1)).deleteById(product.getProductId());
    }

    @Test
    public void testDeleteProduct_InvalidId() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> productService.deleteById(""));

        assertEquals("Product ID cannot be null or empty", thrown.getMessage());

        // Pastikan metode repository tidak dipanggil sama sekali karena ID kosong
        verify(productRepository, never()).deleteById(anyString());
    }

}
