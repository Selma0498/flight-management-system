package payments.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A Payment.
 */
@Entity
@Table(name = "payment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Payment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "passenger_id", nullable = false)
    private String passengerId;

    @NotNull
    @Column(name = "to_pay", nullable = false)
    private Double toPay;

    @NotNull
    @Column(name = "booking_number", nullable = false)
    private Integer bookingNumber;

    @OneToOne
    @JoinColumn(unique = true)
    private CreditCard creditCard;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassengerId() {
        return passengerId;
    }

    public Payment passengerId(String passengerId) {
        this.passengerId = passengerId;
        return this;
    }

    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
    }

    public Double getToPay() {
        return toPay;
    }

    public Payment toPay(Double toPay) {
        this.toPay = toPay;
        return this;
    }

    public void setToPay(Double toPay) {
        this.toPay = toPay;
    }

    public Integer getBookingNumber() {
        return bookingNumber;
    }

    public Payment bookingNumber(Integer bookingNumber) {
        this.bookingNumber = bookingNumber;
        return this;
    }

    public void setBookingNumber(Integer bookingNumber) {
        this.bookingNumber = bookingNumber;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public Payment creditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
        return this;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Payment)) {
            return false;
        }
        return id != null && id.equals(((Payment) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Payment{" +
            "id=" + getId() +
            ", passengerId='" + getPassengerId() + "'" +
            ", toPay=" + getToPay() +
            ", bookingNumber=" + getBookingNumber() +
            "}";
    }
}
