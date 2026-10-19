package org.fastappointment.mapper;

import org.fastappointment.api.dto.BusinessResponse;
import org.fastappointment.entity.concretes.Business;
import org.springframework.stereotype.Component;

@Component
public class BusinessEntityToBusinessResponse {

    public BusinessResponse toResponse(Business business) {
        return new BusinessResponse(
                business.getId(),
                business.getSlug(),
                business.getName(),
                business.getDescription(),
                business.getTimezone(),
                business.getAddress(),
                business.getPhone(),
                business.getEmail(),
                business.getCdate()
        );
    }
}
