package bookings.repository;

import bookings.domain.Airline;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Airline entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AirlineRepository extends JpaRepository<Airline, Long> {
}
