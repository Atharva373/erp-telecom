package com.atharva.erp_telecom.service;


import com.atharva.erp_telecom.entity.Customer;
import com.atharva.erp_telecom.exception.custom_exceptions.ResourceNotFoundException;
import com.atharva.erp_telecom.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CustomerService {
    private static final Logger customerServiceLogger = LoggerFactory.getLogger(CustomerService.class);
    // Better practice to use dependency injection in the constructor instead of field
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository){
        this.customerRepository = customerRepository;
    }

    public Optional<Customer> getCustomerById(Long customerId){
        return Optional.ofNullable(customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID:" + customerId)));
    }

    public Customer createCustomer(Customer customer){
        // customerServiceLogger.info("Creating customer with id:{}",customer.getCustomerId());
        return customerRepository.save(customer);
    }

    public List<Customer> getAllCustomers(){
        return customerRepository.findAll();
    }

}
