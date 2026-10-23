package org.fastappointment.service.impl;

import org.fastappointment.api.dto.CreateCustomerRequest;
import org.fastappointment.api.dto.CustomerResponse;
import org.fastappointment.entity.concretes.Business;
import org.fastappointment.entity.concretes.Customer;
import org.fastappointment.exception.ConflictException;
import org.fastappointment.exception.ErrorMessageKeys;
import org.fastappointment.exception.NotFoundException;
import org.fastappointment.mapper.CustomerEntityToCustomerResponse;
import org.fastappointment.repository.BusinessRepository;
import org.fastappointment.repository.CustomerRepository;
import org.fastappointment.service.CustomerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final BusinessRepository businessRepository;
    private final CustomerEntityToCustomerResponse customerEntityToCustomerResponse;

    public CustomerServiceImpl(CustomerRepository customerRepository, BusinessRepository businessRepository,
                               CustomerEntityToCustomerResponse customerEntityToCustomerResponse) {
        this.customerRepository = customerRepository;
        this.businessRepository = businessRepository;
        this.customerEntityToCustomerResponse = customerEntityToCustomerResponse;
    }

    @Override
    public List<CustomerResponse> findAllForOwner(Long ownerId) {
        Business business = findOwnerBusiness(ownerId);
        return customerRepository.findAllOfBusiness(business.getId()).stream()
                .map(customerEntityToCustomerResponse::toResponse)
                .toList();
    }

    @Override
    public CustomerResponse findByIdForOwner(Long ownerId, Long id) {
        Business business = findOwnerBusiness(ownerId);
        return customerRepository.findByIdOfBusiness(id, business.getId())
                .map(customerEntityToCustomerResponse::toResponse)
                .orElseThrow(() -> new NotFoundException(ErrorMessageKeys.CUSTOMER_NOT_FOUND, id));
    }

    @Override
    @Transactional
    public CustomerResponse createForOwner(Long ownerId, CreateCustomerRequest request) {
        Business business = findOwnerBusiness(ownerId);
        String phone = normalizePhone(request.getPhone());
        if (customerRepository.existsByBusinessIdAndPhone(business.getId(), phone)) {
            throw new ConflictException(ErrorMessageKeys.CUSTOMER_PHONE_TAKEN, phone);
        }
        Customer customer = Customer.builder()
                .business(business)
                .name(request.getName())
                .phone(phone)
                .email(request.getEmail())
                .build();
        return customerEntityToCustomerResponse.toResponse(customerRepository.save(customer));
    }

    private Business findOwnerBusiness(Long ownerId) {
        return businessRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> new NotFoundException(ErrorMessageKeys.OWNER_HAS_NO_BUSINESS));
    }

    private String normalizePhone(String phone) {
        return phone.replaceAll("[^0-9+]", "");
    }
}
