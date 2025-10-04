package com.atharva.erp_telecom.controller;

import com.atharva.erp_telecom.dto.ProductUpdateRequest;
import com.atharva.erp_telecom.entity.Product;
import com.atharva.erp_telecom.exception.custom_exceptions.ProductNotFoundException;
import com.atharva.erp_telecom.service.ProductService;
import com.atharva.erp_telecom.service.implementation.ProductServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(@Qualifier("web_implementation") ProductService productService){
        this.productService = productService;
    }
    // NOTE: About @PreAuthorize()
    /*
        1. What @PreAuthorize does:
            It is a Spring Security annotation  used at the method level (controller, service, etc.).
            It tells Spring Security:
            -- “Before this method is executed, evaluate the given security expression and only proceed if it’s true.”
            -- If the expression evaluates to false, Spring blocks access and throws an AccessDeniedException
        2. The string you see ("hasRole('ADMIN')" or "hasAnyRole('ADMIN','USER')" etc.) is not a Java method you wrote.
            -- It’s a Spring Expression Language (SpEL) expression, evaluated by Spring Security at runtime.
            -- Some common expressions:
                hasRole('ADMIN') → user must have ROLE_ADMIN.
                hasAnyRole('ADMIN','USER') → user must have at least one of these roles.
                hasAuthority('PRODUCT_CREATE') → check for a fine-grained authority/permission.
                #id == authentication.principal.id → check that the id path variable matches the logged-in user’s ID.
                isAuthenticated() → any logged-in user.
                permitAll() → no restrictions.

     */
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> createProduct(@RequestBody Product productRequest){
        Product savedProduct = productService.createProduct(productRequest);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    // NOTE: According to BEST practices for filtering and fetching entities using a unique value such as a Primary Key like ID,
    // always use the Path Variables like /products/{productId}. While when filtering based on some non-unique params, use query params like
    // /products?productCode = 'SMS'
    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<Product>> getAllProducts(){
        Optional<List<Product>> fetchedProducts = productService.getAllProducts();
        return new ResponseEntity<>(fetchedProducts.get(),HttpStatus.OK);
    }

    // NOTE: The name of the Path variable and the method parameter should be same unless explicitly specified.
    // e.g. If we have a path variable {id}, if the value in @PathVariable("id") is not specified, then the
    // variable name should always be same as the Path variable name, i.e. Long id
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Product> getProductById(@PathVariable("id") Long productId){
        Product fetchedProduct = productService.getProductById(productId)
                .orElseThrow(()->new ProductNotFoundException(productId));
        return ResponseEntity.ok(fetchedProduct);
    }

    @PatchMapping("/{id}")        // Can be replaced with PatchMapping for partial updates for an entity. PUT usually expects an entire payload.
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> updateProduct(@PathVariable("id") Long productId, @RequestBody ProductUpdateRequest newProduct){
        Product updatedProduct = productService.updateProduct(productId,newProduct);
        return new ResponseEntity<>(updatedProduct,HttpStatus.OK);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId){
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

}
