package payments.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A Invoice.
 */
@Entity
@Table(name = "invoice")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Invoice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "invoice_number", nullable = false)
    private Integer invoiceNumber;

    @NotNull
    @Column(name = "amount", nullable = false)
    private Double amount;

    @NotNull
    @Column(name = "passenger_id", nullable = false)
    private String passengerId;

    @NotNull
    @Column(name = "booking_number", nullable = false)
    private Integer bookingNumber;

    @OneToOne(mappedBy = "invoice")
    @JsonIgnore
    private Payment payment;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getInvoiceNumber() {
        return invoiceNumber;
    }

    public Invoice invoiceNumber(Integer invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
        return this;
    }

    public void setInvoiceNumber(Integer invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Double getAmount() {
        return amount;
    }

    public Invoice amount(Double amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getPassengerId() {
        return passengerId;
    }

    public Invoice passengerId(String passengerId) {
        this.passengerId = passengerId;
        return this;
    }

    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
    }

    public Integer getBookingNumber() {
        return bookingNumber;
    }

    public Invoice bookingNumber(Integer bookingNumber) {
        this.bookingNumber = bookingNumber;
        return this;
    }

    public void setBookingNumber(Integer bookingNumber) {
        this.bookingNumber = bookingNumber;
    }

    public Payment getPayment() {
        return payment;
    }

    public Invoice payment(Payment payment) {
        this.payment = payment;
        return this;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Invoice)) {
            return false;
        }
        return id != null && id.equals(((Invoice) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Invoice{" +
            "id=" + getId() +
            ", invoiceNumber=" + getInvoiceNumber() +
            ", amount=" + getAmount() +
            ", passengerId='" + getPassengerId() + "'" +
            ", bookingNumber=" + getBookingNumber() +
            "}";
    }
}
