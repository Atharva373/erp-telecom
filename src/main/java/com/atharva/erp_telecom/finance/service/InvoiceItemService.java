package com.atharva.erp_telecom.finance.service;


import com.atharva.erp_telecom.crm.persistence.masterdata.BusinessEntity;
import com.atharva.erp_telecom.finance.persistence.transactional.InvoiceItemEntity;
import com.atharva.erp_telecom.finance.persistence.transactional.InvoiceEntity;
import com.atharva.erp_telecom.finance.persistence.masterdata.TaxEntity;
import com.atharva.erp_telecom.finance.persistence.masterdata.CompanyEntity;
import com.atharva.erp_telecom.salesorder.persistence.transactional.OrderItem;
import com.atharva.erp_telecom.finance.persistence.repository.InvoiceItemRepository;
import com.atharva.erp_telecom.finance.persistence.repository.TaxRepository;
import com.atharva.erp_telecom.utils.EntityNumberGeneratorUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class InvoiceItemService {
    private final InvoiceItemRepository invoiceItemRepository;
    private final TaxRepository taxRepository;

    @Autowired
    public InvoiceItemService(InvoiceItemRepository invoiceItemRepository, TaxRepository taxRepository) {
        this.invoiceItemRepository = invoiceItemRepository;
        this.taxRepository = taxRepository;
    }

    @Transactional
    public List<InvoiceItemEntity> createInvoiceItemsFromOrderItems(InvoiceEntity invoiceEntity, List<OrderItem> orderItems, CompanyEntity companyEntity){
        if (invoiceEntity == null || orderItems == null || orderItems.isEmpty()) {
            throw new IllegalArgumentException("InvoiceEntity and OrderItems must not be null or empty");
        }

        AtomicInteger counter = new AtomicInteger(1);

        // Mapping InvoiceItemEntity <--> OrderItem one-to-one
        return orderItems.stream().map(orderItem -> {
            InvoiceItemEntity invoiceItemEntity = new InvoiceItemEntity();

            invoiceItemEntity.setInvoiceEntity(invoiceEntity);
            invoiceItemEntity.setOrderItem(orderItem);
            invoiceItemEntity.setQuantity(orderItem.getQuantity());
            if (orderItem.getProduct() == null) {
                throw new IllegalStateException("OrderItem does not have a linked or a valid Product.");
            }
            invoiceItemEntity.setProduct(orderItem.getProduct());
            invoiceItemEntity.setUnitPrice(orderItem.getUnitPrice());
            String invoiceItemNumber = EntityNumberGeneratorUtil.generateInvoiceItemNumber(
                    invoiceEntity.getInvoiceNumber(),
                    counter.getAndIncrement());
            invoiceItemEntity.setInvoiceItemNumber(invoiceItemNumber);

            BigDecimal baseAmount = orderItem.getPrice()
                    .multiply(BigDecimal.valueOf(orderItem.getQuantity()))
                    .setScale(2, RoundingMode.HALF_UP);
            invoiceItemEntity.setBaseAmount(baseAmount);

            BigDecimal taxAmount = calculateApplicableTaxPerInvoiceItem(orderItem, companyEntity, invoiceItemEntity);
            invoiceItemEntity.setTaxAmount(taxAmount);

            BigDecimal total = baseAmount.add(taxAmount).setScale(2, RoundingMode.HALF_UP);
            invoiceItemEntity.setTotalAmount(total);

            invoiceItemEntity.setAdditionalInfo(String.format("Order item: %s and InvoiceEntity number: %s", orderItem.getOrderLineItemNumber(), invoiceEntity.getInvoiceNumber()));

            invoiceEntity.addItem(invoiceItemEntity);
            return invoiceItemEntity;

        }).collect(Collectors.toList());
    }

    @Transactional
    public InvoiceItemEntity getInvoiceItem(Long invoiceItemId) {
        return invoiceItemRepository.findById(invoiceItemId)
                .orElseThrow(() -> new IllegalArgumentException("InvoiceItemEntity not found with ID: " + invoiceItemId));
    }

    @Transactional
    public void deleteInvoiceItem(Long invoiceItemId) {
        InvoiceItemEntity item = getInvoiceItem(invoiceItemId);
        invoiceItemRepository.delete(item);
    }

    // Helper methods:

    /*
        Name: calculateApplicableTaxPerInvoiceItem()
        Purpose: To calculate the tax per order item and set it in each invoice line item.
     */
    private BigDecimal calculateApplicableTaxPerInvoiceItem(OrderItem orderItem, CompanyEntity companyEntity, InvoiceItemEntity invoiceItemEntity) {
        if (orderItem == null || orderItem.getOrder() == null || orderItem.getOrder().getBusinessEntity() == null) {
            throw new IllegalArgumentException("OrderItem OR Order OR Customer cannot be null");
        }

        BusinessEntity customer = orderItem.getOrder().getBusinessEntity();
        boolean isIntraState = customer.getRegion().equalsIgnoreCase(companyEntity.getStateCode());

        Optional<TaxEntity> gstTaxOpt = taxRepository.findByTaxCode("GST_18");
        if (gstTaxOpt.isEmpty()) return BigDecimal.ZERO;

        TaxEntity taxEntity = gstTaxOpt.get();
        invoiceItemEntity.setTaxEntity(taxEntity);

        double rate = isIntraState ?
                taxEntity.getCgstPercentage() + taxEntity.getSgstPercentage() :
                taxEntity.getIgstPercentage();
        BigDecimal totalTaxPercentage = BigDecimal.valueOf(rate);

        return orderItem.getPrice()
                .multiply(BigDecimal.valueOf(orderItem.getQuantity()))
                .multiply(totalTaxPercentage.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP))
                .setScale(2, RoundingMode.HALF_UP);
    }


}
