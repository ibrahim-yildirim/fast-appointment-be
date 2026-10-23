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
@RequestMapping("/api/services")
public class ServiceOfferingController {

    private final ServiceOfferingService serviceOfferingService;

    public ServiceOfferingController(ServiceOfferingService serviceOfferingService) {
        this.serviceOfferingService = serviceOfferingService;
    }

    @GetMapping
    public ResponseEntity<List<ServiceOfferingResponse>> getAll() {
        return ResponseEntity.ok().body(serviceOfferingService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceOfferingResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok().body(serviceOfferingService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ServiceOfferingResponse> create(Authentication authentication,
                                                          @Valid @RequestBody CreateServiceOfferingRequest request) {
        Long ownerId = Long.valueOf(authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(serviceOfferingService.create(ownerId, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceOfferingResponse> update(Authentication authentication, @PathVariable Long id,
                                                          @Valid @RequestBody UpdateServiceOfferingRequest request) {
        Long ownerId = Long.valueOf(authentication.getName());
        return ResponseEntity.ok().body(serviceOfferingService.update(ownerId, id, request));
    }
}
