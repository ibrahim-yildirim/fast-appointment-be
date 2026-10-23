package org.fastappointment.service.impl;

import org.fastappointment.api.dto.CreateStaffRequest;
import org.fastappointment.api.dto.StaffResponse;
import org.fastappointment.entity.concretes.Business;
import org.fastappointment.entity.concretes.Staff;
import org.fastappointment.exception.ErrorMessageKeys;
import org.fastappointment.exception.NotFoundException;
import org.fastappointment.mapper.StaffEntityToStaffResponse;
import org.fastappointment.repository.BusinessRepository;
import org.fastappointment.repository.StaffRepository;
import org.fastappointment.service.StaffService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;
    private final BusinessRepository businessRepository;
    private final StaffEntityToStaffResponse staffEntityToStaffResponse;

    public StaffServiceImpl(StaffRepository staffRepository, BusinessRepository businessRepository,
                            StaffEntityToStaffResponse staffEntityToStaffResponse) {
        this.staffRepository = staffRepository;
        this.businessRepository = businessRepository;
        this.staffEntityToStaffResponse = staffEntityToStaffResponse;
    }

    @Override
    public List<StaffResponse> findAll() {
        return staffRepository.findAll().stream()
                .map(staffEntityToStaffResponse::toResponse)
                .toList();
    }

    @Override
    public StaffResponse findById(Long id) {
        return staffRepository.findById(id)
                .map(staffEntityToStaffResponse::toResponse)
                .orElseThrow(() -> new NotFoundException(ErrorMessageKeys.STAFF_NOT_FOUND, id));
    }

    @Override
    @Transactional
    public StaffResponse create(Long ownerId, CreateStaffRequest request) {
        Business business = businessRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> new NotFoundException(ErrorMessageKeys.OWNER_HAS_NO_BUSINESS));
        Staff staff = Staff.builder()
                .business(business)
                .name(request.getName())
                .email(request.getEmail())
                .colorTag(request.getColorTag())
                .build();
        return staffEntityToStaffResponse.toResponse(staffRepository.save(staff));
    }
}
