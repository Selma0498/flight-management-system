package luggage.web.rest;

import luggage.domain.Luggage;
import luggage.repository.LuggageRepository;
import luggage.security.SecurityUtils;
import luggage.service.LuggageKafkaProducer;
import luggage.web.rest.errors.BadRequestAlertException;

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
 * REST controller for managing {@link luggage.domain.Luggage}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class LuggageResource {

    private final Logger log = LoggerFactory.getLogger(LuggageResource.class);

    private static final String ENTITY_NAME = "luggageLuggage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LuggageRepository luggageRepository;
    private final LuggageKafkaProducer luggageKafkaProducer;

    public LuggageResource(LuggageRepository luggageRepository, LuggageKafkaProducer luggageKafkaProducer) {
        this.luggageRepository = luggageRepository;
        this.luggageKafkaProducer = luggageKafkaProducer;
    }

    /**
     * {@code POST  /luggages} : Create a new luggage.
     *
     * @param luggage the luggage to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new luggage, or with status {@code 400 (Bad Request)} if the luggage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/luggages")
    public ResponseEntity<Luggage> createLuggage(@Valid @RequestBody Luggage luggage) throws URISyntaxException {
        log.debug("REST request to save Luggage : {}", luggage);
        if (luggage.getId() != null) {
            throw new BadRequestAlertException("A new luggage cannot already have an ID", ENTITY_NAME, "idexists");
        }

        Luggage result = luggageRepository.save(luggage);
        luggageKafkaProducer.sendLuggageEvent(result);

        return ResponseEntity.created(new URI("/api/luggages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /luggages} : Updates an existing luggage.
     *
     * @param luggage the luggage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated luggage,
     * or with status {@code 400 (Bad Request)} if the luggage is not valid,
     * or with status {@code 500 (Internal Server Error)} if the luggage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/luggages")
    public ResponseEntity<Luggage> updateLuggage(@Valid @RequestBody Luggage luggage) throws URISyntaxException {
        log.debug("REST request to update Luggage : {}", luggage);
        if (luggage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        Luggage result = luggageRepository.save(luggage);
        luggageKafkaProducer.sendLuggageEvent(result);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, luggage.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /luggages} : get all the luggages.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of luggages in body.
     */
    @GetMapping("/luggages")
    public List<Luggage> getAllLuggages() {
        log.debug("REST request to get all Luggages");

        List<Luggage> resultingLuggage = new ArrayList<>();

        // Return 404 if the entity is not owned by the connected user
        Optional<String> userLogin = SecurityUtils.getCurrentUserLogin();

        for (Luggage l : luggageRepository.findAll()) {
            if (userLogin.isPresent() && userLogin.get().equals(l.getPassengerId())) {
                resultingLuggage.add(l);
            }
        }
        return resultingLuggage;
    }


    /**
     * {@code GET  /luggages/:id} : get the "id" luggage.
     *
     * @param id the id of the luggage to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the luggage, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/luggages/{id}")
    public ResponseEntity<Luggage> getLuggage(@PathVariable Long id) {
        log.debug("REST request to get Luggage : {}", id);
        Optional<Luggage> luggage = luggageRepository.findById(id);

        // Return 404 if the entity is not owned by the connected user
        Optional<String> userLogin = SecurityUtils.getCurrentUserLogin();
        if(luggage.isPresent() &&
            userLogin.isPresent() &&
            userLogin.get().equals(luggage.get().getPassengerId())) {
            return ResponseUtil.wrapOrNotFound(luggage);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * {@code DELETE  /luggages/:id} : delete the "id" luggage.
     *
     * @param id the id of the luggage to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/luggages/{id}")
    public ResponseEntity<Void> deleteLuggage(@PathVariable Long id) {
        log.debug("REST request to delete Luggage : {}", id);
        Optional<Luggage> luggage = luggageRepository.findById(id);

        // Return 404 if the entity is not owned by the connected user
        Optional<String> userLogin = SecurityUtils.getCurrentUserLogin();
        if(luggage.isPresent() &&
            userLogin.isPresent() &&
            userLogin.get().equals(luggage.get().getPassengerId())) {
            luggageRepository.deleteById(id);
        } else {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
