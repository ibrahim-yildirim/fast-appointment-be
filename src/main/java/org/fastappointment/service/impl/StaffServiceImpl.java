package org.fastappointment.service.impl;

import org.fastappointment.api.dto.CreateStaffRequest;
import org.fastappointment.api.dto.PublicStaffResponse;
import org.fastappointment.api.dto.StaffResponse;
import org.fastappointment.api.dto.UpdateStaffRequest;
import org.fastappointment.entity.concretes.Business;
import org.fastappointment.entity.concretes.ServiceOffering;
import org.fastappointment.entity.concretes.Staff;
import org.fastappointment.exception.ErrorMessageKeys;
import org.fastappointment.exception.NotFoundException;
import org.fastappointment.mapper.StaffEntityToPublicStaffResponse;
import org.fastappointment.mapper.StaffEntityToStaffResponse;
import org.fastappointment.repository.BusinessRepository;
import org.fastappointment.repository.ServiceOfferingRepository;
import org.fastappointment.repository.StaffRepository;
import org.fastappointment.service.StaffService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;
    private final BusinessRepository businessRepository;
    private final ServiceOfferingRepository serviceOfferingRepository;
    private final StaffEntityToStaffResponse staffEntityToStaffResponse;
    private final StaffEntityToPublicStaffResponse staffEntityToPublicStaffResponse;

    public StaffServiceImpl(StaffRepository staffRepository, BusinessRepository businessRepository,
                            ServiceOfferingRepository serviceOfferingRepository,
                            StaffEntityToStaffResponse staffEntityToStaffResponse,
                            StaffEntityToPublicStaffResponse staffEntityToPublicStaffResponse) {
        this.staffRepository = staffRepository;
        this.businessRepository = businessRepository;
        this.serviceOfferingRepository = serviceOfferingRepository;
        this.staffEntityToStaffResponse = staffEntityToStaffResponse;
        this.staffEntityToPublicStaffResponse = staffEntityToPublicStaffResponse;
    }

    @Override
    public List<PublicStaffResponse> findAllByBusinessSlug(String slug) {
        Business business = businessRepository.findBySlug(slug)
                .orElseThrow(() -> new NotFoundException(ErrorMessageKeys.BUSINESS_NOT_FOUND, slug));
        return staffRepository.findByBusinessIdAndActiveTrue(business.getId()).stream()
                .map(staffEntityToPublicStaffResponse::toResponse)
                .toList();
    }

    @Override
    public List<StaffResponse> findAllForOwner(Long ownerId) {
        Business business = findOwnerBusiness(ownerId);
        return staffRepository.findByBusinessIdOrderByIdAsc(business.getId()).stream()
                .map(staffEntityToStaffResponse::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public StaffResponse create(Long ownerId, CreateStaffRequest request) {
        Business business = findOwnerBusiness(ownerId);
        List<ServiceOffering> services = findOwnServices(business, request.getServiceIds());
        Staff staff = staffRepository.save(Staff.builder()
                .business(business)
                .name(request.getName())
                .email(request.getEmail())
                .colorTag(request.getColorTag())
                .build());
        assignServices(business, staff, services);
        return staffEntityToStaffResponse.toResponse(staff);
    }

    @Override
    @Transactional
    public StaffResponse update(Long ownerId, Long id, UpdateStaffRequest request) {
        Business business = findOwnerBusiness(ownerId);
        Staff staff = findOwnStaff(business, id);
        List<ServiceOffering> services = findOwnServices(business, request.getServiceIds());
        staff.update(request.getName(), request.getEmail(), request.getColorTag());
        assignServices(business, staff, services);
        return staffEntityToStaffResponse.toResponse(staff);
    }

    @Override
    @Transactional
    public void deactivate(Long ownerId, Long id) {
        Business business = findOwnerBusiness(ownerId);
        findOwnStaff(business, id).deactivate();
    }

    private Business findOwnerBusiness(Long ownerId) {
        return businessRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> new NotFoundException(ErrorMessageKeys.OWNER_HAS_NO_BUSINESS));
    }

    private Staff findOwnStaff(Business business, Long id) {
        return staffRepository.findByIdAndBusinessId(id, business.getId())
                .orElseThrow(() -> new NotFoundException(ErrorMessageKeys.STAFF_NOT_FOUND, id));
    }

    private List<ServiceOffering> findOwnServices(Business business, List<Long> serviceIds) {
        List<ServiceOffering> services = serviceOfferingRepository.findByBusinessIdAndIdIn(business.getId(), serviceIds);
        List<Long> foundIds = services.stream().map(ServiceOffering::getId).toList();
        serviceIds.stream()
                .filter(id -> !foundIds.contains(id))
                .findFirst()
                .ifPresent(id -> {
                    throw new NotFoundException(ErrorMessageKeys.SERVICE_NOT_FOUND, id);
                });
        return services;
    }

    private void assignServices(Business business, Staff staff, List<ServiceOffering> services) {
        for (ServiceOffering service : serviceOfferingRepository.findByBusinessIdOrderByIdAsc(business.getId())) {
            if (services.contains(service)) {
                service.addStaff(staff);
            } else {
                service.removeStaff(staff);
            }
        }
    }
}
