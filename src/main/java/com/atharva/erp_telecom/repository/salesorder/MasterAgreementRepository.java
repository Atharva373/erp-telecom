package com.atharva.erp_telecom.repository.salesorder;

import com.atharva.erp_telecom.entity.salesorder.MasterAgreement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MasterAgreementRepository extends JpaRepository<MasterAgreement,Long> {
    MasterAgreement findByAgreementNumber(String agreementNumber);
}
