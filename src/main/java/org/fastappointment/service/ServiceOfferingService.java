package org.fastappointment.service;

import org.fastappointment.api.dto.CreateServiceOfferingRequest;
import org.fastappointment.api.dto.ServiceOfferingResponse;

import java.util.List;

public interface ServiceOfferingService {

    List<ServiceOfferingResponse> findAll();

    ServiceOfferingResponse findById(Long id);

    ServiceOfferingResponse create(CreateServiceOfferingRequest request);
}
