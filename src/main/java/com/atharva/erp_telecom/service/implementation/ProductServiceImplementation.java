package com.atharva.erp_telecom.service.implementation;

import com.atharva.erp_telecom.dto.ProductUpdateRequest;
import com.atharva.erp_telecom.entity.ChargePlan;
import com.atharva.erp_telecom.entity.Product;
import com.atharva.erp_telecom.enums.PlanType;
import com.atharva.erp_telecom.exception.custom_exceptions.ChargePlanNotFoundException;
import com.atharva.erp_telecom.exception.custom_exceptions.ProductNotFoundException;
import com.atharva.erp_telecom.repository.ProductRepository;
import com.atharva.erp_telecom.service.ProductService;
import com.atharva.erp_telecom.utils.CrudUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service("web_implementation")
public class ProductServiceImplementation implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImplementation(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    // Explicitly back-mapping the current product to the Chargeplan.
    @Override
    public Product createProduct(Product product) {
        for(ChargePlan chargePlan: product.getChargePlans()){
            chargePlan.setProduct(product);
        }
        return productRepository.save(product);
    }

    @Override
    public Optional<List<Product>> getAllProducts() {
        return Optional.of(productRepository.findAll());
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Product updateProduct(Long id, ProductUpdateRequest newProduct) {
        Product existingProduct = getProductById(id).orElseThrow(() -> new ProductNotFoundException(id));
        CrudUtils.updateIfNotNull(existingProduct::setProductCode, newProduct.getProductCode());
        CrudUtils.updateIfNotNull(existingProduct::setProductName, newProduct.getProductName());
        CrudUtils.updateIfNotNull(existingProduct::setProductDescription, newProduct.getProductDescription());
        CrudUtils.updateIfNotNull(existingProduct::setProductCategory, newProduct.getProductCategory());
        CrudUtils.updateIfNotNull(existingProduct::setActive, newProduct.isActive());
        CrudUtils.updateIfNotNull(existingProduct::setBundle, newProduct.isBundle());
        return productRepository.save(existingProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }


    // ------------------
    // Helper Methods
    // ------------------

    public BigDecimal getDisplayOnlyProductPrice(Product product) {
         ChargePlan defaultChargePlan = product.getChargePlans().stream()
                .filter(ChargePlan::isDefault)
                .findFirst()
                 .orElseThrow(() -> new ChargePlanNotFoundException("Default ChargePlan not found"));

        BigDecimal recurring = defaultChargePlan.getRecurringCharge() != null
                ? defaultChargePlan.getRecurringCharge()
                : BigDecimal.ZERO;

        BigDecimal oneOff = defaultChargePlan.getOneOffCharge() != null
                ? defaultChargePlan.getOneOffCharge()
                : BigDecimal.ZERO;

        return recurring.add(oneOff).setScale(2, RoundingMode.HALF_UP);
    }

}
