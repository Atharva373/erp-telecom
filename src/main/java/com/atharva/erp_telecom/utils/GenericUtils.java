package com.atharva.erp_telecom.utils;

import com.atharva.erp_telecom.entity.ChargePlan;
import com.atharva.erp_telecom.entity.Product;
import com.atharva.erp_telecom.enums.OrderType;
import com.atharva.erp_telecom.enums.PlanType;
import com.atharva.erp_telecom.exception.custom_exceptions.ChargePlanNotFoundException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GenericUtils {
    public static ChargePlan getDefaultChargePlan(Product product){
        if(product == null || product.getChargePlans() == null || product.getChargePlans().isEmpty()){
            assert product != null;
            throw new ChargePlanNotFoundException("Either Product or null || ChargePlans not found for Product: " + product.getProductId());
        }
        return product.getChargePlans()
                .stream()
                .filter(ChargePlan::isDefault)
                .findFirst()
                .orElseThrow(() -> new ChargePlanNotFoundException("No ChargePlans found for Product: "+product.getProductId()));
    }
    public static OrderType deriveOrderTypeFromChargePlan(List<Product> products) {
        if (products == null || products.isEmpty()) {
            throw new IllegalArgumentException("No products found in the order");
        }

        Set<PlanType> chargeTypes = products.stream()
                .flatMap(product -> product.getChargePlans().stream())
                .filter(ChargePlan::isDefault)
                .map(ChargePlan::getPlanType)
                .collect(Collectors.toSet());

        if (chargeTypes.isEmpty()) {
            throw new ChargePlanNotFoundException("No Charge Plans found for provided products");
        }

        if (chargeTypes.size() == 1) {
            PlanType singleType = chargeTypes.iterator().next();
            return (singleType == PlanType.PREPAID) ? OrderType.PREPAID : OrderType.POSTPAID;
        }
        throw new IllegalArgumentException("Mixed PREPAID and POSTPAID products not allowed in the same order");
    }
}
