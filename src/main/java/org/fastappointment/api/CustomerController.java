package org.fastappointment.api;

import jakarta.validation.Valid;
import org.fastappointment.api.dto.CreateCustomerRequest;
import org.fastappointment.api.dto.CustomerResponse;
import org.fastappointment.api.dto.UpdateCustomerRequest;
import org.fastappointment.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getAll(Authentication authentication) {
        Long ownerId = Long.valueOf(authentication.getName());
        return ResponseEntity.ok().body(customerService.findAllForOwner(ownerId));
    }

    @GetMapping("/me")
    public ResponseEntity<CustomerResponse> getMe(Authentication authentication) {
        Long userId = Long.valueOf(authentication.getName());
        return ResponseEntity.ok().body(customerService.findForCustomer(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getById(Authentication authentication, @PathVariable Long id) {
        Long ownerId = Long.valueOf(authentication.getName());
        return ResponseEntity.ok().body(customerService.findByIdForOwner(ownerId, id));
    }

    @PostMapping
    public ResponseEntity<CustomerResponse> create(Authentication authentication,
                                                   @Valid @RequestBody CreateCustomerRequest request) {
        Long ownerId = Long.valueOf(authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.createForOwner(ownerId, request));
    }

    @PutMapping("/me")
    public ResponseEntity<CustomerResponse> updateMe(Authentication authentication,
                                                     @Valid @RequestBody UpdateCustomerRequest request) {
        Long userId = Long.valueOf(authentication.getName());
        return ResponseEntity.ok().body(customerService.updateForCustomer(userId, request));
    }
}
