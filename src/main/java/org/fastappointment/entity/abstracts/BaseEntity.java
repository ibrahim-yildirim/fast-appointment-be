package org.fastappointment.entity.abstracts;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime cdate;

    @CreatedBy
    @Column(updatable = false)
    private String cuser;

    @LastModifiedDate
    private LocalDateTime udate;

    @LastModifiedBy
    private String uuser;

    public LocalDateTime getCdate() {
        return cdate;
    }

    public String getCuser() {
        return cuser;
    }

    public LocalDateTime getUdate() {
        return udate;
    }

    public String getUuser() {
        return uuser;
    }
}
