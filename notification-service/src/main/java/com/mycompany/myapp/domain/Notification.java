package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

import com.mycompany.myapp.domain.enumeration.ENotificationType;

import com.mycompany.myapp.domain.enumeration.ENotificationStatus;

/**
 * A Notification.
 */
@Entity
@Table(name = "notification")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "notification")
public class Notification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ENotificationType type;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private ENotificationStatus state;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "is_sent")
    private Boolean isSent;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ENotificationType getType() {
        return type;
    }

    public Notification type(ENotificationType type) {
        this.type = type;
        return this;
    }

    public void setType(ENotificationType type) {
        this.type = type;
    }

    public ENotificationStatus getState() {
        return state;
    }

    public Notification state(ENotificationStatus state) {
        this.state = state;
        return this;
    }

    public void setState(ENotificationStatus state) {
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public Notification description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isIsSent() {
        return isSent;
    }

    public Notification isSent(Boolean isSent) {
        this.isSent = isSent;
        return this;
    }

    public void setIsSent(Boolean isSent) {
        this.isSent = isSent;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Notification)) {
            return false;
        }
        return id != null && id.equals(((Notification) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Notification{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", state='" + getState() + "'" +
            ", description='" + getDescription() + "'" +
            ", isSent='" + isIsSent() + "'" +
            "}";
    }
}
