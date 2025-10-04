package com.atharva.erp_telecom.service;

import com.atharva.erp_telecom.dto.ProductUpdateRequest;
import com.atharva.erp_telecom.entity.Product;

import java.util.List;
import java.util.Optional;

// Using an interface instead of a class for loose coupling and having multiple implementations of the same service.

public interface ProductService {
    Product createProduct(Product product);
    Optional<List<Product>> getAllProducts();
    Optional<Product> getProductById(Long id);
    Product updateProduct(Long id, ProductUpdateRequest product);
    void deleteProduct(Long id);
}
