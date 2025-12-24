package com.atharva.erp_telecom.controller;

import com.atharva.erp_telecom.dto.finance.CompanyRequest;
import com.atharva.erp_telecom.dto.finance.CompanyResponse;
import com.atharva.erp_telecom.service.finance.CompanyService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("companies")
@PreAuthorize("hasAnyRole('ADMIN','CONSULTANT')")
public class CompanyController {
    @Autowired
    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createCustomer(@RequestParam(value = "bulk",defaultValue = "false")
                                              boolean bulk, @RequestBody Object request){
        ObjectMapper mapper = new ObjectMapper();
        if(bulk){
            List<CompanyResponse> companies = companyService.createAllCompanies(mapper.convertValue(request, new TypeReference<List<CompanyRequest>>() {}));
            return new ResponseEntity<>(companies, HttpStatus.CREATED);
        }else{
            CompanyResponse company = companyService.createCompany((mapper.convertValue(request,CompanyRequest.class)));
            return new ResponseEntity<>(company,HttpStatus.CREATED);
        }
    }

    @GetMapping
    public ResponseEntity<List<CompanyResponse>> getAllCompanies(){
        return new ResponseEntity<>(companyService.getAllCompanies(),HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyResponse> getCompanyById(@PathVariable("id") Long id){
        return new ResponseEntity<>(companyService.getCompanyById(id),HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CompanyResponse> updateCompany(
            @PathVariable Long id,
            @RequestBody CompanyRequest request) {
        CompanyResponse updatedCompany = companyService.updateCompany(id, request);
        return ResponseEntity.ok(updatedCompany);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String,Object>> deleteCompany(@PathVariable(value = "id") Long id){
        Map<String, Object> response = companyService.deleteCompany(id);
        return ResponseEntity.ok(response);
    }

}
