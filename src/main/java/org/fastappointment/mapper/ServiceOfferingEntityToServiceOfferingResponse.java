package org.fastappointment.mapper;

import org.fastappointment.api.dto.ServiceOfferingResponse;
import org.fastappointment.entity.concretes.ServiceOffering;
import org.springframework.stereotype.Component;

@Component
public class ServiceOfferingEntityToServiceOfferingResponse {

    public ServiceOfferingResponse toResponse(ServiceOffering serviceOffering) {
        return new ServiceOfferingResponse(
                serviceOffering.getId(),
                serviceOffering.getBusiness().getId(),
                serviceOffering.getName(),
                serviceOffering.getDescription(),
                serviceOffering.getDurationMinutes(),
                serviceOffering.getPrice(),
                serviceOffering.getActive()
        );
    }
}
