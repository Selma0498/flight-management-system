package luggage.web.rest;

import luggage.LuggageApp;
import luggage.domain.Luggage;
import luggage.repository.LuggageRepository;

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

import luggage.domain.enumeration.ELuggageType;
/**
 * Integration tests for the {@link LuggageResource} REST controller.
 */
@SpringBootTest(classes = LuggageApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class LuggageResourceIT {

    private static final ELuggageType DEFAULT_TYPE = ELuggageType.CARRY_ON;
    private static final ELuggageType UPDATED_TYPE = ELuggageType.CABIN_BAG_10KG;

    private static final Integer DEFAULT_LUGGAGE_NUMBER = 1;
    private static final Integer UPDATED_LUGGAGE_NUMBER = 2;

    private static final String DEFAULT_FLIGHT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_FLIGHT_NUMBER = "BBBBBBBBBB";

    private static final Integer DEFAULT_BOOKING_NUMBER = 1;
    private static final Integer UPDATED_BOOKING_NUMBER = 2;

    private static final String DEFAULT_PASSENGER_ID = "AAAAAAAAAA";
    private static final String UPDATED_PASSENGER_ID = "BBBBBBBBBB";

    private static final Integer DEFAULT_WEIGHT_CATEGORY = 1;
    private static final Integer UPDATED_WEIGHT_CATEGORY = 2;

    private static final String DEFAULT_RFID_TAG = "AAAAAAAAAA";
    private static final String UPDATED_RFID_TAG = "BBBBBBBBBB";

    @Autowired
    private LuggageRepository luggageRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLuggageMockMvc;

    private Luggage luggage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Luggage createEntity(EntityManager em) {
        Luggage luggage = new Luggage()
            .type(DEFAULT_TYPE)
            .luggageNumber(DEFAULT_LUGGAGE_NUMBER)
            .flightNumber(DEFAULT_FLIGHT_NUMBER)
            .bookingNumber(DEFAULT_BOOKING_NUMBER)
            .passengerId(DEFAULT_PASSENGER_ID)
            .weightCategory(DEFAULT_WEIGHT_CATEGORY)
            .rfidTag(DEFAULT_RFID_TAG);
        return luggage;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Luggage createUpdatedEntity(EntityManager em) {
        Luggage luggage = new Luggage()
            .type(UPDATED_TYPE)
            .luggageNumber(UPDATED_LUGGAGE_NUMBER)
            .flightNumber(UPDATED_FLIGHT_NUMBER)
            .bookingNumber(UPDATED_BOOKING_NUMBER)
            .passengerId(UPDATED_PASSENGER_ID)
            .weightCategory(UPDATED_WEIGHT_CATEGORY)
            .rfidTag(UPDATED_RFID_TAG);
        return luggage;
    }

    @BeforeEach
    public void initTest() {
        luggage = createEntity(em);
    }

    @Test
    @Transactional
    public void createLuggage() throws Exception {
        int databaseSizeBeforeCreate = luggageRepository.findAll().size();
        // Create the Luggage
        restLuggageMockMvc.perform(post("/api/luggages")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(luggage)))
            .andExpect(status().isCreated());

        // Validate the Luggage in the database
        List<Luggage> luggageList = luggageRepository.findAll();
        assertThat(luggageList).hasSize(databaseSizeBeforeCreate + 1);
        Luggage testLuggage = luggageList.get(luggageList.size() - 1);
        assertThat(testLuggage.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testLuggage.getLuggageNumber()).isEqualTo(DEFAULT_LUGGAGE_NUMBER);
        assertThat(testLuggage.getFlightNumber()).isEqualTo(DEFAULT_FLIGHT_NUMBER);
        assertThat(testLuggage.getBookingNumber()).isEqualTo(DEFAULT_BOOKING_NUMBER);
        assertThat(testLuggage.getPassengerId()).isEqualTo(DEFAULT_PASSENGER_ID);
        assertThat(testLuggage.getWeightCategory()).isEqualTo(DEFAULT_WEIGHT_CATEGORY);
        assertThat(testLuggage.getRfidTag()).isEqualTo(DEFAULT_RFID_TAG);
    }

    @Test
    @Transactional
    public void createLuggageWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = luggageRepository.findAll().size();

        // Create the Luggage with an existing ID
        luggage.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLuggageMockMvc.perform(post("/api/luggages")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(luggage)))
            .andExpect(status().isBadRequest());

        // Validate the Luggage in the database
        List<Luggage> luggageList = luggageRepository.findAll();
        assertThat(luggageList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = luggageRepository.findAll().size();
        // set the field null
        luggage.setType(null);

        // Create the Luggage, which fails.


        restLuggageMockMvc.perform(post("/api/luggages")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(luggage)))
            .andExpect(status().isBadRequest());

        List<Luggage> luggageList = luggageRepository.findAll();
        assertThat(luggageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLuggageNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = luggageRepository.findAll().size();
        // set the field null
        luggage.setLuggageNumber(null);

        // Create the Luggage, which fails.


        restLuggageMockMvc.perform(post("/api/luggages")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(luggage)))
            .andExpect(status().isBadRequest());

        List<Luggage> luggageList = luggageRepository.findAll();
        assertThat(luggageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFlightNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = luggageRepository.findAll().size();
        // set the field null
        luggage.setFlightNumber(null);

        // Create the Luggage, which fails.


        restLuggageMockMvc.perform(post("/api/luggages")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(luggage)))
            .andExpect(status().isBadRequest());

        List<Luggage> luggageList = luggageRepository.findAll();
        assertThat(luggageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBookingNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = luggageRepository.findAll().size();
        // set the field null
        luggage.setBookingNumber(null);

        // Create the Luggage, which fails.


        restLuggageMockMvc.perform(post("/api/luggages")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(luggage)))
            .andExpect(status().isBadRequest());

        List<Luggage> luggageList = luggageRepository.findAll();
        assertThat(luggageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPassengerIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = luggageRepository.findAll().size();
        // set the field null
        luggage.setPassengerId(null);

        // Create the Luggage, which fails.


        restLuggageMockMvc.perform(post("/api/luggages")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(luggage)))
            .andExpect(status().isBadRequest());

        List<Luggage> luggageList = luggageRepository.findAll();
        assertThat(luggageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkWeightCategoryIsRequired() throws Exception {
        int databaseSizeBeforeTest = luggageRepository.findAll().size();
        // set the field null
        luggage.setWeightCategory(null);

        // Create the Luggage, which fails.


        restLuggageMockMvc.perform(post("/api/luggages")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(luggage)))
            .andExpect(status().isBadRequest());

        List<Luggage> luggageList = luggageRepository.findAll();
        assertThat(luggageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLuggages() throws Exception {
        // Initialize the database
        luggageRepository.saveAndFlush(luggage);

        // Get all the luggageList
        restLuggageMockMvc.perform(get("/api/luggages?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(luggage.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].luggageNumber").value(hasItem(DEFAULT_LUGGAGE_NUMBER)))
            .andExpect(jsonPath("$.[*].flightNumber").value(hasItem(DEFAULT_FLIGHT_NUMBER)))
            .andExpect(jsonPath("$.[*].bookingNumber").value(hasItem(DEFAULT_BOOKING_NUMBER)))
            .andExpect(jsonPath("$.[*].passengerId").value(hasItem(DEFAULT_PASSENGER_ID)))
            .andExpect(jsonPath("$.[*].weightCategory").value(hasItem(DEFAULT_WEIGHT_CATEGORY)))
            .andExpect(jsonPath("$.[*].rfidTag").value(hasItem(DEFAULT_RFID_TAG)));
    }
    
    @Test
    @Transactional
    public void getLuggage() throws Exception {
        // Initialize the database
        luggageRepository.saveAndFlush(luggage);

        // Get the luggage
        restLuggageMockMvc.perform(get("/api/luggages/{id}", luggage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(luggage.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.luggageNumber").value(DEFAULT_LUGGAGE_NUMBER))
            .andExpect(jsonPath("$.flightNumber").value(DEFAULT_FLIGHT_NUMBER))
            .andExpect(jsonPath("$.bookingNumber").value(DEFAULT_BOOKING_NUMBER))
            .andExpect(jsonPath("$.passengerId").value(DEFAULT_PASSENGER_ID))
            .andExpect(jsonPath("$.weightCategory").value(DEFAULT_WEIGHT_CATEGORY))
            .andExpect(jsonPath("$.rfidTag").value(DEFAULT_RFID_TAG));
    }
    @Test
    @Transactional
    public void getNonExistingLuggage() throws Exception {
        // Get the luggage
        restLuggageMockMvc.perform(get("/api/luggages/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLuggage() throws Exception {
        // Initialize the database
        luggageRepository.saveAndFlush(luggage);

        int databaseSizeBeforeUpdate = luggageRepository.findAll().size();

        // Update the luggage
        Luggage updatedLuggage = luggageRepository.findById(luggage.getId()).get();
        // Disconnect from session so that the updates on updatedLuggage are not directly saved in db
        em.detach(updatedLuggage);
        updatedLuggage
            .type(UPDATED_TYPE)
            .luggageNumber(UPDATED_LUGGAGE_NUMBER)
            .flightNumber(UPDATED_FLIGHT_NUMBER)
            .bookingNumber(UPDATED_BOOKING_NUMBER)
            .passengerId(UPDATED_PASSENGER_ID)
            .weightCategory(UPDATED_WEIGHT_CATEGORY)
            .rfidTag(UPDATED_RFID_TAG);

        restLuggageMockMvc.perform(put("/api/luggages")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedLuggage)))
            .andExpect(status().isOk());

        // Validate the Luggage in the database
        List<Luggage> luggageList = luggageRepository.findAll();
        assertThat(luggageList).hasSize(databaseSizeBeforeUpdate);
        Luggage testLuggage = luggageList.get(luggageList.size() - 1);
        assertThat(testLuggage.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testLuggage.getLuggageNumber()).isEqualTo(UPDATED_LUGGAGE_NUMBER);
        assertThat(testLuggage.getFlightNumber()).isEqualTo(UPDATED_FLIGHT_NUMBER);
        assertThat(testLuggage.getBookingNumber()).isEqualTo(UPDATED_BOOKING_NUMBER);
        assertThat(testLuggage.getPassengerId()).isEqualTo(UPDATED_PASSENGER_ID);
        assertThat(testLuggage.getWeightCategory()).isEqualTo(UPDATED_WEIGHT_CATEGORY);
        assertThat(testLuggage.getRfidTag()).isEqualTo(UPDATED_RFID_TAG);
    }

    @Test
    @Transactional
    public void updateNonExistingLuggage() throws Exception {
        int databaseSizeBeforeUpdate = luggageRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLuggageMockMvc.perform(put("/api/luggages")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(luggage)))
            .andExpect(status().isBadRequest());

        // Validate the Luggage in the database
        List<Luggage> luggageList = luggageRepository.findAll();
        assertThat(luggageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteLuggage() throws Exception {
        // Initialize the database
        luggageRepository.saveAndFlush(luggage);

        int databaseSizeBeforeDelete = luggageRepository.findAll().size();

        // Delete the luggage
        restLuggageMockMvc.perform(delete("/api/luggages/{id}", luggage.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Luggage> luggageList = luggageRepository.findAll();
        assertThat(luggageList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
