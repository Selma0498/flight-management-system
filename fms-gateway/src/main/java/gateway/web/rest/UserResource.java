package gateway.web.rest;

import feign.Response;
import gateway.config.Constants;
import gateway.domain.User;
import gateway.repository.UserRepository;
import gateway.security.AuthoritiesConstants;
import gateway.service.MailService;
import gateway.service.UserService;
import gateway.service.dto.UserDTO;
import gateway.web.rest.errors.BadRequestAlertException;
import gateway.web.rest.errors.EmailAlreadyUsedException;
import gateway.web.rest.errors.LoginAlreadyUsedException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.*;

/**
 * REST controller for managing users.
 * <p>
 * This class accesses the {@link User} entity, and needs to fetch its collection of authorities.
 * <p>
 * For a normal use-case, it would be better to have an eager relationship between User and Authority,
 * and send everything to the client side: there would be no View Model and DTO, a lot less code, and an outer-join
 * which would be good for performance.
 * <p>
 * We use a View Model and a DTO for 3 reasons:
 * <ul>
 * <li>We want to keep a lazy association between the user and the authorities, because people will
 * quite often do relationships with the user, and we don't want them to get the authorities all
 * the time for nothing (for performance reasons). This is the #1 goal: we should not impact our users'
 * application because of this use-case.</li>
 * <li> Not having an outer join causes n+1 requests to the database. This is not a real issue as
 * we have by default a second-level cache. This means on the first HTTP call we do the n+1 requests,
 * but then all authorities come from the cache, so in fact it's much better than doing an outer join
 * (which will get lots of data from the database, for each HTTP call).</li>
 * <li> As this manages users, for security reasons, we'd rather have a DTO layer.</li>
 * </ul>
 * <p>
 * Another option would be to have a specific JPA entity graph to handle this case.
 */
@RestController
@RequestMapping("/api")
public class UserResource {

    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserService userService;

    private final UserRepository userRepository;

    private final MailService mailService;

    // Access to passenger microservice
    private final String passengerMicroserviceBaseURL = "http://localhost:8081/";
    private RestTemplate restTemplate;

    public UserResource(UserService userService, UserRepository userRepository, MailService mailService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.mailService = mailService;
        this.restTemplate = new RestTemplate();
    }

    /**
     * {@code POST  /users}  : Creates a new user.
     * <p>
     * Creates a new user if the login and email are not already used, and sends an
     * mail with an activation link.
     * The user needs to be activated on creation.
     *
     * Each time an admin creates a user on the JHI side, the equivalent passenger object will be created in passenger microservice
     * using provided endpoints by passenger microservice.
     *
     * @param userDTO the user to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new user, or with status {@code 400 (Bad Request)} if the login or email is already in use.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     * @throws BadRequestAlertException {@code 400 (Bad Request)} if the login or email is already in use.
     */
    // TODO Each time a user is created by an admin on the JHI Admin side, create an equivalent passenger in passenger microservice.
    @PostMapping("/users")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<User> createUser(@Valid @RequestBody UserDTO userDTO) throws URISyntaxException {
        log.debug("REST request to save User : {}", userDTO);

        if (userDTO.getId() != null) {
            throw new BadRequestAlertException("A new user cannot already have an ID", "userManagement", "idexists");
            // Lowercase the user login before comparing with database
        } else if (userRepository.findOneByLogin(userDTO.getLogin().toLowerCase()).isPresent()) {
            throw new LoginAlreadyUsedException();
        } else if (userRepository.findOneByEmailIgnoreCase(userDTO.getEmail()).isPresent()) {
            throw new EmailAlreadyUsedException();
        } else {
            User newUser = userService.createUser(userDTO);

            // create the equivalent passenger in passenger microservice
            // set all necessary information in headers of HTTP request
            //TODO TRY FIX AUTHORIZATION BUG - ASK MENTOR?
            HttpHeaders headers = createAuthHeaders(newUser.getLogin(), newUser.getPassword());
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
            HttpEntity<User> request = new HttpEntity<>(newUser, headers);

            ResponseEntity resultCode = restTemplate.postForObject(
                passengerMicroserviceBaseURL+"/api/passengers/",
                request,
                ResponseEntity.class
            );

            if(resultCode != null && !resultCode.getStatusCode().equals(HttpStatus.valueOf(200))) {
                log.error("Could not create a passenger that matches newly created user. Please try again.");
            }

            mailService.sendCreationEmail(newUser);
            return ResponseEntity.created(new URI("/api/users/" + newUser.getLogin()))
                .headers(HeaderUtil.createAlert(applicationName,  "A user is created with identifier " + newUser.getLogin(), newUser.getLogin()))
                .body(newUser);
        }
    }

    /**
     * {@code PUT /users} : Updates an existing User.
     *
     * @param userDTO the user to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated user.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already in use.
     * @throws LoginAlreadyUsedException {@code 400 (Bad Request)} if the login is already in use.
     */
    // TODO Update the passenger equivalent at the passenger microservice side
    @PutMapping("/users")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UserDTO userDTO) {
        log.debug("REST request to update User : {}", userDTO);
        Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(userDTO.getEmail());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(userDTO.getId()))) {
            throw new EmailAlreadyUsedException();
        }
        existingUser = userRepository.findOneByLogin(userDTO.getLogin().toLowerCase());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(userDTO.getId()))) {
            throw new LoginAlreadyUsedException();
        }
        Optional<UserDTO> updatedUser = userService.updateUser(userDTO);

        // update passenger equivalent
        ResponseEntity resultCode = restTemplate.exchange(
            passengerMicroserviceBaseURL+"/api/passengers/",
            HttpMethod.PUT,
            new HttpEntity<>(updatedUser),
            Void.class
        );


        if(!resultCode.getStatusCode().equals(HttpStatus.valueOf(200))) {
            log.error("Could not update the provided passenger. Please try again.");
        }

        return ResponseUtil.wrapOrNotFound(updatedUser,
            HeaderUtil.createAlert(applicationName, "A user is updated with identifier " + userDTO.getLogin(), userDTO.getLogin()));
    }

    /**
     * {@code GET /users} : get all users.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body all users.
     */
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers(Pageable pageable) {
        final Page<UserDTO> page = userService.getAllManagedUsers(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * Gets a list of all roles.
     * @return a string list of all roles.
     */
    @GetMapping("/users/authorities")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public List<String> getAuthorities() {
        return userService.getAuthorities();
    }

    /**
     * {@code GET /users/:login} : get the "login" user.
     *
     * Since the user login is predefined by JHipster use it to create a passenger with same login information (and same User object
     * information) upon this login. I.e. If the login is requested for user with username 'user' and the same user does not already
     * exist in passenger microservice database, register him. Otherwise upon login check if the equivalent object exists in the
     * passenger microservice. It cannot happen that it doesn't, in that case the attempted login must be registered first.
     *
     * @param login the login of the user to find.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the "login" user, or with status {@code 404 (Not Found)}.
     */
    // TODO Automatically create passenger if login is for user, user, else check if passenger also exists with same login
    @GetMapping("/users/{login:" + Constants.LOGIN_REGEX + "}")
    public ResponseEntity<UserDTO> getUser(@PathVariable String login) {
        log.debug("REST request to get User : {}", login);

        // check if user object for provided login exists on JHI User side
        ResponseEntity<UserDTO> requestedUser = ResponseUtil.wrapOrNotFound(
            userService.getUserWithAuthoritiesByLogin(login)
                .map(UserDTO::new));

        // if user with provided login info found check for user login case and passenger database
        if(requestedUser.getStatusCode().equals(HttpStatus.valueOf(200))) {

            // try to retrieve same object from the passenger database
            ResponseEntity result = restTemplate.getForObject(
                passengerMicroserviceBaseURL+"/api//passengers/"+login,
                ResponseEntity.class
            );

            // if the user is logged in for the first time with username 'user' register a passenger at the passenger microservice
            if(login.equals("user") && result == null) {

                log.debug("MY PRE SENDING TO PASENGER MS USER IS   " + userService.getUserWithAuthoritiesByLogin(login).map(UserDTO::new).toString());
                ResponseEntity resultCode = restTemplate.postForObject(
                    passengerMicroserviceBaseURL+"/api/passengers/",
                    userService.getUserWithAuthoritiesByLogin(login).map(UserDTO::new),
                    ResponseEntity.class
                );

                if(resultCode != null && !resultCode.getStatusCode().equals(HttpStatus.valueOf(200))) {
                    log.error("Could not create a passenger with 'user' username. Please try again.");
                }
            }

            // else check for errors in received response
            if(result != null && !result.getStatusCode().equals(HttpStatus.valueOf(200))) {
                log.error("Passenger not found in the database. Passenger must be registered first.");
            }
        }

        return requestedUser;
    }

    /**
     * {@code DELETE /users/:login} : delete the "login" User.
     *
     * @param login the login of the user to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    // TODO matching delete in passenger microservice
    @DeleteMapping("/users/{login:" + Constants.LOGIN_REGEX + "}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> deleteUser(@PathVariable String login) {
        log.debug("REST request to delete User: {}", login);
        userService.deleteUser(login);

        // delete the same passenger object from the passenger microservice database
        restTemplate.delete(
            passengerMicroserviceBaseURL+"/api/passengers/"+login,
            Void.class
        );

        return ResponseEntity.noContent().headers(HeaderUtil.createAlert(applicationName,  "A user is deleted with identifier " + login, login)).build();
    }


    // Create headers manually to avoid authorization errors during other microservices' endpoint access
    // source tutorial: https://www.baeldung.com/how-to-use-resttemplate-with-basic-authentication-in-spring#manual_auth
    private HttpHeaders createAuthHeaders(String login, String password) {

        return new HttpHeaders() {{
            String auth = login + ":" + password;
            byte[] base64EncodedAuth = Base64.getEncoder().encode(
                auth.getBytes(Charset.forName("US-ASCII"))
            );

            set("Authorization",  "Basic " + new String(base64EncodedAuth));
        }};
    }
}
