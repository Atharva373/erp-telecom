package com.atharva.erp_telecom.finance.service;


import com.atharva.erp_telecom.finance.dto.CompanyRequest;
import com.atharva.erp_telecom.finance.dto.CompanyResponse;
import com.atharva.erp_telecom.finance.persistence.masterdata.CompanyEntity;
import com.atharva.erp_telecom.exception.custom_exceptions.ResourceNotFoundException;
import com.atharva.erp_telecom.finance.persistence.repository.CompanyRepository;
import com.atharva.erp_telecom.utils.CrudUtils;
import com.atharva.erp_telecom.utils.EntityDtoMappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CompanyService {

    @Autowired
    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public CompanyResponse createCompany(CompanyRequest request){
        if (companyRepository.existsByCompanyCode(request.getCompanyCode())) {
            throw new RuntimeException("CompanyEntity code already exists: " + request.getCompanyCode());
        }
        CompanyEntity mappedCompanyEntity = EntityDtoMappers.mapCompanyRequestToCompany(request,companyRepository);
        CompanyEntity savedCompanyEntity = companyRepository.save(mappedCompanyEntity);
        return EntityDtoMappers.mapCompanyToCompanyResponse(savedCompanyEntity,companyRepository);

    }

    public List<CompanyResponse> createAllCompanies(List<CompanyRequest> companies){
        List<CompanyEntity> companiesToSave = companies.stream()
                .map(company -> EntityDtoMappers.mapCompanyRequestToCompany(company,companyRepository)).toList();
        List<CompanyEntity> savedCompanies = companyRepository.saveAll(companiesToSave);
        return savedCompanies.stream()
                .map(company -> EntityDtoMappers.mapCompanyToCompanyResponse(company,companyRepository)).toList();
    }

    public CompanyResponse getCompanyById(Long id){
        CompanyEntity retrievedCompanyEntity = companyRepository.findById(id).orElseThrow(() -> new RuntimeException("CompanyEntity for id: "+ id + "Not Found"));
        return EntityDtoMappers.mapCompanyToCompanyResponse(retrievedCompanyEntity,companyRepository);
    }

    public List<CompanyResponse> getAllCompanies(){
        List<CompanyEntity> fetchedCompanies = companyRepository.findAll();
        return fetchedCompanies.stream()
                .map(company -> EntityDtoMappers.mapCompanyToCompanyResponse(company,companyRepository))
                .toList();
    }

    public CompanyResponse updateCompany(Long id,CompanyRequest newCompany){
        CompanyEntity existingCompanyEntity = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CompanyEntity not found with ID: " + id));

        CrudUtils.updateIfNotNull(existingCompanyEntity::setCompanyName,newCompany.getCompanyName());
        CrudUtils.updateIfNotNull(existingCompanyEntity::setAddressLine1,newCompany.getAddressLine1());
        CrudUtils.updateIfNotNull(existingCompanyEntity::setAddressLine2,newCompany.getAddressLine2());
        CrudUtils.updateIfNotNull(existingCompanyEntity::setCity,newCompany.getCity());
        CrudUtils.updateIfNotNull(existingCompanyEntity::setState,newCompany.getState());
        CrudUtils.updateIfNotNull(existingCompanyEntity::setStateCode,newCompany.getStateCode());
        CrudUtils.updateIfNotNull(existingCompanyEntity::setGstCode,newCompany.getGstCode());
        CrudUtils.updateIfNotNull(existingCompanyEntity::setGstNumber,newCompany.getGstNumber());
        CrudUtils.updateIfNotNull(existingCompanyEntity::setPanNumber,newCompany.getPanNumber());
        CrudUtils.updateIfNotNull(existingCompanyEntity::setCinNumber,newCompany.getCinNumber());
        CrudUtils.updateIfNotNull(existingCompanyEntity::setEmail,newCompany.getEmail());
        CrudUtils.updateIfNotNull(existingCompanyEntity::setPhoneNumber,newCompany.getPhoneNumber());
        CrudUtils.updateIfNotNull(existingCompanyEntity::setParent,newCompany.getIsParent());

        if (newCompany.getParentCompanyId() != null) {
            CompanyEntity parent = companyRepository.findById(newCompany.getParentCompanyId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent company not found with ID: " + newCompany.getParentCompanyId()));
            existingCompanyEntity.setParentCompanyEntity(parent);
        }

        existingCompanyEntity.setUpdatedOn(LocalDateTime.now());
        existingCompanyEntity.setUpdatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        CompanyEntity saved = companyRepository.save(existingCompanyEntity);
        return EntityDtoMappers.mapCompanyToCompanyResponse(saved,companyRepository);
    }

    public Map<String,Object> deleteCompany(Long id){
        CompanyEntity companyEntityToDelete = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CompanyEntity not found with ID: " + id));

        companyRepository.delete(companyEntityToDelete);

        Map<String,Object> response = new HashMap<>();
        response.put("message","CompanyEntity deletion successful.");
        response.put("deletedCompanyId",id);
        response.put("timeStamp",LocalDateTime.now());
        response.put("status", HttpStatus.OK.value());

        return response;
    }

}
