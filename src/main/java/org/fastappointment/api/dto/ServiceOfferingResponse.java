package org.fastappointment.api.dto;

import java.math.BigDecimal;

public class ServiceOfferingResponse {

    private final Long id;
    private final Long businessId;
    private final String name;
    private final String description;
    private final Integer durationMinutes;
    private final BigDecimal price;
    private final Boolean active;

    public ServiceOfferingResponse(Long id, Long businessId, String name, String description,
                                   Integer durationMinutes, BigDecimal price, Boolean active) {
        this.id = id;
        this.businessId = businessId;
        this.name = name;
        this.description = description;
        this.durationMinutes = durationMinutes;
        this.price = price;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Boolean getActive() {
        return active;
    }
}
