package bookings.repository;

import bookings.domain.Plane;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Plane entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlaneRepository extends JpaRepository<Plane, Long> {
}
