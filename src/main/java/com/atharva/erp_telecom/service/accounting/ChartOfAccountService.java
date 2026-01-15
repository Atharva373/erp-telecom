package com.atharva.erp_telecom.service.accounting;

import com.atharva.erp_telecom.dto.accounting.ChartOfAccountRequest;
import com.atharva.erp_telecom.dto.accounting.ChartOfAccountResponse;
import com.atharva.erp_telecom.entity.accounting.ChartOfAccount;
import com.atharva.erp_telecom.entity.finance.Company;
import com.atharva.erp_telecom.exception.custom_exceptions.ResourceNotFoundException;
import com.atharva.erp_telecom.repository.accounting.ChartOfAccountRepository;
import com.atharva.erp_telecom.repository.finance.CompanyRepository;
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
        Company company = companyRepository
                .findByCompanyCode(request.getCompanyCode())
                .orElseThrow(() -> new ResourceNotFoundException("Company NOT FOUND for Company Code :-> "+request.getCompanyCode()));

        ChartOfAccount saved = repository.save(EntityDtoMappers.mapCoaRequestToCoa(request,company));
        return EntityDtoMappers.mapCoaToCoaResponse(saved);
    }

    public List<ChartOfAccountResponse> getAll() {
        return repository.findAll().stream().map(EntityDtoMappers::mapCoaToCoaResponse).toList();
    }

    public ChartOfAccountResponse getById(Long id) {
        ChartOfAccount fetchedCoa = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("CoA NOT FOUND for Id :-> "+id));
        return EntityDtoMappers.mapCoaToCoaResponse(fetchedCoa);
    }

    public ChartOfAccountResponse update(Long id, ChartOfAccountRequest request) {

        ChartOfAccount existing = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("CoA NOT FOUND for Id :-> "+id));

        existing.setAccountCode(request.getAccountCode());
        existing.setAccountName(request.getAccountName());
        existing.setActive(request.getActive());
        existing.setCategory(request.getCategory());
        existing.setSubtype(request.getSubtype());
        existing.setNormalBalance(request.getNormalBalance());
        existing.setEffectiveFrom(request.getEffectiveFrom());
        existing.setEffectiveTo(request.getEffectiveTo());

        ChartOfAccount updated = repository.save(existing);

        return EntityDtoMappers.mapCoaToCoaResponse(updated);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
