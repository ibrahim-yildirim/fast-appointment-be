package org.fastappointment.service.impl;

import org.fastappointment.api.dto.CreateServiceOfferingRequest;
import org.fastappointment.api.dto.ServiceOfferingResponse;
import org.fastappointment.entity.concretes.Business;
import org.fastappointment.entity.concretes.ServiceOffering;
import org.fastappointment.mapper.ServiceOfferingEntityToServiceOfferingResponse;
import org.fastappointment.repository.BusinessRepository;
import org.fastappointment.repository.ServiceOfferingRepository;
import org.fastappointment.service.ServiceOfferingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ServiceOfferingServiceImpl implements ServiceOfferingService {

    private final ServiceOfferingRepository serviceOfferingRepository;
    private final BusinessRepository businessRepository;
    private final ServiceOfferingEntityToServiceOfferingResponse serviceOfferingEntityToServiceOfferingResponse;

    public ServiceOfferingServiceImpl(ServiceOfferingRepository serviceOfferingRepository,
                                      BusinessRepository businessRepository,
                                      ServiceOfferingEntityToServiceOfferingResponse serviceOfferingEntityToServiceOfferingResponse) {
        this.serviceOfferingRepository = serviceOfferingRepository;
        this.businessRepository = businessRepository;
        this.serviceOfferingEntityToServiceOfferingResponse = serviceOfferingEntityToServiceOfferingResponse;
    }

    @Override
    public List<ServiceOfferingResponse> findAll() {
        return serviceOfferingRepository.findAll().stream()
                .map(serviceOfferingEntityToServiceOfferingResponse::toResponse)
                .toList();
    }

    @Override
    public ServiceOfferingResponse findById(Long id) {
        return serviceOfferingRepository.findById(id)
                .map(serviceOfferingEntityToServiceOfferingResponse::toResponse)
                .orElseThrow();
    }

    @Override
    @Transactional
    public ServiceOfferingResponse create(CreateServiceOfferingRequest request) {
        Business business = businessRepository.findById(request.getBusinessId()).orElseThrow();
        ServiceOffering serviceOffering = ServiceOffering.builder()
                .business(business)
                .name(request.getName())
                .description(request.getDescription())
                .durationMinutes(request.getDurationMinutes())
                .price(request.getPrice())
                .active(request.getActive())
                .build();
        return serviceOfferingEntityToServiceOfferingResponse.toResponse(serviceOfferingRepository.save(serviceOffering));
    }
}
