package org.fastappointment.service;

import org.fastappointment.api.dto.BusinessResponse;
import org.fastappointment.api.dto.CreateBusinessRequest;

import java.util.List;

public interface BusinessService {

    List<BusinessResponse> findAll();

    BusinessResponse findBySlug(String slug);

    BusinessResponse create(Long ownerId, CreateBusinessRequest request);
}
