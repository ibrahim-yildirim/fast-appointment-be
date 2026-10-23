package org.fastappointment.api;

import jakarta.validation.Valid;
import org.fastappointment.api.dto.CreateStaffRequest;
import org.fastappointment.api.dto.PublicStaffResponse;
import org.fastappointment.api.dto.StaffResponse;
import org.fastappointment.api.dto.UpdateStaffRequest;
import org.fastappointment.service.StaffService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class StaffController {

    private final StaffService staffService;

    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @GetMapping("/businesses/{slug}/staff")
    public ResponseEntity<List<PublicStaffResponse>> getAllByBusiness(@PathVariable String slug) {
        return ResponseEntity.ok().body(staffService.findAllByBusinessSlug(slug));
    }

    @PostMapping("/staff")
    public ResponseEntity<StaffResponse> create(Authentication authentication,
                                                @Valid @RequestBody CreateStaffRequest request) {
        Long ownerId = Long.valueOf(authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(staffService.create(ownerId, request));
    }

    @PutMapping("/staff/{id}")
    public ResponseEntity<StaffResponse> update(Authentication authentication, @PathVariable Long id,
                                                @Valid @RequestBody UpdateStaffRequest request) {
        Long ownerId = Long.valueOf(authentication.getName());
        return ResponseEntity.ok().body(staffService.update(ownerId, id, request));
    }
}
