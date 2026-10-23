package org.fastappointment.service;

import org.fastappointment.api.dto.CreateStaffRequest;
import org.fastappointment.api.dto.StaffResponse;

import java.util.List;

public interface StaffService {

    List<StaffResponse> findAll();

    StaffResponse findById(Long id);

    StaffResponse create(Long ownerId, CreateStaffRequest request);
}
