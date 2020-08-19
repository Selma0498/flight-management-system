package bookings.repository;

import bookings.domain.Passenger;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Passenger entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Long> {
}
