package com.atharva.erp_telecom.finance.persistence.repository;

import com.atharva.erp_telecom.finance.persistence.transactional.InvoiceItemEntity;
import com.atharva.erp_telecom.finance.persistence.transactional.InvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceItemRepository extends JpaRepository<InvoiceItemEntity,Long> {
    List<InvoiceItemEntity> findByInvoiceEntity(InvoiceEntity invoiceEntity);

    Optional<InvoiceItemEntity> findByInvoiceItemNumber(String invoiceItemNumber);

    Long countByInvoiceEntity(InvoiceEntity invoiceEntity);
}

