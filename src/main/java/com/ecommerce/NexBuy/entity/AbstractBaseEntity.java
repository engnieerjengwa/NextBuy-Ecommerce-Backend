package com.ecommerce.NexBuy.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractBaseEntity implements Serializable {

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="date_created", nullable = false, updatable = false)
    @CreatedDate
    protected Date dateCreated;

    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="last_updated", nullable = false)
    @LastModifiedDate
    protected Date lastUpdated;

//    @JsonIgnore
//    @CreatedBy
//    @Column(name="created_by", nullable = false, updatable = false)
//    protected String createdBy;
//
//    @JsonIgnore
//    @LastModifiedBy
//    @Column(name="modified_by", nullable = false)
//    protected String modifiedBy;

}
