package com.atharva.erp_telecom.utils;

import com.atharva.erp_telecom.dto.accounting.*;
import com.atharva.erp_telecom.dto.charging.ChargePlanResponse;
import com.atharva.erp_telecom.dto.crm.CustomerInfoResponse;
import com.atharva.erp_telecom.dto.finance.CompanyRequest;
import com.atharva.erp_telecom.dto.finance.CompanyResponse;
import com.atharva.erp_telecom.dto.finance.InvoiceItemResponse;
import com.atharva.erp_telecom.dto.finance.InvoiceResponse;
import com.atharva.erp_telecom.dto.salesorder.OrderItemResponse;
import com.atharva.erp_telecom.dto.salesorder.OrderResponse;
import com.atharva.erp_telecom.dto.salesorder.ProductResponse;
import com.atharva.erp_telecom.entity.accounting.ChartOfAccount;
import com.atharva.erp_telecom.entity.accounting.PostingRule;
import com.atharva.erp_telecom.entity.accounting.PostingRuleLine;
import com.atharva.erp_telecom.entity.charging.ChargePlan;
import com.atharva.erp_telecom.entity.crm.Customer;
import com.atharva.erp_telecom.entity.finance.Invoice;
import com.atharva.erp_telecom.entity.finance.InvoiceItem;
import com.atharva.erp_telecom.entity.salesorder.Company;
import com.atharva.erp_telecom.entity.salesorder.Order;
import com.atharva.erp_telecom.entity.salesorder.OrderItem;
import com.atharva.erp_telecom.entity.salesorder.Product;
import com.atharva.erp_telecom.exception.custom_exceptions.ResourceNotFoundException;
import com.atharva.erp_telecom.repository.accounting.ChartOfAccountRepository;
import com.atharva.erp_telecom.repository.finance.CompanyRepository;

import java.util.Collections;
import java.util.List;

public class EntityDtoMappers {

    public static OrderResponse mapOrderToOrderResponse(Order order){
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setOrderId(order.getOrderId());
        orderResponse.setOrderNumber(order.getOrderNumber());
        orderResponse.setOrderStatus(order.getStatus());
        orderResponse.setOrderType(order.getOrderType());

        // Null check for Order items in an order.
        List<OrderItem> items = (order.getItems() != null)
                ? order.getItems()
                : Collections.emptyList();

        orderResponse.setOrderItems(
                order.getItems()
                        .stream()
                        .map(EntityDtoMappers::mapOrderItemToOrderItemResponse)
                        .toList()
        );
        orderResponse.setTotalAmount(order.getTotalAmount());
        orderResponse.setCustomerInfo(EntityDtoMappers.mapCustomerToCustomerInfoResponse(order.getCustomer()));
        orderResponse.setRemarks(order.getRemarks());
        orderResponse.setInvoice(EntityDtoMappers.mapInvoiceToInvoiceResponse(order.getInvoice()));
        return orderResponse;
    }

    public static OrderItemResponse mapOrderItemToOrderItemResponse(OrderItem orderItem){
        if (orderItem == null) return null;
        OrderItemResponse orderItemResponse = new OrderItemResponse();
        orderItemResponse.setOrderItemId(orderItem.getOrderItemId());
        orderItemResponse.setOrderLineItemNumber(orderItem.getOrderLineItemNumber());
        orderItemResponse.setProduct(EntityDtoMappers.mapProductToProductResponse(orderItem.getProduct()));
        orderItemResponse.setQuantity(orderItem.getQuantity());
        orderItemResponse.setPrice(orderItem.getPrice());
        orderItemResponse.setUnitPrice(orderItem.getUnitPrice());
        return orderItemResponse;
    }

    public static ProductResponse mapProductToProductResponse(Product product){
        if (product == null) return null;
        ProductResponse productResponse = new ProductResponse();
        productResponse.setProductId(product.getProductId());
        productResponse.setProductCode(product.getProductCode());
        productResponse.setProductName(product.getProductName());
        productResponse.setProductDescription(product.getProductDescription());
        productResponse.setProductCategory(product.getProductCategory());
        ChargePlan defaultPlan = GenericUtils.getDefaultChargePlan(product);
        productResponse.setChargePlan(
                EntityDtoMappers.mapChargePlanToChargePlanResponse(defaultPlan)
        );
        return productResponse;
    }

    public static ChargePlanResponse mapChargePlanToChargePlanResponse(ChargePlan chargePlan){
        if (chargePlan == null) return null;
        ChargePlanResponse chargePlanResponse = new ChargePlanResponse();
        chargePlanResponse.setChargePlanId(chargePlan.getChargePlanId());
        chargePlanResponse.setPlanType(chargePlan.getPlanType().toString());
        chargePlanResponse.setRecurringCharge(chargePlan.getRecurringCharge());
        chargePlanResponse.setOneOffCharge(chargePlan.getOneOffCharge());
        chargePlanResponse.setRatePerUnit(chargePlan.getRatePerUnit());
        chargePlanResponse.setValidityDays(chargePlan.getValidityDays());
        chargePlanResponse.setCurrency(chargePlan.getCurrency());
        return chargePlanResponse;

    }

    public static CustomerInfoResponse mapCustomerToCustomerInfoResponse(Customer customer){
        if (customer == null) return null;
        CustomerInfoResponse customerInfoResponse = new CustomerInfoResponse();
        customerInfoResponse.setCustomerId(customer.getCustomerId());
        customerInfoResponse.setFirstName(customer.getCustomerFirstName());
        customerInfoResponse.setLastName(customer.getCustomerLastName());
        customerInfoResponse.setFullName(customerInfoResponse.getLastName() + ", " + customerInfoResponse.getFirstName());
        customerInfoResponse.setCustomerClass(customer.getCustomerClass());
        customerInfoResponse.setCustomerSubClass(customer.getCustomerSubClass());
        customerInfoResponse.setEmail(customer.getEmail());
        customerInfoResponse.setContactNumber(customer.getContactNumber());
        customerInfoResponse.setRegion(customer.getRegion());
        customerInfoResponse.setGovernmentId(customer.getGovernmentId());
        return customerInfoResponse;
    }

    public static InvoiceResponse mapInvoiceToInvoiceResponse(Invoice invoice){
        if (invoice == null) return null;
        InvoiceResponse invoiceResponse = new InvoiceResponse();
        invoiceResponse.setInvoiceId(invoice.getInvoiceId());
        invoiceResponse.setInvoiceNumber(invoice.getInvoiceNumber());
        invoiceResponse.setSubtotal(invoice.getSubTotal());
        invoiceResponse.setTaxTotal(invoice.getTaxTotal());
        invoiceResponse.setTotalAmount(invoice.getTotalAmount());
        invoiceResponse.setInvoiceDate(invoice.getInvoiceDate());
        invoiceResponse.setDueDate(invoice.getDueDate());
        invoiceResponse.setRemarks(invoice.getRemarks());
        invoiceResponse.setPaymentStatus(invoice.getPaymentStatus());

        // Null check for Invoice items.
        List<InvoiceItem> items = (invoice.getInvoiceItems() != null)
                ? invoice.getInvoiceItems()
                : Collections.emptyList();

        invoiceResponse.setInvoiceItems(
                invoice.getInvoiceItems()
                        .stream()
                        .map(EntityDtoMappers::mapInvoiceItemToInvoiceItemResponse)
                        .toList()
        );
        return invoiceResponse;
    }

    public static InvoiceItemResponse mapInvoiceItemToInvoiceItemResponse(InvoiceItem invoiceItem){
        if (invoiceItem == null) return null;
        InvoiceItemResponse invoiceItemResponse = new InvoiceItemResponse();
        invoiceItemResponse.setInvoiceItemId(invoiceItem.getInvoiceItemId());
        invoiceItemResponse.setInvoiceItemNumber(invoiceItem.getInvoiceItemNumber());
        OrderItem orderItem = invoiceItem.getOrderItem();
        if (orderItem != null) {
            invoiceItemResponse.setOrderItemNumber(orderItem.getOrderLineItemNumber());
        }
        invoiceItemResponse.setQuantity(invoiceItem.getQuantity());
        invoiceItemResponse.setUnitPrice(invoiceItem.getUnitPrice());
        invoiceItemResponse.setBaseAmount(invoiceItem.getBaseAmount());
        invoiceItemResponse.setTaxAmount(invoiceItem.getTaxAmount());
        invoiceItemResponse.setTotalAmount(invoiceItem.getTotalAmount());
        invoiceItemResponse.setAdditionalInfo(invoiceItem.getAdditionalInfo());
        return invoiceItemResponse;
    }

    /**
     *  COMPANY RELATED MAPPERS
     */
    public static Company mapCompanyRequestToCompany(CompanyRequest request, CompanyRepository companyRepository){
        Company company = new Company();
        company.setCompanyCode(request.getCompanyCode());
        company.setCompanyName(request.getCompanyName());
        company.setAddressLine1(request.getAddressLine1());
        company.setAddressLine2(request.getAddressLine2());
        company.setCity(request.getCity());
        company.setState(request.getState());
        company.setStateCode(request.getStateCode());
        company.setGstCode(request.getGstCode());
        company.setGstNumber(request.getGstNumber());
        company.setPanNumber(request.getPanNumber());
        company.setCinNumber(request.getCinNumber());
        company.setEmail(request.getEmail());
        company.setCurrencyCode(request.getCurrencyCode());
        company.setPhoneNumber(request.getPhoneNumber());
        company.setParent(request.getIsParent());

        if(request.getParentCompanyId() != null){
            Company parent = companyRepository.findById(request.getParentCompanyId())
                    .orElseThrow(() -> new RuntimeException("Parent company not found"));
            company.setParentCompany(parent);
        }
        return company;
    }

    public static CompanyResponse mapCompanyToCompanyResponse(Company fetchedCompany, CompanyRepository companyRepository){
        CompanyResponse response = new CompanyResponse();
        response.setCompanyId(fetchedCompany.getCompanyId());
        response.setCompanyCode(fetchedCompany.getCompanyCode());
        response.setCompanyName(fetchedCompany.getCompanyName());
        response.setAddressLine1(fetchedCompany.getAddressLine1());
        response.setAddressLine2(fetchedCompany.getAddressLine2());
        response.setCity(fetchedCompany.getCity());
        response.setState(fetchedCompany.getState());
        response.setStateCode(fetchedCompany.getStateCode());
        response.setGstCode(fetchedCompany.getGstCode());
        response.setGstNumber(fetchedCompany.getGstNumber());
        response.setPanNumber(fetchedCompany.getPanNumber());
        response.setCinNumber(fetchedCompany.getCinNumber());
        response.setEmail(fetchedCompany.getEmail());
        response.setCurrencyCode(fetchedCompany.getCurrencyCode());
        response.setPhoneNumber(fetchedCompany.getPhoneNumber());
        response.setIsParent(fetchedCompany.getParent());
        List<Company> childCompanies = companyRepository.
                findByParentCompany(companyRepository
                        .findById(fetchedCompany.getCompanyId()).orElseThrow(() -> new RuntimeException("Parent not found.")));
        response.setChildCompanies(childCompanies);
        response.setCreatedOn(fetchedCompany.getCreatedOn());
        response.setUpdatedOn(fetchedCompany.getUpdatedOn());
        return response;
    }


    /**
    *   POSTING RULES - MAPPERS.
    */

    public static PostingRule mapPostingRuleRequestToPostingRuleEntity(PostingRuleRequest request, ChartOfAccountRepository chartOfAccountRepository, CompanyRepository companyRepository){
        if(request == null) return null;
        PostingRule postingRule = new PostingRule();
        postingRule.setEventType(request.getEventType());
        postingRule.setDescription(request.getDescription());
        postingRule.setCompany(companyRepository.findByCompanyCode(
                request.getCompanyCode())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Company NOT FOUND for Code :-> "+request.getCompanyCode())
                )
        );
        postingRule.setHeaderConditionExpression(request.getConditionExpression());
        postingRule.setEffectiveFrom(request.getEffectiveFrom());
        postingRule.setEffectiveTo(request.getEffectiveTo());
        postingRule.setActive(request.getActive());
        postingRule.setItems(
                request.getLines()
                .stream()
                .map(line -> mapPostingRuleLineRequestToPostingRuleLine(line,chartOfAccountRepository))
                .toList()
        );
        return postingRule;
    }

    public static PostingRuleLine mapPostingRuleLineRequestToPostingRuleLine(PostingRuleLineDTO line, ChartOfAccountRepository chartOfAccountRepository){
        if(line == null) return null;
        PostingRuleLine postingRuleLine = new PostingRuleLine();
        postingRuleLine.setAccount(chartOfAccountRepository.findByAccountCode(line.getAccountCode())
                .orElseThrow(() -> new ResourceNotFoundException("CoA NOT FOUND FOR :-> "+line.getAccountCode())));
        postingRuleLine.setEntryType(line.getEntryType());
        postingRuleLine.setAmountExpression(line.getAmountExpression());
        return postingRuleLine;
    }

    public static PostingRuleResponse mapPostingRuleToPostingRuleResponse(PostingRule postingRule){
        if(postingRule == null) return null;
        PostingRuleResponse response = new PostingRuleResponse();
        response.setId(postingRule.getId());
        response.setEventType(postingRule.getEventType());
        response.setDescription(postingRule.getDescription());
        response.setConditionExpression(postingRule.getHeaderConditionExpression());
        response.setCompanyCode(postingRule.getCompany().getCompanyCode());
        response.setEffectiveFrom(postingRule.getEffectiveFrom());
        response.setEffectiveTo(postingRule.getEffectiveTo());
        response.setCreatedOn(postingRule.getCreatedOn());
        response.setModifiedOn(postingRule.getModifiedOn());
        response.setCreatedBy(postingRule.getCreatedBy());
        response.setActive(postingRule.isActive());
        response.setModifiedBy(postingRule.getModifiedBy());
        response.setLines(
                postingRule.getItems()
                        .stream()
                        .map(EntityDtoMappers::mapPostingRuleLineToPostingRuleLineDTO)
                        .toList()
        );
        return response;
    }

    public static PostingRuleLineDTO mapPostingRuleLineToPostingRuleLineDTO(PostingRuleLine line){
        if(line == null) return null;
        PostingRuleLineDTO responseLine = new PostingRuleLineDTO();
        responseLine.setEntryType(line.getEntryType());
        responseLine.setAmountExpression(line.getAmountExpression());
        responseLine.setAccountCode(line.getAccount().getAccountCode());
        return responseLine;
    }

    /**
     *   CHART OF ACCOUNT - MAPPERS
     */
    public static ChartOfAccount mapCoaRequestToCoa(ChartOfAccountRequest request, Company company){
        ChartOfAccount chartOfAccount = new ChartOfAccount();
        chartOfAccount.setAccountCode(request.getAccountCode());
        chartOfAccount.setAccountName(request.getAccountName());
        chartOfAccount.setCategory(request.getCategory());
        chartOfAccount.setSubtype(request.getSubtype());
        chartOfAccount.setNormalBalance(request.getNormalBalance());
        chartOfAccount.setActive(request.getActive());
        chartOfAccount.setEffectiveFrom(request.getEffectiveFrom());
        chartOfAccount.setEffectiveTo(request.getEffectiveTo());
        chartOfAccount.setCompany(company);
        return chartOfAccount;
    }


    public static ChartOfAccountResponse mapCoaToCoaResponse(ChartOfAccount chartOfAccount){
        ChartOfAccountResponse response = new ChartOfAccountResponse();
        response.setId(chartOfAccount.getId());
        response.setAccountCode(chartOfAccount.getAccountCode());
        response.setAccountName(chartOfAccount.getAccountName());
        response.setCategory(chartOfAccount.getCategory());
        response.setSubtype(chartOfAccount.getSubtype());
        response.setNormalBalance(chartOfAccount.getNormalBalance());
        response.setActive(chartOfAccount.isActive());
        response.setEffectiveFrom(chartOfAccount.getEffectiveFrom());
        response.setEffectiveTo(chartOfAccount.getEffectiveTo());
        response.setCreatedOn(chartOfAccount.getCreatedOn());
        response.setModifiedOn(chartOfAccount.getModifiedOn());
        response.setCreatedBy(chartOfAccount.getCreatedBy());
        response.setModifiedBy(chartOfAccount.getModifiedBy());
        response.setCompanyCode(chartOfAccount.getCompany().getCompanyCode());
        return response;
    }

}
