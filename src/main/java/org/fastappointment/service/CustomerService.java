package org.fastappointment.service;

import org.fastappointment.api.dto.CreateCustomerRequest;
import org.fastappointment.api.dto.CustomerResponse;

import java.util.List;

public interface CustomerService {

    List<CustomerResponse> findAll();

    CustomerResponse findById(Long id);

    CustomerResponse create(CreateCustomerRequest request);
}
