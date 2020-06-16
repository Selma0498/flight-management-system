package flights.web.rest;

import flights.domain.Fare;
import flights.repository.FareRepository;
import flights.web.rest.errors.BadRequestAlertException;

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
 * REST controller for managing {@link flights.domain.Fare}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class FareResource {

    private final Logger log = LoggerFactory.getLogger(FareResource.class);

    private static final String ENTITY_NAME = "flightsFare";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FareRepository fareRepository;

    public FareResource(FareRepository fareRepository) {
        this.fareRepository = fareRepository;
    }

    /**
     * {@code POST  /fares} : Create a new fare.
     *
     * @param fare the fare to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fare, or with status {@code 400 (Bad Request)} if the fare has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/fares")
    public ResponseEntity<Fare> createFare(@Valid @RequestBody Fare fare) throws URISyntaxException {
        log.debug("REST request to save Fare : {}", fare);
        if (fare.getId() != null) {
            throw new BadRequestAlertException("A new fare cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Fare result = fareRepository.save(fare);
        return ResponseEntity.created(new URI("/api/fares/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /fares} : Updates an existing fare.
     *
     * @param fare the fare to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fare,
     * or with status {@code 400 (Bad Request)} if the fare is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fare couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/fares")
    public ResponseEntity<Fare> updateFare(@Valid @RequestBody Fare fare) throws URISyntaxException {
        log.debug("REST request to update Fare : {}", fare);
        if (fare.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Fare result = fareRepository.save(fare);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, fare.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /fares} : get all the fares.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fares in body.
     */
    @GetMapping("/fares")
    public List<Fare> getAllFares() {
        log.debug("REST request to get all Fares");
        return fareRepository.findAll();
    }

    /**
     * {@code GET  /fares/:id} : get the "id" fare.
     *
     * @param id the id of the fare to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fare, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/fares/{id}")
    public ResponseEntity<Fare> getFare(@PathVariable Long id) {
        log.debug("REST request to get Fare : {}", id);
        Optional<Fare> fare = fareRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(fare);
    }

    /**
     * {@code DELETE  /fares/:id} : delete the "id" fare.
     *
     * @param id the id of the fare to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/fares/{id}")
    public ResponseEntity<Void> deleteFare(@PathVariable Long id) {
        log.debug("REST request to delete Fare : {}", id);

        fareRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
