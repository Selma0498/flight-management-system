package flights.web.rest;

import flights.FlightsApp;
import flights.domain.Airport;
import flights.repository.AirportRepository;

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
 * Integration tests for the {@link AirportResource} REST controller.
 */
@SpringBootTest(classes = FlightsApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class AirportResourceIT {

    private static final String DEFAULT_AIRPORT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_AIRPORT_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_AIRPORT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_AIRPORT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CITY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CITY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_POSTAL_CODE = "AAAAAAAAAA";
    private static final String UPDATED_POSTAL_CODE = "BBBBBBBBBB";

    @Autowired
    private AirportRepository airportRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAirportMockMvc;

    private Airport airport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Airport createEntity(EntityManager em) {
        Airport airport = new Airport()
            .airportCode(DEFAULT_AIRPORT_CODE)
            .airportName(DEFAULT_AIRPORT_NAME)
            .countryName(DEFAULT_COUNTRY_NAME)
            .cityName(DEFAULT_CITY_NAME)
            .postalCode(DEFAULT_POSTAL_CODE);
        return airport;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Airport createUpdatedEntity(EntityManager em) {
        Airport airport = new Airport()
            .airportCode(UPDATED_AIRPORT_CODE)
            .airportName(UPDATED_AIRPORT_NAME)
            .countryName(UPDATED_COUNTRY_NAME)
            .cityName(UPDATED_CITY_NAME)
            .postalCode(UPDATED_POSTAL_CODE);
        return airport;
    }

    @BeforeEach
    public void initTest() {
        airport = createEntity(em);
    }

    @Test
    @Transactional
    public void createAirport() throws Exception {
        int databaseSizeBeforeCreate = airportRepository.findAll().size();
        // Create the Airport
        restAirportMockMvc.perform(post("/api/airports")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(airport)))
            .andExpect(status().isCreated());

        // Validate the Airport in the database
        List<Airport> airportList = airportRepository.findAll();
        assertThat(airportList).hasSize(databaseSizeBeforeCreate + 1);
        Airport testAirport = airportList.get(airportList.size() - 1);
        assertThat(testAirport.getAirportCode()).isEqualTo(DEFAULT_AIRPORT_CODE);
        assertThat(testAirport.getAirportName()).isEqualTo(DEFAULT_AIRPORT_NAME);
        assertThat(testAirport.getCountryName()).isEqualTo(DEFAULT_COUNTRY_NAME);
        assertThat(testAirport.getCityName()).isEqualTo(DEFAULT_CITY_NAME);
        assertThat(testAirport.getPostalCode()).isEqualTo(DEFAULT_POSTAL_CODE);
    }

    @Test
    @Transactional
    public void createAirportWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = airportRepository.findAll().size();

        // Create the Airport with an existing ID
        airport.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAirportMockMvc.perform(post("/api/airports")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(airport)))
            .andExpect(status().isBadRequest());

        // Validate the Airport in the database
        List<Airport> airportList = airportRepository.findAll();
        assertThat(airportList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkAirportCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = airportRepository.findAll().size();
        // set the field null
        airport.setAirportCode(null);

        // Create the Airport, which fails.


        restAirportMockMvc.perform(post("/api/airports")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(airport)))
            .andExpect(status().isBadRequest());

        List<Airport> airportList = airportRepository.findAll();
        assertThat(airportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAirportNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = airportRepository.findAll().size();
        // set the field null
        airport.setAirportName(null);

        // Create the Airport, which fails.


        restAirportMockMvc.perform(post("/api/airports")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(airport)))
            .andExpect(status().isBadRequest());

        List<Airport> airportList = airportRepository.findAll();
        assertThat(airportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCountryNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = airportRepository.findAll().size();
        // set the field null
        airport.setCountryName(null);

        // Create the Airport, which fails.


        restAirportMockMvc.perform(post("/api/airports")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(airport)))
            .andExpect(status().isBadRequest());

        List<Airport> airportList = airportRepository.findAll();
        assertThat(airportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCityNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = airportRepository.findAll().size();
        // set the field null
        airport.setCityName(null);

        // Create the Airport, which fails.


        restAirportMockMvc.perform(post("/api/airports")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(airport)))
            .andExpect(status().isBadRequest());

        List<Airport> airportList = airportRepository.findAll();
        assertThat(airportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAirports() throws Exception {
        // Initialize the database
        airportRepository.saveAndFlush(airport);

        // Get all the airportList
        restAirportMockMvc.perform(get("/api/airports?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(airport.getId().intValue())))
            .andExpect(jsonPath("$.[*].airportCode").value(hasItem(DEFAULT_AIRPORT_CODE)))
            .andExpect(jsonPath("$.[*].airportName").value(hasItem(DEFAULT_AIRPORT_NAME)))
            .andExpect(jsonPath("$.[*].countryName").value(hasItem(DEFAULT_COUNTRY_NAME)))
            .andExpect(jsonPath("$.[*].cityName").value(hasItem(DEFAULT_CITY_NAME)))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE)));
    }
    
    @Test
    @Transactional
    public void getAirport() throws Exception {
        // Initialize the database
        airportRepository.saveAndFlush(airport);

        // Get the airport
        restAirportMockMvc.perform(get("/api/airports/{id}", airport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(airport.getId().intValue()))
            .andExpect(jsonPath("$.airportCode").value(DEFAULT_AIRPORT_CODE))
            .andExpect(jsonPath("$.airportName").value(DEFAULT_AIRPORT_NAME))
            .andExpect(jsonPath("$.countryName").value(DEFAULT_COUNTRY_NAME))
            .andExpect(jsonPath("$.cityName").value(DEFAULT_CITY_NAME))
            .andExpect(jsonPath("$.postalCode").value(DEFAULT_POSTAL_CODE));
    }
    @Test
    @Transactional
    public void getNonExistingAirport() throws Exception {
        // Get the airport
        restAirportMockMvc.perform(get("/api/airports/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAirport() throws Exception {
        // Initialize the database
        airportRepository.saveAndFlush(airport);

        int databaseSizeBeforeUpdate = airportRepository.findAll().size();

        // Update the airport
        Airport updatedAirport = airportRepository.findById(airport.getId()).get();
        // Disconnect from session so that the updates on updatedAirport are not directly saved in db
        em.detach(updatedAirport);
        updatedAirport
            .airportCode(UPDATED_AIRPORT_CODE)
            .airportName(UPDATED_AIRPORT_NAME)
            .countryName(UPDATED_COUNTRY_NAME)
            .cityName(UPDATED_CITY_NAME)
            .postalCode(UPDATED_POSTAL_CODE);

        restAirportMockMvc.perform(put("/api/airports")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedAirport)))
            .andExpect(status().isOk());

        // Validate the Airport in the database
        List<Airport> airportList = airportRepository.findAll();
        assertThat(airportList).hasSize(databaseSizeBeforeUpdate);
        Airport testAirport = airportList.get(airportList.size() - 1);
        assertThat(testAirport.getAirportCode()).isEqualTo(UPDATED_AIRPORT_CODE);
        assertThat(testAirport.getAirportName()).isEqualTo(UPDATED_AIRPORT_NAME);
        assertThat(testAirport.getCountryName()).isEqualTo(UPDATED_COUNTRY_NAME);
        assertThat(testAirport.getCityName()).isEqualTo(UPDATED_CITY_NAME);
        assertThat(testAirport.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    public void updateNonExistingAirport() throws Exception {
        int databaseSizeBeforeUpdate = airportRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAirportMockMvc.perform(put("/api/airports")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(airport)))
            .andExpect(status().isBadRequest());

        // Validate the Airport in the database
        List<Airport> airportList = airportRepository.findAll();
        assertThat(airportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAirport() throws Exception {
        // Initialize the database
        airportRepository.saveAndFlush(airport);

        int databaseSizeBeforeDelete = airportRepository.findAll().size();

        // Delete the airport
        restAirportMockMvc.perform(delete("/api/airports/{id}", airport.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Airport> airportList = airportRepository.findAll();
        assertThat(airportList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
