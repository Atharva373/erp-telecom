package com.atharva.erp_telecom.salesorder.service;

import com.atharva.erp_telecom.salesorder.dto.ProductResponse;
import com.atharva.erp_telecom.salesorder.dto.ProductUpdateRequest;
import com.atharva.erp_telecom.salesorder.persistence.masterdata.Product;

import java.util.List;
import java.util.Optional;

// Using an interface instead of a class for loose coupling and having multiple implementations of the same service.

public interface ProductService {
    ProductResponse createProduct(Product product);
    Optional<List<ProductResponse>> getAllProducts();
    Optional<ProductResponse> getProductById(Long id);
    ProductResponse updateProduct(Long id, ProductUpdateRequest product);
    void deleteProduct(Long id);
}