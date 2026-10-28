package org.fastappointment.api.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import org.fastappointment.util.AppConstants;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class SlotSearchRequest {

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate from;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate to;

    @AssertTrue(message = "{error.slots.invalidRange}")
    public boolean isValidRange() {
        if (from == null || to == null) {
            return true;
        }
        return !to.isBefore(from) && !to.isAfter(from.plusDays(AppConstants.SLOT_SEARCH_MAX_DAYS - 1));
    }

    public LocalDate getFrom() {
        return from;
    }

    public void setFrom(LocalDate from) {
        this.from = from;
    }

    public LocalDate getTo() {
        return to;
    }

    public void setTo(LocalDate to) {
        this.to = to;
    }
}
