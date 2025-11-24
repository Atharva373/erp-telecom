package com.atharva.erp_telecom.repository;

import com.atharva.erp_telecom.entity.Customer;
import com.atharva.erp_telecom.entity.Invoice;
import com.atharva.erp_telecom.entity.Order;
import com.atharva.erp_telecom.enums.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface InvoiceRepository extends JpaRepository<Invoice,Long> {
    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);

    Optional<Invoice> findByOrder(Order order);

    List<Invoice> findByCustomer(Customer customer);

    List<Invoice> findByStatus(InvoiceStatus status);

    List<Invoice> findByInvoiceDateBetween(LocalDateTime start, LocalDateTime end);
}
