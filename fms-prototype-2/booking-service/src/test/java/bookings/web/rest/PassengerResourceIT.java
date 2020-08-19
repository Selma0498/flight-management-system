package bookings.web.rest;

import bookings.BookingsApp;
import bookings.domain.Passenger;
import bookings.repository.PassengerRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link PassengerResource} REST controller.
 */
@SpringBootTest(classes = BookingsApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class PassengerResourceIT {

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SURNAME = "AAAAAAAAAA";
    private static final String UPDATED_SURNAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPassengerMockMvc;

    private Passenger passenger;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Passenger createEntity(EntityManager em) {
        Passenger passenger = new Passenger()
            .username(DEFAULT_USERNAME)
            .password(DEFAULT_PASSWORD)
            .name(DEFAULT_NAME)
            .surname(DEFAULT_SURNAME)
            .email(DEFAULT_EMAIL);
        return passenger;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Passenger createUpdatedEntity(EntityManager em) {
        Passenger passenger = new Passenger()
            .username(UPDATED_USERNAME)
            .password(UPDATED_PASSWORD)
            .name(UPDATED_NAME)
            .surname(UPDATED_SURNAME)
            .email(UPDATED_EMAIL);
        return passenger;
    }

    @BeforeEach
    public void initTest() {
        passenger = createEntity(em);
    }

    @Test
    @Transactional
    public void createPassenger() throws Exception {
        int databaseSizeBeforeCreate = passengerRepository.findAll().size();
        // Create the Passenger
        restPassengerMockMvc.perform(post("/api/passengers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(passenger)))
            .andExpect(status().isCreated());

        // Validate the Passenger in the database
        List<Passenger> passengerList = passengerRepository.findAll();
        assertThat(passengerList).hasSize(databaseSizeBeforeCreate + 1);
        Passenger testPassenger = passengerList.get(passengerList.size() - 1);
        assertThat(testPassenger.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testPassenger.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testPassenger.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPassenger.getSurname()).isEqualTo(DEFAULT_SURNAME);
        assertThat(testPassenger.getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    public void createPassengerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = passengerRepository.findAll().size();

        // Create the Passenger with an existing ID
        passenger.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPassengerMockMvc.perform(post("/api/passengers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(passenger)))
            .andExpect(status().isBadRequest());

        // Validate the Passenger in the database
        List<Passenger> passengerList = passengerRepository.findAll();
        assertThat(passengerList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkUsernameIsRequired() throws Exception {
        int databaseSizeBeforeTest = passengerRepository.findAll().size();
        // set the field null
        passenger.setUsername(null);

        // Create the Passenger, which fails.


        restPassengerMockMvc.perform(post("/api/passengers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(passenger)))
            .andExpect(status().isBadRequest());

        List<Passenger> passengerList = passengerRepository.findAll();
        assertThat(passengerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPasswordIsRequired() throws Exception {
        int databaseSizeBeforeTest = passengerRepository.findAll().size();
        // set the field null
        passenger.setPassword(null);

        // Create the Passenger, which fails.


        restPassengerMockMvc.perform(post("/api/passengers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(passenger)))
            .andExpect(status().isBadRequest());

        List<Passenger> passengerList = passengerRepository.findAll();
        assertThat(passengerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = passengerRepository.findAll().size();
        // set the field null
        passenger.setName(null);

        // Create the Passenger, which fails.


        restPassengerMockMvc.perform(post("/api/passengers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(passenger)))
            .andExpect(status().isBadRequest());

        List<Passenger> passengerList = passengerRepository.findAll();
        assertThat(passengerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSurnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = passengerRepository.findAll().size();
        // set the field null
        passenger.setSurname(null);

        // Create the Passenger, which fails.


        restPassengerMockMvc.perform(post("/api/passengers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(passenger)))
            .andExpect(status().isBadRequest());

        List<Passenger> passengerList = passengerRepository.findAll();
        assertThat(passengerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = passengerRepository.findAll().size();
        // set the field null
        passenger.setEmail(null);

        // Create the Passenger, which fails.


        restPassengerMockMvc.perform(post("/api/passengers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(passenger)))
            .andExpect(status().isBadRequest());

        List<Passenger> passengerList = passengerRepository.findAll();
        assertThat(passengerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPassengers() throws Exception {
        // Initialize the database
        passengerRepository.saveAndFlush(passenger);

        // Get all the passengerList
        restPassengerMockMvc.perform(get("/api/passengers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(passenger.getId().intValue())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].surname").value(hasItem(DEFAULT_SURNAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));
    }
    
    @Test
    @Transactional
    public void getPassenger() throws Exception {
        // Initialize the database
        passengerRepository.saveAndFlush(passenger);

        // Get the passenger
        restPassengerMockMvc.perform(get("/api/passengers/{id}", passenger.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(passenger.getId().intValue()))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.surname").value(DEFAULT_SURNAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL));
    }
    @Test
    @Transactional
    public void getNonExistingPassenger() throws Exception {
        // Get the passenger
        restPassengerMockMvc.perform(get("/api/passengers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePassenger() throws Exception {
        // Initialize the database
        passengerRepository.saveAndFlush(passenger);

        int databaseSizeBeforeUpdate = passengerRepository.findAll().size();

        // Update the passenger
        Passenger updatedPassenger = passengerRepository.findById(passenger.getId()).get();
        // Disconnect from session so that the updates on updatedPassenger are not directly saved in db
        em.detach(updatedPassenger);
        updatedPassenger
            .username(UPDATED_USERNAME)
            .password(UPDATED_PASSWORD)
            .name(UPDATED_NAME)
            .surname(UPDATED_SURNAME)
            .email(UPDATED_EMAIL);

        restPassengerMockMvc.perform(put("/api/passengers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedPassenger)))
            .andExpect(status().isOk());

        // Validate the Passenger in the database
        List<Passenger> passengerList = passengerRepository.findAll();
        assertThat(passengerList).hasSize(databaseSizeBeforeUpdate);
        Passenger testPassenger = passengerList.get(passengerList.size() - 1);
        assertThat(testPassenger.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testPassenger.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testPassenger.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPassenger.getSurname()).isEqualTo(UPDATED_SURNAME);
        assertThat(testPassenger.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void updateNonExistingPassenger() throws Exception {
        int databaseSizeBeforeUpdate = passengerRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPassengerMockMvc.perform(put("/api/passengers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(passenger)))
            .andExpect(status().isBadRequest());

        // Validate the Passenger in the database
        List<Passenger> passengerList = passengerRepository.findAll();
        assertThat(passengerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePassenger() throws Exception {
        // Initialize the database
        passengerRepository.saveAndFlush(passenger);

        int databaseSizeBeforeDelete = passengerRepository.findAll().size();

        // Delete the passenger
        restPassengerMockMvc.perform(delete("/api/passengers/{id}", passenger.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Passenger> passengerList = passengerRepository.findAll();
        assertThat(passengerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
