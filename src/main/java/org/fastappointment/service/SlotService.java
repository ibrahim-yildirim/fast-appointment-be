package org.fastappointment.service;

import org.fastappointment.api.dto.SlotResponse;
import org.fastappointment.api.dto.SlotSearchRequest;

import java.util.List;

public interface SlotService {

    List<SlotResponse> findAvailableSlots(String slug, Long serviceId, SlotSearchRequest request);
}
