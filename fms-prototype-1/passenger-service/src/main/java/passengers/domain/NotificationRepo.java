package passengers.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A NotificationRepo.
 */
@Entity
@Table(name = "notification_repo")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class NotificationRepo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(name = "notification_repo_passenger",
               joinColumns = @JoinColumn(name = "notification_repo_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "passenger_id", referencedColumnName = "id"))
    private Set<Passenger> passengers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public NotificationRepo name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public NotificationRepo description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Passenger> getPassengers() {
        return passengers;
    }

    public NotificationRepo passengers(Set<Passenger> passengers) {
        this.passengers = passengers;
        return this;
    }

    public NotificationRepo addPassenger(Passenger passenger) {
        this.passengers.add(passenger);
        passenger.getNotificationRepos().add(this);
        return this;
    }

    public NotificationRepo removePassenger(Passenger passenger) {
        this.passengers.remove(passenger);
        passenger.getNotificationRepos().remove(this);
        return this;
    }

    public void setPassengers(Set<Passenger> passengers) {
        this.passengers = passengers;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NotificationRepo)) {
            return false;
        }
        return id != null && id.equals(((NotificationRepo) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotificationRepo{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
