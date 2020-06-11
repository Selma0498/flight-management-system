package passenger.web.rest;

import passenger.domain.Passenger;
import passenger.repository.PassengerRepository;
import passenger.repository.search.PassengerSearchRepository;
import passenger.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link Passenger}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PassengerResource {

    private final Logger log = LoggerFactory.getLogger(PassengerResource.class);

    private static final String ENTITY_NAME = "passengersPassenger";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PassengerRepository passengerRepository;

    private final PassengerSearchRepository passengerSearchRepository;

    public PassengerResource(PassengerRepository passengerRepository, PassengerSearchRepository passengerSearchRepository) {
        this.passengerRepository = passengerRepository;
        this.passengerSearchRepository = passengerSearchRepository;
    }

    /**
     * {@code POST  /passengers} : Create a new passenger.
     *
     * @param passenger the passenger to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new passenger, or with status {@code 400 (Bad Request)} if the passenger has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/passengers")
    public ResponseEntity<Passenger> createPassenger(@RequestBody Passenger passenger) throws URISyntaxException {
        log.debug("REST request to save Passenger : {}", passenger);
        if (passenger.getId() != null) {
            throw new BadRequestAlertException("A new passenger cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Passenger result = passengerRepository.save(passenger);
        passengerSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/passengers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /passengers} : Updates an existing passenger.
     *
     * @param passenger the passenger to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated passenger,
     * or with status {@code 400 (Bad Request)} if the passenger is not valid,
     * or with status {@code 500 (Internal Server Error)} if the passenger couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/passengers")
    public ResponseEntity<Passenger> updatePassenger(@RequestBody Passenger passenger) throws URISyntaxException {
        log.debug("REST request to update Passenger : {}", passenger);
        if (passenger.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Passenger result = passengerRepository.save(passenger);
        passengerSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, passenger.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /passengers} : get all the passengers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of passengers in body.
     */
    @GetMapping("/passengers")
    public List<Passenger> getAllPassengers() {
        log.debug("REST request to get all Passengers");
        return passengerRepository.findAll();
    }

    /**
     * {@code GET  /passengers/:id} : get the "id" passenger.
     *
     * @param id the id of the passenger to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the passenger, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/passengers/{id}")
    public ResponseEntity<Passenger> getPassenger(@PathVariable Long id) {
        log.debug("REST request to get Passenger : {}", id);
        Optional<Passenger> passenger = passengerRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(passenger);
    }

    /**
     * {@code DELETE  /passengers/:id} : delete the "id" passenger.
     *
     * @param id the id of the passenger to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/passengers/{id}")
    public ResponseEntity<Void> deletePassenger(@PathVariable Long id) {
        log.debug("REST request to delete Passenger : {}", id);

        passengerRepository.deleteById(id);
        passengerSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/passengers?query=:query} : search for the passenger corresponding
     * to the query.
     *
     * @param query the query of the passenger search.
     * @return the result of the search.
     */
    @GetMapping("/_search/passengers")
    public List<Passenger> searchPassengers(@RequestParam String query) {
        log.debug("REST request to search Passengers for query {}", query);
        return StreamSupport
            .stream(passengerSearchRepository.search(queryStringQuery(query)).spliterator(), false)
        .collect(Collectors.toList());
    }
}
