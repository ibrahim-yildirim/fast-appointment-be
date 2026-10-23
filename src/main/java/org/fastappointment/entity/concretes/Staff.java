package org.fastappointment.entity.concretes;

import jakarta.persistence.*;
import org.fastappointment.entity.abstracts.BaseEntity;

@Entity
@Table(name = "staff")
public class Staff extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Business business;

    private String name;

    private String email;

    private String colorTag;

    protected Staff() {
    }

    private Staff(Builder builder) {
        this.business = builder.business;
        this.name = builder.name;
        this.email = builder.email;
        this.colorTag = builder.colorTag;
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

    public String getEmail() {
        return email;
    }

    public String getColorTag() {
        return colorTag;
    }

    public void update(String name, String email, String colorTag) {
        this.name = name;
        this.email = email;
        this.colorTag = colorTag;
    }

    public static class Builder {

        private Business business;
        private String name;
        private String email;
        private String colorTag;

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

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder colorTag(String colorTag) {
            this.colorTag = colorTag;
            return this;
        }

        public Staff build() {
            return new Staff(this);
        }
    }
}
