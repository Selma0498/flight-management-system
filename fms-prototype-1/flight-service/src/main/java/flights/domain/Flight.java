package flights.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;

import flights.domain.enumeration.EFlightType;

import flights.domain.enumeration.EFareType;

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

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "fare_type", nullable = false)
    private EFareType fareType;

    @Column(name = "pilot")
    private String pilot;

    @Column(name = "plane_model_number")
    private String planeModelNumber;

    @NotNull
    @Column(name = "price", nullable = false)
    private Double price;

    @NotNull
    @Column(name = "departure_date", nullable = false)
    private LocalDate departureDate;

    @NotNull
    @Column(name = "boarding_gate", nullable = false)
    private Integer boardingGate;

    @Column(name = "airline_name")
    private String airlineName;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "flights", allowSetters = true)
    private Airport origin;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "flights", allowSetters = true)
    private Airport destination;

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

    public EFareType getFareType() {
        return fareType;
    }

    public Flight fareType(EFareType fareType) {
        this.fareType = fareType;
        return this;
    }

    public void setFareType(EFareType fareType) {
        this.fareType = fareType;
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

    public String getPlaneModelNumber() {
        return planeModelNumber;
    }

    public Flight planeModelNumber(String planeModelNumber) {
        this.planeModelNumber = planeModelNumber;
        return this;
    }

    public void setPlaneModelNumber(String planeModelNumber) {
        this.planeModelNumber = planeModelNumber;
    }

    public Double getPrice() {
        return price;
    }

    public Flight price(Double price) {
        this.price = price;
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public Flight departureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
        return this;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    public Integer getBoardingGate() {
        return boardingGate;
    }

    public Flight boardingGate(Integer boardingGate) {
        this.boardingGate = boardingGate;
        return this;
    }

    public void setBoardingGate(Integer boardingGate) {
        this.boardingGate = boardingGate;
    }

    public String getAirlineName() {
        return airlineName;
    }

    public Flight airlineName(String airlineName) {
        this.airlineName = airlineName;
        return this;
    }

    public void setAirlineName(String airlineName) {
        this.airlineName = airlineName;
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
            ", fareType='" + getFareType() + "'" +
            ", pilot='" + getPilot() + "'" +
            ", planeModelNumber='" + getPlaneModelNumber() + "'" +
            ", price=" + getPrice() +
            ", departureDate='" + getDepartureDate() + "'" +
            ", boardingGate=" + getBoardingGate() +
            ", airlineName='" + getAirlineName() + "'" +
            "}";
    }
}
