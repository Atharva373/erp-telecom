package com.atharva.erp_telecom.repository;

import com.atharva.erp_telecom.entity.MasterAgreement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MasterAgreementRepository extends JpaRepository<MasterAgreement,Long> {
    MasterAgreement findByAgreementNumber(String agreementNumber);
}
