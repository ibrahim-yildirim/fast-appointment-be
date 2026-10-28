package org.fastappointment.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.fastappointment.util.PhoneNumbers;

public class RegisterCustomerRequest extends RegisterRequest {

    @NotBlank
    @Pattern(regexp = PhoneNumbers.MOBILE_PATTERN, message = "{error.validation.mobilePhone}")
    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
