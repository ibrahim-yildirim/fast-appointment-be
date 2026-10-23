package org.fastappointment.entity.concretes;

import jakarta.persistence.*;
import org.fastappointment.entity.abstracts.BaseEntity;

import java.math.BigDecimal;

@Entity
@Table(name = "services")
public class ServiceOffering extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Business business;

    private String name;

    @Column(columnDefinition = "text")
    private String description;

    private Integer durationMinutes;

    private BigDecimal price;

    private Boolean active;

    protected ServiceOffering() {
    }

    private ServiceOffering(Builder builder) {
        this.business = builder.business;
        this.name = builder.name;
        this.description = builder.description;
        this.durationMinutes = builder.durationMinutes;
        this.price = builder.price;
        this.active = builder.active;
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

    public void update(String name, String description, Integer durationMinutes, BigDecimal price, Boolean active) {
        this.name = name;
        this.description = description;
        this.durationMinutes = durationMinutes;
        this.price = price;
        this.active = active;
    }

    public static class Builder {

        private Business business;
        private String name;
        private String description;
        private Integer durationMinutes;
        private BigDecimal price;
        private Boolean active;

        private Builder() {
        }

        public Builder business(Business business) {
            this.business = business;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder durationMinutes(Integer durationMinutes) {
            this.durationMinutes = durationMinutes;
            return this;
        }

        public Builder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public Builder active(Boolean active) {
            this.active = active;
            return this;
        }

        public ServiceOffering build() {
            return new ServiceOffering(this);
        }
    }
}
