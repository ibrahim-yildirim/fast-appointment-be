package org.fastappointment.api.dto;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class BusinessHoursResponse {

    private final DayOfWeek day;
    private final Boolean enabled;
    private final LocalTime start;
    private final LocalTime end;

    public BusinessHoursResponse(DayOfWeek day, Boolean enabled, LocalTime start, LocalTime end) {
        this.day = day;
        this.enabled = enabled;
        this.start = start;
        this.end = end;
    }

    public DayOfWeek getDay() {
        return day;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public LocalTime getStart() {
        return start;
    }

    public LocalTime getEnd() {
        return end;
    }
}
