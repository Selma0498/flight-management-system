package bookings.web.rest;

import bookings.domain.Booking;
import bookings.domain.enumeration.ETopicType;
import bookings.repository.BookingRepository;
import bookings.security.SecurityUtils;
import bookings.service.BookingKafkaProducer;
import bookings.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link bookings.domain.Booking}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class BookingResource {

    private final Logger log = LoggerFactory.getLogger(BookingResource.class);

    private static final String ENTITY_NAME = "bookingsBooking";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BookingRepository bookingRepository;
    private final BookingKafkaProducer bookingKafkaProducer;

    public BookingResource(BookingRepository bookingRepository, BookingKafkaProducer bookingKafkaProducer) {
        this.bookingRepository = bookingRepository;
        this.bookingKafkaProducer = bookingKafkaProducer;
    }

    /**
     * {@code POST  /bookings} : Create a new booking.
     *
     * @param booking the booking to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new booking, or with status {@code 400 (Bad Request)} if the booking has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/bookings")
    public ResponseEntity<Booking> createBooking(@Valid @RequestBody Booking booking) throws URISyntaxException {
        log.debug("REST request to save Booking : {}", booking);
        if (booking.getId() != null) {
            throw new BadRequestAlertException("A new booking cannot already have an ID", ENTITY_NAME, "idexists");
        }

        Booking result = bookingRepository.save(booking);
        bookingKafkaProducer.sendBookingEvent(result, ETopicType.SET);

        return ResponseEntity.created(new URI("/api/bookings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /bookings} : Updates an existing booking.
     *
     * @param booking the booking to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated booking,
     * or with status {@code 400 (Bad Request)} if the booking is not valid,
     * or with status {@code 500 (Internal Server Error)} if the booking couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/bookings")
    public ResponseEntity<Booking> updateBooking(@Valid @RequestBody Booking booking) throws URISyntaxException {
        log.debug("REST request to update Booking : {}", booking);
        if (booking.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        Booking result = bookingRepository.save(booking);
        bookingKafkaProducer.sendBookingEvent(result, ETopicType.UPDATED);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, booking.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /bookings} : get all the bookings.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bookings in body.
     */
    @GetMapping("/bookings")
    public List<Booking> getAllBookings() {
        log.debug("REST request to get all Bookings");
        List<Booking> resultingBookings = new ArrayList<>();
        // Return 404 if the entity is not owned by the connected user
        Optional<String> userLogin = SecurityUtils.getCurrentUserLogin();

        for(Booking b: bookingRepository.findAll()) {
            if(userLogin.isPresent() && userLogin.get().equals(b.getPassengerId())) {
                resultingBookings.add(b);
            }
        }

        return resultingBookings;
    }

    /**
     * {@code GET  /bookings/:id} : get the "id" booking.
     *
     * @param id the id of the booking to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the booking, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/bookings/{id}")
    public ResponseEntity<Booking> getBooking(@PathVariable Long id) {
        log.debug("REST request to get Booking : {}", id);
        Optional<Booking> booking = bookingRepository.findById(id);

        // Return 404 if the entity is not owned by the connected user
        Optional<String> userLogin = SecurityUtils.getCurrentUserLogin();
        if(booking.isPresent() &&
            userLogin.isPresent() &&
            userLogin.get().equals(booking.get().getPassengerId())) {
            return ResponseUtil.wrapOrNotFound(booking);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * {@code DELETE  /bookings/:id} : delete the "id" booking.
     *
     * @param id the id of the booking to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/bookings/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        log.debug("REST request to delete Booking : {}", id);

        Optional<Booking> booking = bookingRepository.findById(id);

        // Return 404 if the entity is not owned by the connected user
        Optional<String> userLogin = SecurityUtils.getCurrentUserLogin();
        if(booking.isPresent() &&
            userLogin.isPresent() &&
            userLogin.get().equals(booking.get().getPassengerId())) {
            bookingRepository.deleteById(id);
            if(bookingRepository.findById(id).isPresent()) {
                bookingKafkaProducer.sendBookingEvent(bookingRepository.findById(id).get(), ETopicType.CANCELLED);
            }
        } else {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
