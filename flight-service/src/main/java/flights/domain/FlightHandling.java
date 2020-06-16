package flights.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A FlightHandling.
 */
@Entity
@Table(name = "flight_handling")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class FlightHandling implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "boarding_gate", nullable = false)
    private Integer boardingGate;

    @Column(name = "delay")
    private Double delay;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getBoardingGate() {
        return boardingGate;
    }

    public FlightHandling boardingGate(Integer boardingGate) {
        this.boardingGate = boardingGate;
        return this;
    }

    public void setBoardingGate(Integer boardingGate) {
        this.boardingGate = boardingGate;
    }

    public Double getDelay() {
        return delay;
    }

    public FlightHandling delay(Double delay) {
        this.delay = delay;
        return this;
    }

    public void setDelay(Double delay) {
        this.delay = delay;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FlightHandling)) {
            return false;
        }
        return id != null && id.equals(((FlightHandling) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FlightHandling{" +
            "id=" + getId() +
            ", boardingGate=" + getBoardingGate() +
            ", delay=" + getDelay() +
            "}";
    }
}
