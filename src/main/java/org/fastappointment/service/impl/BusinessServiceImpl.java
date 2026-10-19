package org.fastappointment.service.impl;

import org.fastappointment.api.dto.BusinessResponse;
import org.fastappointment.api.dto.CreateBusinessRequest;
import org.fastappointment.entity.concretes.Business;
import org.fastappointment.mapper.BusinessEntityToBusinessResponse;
import org.fastappointment.repository.BusinessRepository;
import org.fastappointment.service.BusinessService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BusinessServiceImpl implements BusinessService {

    private final BusinessRepository businessRepository;
    private final BusinessEntityToBusinessResponse businessEntityToBusinessResponse;

    public BusinessServiceImpl(BusinessRepository businessRepository, BusinessEntityToBusinessResponse businessEntityToBusinessResponse) {
        this.businessRepository = businessRepository;
        this.businessEntityToBusinessResponse = businessEntityToBusinessResponse;
    }

    @Override
    public List<BusinessResponse> findAll() {
        return businessRepository.findAll().stream()
                .map(businessEntityToBusinessResponse::toResponse)
                .toList();
    }

    @Override
    public BusinessResponse findBySlug(String slug) {
        return businessRepository.findBySlug(slug)
                .map(businessEntityToBusinessResponse::toResponse)
                .orElseThrow();
    }

    @Override
    @Transactional
    public BusinessResponse create(CreateBusinessRequest request) {
        if (businessRepository.existsBySlug(request.getSlug())) {
            throw new IllegalArgumentException("Slug already taken: " + request.getSlug());
        }
        Business business = Business.builder()
                .slug(request.getSlug())
                .name(request.getName())
                .description(request.getDescription())
                .timezone(request.getTimezone())
                .address(request.getAddress())
                .phone(request.getPhone())
                .email(request.getEmail())
                .build();
        return businessEntityToBusinessResponse.toResponse(businessRepository.save(business));
    }
}
