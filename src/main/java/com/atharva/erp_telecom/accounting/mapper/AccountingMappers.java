package com.atharva.erp_telecom.accounting.mapper;

import com.atharva.erp_telecom.accounting.dto.PostingPeriodRequest;
import com.atharva.erp_telecom.accounting.dto.PostingPeriodResponse;
import com.atharva.erp_telecom.accounting.enums.PeriodStatus;
import com.atharva.erp_telecom.accounting.persistence.config.PostingPeriodEntity;
import com.atharva.erp_telecom.finance.persistence.masterdata.CompanyEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountingMappers {

    /********************************************
     *  Posting Period related mappers
     ********************************************/

    /**
     * Posting Period Request DTO -> Entity
     */
    public PostingPeriodEntity toPostingPeriodEntity(PostingPeriodRequest request, CompanyEntity company){
        PostingPeriodEntity period = new PostingPeriodEntity();
        period.setCompany(company);
        period.setFiscalYear(request.getFiscalYear());
        period.setPostingPeriod(request.getPostingPeriod());
        period.setPeriodStart(request.getPeriodStart());
        period.setPeriodEnd(request.getPeriodEnd());
        period.setStatus(PeriodStatus.OPEN);
        return period;
    }

    /**
     * Posting Period Entity -> Response DTO
     */
    public PostingPeriodResponse toPostingPeriodResponse(PostingPeriodEntity entity) {
        if (entity == null) return null;

        PostingPeriodResponse response = new PostingPeriodResponse();
        response.setCompanyCode(entity.getCompany() != null ? entity.getCompany().getCompanyCode() : null);
        response.setFiscalYear(entity.getFiscalYear());
        response.setPostingPeriod(entity.getPostingPeriod());
        response.setPeriodStart(entity.getPeriodStart());
        response.setPeriodEnd(entity.getPeriodEnd());
        response.setStatus(entity.getStatus());
        response.setClosedOn(entity.getClosedOn());

        return response;
    }

}
