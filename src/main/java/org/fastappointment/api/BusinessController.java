package org.fastappointment.api;

import jakarta.validation.Valid;
import org.fastappointment.api.dto.BusinessResponse;
import org.fastappointment.api.dto.CreateBusinessRequest;
import org.fastappointment.service.BusinessService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/businesses")
public class BusinessController {

    private final BusinessService businessService;

    public BusinessController(BusinessService businessService) {
        this.businessService = businessService;
    }

    @GetMapping
    public ResponseEntity<List<BusinessResponse>> getAll() {
        return ResponseEntity.ok().body(businessService.findAll());
    }

    @GetMapping("/{slug}")
    public ResponseEntity<BusinessResponse> getBySlug(@PathVariable String slug) {
        return ResponseEntity.ok().body(businessService.findBySlug(slug));
    }

    @PostMapping
    public ResponseEntity<BusinessResponse> create(@Valid @RequestBody CreateBusinessRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(businessService.create(request));
    }
}
