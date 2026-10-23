package org.fastappointment.api;

import jakarta.validation.Valid;
import org.fastappointment.api.dto.SlotResponse;
import org.fastappointment.api.dto.SlotSearchRequest;
import org.fastappointment.service.SlotService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SlotController {

    private final SlotService slotService;

    public SlotController(SlotService slotService) {
        this.slotService = slotService;
    }

    @GetMapping("/businesses/{slug}/services/{serviceId}/slots")
    public ResponseEntity<List<SlotResponse>> getAvailableSlots(@PathVariable String slug, @PathVariable Long serviceId,
                                                                @Valid SlotSearchRequest request) {
        return ResponseEntity.ok().body(slotService.findAvailableSlots(slug, serviceId, request));
    }
}
