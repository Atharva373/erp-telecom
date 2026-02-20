package com.atharva.erp_telecom.finance.persistence.repository;

import com.atharva.erp_telecom.entity.crm.Customer;
import com.atharva.erp_telecom.finance.persistence.transactional.InvoiceEntity;
import com.atharva.erp_telecom.salesorder.persistence.transactional.Order;
import com.atharva.erp_telecom.invoicing.enums.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface InvoiceRepository extends JpaRepository<InvoiceEntity,Long> {
    Optional<InvoiceEntity> findByInvoiceNumber(String invoiceNumber);

    Optional<InvoiceEntity> findByOrder(Order order);

    List<InvoiceEntity> findByCustomer(Customer customer);

    List<InvoiceEntity> findByStatus(InvoiceStatus status);

    List<InvoiceEntity> findByInvoiceDateBetween(LocalDateTime start, LocalDateTime end);
}
