package org.fastappointment.entity.concretes;

import jakarta.persistence.*;
import org.fastappointment.entity.abstracts.BaseEntity;

@Entity
@Table(name = "customers", uniqueConstraints = @UniqueConstraint(columnNames = {"business_id", "phone"}))
public class Customer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Business business;

    private String name;

    private String phone;

    private String email;

    protected Customer() {
    }

    private Customer(Builder builder) {
        this.user = builder.user;
        this.business = builder.business;
        this.name = builder.name;
        this.phone = builder.phone;
        this.email = builder.email;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Business getBusiness() {
        return business;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public static class Builder {

        private User user;
        private Business business;
        private String name;
        private String phone;
        private String email;

        private Builder() {
        }

        public Builder user(User user) {
            this.user = user;
            return this;
        }

        public Builder business(Business business) {
            this.business = business;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Customer build() {
            return new Customer(this);
        }
    }
}
