package payments.web.rest;

import payments.domain.Validator;
import payments.repository.ValidatorRepository;
import payments.web.rest.errors.BadRequestAlertException;

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
 * REST controller for managing {@link payments.domain.Validator}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ValidatorResource {

    private final Logger log = LoggerFactory.getLogger(ValidatorResource.class);

    private static final String ENTITY_NAME = "paymentsValidator";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ValidatorRepository validatorRepository;

    public ValidatorResource(ValidatorRepository validatorRepository) {
        this.validatorRepository = validatorRepository;
    }

    /**
     * {@code POST  /validators} : Create a new validator.
     *
     * @param validator the validator to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new validator, or with status {@code 400 (Bad Request)} if the validator has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/validators")
    public ResponseEntity<Validator> createValidator(@RequestBody Validator validator) throws URISyntaxException {
        log.debug("REST request to save Validator : {}", validator);
        if (validator.getId() != null) {
            throw new BadRequestAlertException("A new validator cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Validator result = validatorRepository.save(validator);
        return ResponseEntity.created(new URI("/api/validators/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /validators} : Updates an existing validator.
     *
     * @param validator the validator to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated validator,
     * or with status {@code 400 (Bad Request)} if the validator is not valid,
     * or with status {@code 500 (Internal Server Error)} if the validator couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/validators")
    public ResponseEntity<Validator> updateValidator(@RequestBody Validator validator) throws URISyntaxException {
        log.debug("REST request to update Validator : {}", validator);
        if (validator.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Validator result = validatorRepository.save(validator);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, validator.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /validators} : get all the validators.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of validators in body.
     */
    @GetMapping("/validators")
    public List<Validator> getAllValidators() {
        log.debug("REST request to get all Validators");
        return validatorRepository.findAll();
    }

    /**
     * {@code GET  /validators/:id} : get the "id" validator.
     *
     * @param id the id of the validator to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the validator, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/validators/{id}")
    public ResponseEntity<Validator> getValidator(@PathVariable Long id) {
        log.debug("REST request to get Validator : {}", id);
        Optional<Validator> validator = validatorRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(validator);
    }

    /**
     * {@code DELETE  /validators/:id} : delete the "id" validator.
     *
     * @param id the id of the validator to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/validators/{id}")
    public ResponseEntity<Void> deleteValidator(@PathVariable Long id) {
        log.debug("REST request to delete Validator : {}", id);

        validatorRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
