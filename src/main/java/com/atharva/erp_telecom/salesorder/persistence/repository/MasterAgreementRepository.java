package com.atharva.erp_telecom.salesorder.persistence.repository;

import com.atharva.erp_telecom.salesorder.persistence.transactional.MasterAgreement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MasterAgreementRepository extends JpaRepository<MasterAgreement,Long> {
    MasterAgreement findByAgreementNumber(String agreementNumber);
}
