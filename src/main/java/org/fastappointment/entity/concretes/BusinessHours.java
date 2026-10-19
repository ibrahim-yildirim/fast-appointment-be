package org.fastappointment.entity.concretes;

import jakarta.persistence.*;
import org.fastappointment.entity.abstracts.BaseEntity;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Table(name = "business_hours")
public class BusinessHours extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Business business;

    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    private Boolean enabled;

    private LocalTime startTime;

    private LocalTime endTime;

    protected BusinessHours() {
    }

    private BusinessHours(Builder builder) {
        this.business = builder.business;
        this.dayOfWeek = builder.dayOfWeek;
        this.enabled = builder.enabled;
        this.startTime = builder.startTime;
        this.endTime = builder.endTime;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getId() {
        return id;
    }

    public Business getBusiness() {
        return business;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public static class Builder {

        private Business business;
        private DayOfWeek dayOfWeek;
        private Boolean enabled;
        private LocalTime startTime;
        private LocalTime endTime;

        private Builder() {
        }

        public Builder business(Business business) {
            this.business = business;
            return this;
        }

        public Builder dayOfWeek(DayOfWeek dayOfWeek) {
            this.dayOfWeek = dayOfWeek;
            return this;
        }

        public Builder enabled(Boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public Builder startTime(LocalTime startTime) {
            this.startTime = startTime;
            return this;
        }

        public Builder endTime(LocalTime endTime) {
            this.endTime = endTime;
            return this;
        }

        public BusinessHours build() {
            return new BusinessHours(this);
        }
    }
}
