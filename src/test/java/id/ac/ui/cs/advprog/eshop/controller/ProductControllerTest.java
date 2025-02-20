package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    public void testCreateProductPage() throws Exception {
        mockMvc.perform(get("/product/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("CreateProduct"))
                .andExpect(model().attributeExists("product"));
    }

    @Test
    public void testCreateProductPost() throws Exception {
        Product product = new Product();
        product.setProductId(UUID.randomUUID().toString());
        product.setProductName("Laptop");
        product.setProductQuantity(10);

        when(productService.create(any(Product.class))).thenReturn(product); // Ganti doNothing()

        mockMvc.perform(post("/product/create")
                        .param("productName", product.getProductName())
                        .param("productQuantity", String.valueOf(product.getProductQuantity())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("list"));
    }

    @Test
    public void testCreateProductPost_WithExistingId() throws Exception {
        Product product = new Product();
        product.setProductId(UUID.randomUUID().toString()); // Simulasi produk sudah punya ID
        product.setProductName("Laptop");
        product.setProductQuantity(10);

        when(productService.create(any(Product.class))).thenReturn(product);

        mockMvc.perform(post("/product/create")
                        .param("productId", product.getProductId()) // Kirim productId yang sudah ada
                        .param("productName", product.getProductName())
                        .param("productQuantity", String.valueOf(product.getProductQuantity())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("list"));

        verify(productService, times(1)).create(any(Product.class));
    }

    @Test
    public void testCreateProductPost_WithNullProductId() throws Exception {
        Product product = new Product();
        product.setProductId(null); // Simulasi productId null
        product.setProductName("Laptop");
        product.setProductQuantity(10);

        when(productService.create(any(Product.class))).thenReturn(product);

        mockMvc.perform(post("/product/create")
                        .param("productName", product.getProductName())
                        .param("productQuantity", String.valueOf(product.getProductQuantity())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("list"));

        verify(productService, times(1)).create(any(Product.class));
    }

    @Test
    public void testCreateProductPost_WithEmptyProductId() throws Exception {
        Product product = new Product();
        product.setProductId(""); // Simulasi productId kosong
        product.setProductName("Laptop");
        product.setProductQuantity(10);

        when(productService.create(any(Product.class))).thenReturn(product);

        mockMvc.perform(post("/product/create")
                        .param("productId", product.getProductId()) // Kirim ID kosong
                        .param("productName", product.getProductName())
                        .param("productQuantity", String.valueOf(product.getProductQuantity())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("list"));

        verify(productService, times(1)).create(any(Product.class));
    }


    @Test
    public void testProductListPage() throws Exception {
        Product product = new Product();
        product.setProductId(UUID.randomUUID().toString());
        product.setProductName("Laptop");
        product.setProductQuantity(1000);

        List<Product> products = List.of(product);
        when(productService.findAll()).thenReturn(products);

        mockMvc.perform(get("/product/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("ProductList"))
                .andExpect(model().attributeExists("products"));
    }


    @Test
    public void testEditProductPage() throws Exception {
        String productId = UUID.randomUUID().toString();
        Product product = new Product();
        product.setProductId(productId);
        product.setProductName("Laptop");

        when(productService.findAll()).thenReturn(List.of(product));

        mockMvc.perform(get("/product/edit/" + productId))
                .andExpect(status().isOk())
                .andExpect(view().name("EditProduct"))
                .andExpect(model().attributeExists("product"));
    }

    @Test
    public void testEditProductPage_NotFound() throws Exception {
        when(productService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/product/edit/random-id"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/list"));
    }

    @Test
    public void testEditProductPage_WithNullId() throws Exception {
        Product product = new Product();
        product.setProductId(null); // Simulasi produk dengan ID null
        product.setProductName("Laptop");
        product.setProductQuantity(10);

        when(productService.findAll()).thenReturn(List.of(product));

        mockMvc.perform(get("/product/edit/" + UUID.randomUUID().toString())) // ID tidak cocok
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/list"));
    }

    @Test
    public void testEditProductPage_ProductNotFound() throws Exception {
        when(productService.findAll()).thenReturn(List.of()); // Simulasi produk tidak ditemukan

        mockMvc.perform(get("/product/edit/" + UUID.randomUUID().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/list"));
    }

    @Test
    public void testEditProductPage_ProductIdNotMatching() throws Exception {
        String existingProductId = UUID.randomUUID().toString();
        Product product = new Product();
        product.setProductId(existingProductId); // Produk dengan ID tertentu
        product.setProductName("Laptop");
        product.setProductQuantity(10);

        when(productService.findAll()).thenReturn(List.of(product));

        String differentId = UUID.randomUUID().toString(); // ID berbeda

        mockMvc.perform(get("/product/edit/" + differentId)) // ID tidak cocok
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/list"));
    }

    @Test
    public void testEditProductPost() throws Exception {
        Product product = new Product();
        product.setProductId(UUID.randomUUID().toString());
        product.setProductName("Updated Laptop");
        product.setProductQuantity(15);

        when(productService.update(any(Product.class))).thenReturn(product); // Ganti doNothing()

        mockMvc.perform(post("/product/edit")
                        .param("productId", product.getProductId())
                        .param("productName", product.getProductName())
                        .param("productQuantity", String.valueOf(product.getProductQuantity())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/list"));
    }



    @Test
    public void testDeleteProduct() throws Exception {
        String productId = UUID.randomUUID().toString();
        doNothing().when(productService).deleteById(productId);

        mockMvc.perform(get("/product/delete/" + productId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/list"));
    }
}
