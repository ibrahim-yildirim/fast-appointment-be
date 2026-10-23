package org.fastappointment.mapper;

import org.fastappointment.api.dto.CustomerResponse;
import org.fastappointment.entity.concretes.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerEntityToCustomerResponse {

    public CustomerResponse toResponse(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getName(),
                customer.getPhone(),
                customer.getEmail()
        );
    }
}
