package flights.repository;

import flights.domain.FlightHandling;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the FlightHandling entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FlightHandlingRepository extends JpaRepository<FlightHandling, Long> {
}
