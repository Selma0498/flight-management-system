package flights.web.rest;

import flights.FlightsApp;
import flights.domain.Airline;
import flights.repository.AirlineRepository;

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
 * Integration tests for the {@link AirlineResource} REST controller.
 */
@SpringBootTest(classes = FlightsApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class AirlineResourceIT {

    private static final String DEFAULT_AIRLINE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_AIRLINE_NAME = "BBBBBBBBBB";

    @Autowired
    private AirlineRepository airlineRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAirlineMockMvc;

    private Airline airline;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Airline createEntity(EntityManager em) {
        Airline airline = new Airline()
            .airlineName(DEFAULT_AIRLINE_NAME);
        return airline;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Airline createUpdatedEntity(EntityManager em) {
        Airline airline = new Airline()
            .airlineName(UPDATED_AIRLINE_NAME);
        return airline;
    }

    @BeforeEach
    public void initTest() {
        airline = createEntity(em);
    }

    @Test
    @Transactional
    public void createAirline() throws Exception {
        int databaseSizeBeforeCreate = airlineRepository.findAll().size();
        // Create the Airline
        restAirlineMockMvc.perform(post("/api/airlines")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(airline)))
            .andExpect(status().isCreated());

        // Validate the Airline in the database
        List<Airline> airlineList = airlineRepository.findAll();
        assertThat(airlineList).hasSize(databaseSizeBeforeCreate + 1);
        Airline testAirline = airlineList.get(airlineList.size() - 1);
        assertThat(testAirline.getAirlineName()).isEqualTo(DEFAULT_AIRLINE_NAME);
    }

    @Test
    @Transactional
    public void createAirlineWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = airlineRepository.findAll().size();

        // Create the Airline with an existing ID
        airline.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAirlineMockMvc.perform(post("/api/airlines")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(airline)))
            .andExpect(status().isBadRequest());

        // Validate the Airline in the database
        List<Airline> airlineList = airlineRepository.findAll();
        assertThat(airlineList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllAirlines() throws Exception {
        // Initialize the database
        airlineRepository.saveAndFlush(airline);

        // Get all the airlineList
        restAirlineMockMvc.perform(get("/api/airlines?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(airline.getId().intValue())))
            .andExpect(jsonPath("$.[*].airlineName").value(hasItem(DEFAULT_AIRLINE_NAME)));
    }
    
    @Test
    @Transactional
    public void getAirline() throws Exception {
        // Initialize the database
        airlineRepository.saveAndFlush(airline);

        // Get the airline
        restAirlineMockMvc.perform(get("/api/airlines/{id}", airline.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(airline.getId().intValue()))
            .andExpect(jsonPath("$.airlineName").value(DEFAULT_AIRLINE_NAME));
    }
    @Test
    @Transactional
    public void getNonExistingAirline() throws Exception {
        // Get the airline
        restAirlineMockMvc.perform(get("/api/airlines/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAirline() throws Exception {
        // Initialize the database
        airlineRepository.saveAndFlush(airline);

        int databaseSizeBeforeUpdate = airlineRepository.findAll().size();

        // Update the airline
        Airline updatedAirline = airlineRepository.findById(airline.getId()).get();
        // Disconnect from session so that the updates on updatedAirline are not directly saved in db
        em.detach(updatedAirline);
        updatedAirline
            .airlineName(UPDATED_AIRLINE_NAME);

        restAirlineMockMvc.perform(put("/api/airlines")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedAirline)))
            .andExpect(status().isOk());

        // Validate the Airline in the database
        List<Airline> airlineList = airlineRepository.findAll();
        assertThat(airlineList).hasSize(databaseSizeBeforeUpdate);
        Airline testAirline = airlineList.get(airlineList.size() - 1);
        assertThat(testAirline.getAirlineName()).isEqualTo(UPDATED_AIRLINE_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingAirline() throws Exception {
        int databaseSizeBeforeUpdate = airlineRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAirlineMockMvc.perform(put("/api/airlines")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(airline)))
            .andExpect(status().isBadRequest());

        // Validate the Airline in the database
        List<Airline> airlineList = airlineRepository.findAll();
        assertThat(airlineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAirline() throws Exception {
        // Initialize the database
        airlineRepository.saveAndFlush(airline);

        int databaseSizeBeforeDelete = airlineRepository.findAll().size();

        // Delete the airline
        restAirlineMockMvc.perform(delete("/api/airlines/{id}", airline.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Airline> airlineList = airlineRepository.findAll();
        assertThat(airlineList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
