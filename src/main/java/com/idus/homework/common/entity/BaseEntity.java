package com.idus.homework.common.entity;

import com.idus.homework.common.util.DateUtil;
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
        this.createdAt = DateUtil.getNow();
        this.updatedAt = DateUtil.getNow();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = DateUtil.getNow();
    }
}
