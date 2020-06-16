package flights.web.rest;

import flights.FlightsApp;
import flights.domain.FlightHandling;
import flights.repository.FlightHandlingRepository;

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
 * Integration tests for the {@link FlightHandlingResource} REST controller.
 */
@SpringBootTest(classes = FlightsApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class FlightHandlingResourceIT {

    private static final Integer DEFAULT_BOARDING_GATE = 1;
    private static final Integer UPDATED_BOARDING_GATE = 2;

    private static final Double DEFAULT_DELAY = 1D;
    private static final Double UPDATED_DELAY = 2D;

    @Autowired
    private FlightHandlingRepository flightHandlingRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFlightHandlingMockMvc;

    private FlightHandling flightHandling;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FlightHandling createEntity(EntityManager em) {
        FlightHandling flightHandling = new FlightHandling()
            .boardingGate(DEFAULT_BOARDING_GATE)
            .delay(DEFAULT_DELAY);
        return flightHandling;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FlightHandling createUpdatedEntity(EntityManager em) {
        FlightHandling flightHandling = new FlightHandling()
            .boardingGate(UPDATED_BOARDING_GATE)
            .delay(UPDATED_DELAY);
        return flightHandling;
    }

    @BeforeEach
    public void initTest() {
        flightHandling = createEntity(em);
    }

    @Test
    @Transactional
    public void createFlightHandling() throws Exception {
        int databaseSizeBeforeCreate = flightHandlingRepository.findAll().size();
        // Create the FlightHandling
        restFlightHandlingMockMvc.perform(post("/api/flight-handlings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(flightHandling)))
            .andExpect(status().isCreated());

        // Validate the FlightHandling in the database
        List<FlightHandling> flightHandlingList = flightHandlingRepository.findAll();
        assertThat(flightHandlingList).hasSize(databaseSizeBeforeCreate + 1);
        FlightHandling testFlightHandling = flightHandlingList.get(flightHandlingList.size() - 1);
        assertThat(testFlightHandling.getBoardingGate()).isEqualTo(DEFAULT_BOARDING_GATE);
        assertThat(testFlightHandling.getDelay()).isEqualTo(DEFAULT_DELAY);
    }

    @Test
    @Transactional
    public void createFlightHandlingWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = flightHandlingRepository.findAll().size();

        // Create the FlightHandling with an existing ID
        flightHandling.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFlightHandlingMockMvc.perform(post("/api/flight-handlings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(flightHandling)))
            .andExpect(status().isBadRequest());

        // Validate the FlightHandling in the database
        List<FlightHandling> flightHandlingList = flightHandlingRepository.findAll();
        assertThat(flightHandlingList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkBoardingGateIsRequired() throws Exception {
        int databaseSizeBeforeTest = flightHandlingRepository.findAll().size();
        // set the field null
        flightHandling.setBoardingGate(null);

        // Create the FlightHandling, which fails.


        restFlightHandlingMockMvc.perform(post("/api/flight-handlings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(flightHandling)))
            .andExpect(status().isBadRequest());

        List<FlightHandling> flightHandlingList = flightHandlingRepository.findAll();
        assertThat(flightHandlingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFlightHandlings() throws Exception {
        // Initialize the database
        flightHandlingRepository.saveAndFlush(flightHandling);

        // Get all the flightHandlingList
        restFlightHandlingMockMvc.perform(get("/api/flight-handlings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(flightHandling.getId().intValue())))
            .andExpect(jsonPath("$.[*].boardingGate").value(hasItem(DEFAULT_BOARDING_GATE)))
            .andExpect(jsonPath("$.[*].delay").value(hasItem(DEFAULT_DELAY.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getFlightHandling() throws Exception {
        // Initialize the database
        flightHandlingRepository.saveAndFlush(flightHandling);

        // Get the flightHandling
        restFlightHandlingMockMvc.perform(get("/api/flight-handlings/{id}", flightHandling.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(flightHandling.getId().intValue()))
            .andExpect(jsonPath("$.boardingGate").value(DEFAULT_BOARDING_GATE))
            .andExpect(jsonPath("$.delay").value(DEFAULT_DELAY.doubleValue()));
    }
    @Test
    @Transactional
    public void getNonExistingFlightHandling() throws Exception {
        // Get the flightHandling
        restFlightHandlingMockMvc.perform(get("/api/flight-handlings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFlightHandling() throws Exception {
        // Initialize the database
        flightHandlingRepository.saveAndFlush(flightHandling);

        int databaseSizeBeforeUpdate = flightHandlingRepository.findAll().size();

        // Update the flightHandling
        FlightHandling updatedFlightHandling = flightHandlingRepository.findById(flightHandling.getId()).get();
        // Disconnect from session so that the updates on updatedFlightHandling are not directly saved in db
        em.detach(updatedFlightHandling);
        updatedFlightHandling
            .boardingGate(UPDATED_BOARDING_GATE)
            .delay(UPDATED_DELAY);

        restFlightHandlingMockMvc.perform(put("/api/flight-handlings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedFlightHandling)))
            .andExpect(status().isOk());

        // Validate the FlightHandling in the database
        List<FlightHandling> flightHandlingList = flightHandlingRepository.findAll();
        assertThat(flightHandlingList).hasSize(databaseSizeBeforeUpdate);
        FlightHandling testFlightHandling = flightHandlingList.get(flightHandlingList.size() - 1);
        assertThat(testFlightHandling.getBoardingGate()).isEqualTo(UPDATED_BOARDING_GATE);
        assertThat(testFlightHandling.getDelay()).isEqualTo(UPDATED_DELAY);
    }

    @Test
    @Transactional
    public void updateNonExistingFlightHandling() throws Exception {
        int databaseSizeBeforeUpdate = flightHandlingRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFlightHandlingMockMvc.perform(put("/api/flight-handlings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(flightHandling)))
            .andExpect(status().isBadRequest());

        // Validate the FlightHandling in the database
        List<FlightHandling> flightHandlingList = flightHandlingRepository.findAll();
        assertThat(flightHandlingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFlightHandling() throws Exception {
        // Initialize the database
        flightHandlingRepository.saveAndFlush(flightHandling);

        int databaseSizeBeforeDelete = flightHandlingRepository.findAll().size();

        // Delete the flightHandling
        restFlightHandlingMockMvc.perform(delete("/api/flight-handlings/{id}", flightHandling.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FlightHandling> flightHandlingList = flightHandlingRepository.findAll();
        assertThat(flightHandlingList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
