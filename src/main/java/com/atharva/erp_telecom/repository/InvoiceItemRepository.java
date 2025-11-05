package com.atharva.erp_telecom.repository;

import com.atharva.erp_telecom.entity.Invoice;
import com.atharva.erp_telecom.entity.InvoiceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceItemRepository extends JpaRepository<InvoiceItem,Long> {
    List<InvoiceItem> findByInvoice(Invoice invoice);

    Optional<InvoiceItem> findByInvoiceItemNumber(String invoiceItemNumber);

    Long countByInvoice(Invoice invoice);
}
