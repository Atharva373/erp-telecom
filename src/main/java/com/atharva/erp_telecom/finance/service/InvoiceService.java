package com.atharva.erp_telecom.finance.service;

import com.atharva.erp_telecom.crm.persistence.masterdata.BusinessEntity;
import com.atharva.erp_telecom.finance.persistence.transactional.InvoiceItemEntity;
import com.atharva.erp_telecom.finance.persistence.transactional.InvoiceEntity;
import com.atharva.erp_telecom.finance.persistence.masterdata.CompanyEntity;
import com.atharva.erp_telecom.salesorder.persistence.transactional.Order;
import com.atharva.erp_telecom.crm.enums.IndianState;
import com.atharva.erp_telecom.invoicing.enums.InvoiceStatus;
import com.atharva.erp_telecom.invoicing.enums.PaymentStatus;
import com.atharva.erp_telecom.exception.custom_exceptions.ResourceNotFoundException;
import com.atharva.erp_telecom.finance.persistence.repository.CompanyRepository;
import com.atharva.erp_telecom.finance.persistence.repository.InvoiceRepository;
import com.atharva.erp_telecom.salesorder.persistence.repository.OrderRepository;
import com.atharva.erp_telecom.utils.EntityNumberGeneratorUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceItemService invoiceItemService;
    private final OrderRepository orderRepository;
    private final CompanyRepository companyRepository;

    @Autowired
    public InvoiceService(InvoiceRepository invoiceRepository, InvoiceItemService invoiceItemService,OrderRepository orderRepository, CompanyRepository companyRepository) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceItemService = invoiceItemService;
        this.orderRepository = orderRepository;
        this.companyRepository = companyRepository;
    }

    @Transactional
    public InvoiceEntity createInvoiceFromOrder(Long orderId){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));

        if (order.getInvoiceEntity() != null) {
            throw new IllegalStateException("InvoiceEntity already exists for Order: " + orderId);
        }

        InvoiceEntity invoiceEntity = new InvoiceEntity();
        BusinessEntity customerFromOrder = order.getBusinessEntity();
        invoiceEntity.setInvoiceNumber(EntityNumberGeneratorUtil.generateInvoiceNumber(order.getOrderId(),customerFromOrder.getBusinessEntityId()));
        invoiceEntity.setBusinessEntity(customerFromOrder);
        // Setting order here because InvoiceEntity is the owning side.
        invoiceEntity.setOrder(order);
        invoiceEntity.setInvoiceDate(LocalDateTime.now());
        invoiceEntity.setStatus(InvoiceStatus.CREATED);
        invoiceEntity.setCreatedBy("PHOTON_ERP");
        // 10 Days of leeway before the InvoiceEntity gets overdue.
        invoiceEntity.setDueDate(LocalDate.now().plusDays(10));

        InvoiceEntity savedInvoiceEntity = invoiceRepository.save(invoiceEntity);

        CompanyEntity companyEntity = companyRepository
                .findByStateCode(IndianState.MAHARASHTRA.getAbbreviation())
                .orElseThrow(() -> new ResourceNotFoundException("CompanyEntity NOT FOUND for State Code :-> "+IndianState.MAHARASHTRA.getAbbreviation()));
        List<InvoiceItemEntity> invoiceItemEntities = invoiceItemService.createInvoiceItemsFromOrderItems(invoiceEntity, invoiceEntity.getOrder().getItems(), companyEntity);
        invoiceEntity.setInvoiceItemEntities(invoiceItemEntities);

        // Void function to set all the amount fields.
        calculateAndSetInvoiceAmounts(invoiceEntity);
        invoiceEntity.setPaymentStatus(PaymentStatus.PENDING);

        // order.setInvoiceEntity(invoiceEntity);
        return invoiceRepository.save(savedInvoiceEntity);
    }

    @Transactional
    public InvoiceEntity getInvoiceById(Long invoiceId) {
        return invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new IllegalArgumentException("InvoiceEntity not found with ID: " + invoiceId));
    }

    @Transactional
    public List<InvoiceEntity> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    @Transactional
    public InvoiceEntity updateInvoiceStatus(Long invoiceId, InvoiceStatus newStatus) {
        InvoiceEntity invoiceEntity = getInvoiceById(invoiceId);
        invoiceEntity.setStatus(newStatus);
        return invoiceRepository.save(invoiceEntity);
    }

    @Transactional
    public void deleteInvoice(Long invoiceId) {
        InvoiceEntity invoiceEntity = getInvoiceById(invoiceId);
        invoiceRepository.delete(invoiceEntity);
    }

    // Helper methods

    /*
        Name: calculateAndSetInvoiceAmounts
        Purpose: To calculate and set amount fields such as subtotal, taxTotal and totalAmount.
     */
    private void calculateAndSetInvoiceAmounts(InvoiceEntity invoiceEntity){
        BigDecimal subTotal = invoiceEntity.getInvoiceItemEntities().stream()
                .map(InvoiceItemEntity::getBaseAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal taxTotal = invoiceEntity.getInvoiceItemEntities().stream()
                .map(InvoiceItemEntity::getTaxAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal totalAmount = subTotal.add(taxTotal).setScale(2, RoundingMode.HALF_UP);

        invoiceEntity.setSubTotal(subTotal);
        invoiceEntity.setTaxTotal(taxTotal);
        invoiceEntity.setTotalAmount(totalAmount);
    }

}
