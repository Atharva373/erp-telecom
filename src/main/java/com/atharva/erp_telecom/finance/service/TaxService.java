package com.atharva.erp_telecom.finance.service;


import com.atharva.erp_telecom.finance.dto.TaxRequest;
import com.atharva.erp_telecom.finance.dto.TaxResponse;
import com.atharva.erp_telecom.finance.persistence.masterdata.TaxEntity;
import com.atharva.erp_telecom.exception.custom_exceptions.ResourceNotFoundException;
import com.atharva.erp_telecom.finance.persistence.repository.TaxRepository;
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
        TaxEntity mappedTaxEntityEntity = mapTaxRequestToEntity(request);
        TaxEntity savedTaxEntityEntity = taxRepository.save(mappedTaxEntityEntity);
        return mapTaxEntityToResponse(savedTaxEntityEntity);
    }

    @Transactional
    public List<TaxResponse> createTaxes(List<TaxRequest> requests){
        List<TaxEntity> saved = taxRepository.saveAll(
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
                .orElseThrow(() -> new ResourceNotFoundException("TaxEntity not found for ID: "+id)));
    }

    @Transactional
    public TaxResponse updateTax(Long id,TaxRequest request){
        TaxEntity existingTaxEntity = taxRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TaxEntity NOT FOUND for id: "+id));

        CrudUtils.updateIfNotNull(existingTaxEntity::setTaxCode, request.getTaxCode());
        CrudUtils.updateIfNotNull(existingTaxEntity::setTaxName, request.getTaxName());
        CrudUtils.updateIfNotNull(existingTaxEntity::setCgstPercentage, request.getCgstPercentage());
        CrudUtils.updateIfNotNull(existingTaxEntity::setSgstPercentage, request.getSgstPercentage());
        CrudUtils.updateIfNotNull(existingTaxEntity::setIgstPercentage, request.getIgstPercentage());
        CrudUtils.updateIfNotNull(existingTaxEntity::setActive, request.getActive());
        CrudUtils.updateIfNotNull(existingTaxEntity::setDescription, request.getDescription());
        CrudUtils.updateIfNotNull(existingTaxEntity::setEffectiveFrom, request.getEffectiveFrom());
        CrudUtils.updateIfNotNull(existingTaxEntity::setEffectiveTo, request.getEffectiveTo());
        existingTaxEntity.setUpdatedOn(LocalDateTime.now());

        TaxEntity updated = taxRepository.save(existingTaxEntity);
        return this.mapTaxEntityToResponse(updated);
    }
    @Transactional
    public Map<String,Object> deleteTax(Long id){
        TaxEntity existing = taxRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TaxEntity not found for ID: " + id));
        taxRepository.delete(existing);
        Map<String,Object> response = new HashMap<>();
        response.put("message","TaxEntity deletion successful.");
        response.put("deletedTaxId",id);
        response.put("timeStamp",LocalDateTime.now());
        response.put("status", HttpStatus.OK.value());

        return response;
    }
    // Helper method to map incoming TaxRequest DTO to the TaxEntity Entity.
    private TaxEntity mapTaxRequestToEntity(TaxRequest request){
        TaxEntity taxEntity = new TaxEntity();
        taxEntity.setTaxCode(request.getTaxCode());
        taxEntity.setTaxName(request.getTaxName());
        taxEntity.setCgstPercentage(request.getCgstPercentage());
        taxEntity.setSgstPercentage(request.getSgstPercentage());
        taxEntity.setIgstPercentage(request.getIgstPercentage());
        taxEntity.setActive(request.getActive() != null ? request.getActive() : true);
        taxEntity.setDescription(request.getDescription());
        taxEntity.setEffectiveFrom(request.getEffectiveFrom());
        taxEntity.setEffectiveTo(request.getEffectiveTo());

        return taxEntity;
    }

    // Helper method to map incoming TaxEntity Entity to the TaxResponse DTO.
    private TaxResponse mapTaxEntityToResponse(TaxEntity taxEntity){
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
        response.setEffectiveFrom(taxEntity.getEffectiveFrom());
        response.setEffectiveTo(taxEntity.getEffectiveTo());
        response.setCreatedOn(taxEntity.getCreatedOn());
        response.setUpdatedOn(taxEntity.getUpdatedOn());
        return response;
    }
}
