package flights.repository;

import flights.domain.Luggage;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Luggage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LuggageRepository extends JpaRepository<Luggage, Long> {
}
