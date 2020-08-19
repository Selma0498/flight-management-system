package flights.web.rest;

import flights.FlightsApp;
import flights.domain.Flight;
import flights.domain.Airport;
import flights.domain.Airline;
import flights.domain.Plane;
import flights.repository.FlightRepository;

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

import flights.domain.enumeration.EFlightType;
import flights.domain.enumeration.EFareType;
/**
 * Integration tests for the {@link FlightResource} REST controller.
 */
@SpringBootTest(classes = FlightsApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class FlightResourceIT {

    private static final String DEFAULT_FLIGHT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_FLIGHT_NUMBER = "BBBBBBBBBB";

    private static final EFlightType DEFAULT_FLIGHT_TYPE = EFlightType.ONE_WAY;
    private static final EFlightType UPDATED_FLIGHT_TYPE = EFlightType.RETURN_TRIP;

    private static final EFareType DEFAULT_FARE_TYPE = EFareType.ECONOMY;
    private static final EFareType UPDATED_FARE_TYPE = EFareType.BUSINESS;

    private static final String DEFAULT_PILOT = "AAAAAAAAAA";
    private static final String UPDATED_PILOT = "BBBBBBBBBB";

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFlightMockMvc;

    private Flight flight;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Flight createEntity(EntityManager em) {
        Flight flight = new Flight()
            .flightNumber(DEFAULT_FLIGHT_NUMBER)
            .flightType(DEFAULT_FLIGHT_TYPE)
            .fareType(DEFAULT_FARE_TYPE)
            .pilot(DEFAULT_PILOT)
            .price(DEFAULT_PRICE);
        // Add required entity
        Airport airport;
        if (TestUtil.findAll(em, Airport.class).isEmpty()) {
            airport = AirportResourceIT.createEntity(em);
            em.persist(airport);
            em.flush();
        } else {
            airport = TestUtil.findAll(em, Airport.class).get(0);
        }
        flight.setOrigin(airport);
        // Add required entity
        flight.setDestination(airport);
        // Add required entity
        Airline airline;
        if (TestUtil.findAll(em, Airline.class).isEmpty()) {
            airline = AirlineResourceIT.createEntity(em);
            em.persist(airline);
            em.flush();
        } else {
            airline = TestUtil.findAll(em, Airline.class).get(0);
        }
        flight.setAirline(airline);
        // Add required entity
        Plane plane;
        if (TestUtil.findAll(em, Plane.class).isEmpty()) {
            plane = PlaneResourceIT.createEntity(em);
            em.persist(plane);
            em.flush();
        } else {
            plane = TestUtil.findAll(em, Plane.class).get(0);
        }
        flight.setPlane(plane);
        return flight;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Flight createUpdatedEntity(EntityManager em) {
        Flight flight = new Flight()
            .flightNumber(UPDATED_FLIGHT_NUMBER)
            .flightType(UPDATED_FLIGHT_TYPE)
            .fareType(UPDATED_FARE_TYPE)
            .pilot(UPDATED_PILOT)
            .price(UPDATED_PRICE);
        // Add required entity
        Airport airport;
        if (TestUtil.findAll(em, Airport.class).isEmpty()) {
            airport = AirportResourceIT.createUpdatedEntity(em);
            em.persist(airport);
            em.flush();
        } else {
            airport = TestUtil.findAll(em, Airport.class).get(0);
        }
        flight.setOrigin(airport);
        // Add required entity
        flight.setDestination(airport);
        // Add required entity
        Airline airline;
        if (TestUtil.findAll(em, Airline.class).isEmpty()) {
            airline = AirlineResourceIT.createUpdatedEntity(em);
            em.persist(airline);
            em.flush();
        } else {
            airline = TestUtil.findAll(em, Airline.class).get(0);
        }
        flight.setAirline(airline);
        // Add required entity
        Plane plane;
        if (TestUtil.findAll(em, Plane.class).isEmpty()) {
            plane = PlaneResourceIT.createUpdatedEntity(em);
            em.persist(plane);
            em.flush();
        } else {
            plane = TestUtil.findAll(em, Plane.class).get(0);
        }
        flight.setPlane(plane);
        return flight;
    }

    @BeforeEach
    public void initTest() {
        flight = createEntity(em);
    }

    @Test
    @Transactional
    public void createFlight() throws Exception {
        int databaseSizeBeforeCreate = flightRepository.findAll().size();
        // Create the Flight
        restFlightMockMvc.perform(post("/api/flights")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(flight)))
            .andExpect(status().isCreated());

        // Validate the Flight in the database
        List<Flight> flightList = flightRepository.findAll();
        assertThat(flightList).hasSize(databaseSizeBeforeCreate + 1);
        Flight testFlight = flightList.get(flightList.size() - 1);
        assertThat(testFlight.getFlightNumber()).isEqualTo(DEFAULT_FLIGHT_NUMBER);
        assertThat(testFlight.getFlightType()).isEqualTo(DEFAULT_FLIGHT_TYPE);
        assertThat(testFlight.getFareType()).isEqualTo(DEFAULT_FARE_TYPE);
        assertThat(testFlight.getPilot()).isEqualTo(DEFAULT_PILOT);
        assertThat(testFlight.getPrice()).isEqualTo(DEFAULT_PRICE);
    }

    @Test
    @Transactional
    public void createFlightWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = flightRepository.findAll().size();

        // Create the Flight with an existing ID
        flight.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFlightMockMvc.perform(post("/api/flights")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(flight)))
            .andExpect(status().isBadRequest());

        // Validate the Flight in the database
        List<Flight> flightList = flightRepository.findAll();
        assertThat(flightList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkFlightNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = flightRepository.findAll().size();
        // set the field null
        flight.setFlightNumber(null);

        // Create the Flight, which fails.


        restFlightMockMvc.perform(post("/api/flights")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(flight)))
            .andExpect(status().isBadRequest());

        List<Flight> flightList = flightRepository.findAll();
        assertThat(flightList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFlightTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = flightRepository.findAll().size();
        // set the field null
        flight.setFlightType(null);

        // Create the Flight, which fails.


        restFlightMockMvc.perform(post("/api/flights")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(flight)))
            .andExpect(status().isBadRequest());

        List<Flight> flightList = flightRepository.findAll();
        assertThat(flightList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFareTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = flightRepository.findAll().size();
        // set the field null
        flight.setFareType(null);

        // Create the Flight, which fails.


        restFlightMockMvc.perform(post("/api/flights")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(flight)))
            .andExpect(status().isBadRequest());

        List<Flight> flightList = flightRepository.findAll();
        assertThat(flightList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = flightRepository.findAll().size();
        // set the field null
        flight.setPrice(null);

        // Create the Flight, which fails.


        restFlightMockMvc.perform(post("/api/flights")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(flight)))
            .andExpect(status().isBadRequest());

        List<Flight> flightList = flightRepository.findAll();
        assertThat(flightList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFlights() throws Exception {
        // Initialize the database
        flightRepository.saveAndFlush(flight);

        // Get all the flightList
        restFlightMockMvc.perform(get("/api/flights?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(flight.getId().intValue())))
            .andExpect(jsonPath("$.[*].flightNumber").value(hasItem(DEFAULT_FLIGHT_NUMBER)))
            .andExpect(jsonPath("$.[*].flightType").value(hasItem(DEFAULT_FLIGHT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].fareType").value(hasItem(DEFAULT_FARE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].pilot").value(hasItem(DEFAULT_PILOT)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getFlight() throws Exception {
        // Initialize the database
        flightRepository.saveAndFlush(flight);

        // Get the flight
        restFlightMockMvc.perform(get("/api/flights/{id}", flight.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(flight.getId().intValue()))
            .andExpect(jsonPath("$.flightNumber").value(DEFAULT_FLIGHT_NUMBER))
            .andExpect(jsonPath("$.flightType").value(DEFAULT_FLIGHT_TYPE.toString()))
            .andExpect(jsonPath("$.fareType").value(DEFAULT_FARE_TYPE.toString()))
            .andExpect(jsonPath("$.pilot").value(DEFAULT_PILOT))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()));
    }
    @Test
    @Transactional
    public void getNonExistingFlight() throws Exception {
        // Get the flight
        restFlightMockMvc.perform(get("/api/flights/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFlight() throws Exception {
        // Initialize the database
        flightRepository.saveAndFlush(flight);

        int databaseSizeBeforeUpdate = flightRepository.findAll().size();

        // Update the flight
        Flight updatedFlight = flightRepository.findById(flight.getId()).get();
        // Disconnect from session so that the updates on updatedFlight are not directly saved in db
        em.detach(updatedFlight);
        updatedFlight
            .flightNumber(UPDATED_FLIGHT_NUMBER)
            .flightType(UPDATED_FLIGHT_TYPE)
            .fareType(UPDATED_FARE_TYPE)
            .pilot(UPDATED_PILOT)
            .price(UPDATED_PRICE);

        restFlightMockMvc.perform(put("/api/flights")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedFlight)))
            .andExpect(status().isOk());

        // Validate the Flight in the database
        List<Flight> flightList = flightRepository.findAll();
        assertThat(flightList).hasSize(databaseSizeBeforeUpdate);
        Flight testFlight = flightList.get(flightList.size() - 1);
        assertThat(testFlight.getFlightNumber()).isEqualTo(UPDATED_FLIGHT_NUMBER);
        assertThat(testFlight.getFlightType()).isEqualTo(UPDATED_FLIGHT_TYPE);
        assertThat(testFlight.getFareType()).isEqualTo(UPDATED_FARE_TYPE);
        assertThat(testFlight.getPilot()).isEqualTo(UPDATED_PILOT);
        assertThat(testFlight.getPrice()).isEqualTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void updateNonExistingFlight() throws Exception {
        int databaseSizeBeforeUpdate = flightRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFlightMockMvc.perform(put("/api/flights")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(flight)))
            .andExpect(status().isBadRequest());

        // Validate the Flight in the database
        List<Flight> flightList = flightRepository.findAll();
        assertThat(flightList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFlight() throws Exception {
        // Initialize the database
        flightRepository.saveAndFlush(flight);

        int databaseSizeBeforeDelete = flightRepository.findAll().size();

        // Delete the flight
        restFlightMockMvc.perform(delete("/api/flights/{id}", flight.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Flight> flightList = flightRepository.findAll();
        assertThat(flightList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
