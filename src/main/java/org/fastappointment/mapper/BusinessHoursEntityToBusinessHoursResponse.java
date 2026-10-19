package org.fastappointment.mapper;

import org.fastappointment.api.dto.BusinessHoursResponse;
import org.fastappointment.entity.concretes.BusinessHours;
import org.springframework.stereotype.Component;

@Component
public class BusinessHoursEntityToBusinessHoursResponse {

    public BusinessHoursResponse toResponse(BusinessHours businessHours) {
        return new BusinessHoursResponse(
                businessHours.getDayOfWeek(),
                businessHours.getEnabled(),
                businessHours.getStartTime(),
                businessHours.getEndTime()
        );
    }
}
