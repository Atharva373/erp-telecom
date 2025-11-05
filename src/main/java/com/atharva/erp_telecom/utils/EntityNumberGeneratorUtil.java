package com.atharva.erp_telecom.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Purpose of this UTIL class is to generate Entity specific ID's which are not system generated and require custom logic.

public class EntityNumberGeneratorUtil {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    public static String generateContractNumber(Long customerId, String productType) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        return String.format("CON_%s_%s_%s", customerId, productType, timestamp);
    }

    public static String generateAgreementNumber(Long customerId, String planType) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        return String.format("AGR_%s_%s_%s", planType, customerId, timestamp);
    }

    public static String generateOrderNumber(Long customerId) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        return String.format("ORD_%s_%s", customerId, timestamp);
    }

    public static String generateInvoiceNumber(Long orderId, Long customerId) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        return String.format("INV_%s_%s_%s", customerId,  orderId, timestamp);
    }
}
