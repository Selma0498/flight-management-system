package bookings.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

import bookings.domain.enumeration.EBookingState;

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

    @Column(name = "invoice_number")
    private Integer invoiceNumber;

    @NotNull
    @Column(name = "invoice_set", nullable = false)
    private Boolean invoiceSet;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private EBookingState state;

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

    public Integer getInvoiceNumber() {
        return invoiceNumber;
    }

    public Booking invoiceNumber(Integer invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
        return this;
    }

    public void setInvoiceNumber(Integer invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Boolean isInvoiceSet() {
        return invoiceSet;
    }

    public Booking invoiceSet(Boolean invoiceSet) {
        this.invoiceSet = invoiceSet;
        return this;
    }

    public void setInvoiceSet(Boolean invoiceSet) {
        this.invoiceSet = invoiceSet;
    }

    public EBookingState getState() {
        return state;
    }

    public Booking state(EBookingState state) {
        this.state = state;
        return this;
    }

    public void setState(EBookingState state) {
        this.state = state;
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
            ", invoiceNumber=" + getInvoiceNumber() +
            ", invoiceSet='" + isInvoiceSet() + "'" +
            ", state='" + getState() + "'" +
            "}";
    }
}
