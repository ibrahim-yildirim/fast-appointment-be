package org.fastappointment.api;

import jakarta.validation.Valid;
import org.fastappointment.api.dto.CreateStaffRequest;
import org.fastappointment.api.dto.StaffResponse;
import org.fastappointment.service.StaffService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/staff")
public class StaffController {

    private final StaffService staffService;

    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @GetMapping
    public ResponseEntity<List<StaffResponse>> getAll() {
        return ResponseEntity.ok().body(staffService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StaffResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok().body(staffService.findById(id));
    }

    @PostMapping
    public ResponseEntity<StaffResponse> create(@Valid @RequestBody CreateStaffRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(staffService.create(request));
    }
}
