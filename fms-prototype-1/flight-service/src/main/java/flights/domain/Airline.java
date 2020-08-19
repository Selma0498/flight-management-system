package flights.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A Airline.
 */
@Entity
@Table(name = "airline")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Airline implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "airline_name")
    private String airlineName;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAirlineName() {
        return airlineName;
    }

    public Airline airlineName(String airlineName) {
        this.airlineName = airlineName;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Airline)) {
            return false;
        }
        return id != null && id.equals(((Airline) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Airline{" +
            "id=" + getId() +
            ", airlineName='" + getAirlineName() + "'" +
            "}";
    }
}
