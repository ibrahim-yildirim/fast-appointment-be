package org.fastappointment.mapper;

import org.fastappointment.api.dto.StaffResponse;
import org.fastappointment.entity.concretes.ServiceOffering;
import org.fastappointment.entity.concretes.Staff;
import org.springframework.stereotype.Component;

@Component
public class StaffEntityToStaffResponse {

    public StaffResponse toResponse(Staff staff) {
        return new StaffResponse(
                staff.getId(),
                staff.getBusiness().getId(),
                staff.getName(),
                staff.getEmail(),
                staff.getPhone(),
                staff.getColorTag(),
                staff.getActive(),
                staff.getServices().stream()
                        .map(ServiceOffering::getId)
                        .sorted()
                        .toList()
        );
    }
}
