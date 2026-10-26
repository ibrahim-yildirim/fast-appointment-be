package org.fastappointment.mapper;

import org.fastappointment.api.dto.BusinessHoursResponse;
import org.fastappointment.api.dto.BusinessResponse;
import org.fastappointment.entity.concretes.Business;
import org.fastappointment.entity.concretes.BusinessHours;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
public class BusinessEntityToBusinessResponse {

    private final BusinessHoursEntityToBusinessHoursResponse businessHoursEntityToBusinessHoursResponse;

    public BusinessEntityToBusinessResponse(BusinessHoursEntityToBusinessHoursResponse businessHoursEntityToBusinessHoursResponse) {
        this.businessHoursEntityToBusinessHoursResponse = businessHoursEntityToBusinessHoursResponse;
    }

    public BusinessResponse toResponse(Business business, List<BusinessHours> hours) {
        List<BusinessHoursResponse> defaultHours = hours.stream()
                .sorted(Comparator.comparing(BusinessHours::getDayOfWeek))
                .map(businessHoursEntityToBusinessHoursResponse::toResponse)
                .toList();
        return new BusinessResponse(
                business.getId(),
                business.getSlug(),
                business.getName(),
                business.getDescription(),
                business.getTimezone(),
                business.getAddress(),
                business.getCity(),
                business.getPhone(),
                business.getEmail(),
                defaultHours,
                business.getCdate()
        );
    }
}
