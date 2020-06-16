package flights.repository;

import flights.domain.Fare;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Fare entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FareRepository extends JpaRepository<Fare, Long> {
}
