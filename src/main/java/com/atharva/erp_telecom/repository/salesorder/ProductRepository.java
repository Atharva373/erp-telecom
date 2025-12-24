package com.atharva.erp_telecom.repository.salesorder;


import com.atharva.erp_telecom.entity.salesorder.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    boolean existsByProductCode(String productCode);
}
