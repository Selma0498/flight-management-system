package passengers.web.rest;

import passengers.domain.NotificationRepo;
import passengers.repository.NotificationRepoRepository;
import passengers.web.rest.errors.BadRequestAlertException;

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
 * REST controller for managing {@link passengers.domain.NotificationRepo}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class NotificationRepoResource {

    private final Logger log = LoggerFactory.getLogger(NotificationRepoResource.class);

    private static final String ENTITY_NAME = "passengersNotificationRepo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NotificationRepoRepository notificationRepoRepository;

    public NotificationRepoResource(NotificationRepoRepository notificationRepoRepository) {
        this.notificationRepoRepository = notificationRepoRepository;
    }

    /**
     * {@code POST  /notification-repos} : Create a new notificationRepo.
     *
     * @param notificationRepo the notificationRepo to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new notificationRepo, or with status {@code 400 (Bad Request)} if the notificationRepo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/notification-repos")
    public ResponseEntity<NotificationRepo> createNotificationRepo(@Valid @RequestBody NotificationRepo notificationRepo) throws URISyntaxException {
        log.debug("REST request to save NotificationRepo : {}", notificationRepo);
        if (notificationRepo.getId() != null) {
            throw new BadRequestAlertException("A new notificationRepo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NotificationRepo result = notificationRepoRepository.save(notificationRepo);
        return ResponseEntity.created(new URI("/api/notification-repos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /notification-repos} : Updates an existing notificationRepo.
     *
     * @param notificationRepo the notificationRepo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated notificationRepo,
     * or with status {@code 400 (Bad Request)} if the notificationRepo is not valid,
     * or with status {@code 500 (Internal Server Error)} if the notificationRepo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/notification-repos")
    public ResponseEntity<NotificationRepo> updateNotificationRepo(@Valid @RequestBody NotificationRepo notificationRepo) throws URISyntaxException {
        log.debug("REST request to update NotificationRepo : {}", notificationRepo);
        if (notificationRepo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        NotificationRepo result = notificationRepoRepository.save(notificationRepo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, notificationRepo.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /notification-repos} : get all the notificationRepos.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of notificationRepos in body.
     */
    @GetMapping("/notification-repos")
    public List<NotificationRepo> getAllNotificationRepos(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all NotificationRepos");
        return notificationRepoRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /notification-repos/:id} : get the "id" notificationRepo.
     *
     * @param id the id of the notificationRepo to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the notificationRepo, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/notification-repos/{id}")
    public ResponseEntity<NotificationRepo> getNotificationRepo(@PathVariable Long id) {
        log.debug("REST request to get NotificationRepo : {}", id);
        Optional<NotificationRepo> notificationRepo = notificationRepoRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(notificationRepo);
    }

    /**
     * {@code DELETE  /notification-repos/:id} : delete the "id" notificationRepo.
     *
     * @param id the id of the notificationRepo to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/notification-repos/{id}")
    public ResponseEntity<Void> deleteNotificationRepo(@PathVariable Long id) {
        log.debug("REST request to delete NotificationRepo : {}", id);

        notificationRepoRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
