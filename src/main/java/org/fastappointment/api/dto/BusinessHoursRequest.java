package org.fastappointment.api.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class BusinessHoursRequest {

    @NotNull
    private DayOfWeek day;

    @NotNull
    private Boolean enabled;

    private LocalTime start;

    private LocalTime end;

    @AssertTrue(message = "{error.hours.invalidTimes}")
    public boolean isValidTimes() {
        if (!Boolean.TRUE.equals(enabled)) {
            return true;
        }
        return start != null && end != null && end.isAfter(start);
    }

    public DayOfWeek getDay() {
        return day;
    }

    public void setDay(DayOfWeek day) {
        this.day = day;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public LocalTime getStart() {
        return start;
    }

    public void setStart(LocalTime start) {
        this.start = start;
    }

    public LocalTime getEnd() {
        return end;
    }

    public void setEnd(LocalTime end) {
        this.end = end;
    }
}
