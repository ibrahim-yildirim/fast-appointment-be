package org.fastappointment.service.impl;

import org.fastappointment.api.dto.CreateCustomerRequest;
import org.fastappointment.api.dto.CustomerResponse;
import org.fastappointment.entity.concretes.Customer;
import org.fastappointment.mapper.CustomerEntityToCustomerResponse;
import org.fastappointment.repository.CustomerRepository;
import org.fastappointment.service.CustomerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerEntityToCustomerResponse customerEntityToCustomerResponse;

    public CustomerServiceImpl(CustomerRepository customerRepository,
                               CustomerEntityToCustomerResponse customerEntityToCustomerResponse) {
        this.customerRepository = customerRepository;
        this.customerEntityToCustomerResponse = customerEntityToCustomerResponse;
    }

    @Override
    public List<CustomerResponse> findAll() {
        return customerRepository.findAll().stream()
                .map(customerEntityToCustomerResponse::toResponse)
                .toList();
    }

    @Override
    public CustomerResponse findById(Long id) {
        return customerRepository.findById(id)
                .map(customerEntityToCustomerResponse::toResponse)
                .orElseThrow();
    }

    @Override
    @Transactional
    public CustomerResponse create(CreateCustomerRequest request) {
        Customer customer = Customer.builder()
                .name(request.getName())
                .email(request.getEmail())
                .build();
        return customerEntityToCustomerResponse.toResponse(customerRepository.save(customer));
    }
}
