package com.atharva.erp_telecom.salesorder.persistence.repository;

import com.atharva.erp_telecom.salesorder.persistence.transactional.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {
}
