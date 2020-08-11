package luggage.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

import luggage.domain.enumeration.ELuggageType;

/**
 * A Luggage.
 */
@Entity
@Table(name = "luggage")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Luggage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "luggage_type", nullable = false)
    private ELuggageType luggageType;

    @NotNull
    @Column(name = "luggage_number", nullable = false)
    private Integer luggageNumber;

    @NotNull
    @Column(name = "flight_number", nullable = false)
    private String flightNumber;

    @NotNull
    @Column(name = "booking_number", nullable = false)
    private Integer bookingNumber;

    @NotNull
    @Column(name = "passenger_id", nullable = false)
    private String passengerId;

    @NotNull
    @Column(name = "weight_category", nullable = false)
    private Integer weightCategory;

    @Column(name = "rfid_tag")
    private String rfidTag;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ELuggageType getLuggageType() {
        return luggageType;
    }

    public Luggage luggageType(ELuggageType luggageType) {
        this.luggageType = luggageType;
        return this;
    }

    public void setLuggageType(ELuggageType luggageType) {
        this.luggageType = luggageType;
    }

    public Integer getLuggageNumber() {
        return luggageNumber;
    }

    public Luggage luggageNumber(Integer luggageNumber) {
        this.luggageNumber = luggageNumber;
        return this;
    }

    public void setLuggageNumber(Integer luggageNumber) {
        this.luggageNumber = luggageNumber;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public Luggage flightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
        return this;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public Integer getBookingNumber() {
        return bookingNumber;
    }

    public Luggage bookingNumber(Integer bookingNumber) {
        this.bookingNumber = bookingNumber;
        return this;
    }

    public void setBookingNumber(Integer bookingNumber) {
        this.bookingNumber = bookingNumber;
    }

    public String getPassengerId() {
        return passengerId;
    }

    public Luggage passengerId(String passengerId) {
        this.passengerId = passengerId;
        return this;
    }

    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
    }

    public Integer getWeightCategory() {
        return weightCategory;
    }

    public Luggage weightCategory(Integer weightCategory) {
        this.weightCategory = weightCategory;
        return this;
    }

    public void setWeightCategory(Integer weightCategory) {
        this.weightCategory = weightCategory;
    }

    public String getRfidTag() {
        return rfidTag;
    }

    public Luggage rfidTag(String rfidTag) {
        this.rfidTag = rfidTag;
        return this;
    }

    public void setRfidTag(String rfidTag) {
        this.rfidTag = rfidTag;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Luggage)) {
            return false;
        }
        return id != null && id.equals(((Luggage) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Luggage{" +
            "id=" + getId() +
            ", luggageType='" + getLuggageType() + "'" +
            ", luggageNumber=" + getLuggageNumber() +
            ", flightNumber='" + getFlightNumber() + "'" +
            ", bookingNumber=" + getBookingNumber() +
            ", passengerId='" + getPassengerId() + "'" +
            ", weightCategory=" + getWeightCategory() +
            ", rfidTag='" + getRfidTag() + "'" +
            "}";
    }
}
