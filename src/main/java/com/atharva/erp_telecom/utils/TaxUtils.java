package com.atharva.erp_telecom.utils;


import com.atharva.erp_telecom.dto.TaxRequest;
import com.atharva.erp_telecom.dto.TaxResponse;
import com.atharva.erp_telecom.entity.Tax;
import org.springframework.stereotype.Component;

@Component
public class TaxUtils {

    public Tax mapToEntity(TaxRequest request){
        Tax tax = new Tax();
        tax.setTaxCode(request.getTaxCode());
        tax.setTaxName(request.getTaxName());
        tax.setCgstPercentage(request.getCgstPercentage());
        tax.setSgstPercentage(request.getSgstPercentage());
        tax.setIgstPercentage(request.getIgstPercentage());
        tax.setActive(request.getActive() != null ? request.getActive() : true);
        tax.setDescription(request.getDescription());
        return tax;
    }

    public TaxResponse mapToResponse(Tax tax){
        TaxResponse response = new TaxResponse();
        response.setTaxId(tax.getTaxId());
        response.setTaxCode(tax.getTaxCode());
        response.setTaxName(tax.getTaxName());
        response.setCgstPercentage(tax.getCgstPercentage());
        response.setSgstPercentage(tax.getSgstPercentage());
        response.setIgstPercentage(tax.getIgstPercentage());
        response.setTotalTaxPercentage(tax.getTotalTaxPercentage());
        response.setActive(tax.getActive());
        response.setDescription(tax.getDescription());
        return response;
    }


}
