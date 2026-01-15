package com.atharva.erp_telecom.service.finance;


import com.atharva.erp_telecom.dto.finance.CompanyRequest;
import com.atharva.erp_telecom.dto.finance.CompanyResponse;
import com.atharva.erp_telecom.entity.finance.Company;
import com.atharva.erp_telecom.exception.custom_exceptions.ResourceNotFoundException;
import com.atharva.erp_telecom.repository.finance.CompanyRepository;
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
            throw new RuntimeException("Company code already exists: " + request.getCompanyCode());
        }
        Company mappedCompany = EntityDtoMappers.mapCompanyRequestToCompany(request,companyRepository);
        Company savedCompany = companyRepository.save(mappedCompany);
        return EntityDtoMappers.mapCompanyToCompanyResponse(savedCompany,companyRepository);

    }

    public List<CompanyResponse> createAllCompanies(List<CompanyRequest> companies){
        List<Company> companiesToSave = companies.stream()
                .map(company -> EntityDtoMappers.mapCompanyRequestToCompany(company,companyRepository)).toList();
        List<Company> savedCompanies = companyRepository.saveAll(companiesToSave);
        return savedCompanies.stream()
                .map(company -> EntityDtoMappers.mapCompanyToCompanyResponse(company,companyRepository)).toList();
    }

    public CompanyResponse getCompanyById(Long id){
        Company retrievedCompany = companyRepository.findById(id).orElseThrow(() -> new RuntimeException("Company for id: "+ id + "Not Found"));
        return EntityDtoMappers.mapCompanyToCompanyResponse(retrievedCompany,companyRepository);
    }

    public List<CompanyResponse> getAllCompanies(){
        List<Company> fetchedCompanies = companyRepository.findAll();
        return fetchedCompanies.stream()
                .map(company -> EntityDtoMappers.mapCompanyToCompanyResponse(company,companyRepository))
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
        return EntityDtoMappers.mapCompanyToCompanyResponse(saved,companyRepository);
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

}
