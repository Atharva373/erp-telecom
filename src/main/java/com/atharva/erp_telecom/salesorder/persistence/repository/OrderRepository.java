package com.atharva.erp_telecom.salesorder.persistence.repository;

import com.atharva.erp_telecom.salesorder.persistence.transactional.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
}
