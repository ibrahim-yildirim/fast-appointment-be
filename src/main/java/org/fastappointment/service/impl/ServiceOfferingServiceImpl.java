package org.fastappointment.service.impl;

import org.fastappointment.api.dto.CreateServiceOfferingRequest;
import org.fastappointment.api.dto.ServiceOfferingResponse;
import org.fastappointment.api.dto.UpdateServiceOfferingRequest;
import org.fastappointment.entity.concretes.Business;
import org.fastappointment.entity.concretes.ServiceOffering;
import org.fastappointment.entity.concretes.Staff;
import org.fastappointment.exception.ErrorMessageKeys;
import org.fastappointment.exception.NotFoundException;
import org.fastappointment.mapper.ServiceOfferingEntityToServiceOfferingResponse;
import org.fastappointment.repository.BusinessRepository;
import org.fastappointment.repository.ServiceOfferingRepository;
import org.fastappointment.repository.StaffRepository;
import org.fastappointment.service.ServiceOfferingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ServiceOfferingServiceImpl implements ServiceOfferingService {

    private final ServiceOfferingRepository serviceOfferingRepository;
    private final BusinessRepository businessRepository;
    private final StaffRepository staffRepository;
    private final ServiceOfferingEntityToServiceOfferingResponse serviceOfferingEntityToServiceOfferingResponse;

    public ServiceOfferingServiceImpl(ServiceOfferingRepository serviceOfferingRepository,
                                      BusinessRepository businessRepository,
                                      StaffRepository staffRepository,
                                      ServiceOfferingEntityToServiceOfferingResponse serviceOfferingEntityToServiceOfferingResponse) {
        this.serviceOfferingRepository = serviceOfferingRepository;
        this.businessRepository = businessRepository;
        this.staffRepository = staffRepository;
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
    public List<ServiceOfferingResponse> findAllForOwner(Long ownerId) {
        Business business = findOwnerBusiness(ownerId);
        return serviceOfferingRepository.findByBusinessIdOrderByIdAsc(business.getId()).stream()
                .map(serviceOfferingEntityToServiceOfferingResponse::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public ServiceOfferingResponse create(Long ownerId, CreateServiceOfferingRequest request) {
        Business business = findOwnerBusiness(ownerId);
        Set<Staff> eligibleStaff = findOwnStaff(business, request.getEligibleStaffIds());
        ServiceOffering serviceOffering = serviceOfferingRepository.save(ServiceOffering.builder()
                .business(business)
                .name(request.getName())
                .description(request.getDescription())
                .durationMinutes(request.getDurationMinutes())
                .price(request.getPrice())
                .active(request.getActive())
                .build());
        serviceOffering.replaceEligibleStaff(eligibleStaff);
        return serviceOfferingEntityToServiceOfferingResponse.toResponse(serviceOffering);
    }

    @Override
    @Transactional
    public ServiceOfferingResponse update(Long ownerId, Long id, UpdateServiceOfferingRequest request) {
        Business business = findOwnerBusiness(ownerId);
        ServiceOffering serviceOffering = findOwnService(business, id);
        Set<Staff> eligibleStaff = findOwnStaff(business, request.getEligibleStaffIds());
        serviceOffering.update(request.getName(), request.getDescription(), request.getDurationMinutes(),
                request.getPrice(), request.getActive());
        serviceOffering.replaceEligibleStaff(eligibleStaff);
        return serviceOfferingEntityToServiceOfferingResponse.toResponse(serviceOffering);
    }

    @Override
    @Transactional
    public void deactivate(Long ownerId, Long id) {
        Business business = findOwnerBusiness(ownerId);
        findOwnService(business, id).deactivate();
    }

    private Business findOwnerBusiness(Long ownerId) {
        return businessRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> new NotFoundException(ErrorMessageKeys.OWNER_HAS_NO_BUSINESS));
    }

    private ServiceOffering findOwnService(Business business, Long id) {
        return serviceOfferingRepository.findByIdAndBusinessId(id, business.getId())
                .orElseThrow(() -> new NotFoundException(ErrorMessageKeys.SERVICE_NOT_FOUND, id));
    }

    private Set<Staff> findOwnStaff(Business business, List<Long> staffIds) {
        List<Staff> staff = staffRepository.findByBusinessIdAndIdIn(business.getId(), staffIds);
        List<Long> foundIds = staff.stream().map(Staff::getId).toList();
        staffIds.stream()
                .filter(id -> !foundIds.contains(id))
                .findFirst()
                .ifPresent(id -> {
                    throw new NotFoundException(ErrorMessageKeys.STAFF_NOT_FOUND, id);
                });
        return new HashSet<>(staff);
    }
}
