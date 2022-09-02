package com.idus.homework.common.entity;

import lombok.Getter;

import javax.persistence.*;
import java.time.ZonedDateTime;

@MappedSuperclass
@Getter
public abstract class BaseEntity {
    @Column(updatable = false, nullable = false)
    private ZonedDateTime createdAt;

    @Column
    private ZonedDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = ZonedDateTime.now();
        this.updatedAt = ZonedDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = ZonedDateTime.now();
    }
}
