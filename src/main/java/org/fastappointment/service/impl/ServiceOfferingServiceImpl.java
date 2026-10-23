package org.fastappointment.service.impl;

import org.fastappointment.api.dto.CreateServiceOfferingRequest;
import org.fastappointment.api.dto.ServiceOfferingResponse;
import org.fastappointment.api.dto.UpdateServiceOfferingRequest;
import org.fastappointment.entity.concretes.Business;
import org.fastappointment.entity.concretes.ServiceOffering;
import org.fastappointment.exception.ErrorMessageKeys;
import org.fastappointment.exception.NotFoundException;
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
    public List<ServiceOfferingResponse> findActiveByBusinessSlug(String slug) {
        Business business = businessRepository.findBySlug(slug)
                .orElseThrow(() -> new NotFoundException(ErrorMessageKeys.BUSINESS_NOT_FOUND, slug));
        return serviceOfferingRepository.findByBusinessIdAndActiveTrue(business.getId()).stream()
                .map(serviceOfferingEntityToServiceOfferingResponse::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public ServiceOfferingResponse create(Long ownerId, CreateServiceOfferingRequest request) {
        Business business = businessRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> new NotFoundException(ErrorMessageKeys.OWNER_HAS_NO_BUSINESS));
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

    @Override
    @Transactional
    public ServiceOfferingResponse update(Long ownerId, Long id, UpdateServiceOfferingRequest request) {
        Business business = businessRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> new NotFoundException(ErrorMessageKeys.OWNER_HAS_NO_BUSINESS));
        ServiceOffering serviceOffering = serviceOfferingRepository.findByIdAndBusinessId(id, business.getId())
                .orElseThrow(() -> new NotFoundException(ErrorMessageKeys.SERVICE_NOT_FOUND, id));
        serviceOffering.update(request.getName(), request.getDescription(), request.getDurationMinutes(),
                request.getPrice(), request.getActive());
        return serviceOfferingEntityToServiceOfferingResponse.toResponse(serviceOffering);
    }
}
