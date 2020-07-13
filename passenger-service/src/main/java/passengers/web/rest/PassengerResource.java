package passengers.web.rest;

import passengers.domain.Passenger;
import passengers.domain.enumeration.EUserRole;
import passengers.repository.PassengerRepository;
import passengers.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.spring.web.json.Json;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REST controller for managing {@link passengers.domain.Passenger}.
 * Data to the endpoints provided by the controller are sent by the gateway!
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

    public PassengerResource(PassengerRepository passengerRepository) {
        this.passengerRepository = passengerRepository;
    }

    /**
     * {@code POST  /passengers} : Create a new passenger. Used for registering a passenger - when a user requests registration
     * via the interface
     *
     * @param passengerJSON the passenger to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new passenger, or with status {@code 400 (Bad Request)} if the passenger has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/passengers")
    public ResponseEntity<Passenger> createPassenger(@RequestBody Map<String, String> passengerJSON) throws URISyntaxException {
        Passenger passenger = null;
        try {
            if(passengerJSON.get("login").equals("user")) {
                passenger = new Passenger(
                    passengerJSON.get("login"),
                    "User",
                    "Userson",
                    passengerJSON.get("email"),
                    ""
                );
            } else {
                String firstName = "";
                String lastName = "";
                String email = "";
                if(passengerJSON.get("firstName").isEmpty()) firstName = "John"; else firstName = passengerJSON.get("firstName");
                if(passengerJSON.get("lastName").isEmpty()) lastName = "Doe"; else lastName = passengerJSON.get("lastName");
                if(passengerJSON.get("email").isEmpty()) email = "johndoe@yahoo.com"; else email = passengerJSON.get("email");

                passenger = new Passenger(
                    passengerJSON.get("login"),
                    firstName,
                    lastName,
                    email,
                    ""
                );
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }

        log.debug("REST request to save Passenger : {}", passenger);
        if (passenger.getId() != null) {
            throw new BadRequestAlertException("A new passenger cannot already have an ID", ENTITY_NAME, "id exists");
        }
        Passenger result = passengerRepository.save(passenger);
        return ResponseEntity.created(new URI("/api/passengers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /passengers} : Updates an existing passenger. When user requests update of his/hers data.
     *
     * @param passengerJSON the passenger to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated passenger,
     * or with status {@code 400 (Bad Request)} if the passenger is not valid,
     * or with status {@code 500 (Internal Server Error)} if the passenger couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/passengers")
    public ResponseEntity<Passenger> updatePassenger(@RequestBody Map<String, String> passengerJSON) throws URISyntaxException {
        Passenger passenger = null;
        try {
            String username = passengerJSON.get("login");

            if(!username.isEmpty() && passengerRepository.findByUsername(username).isPresent()) {
                passenger = new Passenger(
                    passengerJSON.get("login"),
                    passengerJSON.get("firstName"),
                    passengerJSON.get("lastName"),
                    passengerJSON.get("email"),
                    ""
                );
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }

        log.debug("REST request to update Passenger : {}", passenger);
        if (passenger.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "id null");
        }
        Passenger result = passengerRepository.save(passenger);
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
     * {@code GET /passenger/:username} : get the "username" passenger.
     *
     * @param username the username of the passenger to retrieve.
     * @return the {@Link ResponseEntity} with status {@code 200 (OK)} and with body the passenger, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/passengers/{username}")
    public ResponseEntity<Passenger> getPassenger(@PathVariable String username) {
        log.debug("REST request to get passenger : {}", username);
        Optional<Passenger> passenger = passengerRepository.findByUsername(username);
        return ResponseUtil.wrapOrNotFound(passenger);
    }

    /**
     * {@code DELETE  /passengers/:id} : delete the "id" passenger. When a user requests deletion of account.
     *
     * @param id the id of the passenger to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/passengers/{id}")
    public ResponseEntity<Void> deletePassenger(@PathVariable Long id) {
        log.debug("REST request to delete Passenger : {}", id);

        passengerRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code DELETE  /passengers/:username} : delete the "username" passenger. When a user requests deletion of account.
     *
     * @param username the username of the passenger to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/passengers/{username}")
    public ResponseEntity<Void> deletePassenger(@PathVariable String username) {
        log.debug("REST request to delete Passenger : {}", username);

        passengerRepository.deleteByUsername(username);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, username)).build();
    }


}
