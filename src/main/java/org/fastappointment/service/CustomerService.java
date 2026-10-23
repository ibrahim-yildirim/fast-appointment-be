package org.fastappointment.service;

import org.fastappointment.api.dto.CreateCustomerRequest;
import org.fastappointment.api.dto.CustomerResponse;

import java.util.List;

public interface CustomerService {

    List<CustomerResponse> findAllForOwner(Long ownerId);

    CustomerResponse findByIdForOwner(Long ownerId, Long id);

    CustomerResponse createForOwner(Long ownerId, CreateCustomerRequest request);
}
