package org.fastappointment.api;

import jakarta.validation.Valid;
import org.fastappointment.api.dto.BusinessResponse;
import org.fastappointment.api.dto.CreateBusinessRequest;
import org.fastappointment.api.dto.UpdateBusinessRequest;
import org.fastappointment.service.BusinessService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BusinessController {

    private final BusinessService businessService;

    public BusinessController(BusinessService businessService) {
        this.businessService = businessService;
    }

    @GetMapping("/businesses")
    public ResponseEntity<List<BusinessResponse>> getAll() {
        return ResponseEntity.ok().body(businessService.findAll());
    }

    @GetMapping("/businesses/{slug}")
    public ResponseEntity<BusinessResponse> getBySlug(@PathVariable String slug) {
        return ResponseEntity.ok().body(businessService.findBySlug(slug));
    }

    @PostMapping("/businesses")
    public ResponseEntity<BusinessResponse> create(Authentication authentication,
                                                   @Valid @RequestBody CreateBusinessRequest request) {
        Long ownerId = Long.valueOf(authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(businessService.create(ownerId, request));
    }

    @PutMapping("/business")
    public ResponseEntity<BusinessResponse> update(Authentication authentication,
                                                   @Valid @RequestBody UpdateBusinessRequest request) {
        Long ownerId = Long.valueOf(authentication.getName());
        return ResponseEntity.ok().body(businessService.update(ownerId, request));
    }
}
