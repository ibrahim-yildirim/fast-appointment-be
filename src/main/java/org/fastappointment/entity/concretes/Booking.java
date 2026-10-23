package org.fastappointment.entity.concretes;

import jakarta.persistence.*;
import org.fastappointment.entity.abstracts.BaseEntity;
import org.fastappointment.entity.enums.BookingStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
public class Booking extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY)
    private ServiceOffering service;

    @ManyToOne(fetch = FetchType.LAZY)
    private Staff staff;

    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;

    private LocalDateTime startsAt;

    private LocalDateTime endsAt;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @Column(columnDefinition = "text")
    private String notes;

    protected Booking() {
    }

    private Booking(Builder builder) {
        this.business = builder.business;
        this.service = builder.service;
        this.staff = builder.staff;
        this.customer = builder.customer;
        this.startsAt = builder.startsAt;
        this.endsAt = builder.endsAt;
        this.status = builder.status;
        this.notes = builder.notes;
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

    public ServiceOffering getService() {
        return service;
    }

    public Staff getStaff() {
        return staff;
    }

    public Customer getCustomer() {
        return customer;
    }

    public LocalDateTime getStartsAt() {
        return startsAt;
    }

    public LocalDateTime getEndsAt() {
        return endsAt;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public String getNotes() {
        return notes;
    }

    public void reschedule(ServiceOffering service, Staff staff, LocalDateTime startsAt, LocalDateTime endsAt, String notes) {
        this.service = service;
        this.staff = staff;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
        this.notes = notes;
    }

    public void cancel() {
        this.status = BookingStatus.CANCELLED;
    }

    public static class Builder {

        private Business business;
        private ServiceOffering service;
        private Staff staff;
        private Customer customer;
        private LocalDateTime startsAt;
        private LocalDateTime endsAt;
        private BookingStatus status;
        private String notes;

        private Builder() {
        }

        public Builder business(Business business) {
            this.business = business;
            return this;
        }

        public Builder service(ServiceOffering service) {
            this.service = service;
            return this;
        }

        public Builder staff(Staff staff) {
            this.staff = staff;
            return this;
        }

        public Builder customer(Customer customer) {
            this.customer = customer;
            return this;
        }

        public Builder startsAt(LocalDateTime startsAt) {
            this.startsAt = startsAt;
            return this;
        }

        public Builder endsAt(LocalDateTime endsAt) {
            this.endsAt = endsAt;
            return this;
        }

        public Builder status(BookingStatus status) {
            this.status = status;
            return this;
        }

        public Builder notes(String notes) {
            this.notes = notes;
            return this;
        }

        public Booking build() {
            return new Booking(this);
        }
    }
}
