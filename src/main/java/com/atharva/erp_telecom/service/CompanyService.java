package com.atharva.erp_telecom.service;


import com.atharva.erp_telecom.dto.CompanyRequest;
import com.atharva.erp_telecom.dto.CompanyResponse;
import com.atharva.erp_telecom.entity.Company;
import com.atharva.erp_telecom.exception.custom_exceptions.ResourceNotFoundException;
import com.atharva.erp_telecom.repository.CompanyRepository;
import com.atharva.erp_telecom.utils.CrudUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
            throw new RuntimeException("Company code already exists: " + request.getCompanyCode());
        }
        Company mapped = mapCompanyRequest(request);
        Company savedCompany = companyRepository.save(mapped);
        return mapCompanyResponse(savedCompany);

    }

    public List<CompanyResponse> createAllCompanies(List<CompanyRequest> companies){
        List<Company> companiesToSave = companies.stream().map(this::mapCompanyRequest).toList();
        List<Company> savedCompanies = companyRepository.saveAll(companiesToSave);
        return savedCompanies.stream().map(this::mapCompanyResponse).toList();
    }

    public CompanyResponse getCompanyById(Long id){
        Company retrievedCompany = companyRepository.findById(id).orElseThrow(() -> new RuntimeException("Company for id: "+ id + "Not Found"));
        return mapCompanyResponse(retrievedCompany);
    }

    public List<CompanyResponse> getAllCompanies(){
        List<Company> fetchedCompanies = companyRepository.findAll();
        return fetchedCompanies.stream()
                .map(this::mapCompanyResponse)
                .toList();
    }

    public CompanyResponse updateCompany(Long id,CompanyRequest newCompany){
        Company existingCompany = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with ID: " + id));

        CrudUtils.updateIfNotNull(existingCompany::setCompanyName,newCompany.getCompanyName());
        CrudUtils.updateIfNotNull(existingCompany::setAddressLine1,newCompany.getAddressLine1());
        CrudUtils.updateIfNotNull(existingCompany::setAddressLine2,newCompany.getAddressLine2());
        CrudUtils.updateIfNotNull(existingCompany::setCity,newCompany.getCity());
        CrudUtils.updateIfNotNull(existingCompany::setState,newCompany.getState());
        CrudUtils.updateIfNotNull(existingCompany::setStateCode,newCompany.getStateCode());
        CrudUtils.updateIfNotNull(existingCompany::setGstCode,newCompany.getGstCode());
        CrudUtils.updateIfNotNull(existingCompany::setGstNumber,newCompany.getGstNumber());
        CrudUtils.updateIfNotNull(existingCompany::setPanNumber,newCompany.getPanNumber());
        CrudUtils.updateIfNotNull(existingCompany::setCinNumber,newCompany.getCinNumber());
        CrudUtils.updateIfNotNull(existingCompany::setEmail,newCompany.getEmail());
        CrudUtils.updateIfNotNull(existingCompany::setPhoneNumber,newCompany.getPhoneNumber());
        CrudUtils.updateIfNotNull(existingCompany::setParent,newCompany.getIsParent());

        if (newCompany.getParentCompanyId() != null) {
            Company parent = companyRepository.findById(newCompany.getParentCompanyId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent company not found with ID: " + newCompany.getParentCompanyId()));
            existingCompany.setParentCompany(parent);
        }

        existingCompany.setUpdatedOn(LocalDateTime.now());
        existingCompany.setUpdatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        Company saved = companyRepository.save(existingCompany);
        return this.mapCompanyResponse(saved);
    }

    public Map<String,Object> deleteCompany(Long id){
        Company companyToDelete = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with ID: " + id));

        companyRepository.delete(companyToDelete);

        Map<String,Object> response = new HashMap<>();
        response.put("message","Company deletion successful.");
        response.put("deletedCompanyId",id);
        response.put("timeStamp",LocalDateTime.now());
        response.put("status", HttpStatus.OK.value());

        return response;
    }


    // Helper Methods for mapping Request and Response DTO's classes to Company class.
    private Company mapCompanyRequest(CompanyRequest request){
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
        company.setPhoneNumber(request.getPhoneNumber());
        company.setParent(request.getIsParent());

        if(request.getParentCompanyId() != null){
            Company parent = companyRepository.findById(request.getParentCompanyId())
                    .orElseThrow(() -> new RuntimeException("Parent company not found"));
            company.setParentCompany(parent);
        }
        return company;
    }

    private CompanyResponse mapCompanyResponse(Company fetchedCompany){
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

}
