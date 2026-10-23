package org.fastappointment.api;

import jakarta.validation.Valid;
import org.fastappointment.api.dto.CreateServiceOfferingRequest;
import org.fastappointment.api.dto.ServiceOfferingResponse;
import org.fastappointment.api.dto.UpdateServiceOfferingRequest;
import org.fastappointment.service.ServiceOfferingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ServiceOfferingController {

    private final ServiceOfferingService serviceOfferingService;

    public ServiceOfferingController(ServiceOfferingService serviceOfferingService) {
        this.serviceOfferingService = serviceOfferingService;
    }

    @GetMapping("/businesses/{slug}/services")
    public ResponseEntity<List<ServiceOfferingResponse>> getActiveByBusiness(@PathVariable String slug) {
        return ResponseEntity.ok().body(serviceOfferingService.findActiveByBusinessSlug(slug));
    }

    @PostMapping("/services")
    public ResponseEntity<ServiceOfferingResponse> create(Authentication authentication,
                                                          @Valid @RequestBody CreateServiceOfferingRequest request) {
        Long ownerId = Long.valueOf(authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(serviceOfferingService.create(ownerId, request));
    }

    @PutMapping("/services/{id}")
    public ResponseEntity<ServiceOfferingResponse> update(Authentication authentication, @PathVariable Long id,
                                                          @Valid @RequestBody UpdateServiceOfferingRequest request) {
        Long ownerId = Long.valueOf(authentication.getName());
        return ResponseEntity.ok().body(serviceOfferingService.update(ownerId, id, request));
    }
}
