package org.fastappointment.mapper;

import org.fastappointment.api.dto.PublicStaffResponse;
import org.fastappointment.entity.concretes.Staff;
import org.springframework.stereotype.Component;

@Component
public class StaffEntityToPublicStaffResponse {

    public PublicStaffResponse toResponse(Staff staff) {
        return new PublicStaffResponse(
                staff.getId(),
                staff.getName(),
                staff.getColorTag()
        );
    }
}
