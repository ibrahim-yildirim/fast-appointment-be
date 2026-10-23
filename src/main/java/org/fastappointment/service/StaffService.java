package org.fastappointment.service;

import org.fastappointment.api.dto.CreateStaffRequest;
import org.fastappointment.api.dto.PublicStaffResponse;
import org.fastappointment.api.dto.StaffResponse;
import org.fastappointment.api.dto.UpdateStaffRequest;

import java.util.List;

public interface StaffService {

    List<PublicStaffResponse> findAllByBusinessSlug(String slug);

    List<StaffResponse> findAllForOwner(Long ownerId);

    StaffResponse create(Long ownerId, CreateStaffRequest request);

    StaffResponse update(Long ownerId, Long id, UpdateStaffRequest request);

    void deactivate(Long ownerId, Long id);
}
