package com.atharva.erp_telecom.service.finance;


import com.atharva.erp_telecom.dto.finance.TaxRequest;
import com.atharva.erp_telecom.dto.finance.TaxResponse;
import com.atharva.erp_telecom.entity.finance.Tax;
import com.atharva.erp_telecom.exception.custom_exceptions.ResourceNotFoundException;
import com.atharva.erp_telecom.repository.finance.TaxRepository;
import com.atharva.erp_telecom.utils.CrudUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TaxService {

    private final TaxRepository taxRepository;
    @Autowired
    public TaxService(TaxRepository taxRepository) {
        this.taxRepository = taxRepository;
    }
    @Transactional
    public TaxResponse createTax(TaxRequest request){
        Tax mappedTaxEntity = mapTaxRequestToEntity(request);
        Tax savedTaxEntity = taxRepository.save(mappedTaxEntity);
        return mapTaxEntityToResponse(savedTaxEntity);
    }

    @Transactional
    public List<TaxResponse> createTaxes(List<TaxRequest> requests){
        List<Tax> saved = taxRepository.saveAll(
                requests.stream().map(this::mapTaxRequestToEntity).toList()
        );
        return saved.stream().map(this::mapTaxEntityToResponse).toList();
    }

    public List<TaxResponse> getAllTaxes(){
        return taxRepository.findAll()
        .stream().map(this::mapTaxEntityToResponse)
        .toList();
    }

    public TaxResponse getTaxById(Long id){
        return mapTaxEntityToResponse(taxRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tax not found for ID: "+id)));
    }

    @Transactional
    public TaxResponse updateTax(Long id,TaxRequest request){
        Tax existingTax = taxRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tax NOT FOUND for id: "+id));

        CrudUtils.updateIfNotNull(existingTax::setTaxCode, request.getTaxCode());
        CrudUtils.updateIfNotNull(existingTax::setTaxName, request.getTaxName());
        CrudUtils.updateIfNotNull(existingTax::setCgstPercentage, request.getCgstPercentage());
        CrudUtils.updateIfNotNull(existingTax::setSgstPercentage, request.getSgstPercentage());
        CrudUtils.updateIfNotNull(existingTax::setIgstPercentage, request.getIgstPercentage());
        CrudUtils.updateIfNotNull(existingTax::setActive, request.getActive());
        CrudUtils.updateIfNotNull(existingTax::setDescription, request.getDescription());
        CrudUtils.updateIfNotNull(existingTax::setEffectiveFrom, request.getEffectiveFrom());
        CrudUtils.updateIfNotNull(existingTax::setEffectiveTo, request.getEffectiveTo());
        existingTax.setUpdatedOn(LocalDateTime.now());

        Tax updated = taxRepository.save(existingTax);
        return this.mapTaxEntityToResponse(updated);
    }
    @Transactional
    public Map<String,Object> deleteTax(Long id){
        Tax existing = taxRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tax not found for ID: " + id));
        taxRepository.delete(existing);
        Map<String,Object> response = new HashMap<>();
        response.put("message","Tax deletion successful.");
        response.put("deletedTaxId",id);
        response.put("timeStamp",LocalDateTime.now());
        response.put("status", HttpStatus.OK.value());

        return response;
    }
    // Helper method to map incoming TaxRequest DTO to the Tax Entity.
    private Tax mapTaxRequestToEntity(TaxRequest request){
        Tax tax = new Tax();
        tax.setTaxCode(request.getTaxCode());
        tax.setTaxName(request.getTaxName());
        tax.setCgstPercentage(request.getCgstPercentage());
        tax.setSgstPercentage(request.getSgstPercentage());
        tax.setIgstPercentage(request.getIgstPercentage());
        tax.setActive(request.getActive() != null ? request.getActive() : true);
        tax.setDescription(request.getDescription());
        tax.setEffectiveFrom(request.getEffectiveFrom());
        tax.setEffectiveTo(request.getEffectiveTo());

        return tax;
    }

    // Helper method to map incoming Tax Entity to the TaxResponse DTO.
    private TaxResponse mapTaxEntityToResponse(Tax tax){
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
        response.setEffectiveFrom(tax.getEffectiveFrom());
        response.setEffectiveTo(tax.getEffectiveTo());
        response.setCreatedOn(tax.getCreatedOn());
        response.setUpdatedOn(tax.getUpdatedOn());
        return response;
    }
}
