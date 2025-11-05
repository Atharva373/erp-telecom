package com.atharva.erp_telecom.service;


import com.atharva.erp_telecom.entity.*;
import com.atharva.erp_telecom.repository.InvoiceItemRepository;
import com.atharva.erp_telecom.repository.TaxRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

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
    public List<InvoiceItem> createInvoiceItemsFromOrderItems(Invoice invoice, List<OrderItem> orderItems, Company company){
        if (invoice == null || orderItems == null || orderItems.isEmpty()) {
            throw new IllegalArgumentException("Invoice and OrderItems must not be null or empty");
        }

        AtomicInteger counter = new AtomicInteger(1);

        // Mapping InvoiceItem <--> OrderItem one-to-one
        return orderItems.stream().map(orderItem -> {
            InvoiceItem invoiceItem = new InvoiceItem();

            invoiceItem.setInvoice(invoice);
            invoiceItem.setOrderItem(orderItem);

            if (orderItem.getProduct() == null) {
                throw new IllegalStateException("OrderItem does not have a linked Product.");
            }
            invoiceItem.setProduct(orderItem.getProduct());

            String invoiceItemNumber = String.format("%s_ITEM_%d",
                    invoice.getInvoiceNumber(),
                    counter.getAndIncrement());
            invoiceItem.setInvoiceItemNumber(invoiceItemNumber);

            BigDecimal baseAmount = orderItem.getPrice()
                    .multiply(BigDecimal.valueOf(orderItem.getQuantity()))
                    .setScale(2, RoundingMode.HALF_UP);
            invoiceItem.setBaseAmount(baseAmount);

            BigDecimal taxAmount = calculateApplicableTaxPerInvoiceItem(orderItem, company, invoiceItem);
            invoiceItem.setTaxAmount(taxAmount);

            BigDecimal total = baseAmount.add(taxAmount).setScale(2, RoundingMode.HALF_UP);
            invoiceItem.setTotalAmount(total);

            invoiceItem.setAdditionalInfo(String.format("Generated from Order item: %s and Invoice number: %s", orderItem.getOrderLineItemNumber(),invoice.getInvoiceNumber()));

            invoice.addItem(invoiceItem);
            return invoiceItemRepository.save(invoiceItem);

        }).toList();
    }

    @Transactional
    public InvoiceItem getInvoiceItem(Long invoiceItemId) {
        return invoiceItemRepository.findById(invoiceItemId)
                .orElseThrow(() -> new IllegalArgumentException("InvoiceItem not found with ID: " + invoiceItemId));
    }

    @Transactional
    public void deleteInvoiceItem(Long invoiceItemId) {
        InvoiceItem item = getInvoiceItem(invoiceItemId);
        invoiceItemRepository.delete(item);
    }

    // Helper methods:

    /*
        Name: calculateApplicableTaxPerInvoiceItem()
        Purpose: To calculate the tax per order item and set it in each invoice line item.
     */
    private BigDecimal calculateApplicableTaxPerInvoiceItem(OrderItem orderItem, Company company, InvoiceItem invoiceItem) {
        if (orderItem == null || orderItem.getOrder() == null || orderItem.getOrder().getCustomer() == null) {
            throw new IllegalArgumentException("OrderItem OR Order OR Customer cannot be null");
        }

        Customer customer = orderItem.getOrder().getCustomer();
        boolean isIntraState = customer.getRegion().equalsIgnoreCase(company.getStateCode());

        Optional<Tax> gstTaxOpt = taxRepository.findByTaxCode("GST_18");
        if (gstTaxOpt.isEmpty()) {
            return BigDecimal.ZERO;
        }

        Tax tax = gstTaxOpt.get();
        invoiceItem.setTax(tax);

        double rate = isIntraState ?
                tax.getCgstPercentage() + tax.getSgstPercentage() :
                tax.getIgstPercentage();
        BigDecimal totalTaxPercentage = BigDecimal.valueOf(rate);

        return orderItem.getPrice()
                .multiply(BigDecimal.valueOf(orderItem.getQuantity()))
                .multiply(totalTaxPercentage.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP))
                .setScale(2, RoundingMode.HALF_UP);
    }


}
