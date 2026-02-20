package com.atharva.erp_telecom.utils;


import com.atharva.erp_telecom.finance.dto.TaxRequest;
import com.atharva.erp_telecom.finance.dto.TaxResponse;
import com.atharva.erp_telecom.finance.persistence.masterdata.TaxEntity;
import org.springframework.stereotype.Component;

@Component
public class TaxUtils {

    public TaxEntity mapToEntity(TaxRequest request){
        TaxEntity taxEntity = new TaxEntity();
        taxEntity.setTaxCode(request.getTaxCode());
        taxEntity.setTaxName(request.getTaxName());
        taxEntity.setCgstPercentage(request.getCgstPercentage());
        taxEntity.setSgstPercentage(request.getSgstPercentage());
        taxEntity.setIgstPercentage(request.getIgstPercentage());
        taxEntity.setActive(request.getActive() != null ? request.getActive() : true);
        taxEntity.setDescription(request.getDescription());
        return taxEntity;
    }

    public TaxResponse mapToResponse(TaxEntity taxEntity){
        TaxResponse response = new TaxResponse();
        response.setTaxId(taxEntity.getTaxId());
        response.setTaxCode(taxEntity.getTaxCode());
        response.setTaxName(taxEntity.getTaxName());
        response.setCgstPercentage(taxEntity.getCgstPercentage());
        response.setSgstPercentage(taxEntity.getSgstPercentage());
        response.setIgstPercentage(taxEntity.getIgstPercentage());
        response.setTotalTaxPercentage(taxEntity.getTotalTaxPercentage());
        response.setActive(taxEntity.getActive());
        response.setDescription(taxEntity.getDescription());
        return response;
    }


}
