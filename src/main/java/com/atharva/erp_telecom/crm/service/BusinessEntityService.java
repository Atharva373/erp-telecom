package com.atharva.erp_telecom.crm.service;


import com.atharva.erp_telecom.crm.persistence.masterdata.BusinessEntity;
import com.atharva.erp_telecom.exception.custom_exceptions.ResourceNotFoundException;
import com.atharva.erp_telecom.crm.persistence.repository.BusinessEntityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BusinessEntityService {
    private static final Logger customerServiceLogger = LoggerFactory.getLogger(BusinessEntityService.class);
    // Better practice to use dependency injection in the constructor instead of field
    private final BusinessEntityRepository customerRepository;

    @Autowired
    public BusinessEntityService(BusinessEntityRepository customerRepository){
        this.customerRepository = customerRepository;
    }

    public Optional<BusinessEntity> getBusinessEntityById(Long customerId){
        return Optional.ofNullable(customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("BusinessEntity not found with ID:" + customerId)));
    }

    public BusinessEntity createBusinessEntity(BusinessEntity customer){
        // customerServiceLogger.info("Creating customer with id:{}",customer.getBusinessEntityId());
        return customerRepository.save(customer);
    }

    public List<BusinessEntity> getAllBusinessEntities(){
        return customerRepository.findAll();
    }

}
