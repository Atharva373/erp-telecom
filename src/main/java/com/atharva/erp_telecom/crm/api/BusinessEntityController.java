package com.atharva.erp_telecom.crm.api;


import com.atharva.erp_telecom.crm.persistence.masterdata.BusinessEntity;
import com.atharva.erp_telecom.crm.service.BusinessEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.*;

import java.util.*;

@RestController
@RequestMapping("/customer")
public class BusinessEntityController {
    private static final Logger customerControllerLogger = LoggerFactory.getLogger(BusinessEntityController.class);
    private final BusinessEntityService businessEntityService;

    @Autowired
    public BusinessEntityController(BusinessEntityService businessEntityService){
        this.businessEntityService = businessEntityService;
    }

    @GetMapping("/get")
    public Object getBusinessEntitys(@RequestParam(value = "id",required = false) String customerId){
        // Get single customer by ID
        if(customerId!=null) {
            Optional<BusinessEntity> fetchedBusinessEntity = businessEntityService.getBusinessEntityById(Long.parseLong(customerId));
            return fetchedBusinessEntity.map(ResponseEntity::ok)
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }
        // Get All the customers
        else{
            Optional<List<BusinessEntity>> fetchedBusinessEntitys = Optional.ofNullable(businessEntityService.getAllBusinessEntities());
            return fetchedBusinessEntitys.map(ResponseEntity::ok)
                    .orElseGet(()->new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }
    }

    @PostMapping("/new")
    public ResponseEntity<BusinessEntity> createBusinessEntity(@RequestBody BusinessEntity customer){
        BusinessEntity responseFromDB = businessEntityService.createBusinessEntity(customer);
        return new ResponseEntity<BusinessEntity>(responseFromDB,HttpStatus.CREATED);
    }

    @PostMapping("/update")
    public ResponseEntity<BusinessEntity> updateBusinessEntity(@RequestBody BusinessEntity customer){
        return new ResponseEntity<>(customer,HttpStatus.OK);
    }
}
