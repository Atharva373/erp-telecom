package com.atharva.erp_telecom.repository.salesorder;

import com.atharva.erp_telecom.entity.salesorder.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
}
