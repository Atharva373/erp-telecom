package com.atharva.erp_telecom.repository.crm;


import com.atharva.erp_telecom.entity.crm.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {

}
