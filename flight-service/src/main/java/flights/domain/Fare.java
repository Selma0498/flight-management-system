package flights.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

import flights.domain.enumeration.EFareType;

/**
 * A Fare.
 */
@Entity
@Table(name = "fare")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Fare implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private EFareType type;

    @NotNull
    @Column(name = "price", nullable = false)
    private Double price;

    @ManyToOne
    @JsonIgnoreProperties(value = "fares", allowSetters = true)
    private Flight flight;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EFareType getType() {
        return type;
    }

    public Fare type(EFareType type) {
        this.type = type;
        return this;
    }

    public void setType(EFareType type) {
        this.type = type;
    }

    public Double getPrice() {
        return price;
    }

    public Fare price(Double price) {
        this.price = price;
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Flight getFlight() {
        return flight;
    }

    public Fare flight(Flight flight) {
        this.flight = flight;
        return this;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Fare)) {
            return false;
        }
        return id != null && id.equals(((Fare) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Fare{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", price=" + getPrice() +
            "}";
    }
}
