package payment.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;

import payment.domain.enumeration.ECardType;

/**
 * A CreditCard.
 */
@Entity
@Table(name = "credit_card")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CreditCard implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "card_type", nullable = false)
    private ECardType cardType;

    @NotNull
    @Column(name = "cvc", nullable = false)
    private Integer cvc;

    @NotNull
    @Column(name = "card_number", nullable = false)
    private Integer cardNumber;

    @NotNull
    @Column(name = "validity_date", nullable = false)
    private LocalDate validityDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ECardType getCardType() {
        return cardType;
    }

    public CreditCard cardType(ECardType cardType) {
        this.cardType = cardType;
        return this;
    }

    public void setCardType(ECardType cardType) {
        this.cardType = cardType;
    }

    public Integer getCvc() {
        return cvc;
    }

    public CreditCard cvc(Integer cvc) {
        this.cvc = cvc;
        return this;
    }

    public void setCvc(Integer cvc) {
        this.cvc = cvc;
    }

    public Integer getCardNumber() {
        return cardNumber;
    }

    public CreditCard cardNumber(Integer cardNumber) {
        this.cardNumber = cardNumber;
        return this;
    }

    public void setCardNumber(Integer cardNumber) {
        this.cardNumber = cardNumber;
    }

    public LocalDate getValidityDate() {
        return validityDate;
    }

    public CreditCard validityDate(LocalDate validityDate) {
        this.validityDate = validityDate;
        return this;
    }

    public void setValidityDate(LocalDate validityDate) {
        this.validityDate = validityDate;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CreditCard)) {
            return false;
        }
        return id != null && id.equals(((CreditCard) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CreditCard{" +
            "id=" + getId() +
            ", cardType='" + getCardType() + "'" +
            ", cvc=" + getCvc() +
            ", cardNumber=" + getCardNumber() +
            ", validityDate='" + getValidityDate() + "'" +
            "}";
    }
}
