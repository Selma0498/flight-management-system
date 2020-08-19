package bookings.repository;

import bookings.domain.Flight;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Flight entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {
}
