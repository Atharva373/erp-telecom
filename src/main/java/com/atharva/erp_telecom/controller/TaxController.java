package com.atharva.erp_telecom.controller;


import com.atharva.erp_telecom.dto.TaxRequest;
import com.atharva.erp_telecom.dto.TaxResponse;
import com.atharva.erp_telecom.service.TaxService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("taxes")
public class TaxController {

    private final TaxService taxService;
    private final ObjectMapper objectMapper;

    @Autowired
    public TaxController(TaxService taxService,ObjectMapper objectMapper) {
        this.taxService = taxService;
        this.objectMapper = objectMapper;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','CONSULTANT','MANAGER','AGENT')")
    public ResponseEntity<Object> createTax(
            @RequestBody Object request,
            @RequestParam(value = "bulk",required = false,defaultValue ="false") boolean bulk
    ){
        Map<String,Object> response = new HashMap<>();
        if(bulk){
            if(request instanceof List<?> rawList) {
                List<TaxRequest> requests = rawList.stream()
                        .map(req -> objectMapper.convertValue(req, TaxRequest.class))
                        .toList();
                List<TaxResponse> createdTaxes = taxService.createTaxes(requests);
                response.put("message", "Bulk taxes created successfully.");
                response.put("data", createdTaxes);
            }
            else throw new IllegalArgumentException("The data sent should be in the form of a List, please re-trigger.");
        }else{
            TaxRequest singleRequest =  objectMapper.convertValue(request, TaxRequest.class);
            TaxResponse createdTax = taxService.createTax(singleRequest);
            response.put("message", "Single tax created successfully.");
            response.put("data", createdTax);
        }
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.CREATED.value());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','CONSULTANT','MANAGER','AGENT')")
    public List<TaxResponse> getAllTaxes() {
        return taxService.getAllTaxes();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CONSULTANT','MANAGER','AGENT')")
    public TaxResponse getTaxById(@PathVariable Long id) {
        return taxService.getTaxById(id);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CONSULTANT','MANAGER')")
    public ResponseEntity<Object> updateTax(
            @PathVariable Long id,
            @RequestBody TaxRequest request
    ) {
        TaxResponse updatedTax = taxService.updateTax(id, request);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Tax updated successfully.");
        response.put("data", updatedTax);
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.OK.value());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CONSULTANT')")
    public ResponseEntity<Map<String, Object>> deleteTax(@PathVariable Long id) {
        Map<String, Object> response = taxService.deleteTax(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
