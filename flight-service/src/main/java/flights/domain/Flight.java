package flights.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import flights.domain.enumeration.EFlightType;

/**
 * A Flight.
 */
@Entity
@Table(name = "flight")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Flight implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "flight_number", nullable = false)
    private String flightNumber;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "flight_type", nullable = false)
    private EFlightType flightType;

    @Column(name = "pilot")
    private String pilot;

    @OneToOne
    @JoinColumn(unique = true)
    private FlightHandling flightHandler;

    @OneToMany(mappedBy = "flight")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Fare> fares = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "flights", allowSetters = true)
    private Airport origin;

    @ManyToOne
    @JsonIgnoreProperties(value = "flights", allowSetters = true)
    private Airport destination;

    @ManyToOne
    @JsonIgnoreProperties(value = "flights", allowSetters = true)
    private Airline airline;

    @ManyToOne
    @JsonIgnoreProperties(value = "flights", allowSetters = true)
    private Plane plane;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public Flight flightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
        return this;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public EFlightType getFlightType() {
        return flightType;
    }

    public Flight flightType(EFlightType flightType) {
        this.flightType = flightType;
        return this;
    }

    public void setFlightType(EFlightType flightType) {
        this.flightType = flightType;
    }

    public String getPilot() {
        return pilot;
    }

    public Flight pilot(String pilot) {
        this.pilot = pilot;
        return this;
    }

    public void setPilot(String pilot) {
        this.pilot = pilot;
    }

    public FlightHandling getFlightHandler() {
        return flightHandler;
    }

    public Flight flightHandler(FlightHandling flightHandling) {
        this.flightHandler = flightHandling;
        return this;
    }

    public void setFlightHandler(FlightHandling flightHandling) {
        this.flightHandler = flightHandling;
    }

    public Set<Fare> getFares() {
        return fares;
    }

    public Flight fares(Set<Fare> fares) {
        this.fares = fares;
        return this;
    }

    public Flight addFare(Fare fare) {
        this.fares.add(fare);
        fare.setFlight(this);
        return this;
    }

    public Flight removeFare(Fare fare) {
        this.fares.remove(fare);
        fare.setFlight(null);
        return this;
    }

    public void setFares(Set<Fare> fares) {
        this.fares = fares;
    }

    public Airport getOrigin() {
        return origin;
    }

    public Flight origin(Airport airport) {
        this.origin = airport;
        return this;
    }

    public void setOrigin(Airport airport) {
        this.origin = airport;
    }

    public Airport getDestination() {
        return destination;
    }

    public Flight destination(Airport airport) {
        this.destination = airport;
        return this;
    }

    public void setDestination(Airport airport) {
        this.destination = airport;
    }

    public Airline getAirline() {
        return airline;
    }

    public Flight airline(Airline airline) {
        this.airline = airline;
        return this;
    }

    public void setAirline(Airline airline) {
        this.airline = airline;
    }

    public Plane getPlane() {
        return plane;
    }

    public Flight plane(Plane plane) {
        this.plane = plane;
        return this;
    }

    public void setPlane(Plane plane) {
        this.plane = plane;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Flight)) {
            return false;
        }
        return id != null && id.equals(((Flight) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Flight{" +
            "id=" + getId() +
            ", flightNumber='" + getFlightNumber() + "'" +
            ", flightType='" + getFlightType() + "'" +
            ", pilot='" + getPilot() + "'" +
            "}";
    }
}
