package payments.repository;

import payments.domain.Validator;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Validator entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ValidatorRepository extends JpaRepository<Validator, Long> {
}
