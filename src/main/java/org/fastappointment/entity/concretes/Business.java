package org.fastappointment.entity.concretes;

import jakarta.persistence.*;
import org.fastappointment.entity.abstracts.BaseEntity;

@Entity
@Table(name = "businesses")
public class Business extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String slug;

    private String name;

    @Column(columnDefinition = "text")
    private String description;

    private String timezone;

    private String address;

    private String phone;

    private String email;

    protected Business() {
    }

    private Business(Builder builder) {
        this.slug = builder.slug;
        this.name = builder.name;
        this.description = builder.description;
        this.timezone = builder.timezone;
        this.address = builder.address;
        this.phone = builder.phone;
        this.email = builder.email;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getId() {
        return id;
    }

    public String getSlug() {
        return slug;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getTimezone() {
        return timezone;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public static class Builder {

        private String slug;
        private String name;
        private String description;
        private String timezone;
        private String address;
        private String phone;
        private String email;

        private Builder() {
        }

        public Builder slug(String slug) {
            this.slug = slug;
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

        public Builder timezone(String timezone) {
            this.timezone = timezone;
            return this;
        }

        public Builder address(String address) {
            this.address = address;
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

        public Business build() {
            return new Business(this);
        }
    }
}
