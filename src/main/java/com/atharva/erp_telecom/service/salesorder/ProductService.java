package com.atharva.erp_telecom.service.salesorder;

import com.atharva.erp_telecom.dto.salesorder.ProductResponse;
import com.atharva.erp_telecom.dto.salesorder.ProductUpdateRequest;
import com.atharva.erp_telecom.entity.salesorder.Product;

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