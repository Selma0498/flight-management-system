package bookings.web.rest;

import bookings.domain.FlightHandling;
import bookings.repository.FlightHandlingRepository;
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
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link bookings.domain.FlightHandling}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class FlightHandlingResource {

    private final Logger log = LoggerFactory.getLogger(FlightHandlingResource.class);

    private static final String ENTITY_NAME = "bookingsFlightHandling";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FlightHandlingRepository flightHandlingRepository;

    public FlightHandlingResource(FlightHandlingRepository flightHandlingRepository) {
        this.flightHandlingRepository = flightHandlingRepository;
    }

    /**
     * {@code POST  /flight-handlings} : Create a new flightHandling.
     *
     * @param flightHandling the flightHandling to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new flightHandling, or with status {@code 400 (Bad Request)} if the flightHandling has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/flight-handlings")
    public ResponseEntity<FlightHandling> createFlightHandling(@Valid @RequestBody FlightHandling flightHandling) throws URISyntaxException {
        log.debug("REST request to save FlightHandling : {}", flightHandling);
        if (flightHandling.getId() != null) {
            throw new BadRequestAlertException("A new flightHandling cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FlightHandling result = flightHandlingRepository.save(flightHandling);
        return ResponseEntity.created(new URI("/api/flight-handlings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /flight-handlings} : Updates an existing flightHandling.
     *
     * @param flightHandling the flightHandling to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated flightHandling,
     * or with status {@code 400 (Bad Request)} if the flightHandling is not valid,
     * or with status {@code 500 (Internal Server Error)} if the flightHandling couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/flight-handlings")
    public ResponseEntity<FlightHandling> updateFlightHandling(@Valid @RequestBody FlightHandling flightHandling) throws URISyntaxException {
        log.debug("REST request to update FlightHandling : {}", flightHandling);
        if (flightHandling.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FlightHandling result = flightHandlingRepository.save(flightHandling);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, flightHandling.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /flight-handlings} : get all the flightHandlings.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of flightHandlings in body.
     */
    @GetMapping("/flight-handlings")
    public List<FlightHandling> getAllFlightHandlings() {
        log.debug("REST request to get all FlightHandlings");
        return flightHandlingRepository.findAll();
    }

    /**
     * {@code GET  /flight-handlings/:id} : get the "id" flightHandling.
     *
     * @param id the id of the flightHandling to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the flightHandling, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/flight-handlings/{id}")
    public ResponseEntity<FlightHandling> getFlightHandling(@PathVariable Long id) {
        log.debug("REST request to get FlightHandling : {}", id);
        Optional<FlightHandling> flightHandling = flightHandlingRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(flightHandling);
    }

    /**
     * {@code DELETE  /flight-handlings/:id} : delete the "id" flightHandling.
     *
     * @param id the id of the flightHandling to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/flight-handlings/{id}")
    public ResponseEntity<Void> deleteFlightHandling(@PathVariable Long id) {
        log.debug("REST request to delete FlightHandling : {}", id);

        flightHandlingRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
