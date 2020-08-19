package flights.web.rest;

import flights.domain.Airline;
import flights.repository.AirlineRepository;
import flights.web.rest.errors.BadRequestAlertException;

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

/**
 * REST controller for managing {@link flights.domain.Airline}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AirlineResource {

    private final Logger log = LoggerFactory.getLogger(AirlineResource.class);

    private static final String ENTITY_NAME = "flightsAirline";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AirlineRepository airlineRepository;

    public AirlineResource(AirlineRepository airlineRepository) {
        this.airlineRepository = airlineRepository;
    }

    /**
     * {@code POST  /airlines} : Create a new airline.
     *
     * @param airline the airline to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new airline, or with status {@code 400 (Bad Request)} if the airline has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/airlines")
    public ResponseEntity<Airline> createAirline(@RequestBody Airline airline) throws URISyntaxException {
        log.debug("REST request to save Airline : {}", airline);
        if (airline.getId() != null) {
            throw new BadRequestAlertException("A new airline cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Airline result = airlineRepository.save(airline);
        return ResponseEntity.created(new URI("/api/airlines/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /airlines} : Updates an existing airline.
     *
     * @param airline the airline to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated airline,
     * or with status {@code 400 (Bad Request)} if the airline is not valid,
     * or with status {@code 500 (Internal Server Error)} if the airline couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/airlines")
    public ResponseEntity<Airline> updateAirline(@RequestBody Airline airline) throws URISyntaxException {
        log.debug("REST request to update Airline : {}", airline);
        if (airline.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Airline result = airlineRepository.save(airline);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, airline.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /airlines} : get all the airlines.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of airlines in body.
     */
    @GetMapping("/airlines")
    public List<Airline> getAllAirlines() {
        log.debug("REST request to get all Airlines");
        return airlineRepository.findAll();
    }

    /**
     * {@code GET  /airlines/:id} : get the "id" airline.
     *
     * @param id the id of the airline to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the airline, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/airlines/{id}")
    public ResponseEntity<Airline> getAirline(@PathVariable Long id) {
        log.debug("REST request to get Airline : {}", id);
        Optional<Airline> airline = airlineRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(airline);
    }

    /**
     * {@code DELETE  /airlines/:id} : delete the "id" airline.
     *
     * @param id the id of the airline to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/airlines/{id}")
    public ResponseEntity<Void> deleteAirline(@PathVariable Long id) {
        log.debug("REST request to delete Airline : {}", id);

        airlineRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
