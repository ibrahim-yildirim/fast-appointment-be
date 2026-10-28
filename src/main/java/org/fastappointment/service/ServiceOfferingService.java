package org.fastappointment.service;

import org.fastappointment.api.dto.CreateServiceOfferingRequest;
import org.fastappointment.api.dto.ServiceOfferingResponse;
import org.fastappointment.api.dto.UpdateServiceOfferingRequest;

import java.util.List;

public interface ServiceOfferingService {

    List<ServiceOfferingResponse> findActiveByBusinessSlug(String slug);

    List<ServiceOfferingResponse> findAllForOwner(Long ownerId);

    ServiceOfferingResponse create(Long ownerId, CreateServiceOfferingRequest request);

    ServiceOfferingResponse update(Long ownerId, Long id, UpdateServiceOfferingRequest request);

    void deactivate(Long ownerId, Long id);

    void delete(Long ownerId, Long id);
}
