package com.irms.admin.domain;

import com.irms.common.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "audit_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog extends BaseEntity {

    @Column(nullable = false)
    private String action; // e.g., LOGIN_SUCCESS, USER_CREATED

    @Column
    private String entityName; // e.g., User, Role

    @Column
    private String entityId;

    @Column
    private String performedBy; // username

    @Column(columnDefinition = "TEXT")
    private String details;

}
