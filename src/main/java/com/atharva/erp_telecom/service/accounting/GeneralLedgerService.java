//package com.atharva.erp_telecom.service.accounting;
//
//import com.atharva.erp_telecom.dto.accounting.GLLineRow;
//import com.atharva.erp_telecom.enums.EntryType;
//import com.atharva.erp_telecom.repository.accounting.GeneralLedgerRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.math.BigDecimal;
//import java.util.List;
//
//@Service
//public class GeneralLedgerService {
//
//    private final GeneralLedgerRepository repo;
//
//    @Autowired
//    public GeneralLedgerService(GeneralLedgerRepository repo) {
//        this.repo = repo;
//    }
//
//    public List<GLLineRow> getGL(
//            Long companyId,
//            String accountCode,
//            Integer fiscalYear,
//            Integer postingPeriod,
//            String currency
//    ) {
//        List<GLLineRow> rows = repo.fetchGL(
//                companyId, accountCode, fiscalYear, postingPeriod, currency
//        );
//
//        // Running balance
//        BigDecimal balance = BigDecimal.ZERO;
//        for (GLLineRow row : rows) {
//            if (row.getEntryType() == EntryType.DEBIT) {
//                balance = balance.add(row.getAmount());
//            } else {
//                balance = balance.subtract(row.getAmount());
//            }
//            row.setRunningBalance(balance);
//        }
//        return rows;
//    }
//}
