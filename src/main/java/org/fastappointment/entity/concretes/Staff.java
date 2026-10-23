package org.fastappointment.entity.concretes;

import jakarta.persistence.*;
import org.fastappointment.entity.abstracts.BaseEntity;
import org.hibernate.annotations.ColumnDefault;

import java.util.HashSet;
import java.util.Set;

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

    @ColumnDefault("true")
    @Column(nullable = false)
    private Boolean active;

    @ManyToMany(mappedBy = "eligibleStaff")
    private Set<ServiceOffering> services = new HashSet<>();

    protected Staff() {
    }

    private Staff(Builder builder) {
        this.business = builder.business;
        this.name = builder.name;
        this.email = builder.email;
        this.colorTag = builder.colorTag;
        this.active = true;
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

    public Boolean getActive() {
        return active;
    }

    public Set<ServiceOffering> getServices() {
        return services;
    }

    public void update(String name, String email, String colorTag) {
        this.name = name;
        this.email = email;
        this.colorTag = colorTag;
    }

    public void deactivate() {
        this.active = false;
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
