package org.fastappointment.api.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class SlotResponse {

    private final LocalDate date;
    private final LocalTime time;
    private final Long staffId;
    private final SlotStatus status;

    public SlotResponse(LocalDate date, LocalTime time, Long staffId, SlotStatus status) {
        this.date = date;
        this.time = time;
        this.staffId = staffId;
        this.status = status;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public Long getStaffId() {
        return staffId;
    }

    public SlotStatus getStatus() {
        return status;
    }
}
