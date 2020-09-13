package bookings.repository;

import bookings.domain.Booking;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Spring Data  repository for the Booking entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findBookingByBookingNumber(@NotNull Integer bookingNumber);
}
