package org.fastappointment.api;

import jakarta.validation.Valid;
import org.fastappointment.api.dto.CreateServiceOfferingRequest;
import org.fastappointment.api.dto.ServiceOfferingResponse;
import org.fastappointment.service.ServiceOfferingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ServiceOfferingResponse> create(@Valid @RequestBody CreateServiceOfferingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(serviceOfferingService.create(request));
    }
}
