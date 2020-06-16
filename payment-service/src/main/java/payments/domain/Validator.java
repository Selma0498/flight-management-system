package payments.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A Validator.
 */
@Entity
@Table(name = "validator")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Validator implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "is_payment_method_valid")
    private Boolean isPaymentMethodValid;

    @Column(name = "is_invoice_valid")
    private Boolean isInvoiceValid;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isIsPaymentMethodValid() {
        return isPaymentMethodValid;
    }

    public Validator isPaymentMethodValid(Boolean isPaymentMethodValid) {
        this.isPaymentMethodValid = isPaymentMethodValid;
        return this;
    }

    public void setIsPaymentMethodValid(Boolean isPaymentMethodValid) {
        this.isPaymentMethodValid = isPaymentMethodValid;
    }

    public Boolean isIsInvoiceValid() {
        return isInvoiceValid;
    }

    public Validator isInvoiceValid(Boolean isInvoiceValid) {
        this.isInvoiceValid = isInvoiceValid;
        return this;
    }

    public void setIsInvoiceValid(Boolean isInvoiceValid) {
        this.isInvoiceValid = isInvoiceValid;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Validator)) {
            return false;
        }
        return id != null && id.equals(((Validator) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Validator{" +
            "id=" + getId() +
            ", isPaymentMethodValid='" + isIsPaymentMethodValid() + "'" +
            ", isInvoiceValid='" + isIsInvoiceValid() + "'" +
            "}";
    }
}
