package payment.repository;

import payment.domain.CreditCard;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the CreditCard entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, Long> {
}
