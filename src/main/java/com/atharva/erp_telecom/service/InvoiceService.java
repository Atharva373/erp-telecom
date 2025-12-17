package com.atharva.erp_telecom.service;

import com.atharva.erp_telecom.entity.*;
import com.atharva.erp_telecom.enums.IndianState;
import com.atharva.erp_telecom.enums.InvoiceStatus;
import com.atharva.erp_telecom.enums.PaymentStatus;
import com.atharva.erp_telecom.exception.custom_exceptions.ResourceNotFoundException;
import com.atharva.erp_telecom.repository.CompanyRepository;
import com.atharva.erp_telecom.repository.InvoiceItemRepository;
import com.atharva.erp_telecom.repository.InvoiceRepository;
import com.atharva.erp_telecom.repository.OrderRepository;
import com.atharva.erp_telecom.utils.EntityNumberGeneratorUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    public Invoice createInvoiceFromOrder(Long orderId){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));

        if (order.getInvoice() != null) {
            throw new IllegalStateException("Invoice already exists for Order: " + orderId);
        }

        Invoice invoice = new Invoice();
        Customer customerFromOrder = order.getCustomer();
        invoice.setInvoiceNumber(EntityNumberGeneratorUtil.generateInvoiceNumber(order.getOrderId(),customerFromOrder.getCustomerId()));
        invoice.setCustomer(customerFromOrder);
        // Setting order here because Invoice is the owning side.
        invoice.setOrder(order);
        invoice.setInvoiceDate(LocalDateTime.now());
        invoice.setStatus(InvoiceStatus.CREATED);
        invoice.setCreatedBy("PHOTON_ERP");
        // 10 Days of leeway before the Invoice gets overdue.
        invoice.setDueDate(LocalDate.now().plusDays(10));

        Invoice savedInvoice = invoiceRepository.save(invoice);

        Company company = companyRepository
                .findByStateCode(IndianState.MAHARASHTRA.getAbbreviation())
                .orElseThrow(() -> new ResourceNotFoundException("Company NOT FOUND for State Code :-> "+IndianState.MAHARASHTRA.getAbbreviation()));
        List<InvoiceItem> invoiceItems = invoiceItemService.createInvoiceItemsFromOrderItems(invoice,invoice.getOrder().getItems(), company);
        invoice.setInvoiceItems(invoiceItems);

        // Void function to set all the amount fields.
        calculateAndSetInvoiceAmounts(invoice);
        invoice.setPaymentStatus(PaymentStatus.PENDING);

        // order.setInvoice(invoice);
        return invoiceRepository.save(savedInvoice);
    }

    @Transactional
    public Invoice getInvoiceById(Long invoiceId) {
        return invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found with ID: " + invoiceId));
    }

    @Transactional
    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    @Transactional
    public Invoice updateInvoiceStatus(Long invoiceId, InvoiceStatus newStatus) {
        Invoice invoice = getInvoiceById(invoiceId);
        invoice.setStatus(newStatus);
        return invoiceRepository.save(invoice);
    }

    @Transactional
    public void deleteInvoice(Long invoiceId) {
        Invoice invoice = getInvoiceById(invoiceId);
        invoiceRepository.delete(invoice);
    }

    // Helper methods

    /*
        Name: calculateAndSetInvoiceAmounts
        Purpose: To calculate and set amount fields such as subtotal, taxTotal and totalAmount.
     */
    private void calculateAndSetInvoiceAmounts(Invoice invoice){
        BigDecimal subTotal = invoice.getInvoiceItems().stream()
                .map(InvoiceItem::getBaseAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal taxTotal = invoice.getInvoiceItems().stream()
                .map(InvoiceItem::getTaxAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal totalAmount = subTotal.add(taxTotal).setScale(2, RoundingMode.HALF_UP);

        invoice.setSubTotal(subTotal);
        invoice.setTaxTotal(taxTotal);
        invoice.setTotalAmount(totalAmount);
    }

}
