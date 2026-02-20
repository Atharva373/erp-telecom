package com.atharva.erp_telecom.salesorder.dto;

import com.atharva.erp_telecom.crm.dto.BusinessEntityInfoResponse;
import com.atharva.erp_telecom.finance.dto.InvoiceResponse;
import com.atharva.erp_telecom.salesorder.enums.OrderStatus;
import com.atharva.erp_telecom.salesorder.enums.OrderType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
public class OrderResponse {
    private Long orderId;
    private String orderNumber;
    private OrderStatus orderStatus;
    private OrderType orderType;
    private List<OrderItemResponse> orderItems;
    private BigDecimal totalAmount;
    private BusinessEntityInfoResponse businessEntityInfoResponse;
    private String remarks;
    private InvoiceResponse invoice;
}
