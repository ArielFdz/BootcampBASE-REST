package com.bancobase.bootcamp.services;

import com.bancobase.bootcamp.dto.*;
import com.bancobase.bootcamp.dto.request.PreCustomerInfo;
import com.bancobase.bootcamp.exceptions.BusinessException;
import com.bancobase.bootcamp.repositories.CustomerRepository;
import com.bancobase.bootcamp.schemas.*;

import java.util.*;

import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final AccountService accountService;
    private final CustomerRepository customerRepository;

    public CustomerService(AccountService accountService, CustomerRepository customerRepository) {
        this.accountService = accountService;
        this.customerRepository = customerRepository;
    }

    public CustomerInfoDTO getCustomerById(Long customerId) {
        Optional<CustomerSchema> person = customerRepository
                .findById(customerId);

        return person.map(CustomerInfoDTO::getFromSchema).orElse(null);
    }

    public List<CustomerInfoDTO> filterCustomersByName(String name) {
        return customerRepository
                .findByNameContaining(name)
                .stream()
                .map(CustomerInfoDTO::getFromSchema)
                .toList();
    }

    public CustomerDTO createCustomer(PreCustomerInfo information) {
        if (customerRepository.findByCurp(information.getCurp()).isPresent()) {
            throw BusinessException
                    .builder()
                    .message("A customer with the same CURP " + information.getCurp() + " is already registered")
                    .build();
        }

        CustomerSchema customer = new CustomerSchema();

        customer.setBirthdate(information.getBirthdate());
        customer.setCurp(information.getCurp());
        customer.setGender(information.getGender());
        customer.setName(information.getName());

        CustomerSchema savedCustomer = customerRepository.save(customer);
        List<AccountSchema> accounts = accountService.createAccount(savedCustomer);

        return CustomerDTO.getFromSchema(customer, accounts);
    }
}
