package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ProductRepositoryTest {

    private ProductRepository productRepository;
    private Product product;

    @BeforeEach
    public void setUp() {
        productRepository = new ProductRepository();

        product = new Product();
        product.setProductId(UUID.randomUUID().toString());
        product.setProductName("Laptop");
        product.setProductQuantity(10);

        productRepository.create(product);
    }

    @Test
    public void testCreateProduct() {
        Product newProduct = new Product();
        newProduct.setProductId(UUID.randomUUID().toString());
        newProduct.setProductName("Phone");
        newProduct.setProductQuantity(5);

        Product createdProduct = productRepository.create(newProduct);

        assertNotNull(createdProduct);
        assertEquals("Phone", createdProduct.getProductName());
    }

    @Test
    public void testFindAllProducts() {
        Iterator<Product> products = productRepository.findAll();
        assertTrue(products.hasNext());
    }

    @Test
    public void testUpdateProduct_Success() {
        product.setProductName("Updated Laptop");
        product.setProductQuantity(15);

        Product updatedProduct = productRepository.update(product);

        assertNotNull(updatedProduct);
        assertEquals("Updated Laptop", updatedProduct.getProductName());
        assertEquals(15, updatedProduct.getProductQuantity());
    }

    @Test
    public void testUpdateProduct_NotFound() {
        Product nonExistentProduct = new Product();
        nonExistentProduct.setProductId(UUID.randomUUID().toString());
        nonExistentProduct.setProductName("Tablet");
        nonExistentProduct.setProductQuantity(7);

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> productRepository.update(nonExistentProduct));

        assertEquals("Product not found", thrown.getMessage());
    }

    @Test
    public void testDeleteProduct_Success() {
        productRepository.deleteById(product.getProductId());

        Iterator<Product> products = productRepository.findAll();
        assertFalse(products.hasNext());
    }

    @Test
    public void testDeleteProduct_NotFound() {
        String nonExistentProductId = UUID.randomUUID().toString();

        // Tidak ada exception jika produk tidak ada, hanya tidak menghapus apa pun
        assertDoesNotThrow(() -> productRepository.deleteById(nonExistentProductId));
    }

    @Test
    public void testDeleteProduct_InvalidId_Null() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> productRepository.deleteById(null));

        assertEquals("Product ID cannot be null or empty", thrown.getMessage());
    }

    @Test
    public void testDeleteProduct_InvalidId_Empty() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> productRepository.deleteById(""));

        assertEquals("Product ID cannot be null or empty", thrown.getMessage());
    }

    @Test
    public void testDeleteProduct_IdNotFound_NoEffect() {
        String nonExistentProductId = UUID.randomUUID().toString();

        // Ambil ukuran list sebelum penghapusan
        Iterator<Product> beforeDelete = productRepository.findAll();
        int sizeBefore = 0;
        while (beforeDelete.hasNext()) {
            sizeBefore++;
            beforeDelete.next();
        }

        productRepository.deleteById(nonExistentProductId); // Hapus produk yang tidak ada

        // Ambil ukuran list setelah penghapusan
        Iterator<Product> afterDelete = productRepository.findAll();
        int sizeAfter = 0;
        while (afterDelete.hasNext()) {
            sizeAfter++;
            afterDelete.next();
        }

        assertEquals(sizeBefore, sizeAfter); // Pastikan ukuran tidak berubah
    }

    @Test
    public void testDeleteProduct_WithNullId() {
        Product productWithNullId = new Product();
        productWithNullId.setProductId(null);
        productWithNullId.setProductName("Unnamed Product");
        productWithNullId.setProductQuantity(5);

        productRepository.create(productWithNullId); // Tambahkan produk dengan null ID

        // Pastikan produk dengan null ID tetap ada setelah penghapusan produk lain
        productRepository.deleteById(UUID.randomUUID().toString()); // ID tidak cocok

        Iterator<Product> products = productRepository.findAll();
        boolean foundNullIdProduct = false;

        while (products.hasNext()) {
            Product p = products.next();
            if (p.getProductId() == null) {
                foundNullIdProduct = true;
                break;
            }
        }

        assertTrue(foundNullIdProduct, "Produk dengan null ID tidak boleh terhapus.");
    }


}

