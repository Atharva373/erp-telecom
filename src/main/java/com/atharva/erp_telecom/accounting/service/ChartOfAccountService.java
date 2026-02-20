package com.atharva.erp_telecom.accounting.service;

import com.atharva.erp_telecom.accounting.dto.ChartOfAccountRequest;
import com.atharva.erp_telecom.accounting.dto.ChartOfAccountResponse;
import com.atharva.erp_telecom.accounting.persistence.masterdata.ChartOfAccountEntity;
import com.atharva.erp_telecom.finance.persistence.masterdata.CompanyEntity;
import com.atharva.erp_telecom.exception.custom_exceptions.ResourceNotFoundException;
import com.atharva.erp_telecom.accounting.persistence.repository.ChartOfAccountRepository;
import com.atharva.erp_telecom.finance.persistence.repository.CompanyRepository;
import com.atharva.erp_telecom.utils.EntityDtoMappers;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChartOfAccountService {
    private final ChartOfAccountRepository repository;
    private final CompanyRepository companyRepository;

    public ChartOfAccountService(ChartOfAccountRepository repository, CompanyRepository companyRepository) {
        this.repository = repository;
        this.companyRepository = companyRepository;
    }

    public ChartOfAccountResponse create(ChartOfAccountRequest request) {
        CompanyEntity companyEntity = companyRepository
                .findByCompanyCode(request.getCompanyCode())
                .orElseThrow(() -> new ResourceNotFoundException("CompanyEntity NOT FOUND for CompanyEntity Code :-> "+request.getCompanyCode()));

        ChartOfAccountEntity saved = repository.save(EntityDtoMappers.mapCoaRequestToCoa(request, companyEntity));
        return EntityDtoMappers.mapCoaToCoaResponse(saved);
    }

    public List<ChartOfAccountResponse> getAll() {
        return repository.findAll().stream().map(EntityDtoMappers::mapCoaToCoaResponse).toList();
    }

    public ChartOfAccountResponse getById(Long id) {
        ChartOfAccountEntity fetchedCoa = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("CoA NOT FOUND for Id :-> "+id));
        return EntityDtoMappers.mapCoaToCoaResponse(fetchedCoa);
    }

    public ChartOfAccountResponse update(Long id, ChartOfAccountRequest request) {

        ChartOfAccountEntity existing = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("CoA NOT FOUND for Id :-> "+id));

        existing.setAccountCode(request.getAccountCode());
        existing.setAccountName(request.getAccountName());
        existing.setActive(request.getActive());
        existing.setCategory(request.getCategory());
        existing.setSubtype(request.getSubtype());
        existing.setNormalBalance(request.getNormalBalance());
        existing.setEffectiveFrom(request.getEffectiveFrom());
        existing.setEffectiveTo(request.getEffectiveTo());

        ChartOfAccountEntity updated = repository.save(existing);

        return EntityDtoMappers.mapCoaToCoaResponse(updated);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
