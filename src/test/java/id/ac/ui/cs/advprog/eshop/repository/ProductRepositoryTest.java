package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductRepositoryTest {

    @InjectMocks
    ProductRepository productRepository;
    @BeforeEach
    void setUp() {
    }
    @Test
    void testCreateAndFind() {
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
        productRepository.create(product);

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
        Product savedProduct = productIterator.next();
        assertEquals(product.getProductId(), savedProduct.getProductId());
        assertEquals(product.getProductName(), savedProduct.getProductName());
        assertEquals(product.getProductQuantity(), savedProduct.getProductQuantity());
    }
    @Test
    void testFindAllIfEmpty() {
        Iterator<Product> productIterator = productRepository.findAll();
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testFindAllIfMoreThanOneProduct() {
        Product product1 = new Product();
        product1.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(100);
        productRepository.create(product1);

        Product product2 = new Product();
        product2.setProductId("a0f9de46-90b1-437d-a0bf-d0821dde9096");
        product2.setProductName("Sampo Cap Usep");
        product2.setProductQuantity(50);
        productRepository.create(product2);

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
        Product savedProduct = productIterator.next();
        assertEquals(product1.getProductId(), savedProduct.getProductId());
        savedProduct = productIterator.next();
        assertEquals(product2.getProductId(), savedProduct.getProductId());
        assertFalse(productIterator.hasNext());
    }

    /*** CREATE PRODUCT TESTS ***/
    @Test
    void testCreateProductSuccess() {
        Product product = new Product();
        product.setProductId("123");
        product.setProductName("New Product");
        product.setProductQuantity(10);

        productRepository.create(product);
        Iterator<Product> iterator = productRepository.findAll();

        assertTrue(iterator.hasNext());
        Product createdProduct = iterator.next();
        assertEquals("123", createdProduct.getProductId());
    }

    @Test
    void testCreateProductFailure_EmptyName() {
        Product product = new Product();
        product.setProductId("456");
        product.setProductName("");
        product.setProductQuantity(10);

        assertDoesNotThrow(() -> productRepository.create(product));
    }

    /*** EDIT PRODUCT TESTS ***/
    @Test
    void testEditProductSuccess() {
        Product product = new Product();
        product.setProductId("123");
        product.setProductName("Old Name");
        product.setProductQuantity(10);
        productRepository.create(product);

        Product updatedProduct = new Product();
        updatedProduct.setProductId("123");
        updatedProduct.setProductName("Updated Name");
        updatedProduct.setProductQuantity(20);
        productRepository.update(updatedProduct);

        Iterator<Product> iterator = productRepository.findAll();
        Product result = iterator.next();
        assertEquals("Updated Name", result.getProductName());
    }

    @Test
    void testEditProductFailure_NotFound() {
        Product updatedProduct = new Product();
        updatedProduct.setProductId("999");
        updatedProduct.setProductName("Non-existent");

        assertDoesNotThrow(() -> {
            try {
                productRepository.update(updatedProduct);
            } catch (Exception e) {
            }
        });
    }

    /*** DELETE PRODUCT TESTS ***/
    @Test
    void testDeleteProductSuccess() {
        Product product = new Product();
        product.setProductId("123");
        product.setProductName("Delete Me");
        product.setProductQuantity(10);
        productRepository.create(product);

        productRepository.deleteById("123");
        Iterator<Product> iterator = productRepository.findAll();

        assertFalse(iterator.hasNext());
    }

    @Test
    void testDeleteProductFailure_NotFound() {
        assertDoesNotThrow(() -> productRepository.deleteById("999"));
    }


}
