package com.atharva.erp_telecom.utils;

import com.atharva.erp_telecom.accounting.dto.*;
import com.atharva.erp_telecom.invoicing.dto.ChargePlanResponse;
import com.atharva.erp_telecom.crm.dto.BusinessEntityInfoResponse;
import com.atharva.erp_telecom.finance.dto.CompanyRequest;
import com.atharva.erp_telecom.finance.dto.CompanyResponse;
import com.atharva.erp_telecom.finance.dto.InvoiceItemResponse;
import com.atharva.erp_telecom.finance.dto.InvoiceResponse;
import com.atharva.erp_telecom.salesorder.dto.OrderItemResponse;
import com.atharva.erp_telecom.salesorder.dto.OrderResponse;
import com.atharva.erp_telecom.salesorder.dto.ProductResponse;
import com.atharva.erp_telecom.accounting.persistence.masterdata.ChartOfAccountEntity;
import com.atharva.erp_telecom.accounting.persistence.masterdata.PostingRuleEntity;
import com.atharva.erp_telecom.accounting.persistence.masterdata.PostingRuleLineEntity;
import com.atharva.erp_telecom.invoicing.persistence.masterdata.ChargePlan;
import com.atharva.erp_telecom.crm.persistence.masterdata.BusinessEntity;
import com.atharva.erp_telecom.finance.persistence.transactional.InvoiceItemEntity;
import com.atharva.erp_telecom.finance.persistence.transactional.InvoiceEntity;
import com.atharva.erp_telecom.finance.persistence.masterdata.CompanyEntity;
import com.atharva.erp_telecom.salesorder.persistence.transactional.Order;
import com.atharva.erp_telecom.salesorder.persistence.transactional.OrderItem;
import com.atharva.erp_telecom.salesorder.persistence.masterdata.Product;
import com.atharva.erp_telecom.exception.custom_exceptions.ResourceNotFoundException;
import com.atharva.erp_telecom.accounting.persistence.repository.ChartOfAccountRepository;
import com.atharva.erp_telecom.finance.persistence.repository.CompanyRepository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
        orderResponse.setBusinessEntityInfoResponse(EntityDtoMappers.mapCustomerToCustomerInfoResponse(order.getBusinessEntity()));
        orderResponse.setRemarks(order.getRemarks());
        orderResponse.setInvoice(EntityDtoMappers.mapInvoiceToInvoiceResponse(order.getInvoiceEntity()));
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

    public static BusinessEntityInfoResponse mapCustomerToCustomerInfoResponse(BusinessEntity customer){
        if (customer == null) return null;
        BusinessEntityInfoResponse businessEntityInfoResponse = new BusinessEntityInfoResponse();
        businessEntityInfoResponse.setId(customer.getBusinessEntityId());
        businessEntityInfoResponse.setFirstName(customer.getFirstName());
        businessEntityInfoResponse.setLastName(customer.getLastName());
        businessEntityInfoResponse.setFullName(businessEntityInfoResponse.getLastName() + ", " + businessEntityInfoResponse.getFirstName());
        businessEntityInfoResponse.setEntityClass(customer.getEntityClass());
        businessEntityInfoResponse.setEntitySubClass(customer.getEntitySubClass());
        businessEntityInfoResponse.setEmail(customer.getEmail());
        businessEntityInfoResponse.setContactNumber(customer.getContactNumber());
        businessEntityInfoResponse.setRegion(customer.getRegion());
        businessEntityInfoResponse.setGovernmentId(customer.getGovernmentId());
        return businessEntityInfoResponse;
    }

    public static InvoiceResponse mapInvoiceToInvoiceResponse(InvoiceEntity invoiceEntity){
        if (invoiceEntity == null) return null;
        InvoiceResponse invoiceResponse = new InvoiceResponse();
        invoiceResponse.setInvoiceId(invoiceEntity.getInvoiceId());
        invoiceResponse.setInvoiceNumber(invoiceEntity.getInvoiceNumber());
        invoiceResponse.setSubtotal(invoiceEntity.getSubTotal());
        invoiceResponse.setTaxTotal(invoiceEntity.getTaxTotal());
        invoiceResponse.setTotalAmount(invoiceEntity.getTotalAmount());
        invoiceResponse.setInvoiceDate(invoiceEntity.getInvoiceDate());
        invoiceResponse.setDueDate(invoiceEntity.getDueDate());
        invoiceResponse.setRemarks(invoiceEntity.getRemarks());
        invoiceResponse.setPaymentStatus(invoiceEntity.getPaymentStatus());

        // Null check for InvoiceEntity items.
        List<InvoiceItemEntity> items = (invoiceEntity.getInvoiceItemEntities() != null)
                ? invoiceEntity.getInvoiceItemEntities()
                : Collections.emptyList();

        invoiceResponse.setInvoiceItems(
                invoiceEntity.getInvoiceItemEntities()
                        .stream()
                        .map(EntityDtoMappers::mapInvoiceItemToInvoiceItemResponse)
                        .toList()
        );
        return invoiceResponse;
    }

    public static InvoiceItemResponse mapInvoiceItemToInvoiceItemResponse(InvoiceItemEntity invoiceItemEntity){
        if (invoiceItemEntity == null) return null;
        InvoiceItemResponse invoiceItemResponse = new InvoiceItemResponse();
        invoiceItemResponse.setInvoiceItemId(invoiceItemEntity.getInvoiceItemId());
        invoiceItemResponse.setInvoiceItemNumber(invoiceItemEntity.getInvoiceItemNumber());
        OrderItem orderItem = invoiceItemEntity.getOrderItem();
        if (orderItem != null) {
            invoiceItemResponse.setOrderItemNumber(orderItem.getOrderLineItemNumber());
        }
        invoiceItemResponse.setQuantity(invoiceItemEntity.getQuantity());
        invoiceItemResponse.setUnitPrice(invoiceItemEntity.getUnitPrice());
        invoiceItemResponse.setBaseAmount(invoiceItemEntity.getBaseAmount());
        invoiceItemResponse.setTaxAmount(invoiceItemEntity.getTaxAmount());
        invoiceItemResponse.setTotalAmount(invoiceItemEntity.getTotalAmount());
        invoiceItemResponse.setAdditionalInfo(invoiceItemEntity.getAdditionalInfo());
        return invoiceItemResponse;
    }

    /**
     *  COMPANY RELATED MAPPERS
     */
    public static CompanyEntity mapCompanyRequestToCompany(CompanyRequest request, CompanyRepository companyRepository){
        CompanyEntity companyEntity = new CompanyEntity();
        companyEntity.setCompanyCode(request.getCompanyCode());
        companyEntity.setCompanyName(request.getCompanyName());
        companyEntity.setAddressLine1(request.getAddressLine1());
        companyEntity.setAddressLine2(request.getAddressLine2());
        companyEntity.setCity(request.getCity());
        companyEntity.setState(request.getState());
        companyEntity.setStateCode(request.getStateCode());
        companyEntity.setGstCode(request.getGstCode());
        companyEntity.setGstNumber(request.getGstNumber());
        companyEntity.setPanNumber(request.getPanNumber());
        companyEntity.setCinNumber(request.getCinNumber());
        companyEntity.setEmail(request.getEmail());
        companyEntity.setCurrencyCode(request.getCurrencyCode());
        companyEntity.setPhoneNumber(request.getPhoneNumber());
        companyEntity.setParent(request.getIsParent());

        if(request.getParentCompanyId() != null){
            CompanyEntity parent = companyRepository.findById(request.getParentCompanyId())
                    .orElseThrow(() -> new RuntimeException("Parent companyEntity not found"));
            companyEntity.setParentCompanyEntity(parent);
        }
        return companyEntity;
    }

    public static CompanyResponse mapCompanyToCompanyResponse(CompanyEntity fetchedCompanyEntity, CompanyRepository companyRepository){
        CompanyResponse response = new CompanyResponse();
        response.setCompanyId(fetchedCompanyEntity.getCompanyId());
        response.setCompanyCode(fetchedCompanyEntity.getCompanyCode());
        response.setCompanyName(fetchedCompanyEntity.getCompanyName());
        response.setAddressLine1(fetchedCompanyEntity.getAddressLine1());
        response.setAddressLine2(fetchedCompanyEntity.getAddressLine2());
        response.setCity(fetchedCompanyEntity.getCity());
        response.setState(fetchedCompanyEntity.getState());
        response.setStateCode(fetchedCompanyEntity.getStateCode());
        response.setGstCode(fetchedCompanyEntity.getGstCode());
        response.setGstNumber(fetchedCompanyEntity.getGstNumber());
        response.setPanNumber(fetchedCompanyEntity.getPanNumber());
        response.setCinNumber(fetchedCompanyEntity.getCinNumber());
        response.setEmail(fetchedCompanyEntity.getEmail());
        response.setCurrencyCode(fetchedCompanyEntity.getCurrencyCode());
        response.setPhoneNumber(fetchedCompanyEntity.getPhoneNumber());
        response.setIsParent(fetchedCompanyEntity.getParent());
        List<CompanyEntity> childCompanies = companyRepository.
                findByParentCompany(companyRepository
                        .findById(fetchedCompanyEntity.getCompanyId()).orElseThrow(() -> new RuntimeException("Parent not found.")));
        response.setChildCompanies(childCompanies);
        response.setCreatedOn(fetchedCompanyEntity.getCreatedOn());
        response.setUpdatedOn(fetchedCompanyEntity.getUpdatedOn());
        return response;
    }


    /**
    *   POSTING RULES - MAPPERS.
    */

    public static PostingRuleEntity mapPostingRuleRequestToPostingRuleEntity(PostingRuleRequest request, ChartOfAccountRepository chartOfAccountRepository, CompanyRepository companyRepository){
        if(request == null) return null;
        PostingRuleEntity postingRuleEntity = new PostingRuleEntity();
        postingRuleEntity.setEventType(request.getEventType());
        postingRuleEntity.setDescription(request.getDescription());
        postingRuleEntity.setPostingRuleCode(request.getPostingRuleCode());
        postingRuleEntity.setCompanyEntity(companyRepository.findByCompanyCode(
                request.getCompanyCode())
                .orElseThrow(
                        () -> new ResourceNotFoundException("CompanyEntity NOT FOUND for Code :-> "+request.getCompanyCode())
                )
        );
        postingRuleEntity.setHeaderConditionExpression(request.getHeaderConditionExpression());
        postingRuleEntity.setEffectiveFrom(request.getEffectiveFrom());
        postingRuleEntity.setEffectiveTo(request.getEffectiveTo());
        postingRuleEntity.setActive(request.getActive());
        postingRuleEntity.setLines(
                request.getLines()
                .stream()
                .map(line -> mapPostingRuleLineRequestToPostingRuleLine(line,chartOfAccountRepository))
                .collect(Collectors.toList())
        );
        return postingRuleEntity;
    }

    public static PostingRuleLineEntity mapPostingRuleLineRequestToPostingRuleLine(PostingRuleLineDTO line, ChartOfAccountRepository chartOfAccountRepository){
        if(line == null) return null;
        PostingRuleLineEntity postingRuleLineEntity = new PostingRuleLineEntity();
        postingRuleLineEntity.setAccount(chartOfAccountRepository.findByAccountCode(line.getAccountCode())
                .orElseThrow(() -> new ResourceNotFoundException("CoA NOT FOUND FOR :-> "+line.getAccountCode())));
        postingRuleLineEntity.setEntryType(line.getEntryType());
        postingRuleLineEntity.setAmountExpression(line.getAmountExpression());
        postingRuleLineEntity.setSortOrder(line.getSortOrder());
        return postingRuleLineEntity;
    }

    public static PostingRuleResponse mapPostingRuleToPostingRuleResponse(PostingRuleEntity postingRuleEntity){
        if(postingRuleEntity == null) return null;
        PostingRuleResponse response = new PostingRuleResponse();
        response.setId(postingRuleEntity.getId());
        response.setEventType(postingRuleEntity.getEventType());
        response.setDescription(postingRuleEntity.getDescription());
        response.setHeaderConditionExpression(postingRuleEntity.getHeaderConditionExpression());
        response.setCompanyCode(postingRuleEntity.getCompanyEntity().getCompanyCode());
        response.setEffectiveFrom(postingRuleEntity.getEffectiveFrom());
        response.setEffectiveTo(postingRuleEntity.getEffectiveTo());
        response.setCreatedOn(postingRuleEntity.getCreatedOn());
        response.setModifiedOn(postingRuleEntity.getModifiedOn());
        response.setCreatedBy(postingRuleEntity.getCreatedBy());
        response.setActive(postingRuleEntity.isActive());
        response.setModifiedBy(postingRuleEntity.getModifiedBy());
        response.setPostingRuleCode(postingRuleEntity.getPostingRuleCode());
        response.setLines(
                postingRuleEntity.getLines()
                        .stream()
                        .map(EntityDtoMappers::mapPostingRuleLineToPostingRuleLineDTO)
                        .toList()
        );
        return response;
    }

    public static PostingRuleLineDTO mapPostingRuleLineToPostingRuleLineDTO(PostingRuleLineEntity line){
        if(line == null) return null;
        PostingRuleLineDTO responseLine = new PostingRuleLineDTO();
        responseLine.setEntryType(line.getEntryType());
        responseLine.setAmountExpression(line.getAmountExpression());
        responseLine.setAccountCode(line.getAccount().getAccountCode());
        responseLine.setSortOrder(line.getSortOrder());
        return responseLine;
    }

    /**
     *   CHART OF ACCOUNT - MAPPERS
     */
    public static ChartOfAccountEntity mapCoaRequestToCoa(ChartOfAccountRequest request, CompanyEntity companyEntity){
        ChartOfAccountEntity chartOfAccountEntity = new ChartOfAccountEntity();
        chartOfAccountEntity.setAccountCode(request.getAccountCode());
        chartOfAccountEntity.setAccountName(request.getAccountName());
        chartOfAccountEntity.setCategory(request.getCategory());
        chartOfAccountEntity.setSubtype(request.getSubtype());
        chartOfAccountEntity.setNormalBalance(request.getNormalBalance());
        chartOfAccountEntity.setActive(request.getActive());
        chartOfAccountEntity.setEffectiveFrom(request.getEffectiveFrom());
        chartOfAccountEntity.setEffectiveTo(request.getEffectiveTo());
        chartOfAccountEntity.setCompanyEntity(companyEntity);
        return chartOfAccountEntity;
    }


    public static ChartOfAccountResponse mapCoaToCoaResponse(ChartOfAccountEntity chartOfAccountEntity){
        ChartOfAccountResponse response = new ChartOfAccountResponse();
        response.setId(chartOfAccountEntity.getId());
        response.setAccountCode(chartOfAccountEntity.getAccountCode());
        response.setAccountName(chartOfAccountEntity.getAccountName());
        response.setCategory(chartOfAccountEntity.getCategory());
        response.setSubtype(chartOfAccountEntity.getSubtype());
        response.setNormalBalance(chartOfAccountEntity.getNormalBalance());
        response.setActive(chartOfAccountEntity.isActive());
        response.setEffectiveFrom(chartOfAccountEntity.getEffectiveFrom());
        response.setEffectiveTo(chartOfAccountEntity.getEffectiveTo());
        response.setCreatedOn(chartOfAccountEntity.getCreatedOn());
        response.setModifiedOn(chartOfAccountEntity.getModifiedOn());
        response.setCreatedBy(chartOfAccountEntity.getCreatedBy());
        response.setModifiedBy(chartOfAccountEntity.getModifiedBy());
        if(chartOfAccountEntity.getCompanyEntity()!=null)
            response.setCompanyCode(chartOfAccountEntity.getCompanyEntity().getCompanyCode());
        return response;
    }

}
