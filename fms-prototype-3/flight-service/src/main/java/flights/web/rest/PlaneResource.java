package flights.web.rest;

import flights.domain.Plane;
import flights.repository.PlaneRepository;
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
 * REST controller for managing {@link flights.domain.Plane}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PlaneResource {

    private final Logger log = LoggerFactory.getLogger(PlaneResource.class);

    private static final String ENTITY_NAME = "flightsPlane";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PlaneRepository planeRepository;

    public PlaneResource(PlaneRepository planeRepository) {
        this.planeRepository = planeRepository;
    }

    /**
     * {@code POST  /planes} : Create a new plane.
     *
     * @param plane the plane to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new plane, or with status {@code 400 (Bad Request)} if the plane has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/planes")
    public ResponseEntity<Plane> createPlane(@Valid @RequestBody Plane plane) throws URISyntaxException {
        log.debug("REST request to save Plane : {}", plane);
        if (plane.getId() != null) {
            throw new BadRequestAlertException("A new plane cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Plane result = planeRepository.save(plane);
        return ResponseEntity.created(new URI("/api/planes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /planes} : Updates an existing plane.
     *
     * @param plane the plane to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated plane,
     * or with status {@code 400 (Bad Request)} if the plane is not valid,
     * or with status {@code 500 (Internal Server Error)} if the plane couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/planes")
    public ResponseEntity<Plane> updatePlane(@Valid @RequestBody Plane plane) throws URISyntaxException {
        log.debug("REST request to update Plane : {}", plane);
        if (plane.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Plane result = planeRepository.save(plane);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, plane.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /planes} : get all the planes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of planes in body.
     */
    @GetMapping("/planes")
    public List<Plane> getAllPlanes() {
        log.debug("REST request to get all Planes");
        return planeRepository.findAll();
    }

    /**
     * {@code GET  /planes/:id} : get the "id" plane.
     *
     * @param id the id of the plane to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the plane, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/planes/{id}")
    public ResponseEntity<Plane> getPlane(@PathVariable Long id) {
        log.debug("REST request to get Plane : {}", id);
        Optional<Plane> plane = planeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(plane);
    }

    /**
     * {@code DELETE  /planes/:id} : delete the "id" plane.
     *
     * @param id the id of the plane to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/planes/{id}")
    public ResponseEntity<Void> deletePlane(@PathVariable Long id) {
        log.debug("REST request to delete Plane : {}", id);

        planeRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
