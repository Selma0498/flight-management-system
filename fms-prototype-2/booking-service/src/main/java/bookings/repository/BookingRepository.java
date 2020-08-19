package bookings.repository;

import bookings.domain.Booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Booking entity.
 */
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query(value = "select distinct booking from Booking booking left join fetch booking.passengers",
        countQuery = "select count(distinct booking) from Booking booking")
    Page<Booking> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct booking from Booking booking left join fetch booking.passengers")
    List<Booking> findAllWithEagerRelationships();

    @Query("select booking from Booking booking left join fetch booking.passengers where booking.id =:id")
    Optional<Booking> findOneWithEagerRelationships(@Param("id") Long id);
}
