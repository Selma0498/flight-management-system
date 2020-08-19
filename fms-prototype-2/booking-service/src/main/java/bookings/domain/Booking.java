package bookings.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Booking.
 */
@Entity
@Table(name = "booking")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Booking implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "booking_number", nullable = false)
    private Integer bookingNumber;

    @NotNull
    @Column(name = "flight_number", nullable = false)
    private String flightNumber;

    @NotNull
    @Column(name = "passenger_id", nullable = false)
    private String passengerId;

    @ManyToOne
    @JsonIgnoreProperties(value = "bookings", allowSetters = true)
    private Flight flight;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(name = "booking_passenger",
               joinColumns = @JoinColumn(name = "booking_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "passenger_id", referencedColumnName = "id"))
    private Set<Passenger> passengers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getBookingNumber() {
        return bookingNumber;
    }

    public Booking bookingNumber(Integer bookingNumber) {
        this.bookingNumber = bookingNumber;
        return this;
    }

    public void setBookingNumber(Integer bookingNumber) {
        this.bookingNumber = bookingNumber;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public Booking flightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
        return this;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getPassengerId() {
        return passengerId;
    }

    public Booking passengerId(String passengerId) {
        this.passengerId = passengerId;
        return this;
    }

    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
    }

    public Flight getFlight() {
        return flight;
    }

    public Booking flight(Flight flight) {
        this.flight = flight;
        return this;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public Set<Passenger> getPassengers() {
        return passengers;
    }

    public Booking passengers(Set<Passenger> passengers) {
        this.passengers = passengers;
        return this;
    }

    public Booking addPassenger(Passenger passenger) {
        this.passengers.add(passenger);
        passenger.getBookings().add(this);
        return this;
    }

    public Booking removePassenger(Passenger passenger) {
        this.passengers.remove(passenger);
        passenger.getBookings().remove(this);
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
        if (!(o instanceof Booking)) {
            return false;
        }
        return id != null && id.equals(((Booking) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Booking{" +
            "id=" + getId() +
            ", bookingNumber=" + getBookingNumber() +
            ", flightNumber='" + getFlightNumber() + "'" +
            ", passengerId='" + getPassengerId() + "'" +
            "}";
    }
}
