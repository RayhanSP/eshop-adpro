package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product create(Product product) {
        if (product.getProductId() == null || product.getProductId().isEmpty()) {
            product.setProductId(UUID.randomUUID().toString());
        }
        productRepository.create(product);
        return product;
    }


    @Override
    public void deleteById(String productId) {
        if (productId == null || productId.isEmpty()) {
            throw new IllegalArgumentException("Product ID cannot be null or empty");
        }

        productRepository.deleteById(productId);
    }



    @Override
    public List<Product> findAll() {
        Iterator<Product> productIterator = productRepository.findAll();
        List<Product> allProduct = new ArrayList<>();
        productIterator.forEachRemaining(allProduct::add);
        return allProduct;
    }


    @Override
    public Product update(Product updatedProduct) {
        return productRepository.update(updatedProduct);
    }

}
