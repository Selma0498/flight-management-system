package flights.web.rest;

import flights.FlightsApp;
import flights.domain.Fare;
import flights.repository.FareRepository;

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

import flights.domain.enumeration.EFareType;
/**
 * Integration tests for the {@link FareResource} REST controller.
 */
@SpringBootTest(classes = FlightsApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class FareResourceIT {

    private static final EFareType DEFAULT_TYPE = EFareType.ECONOMY;
    private static final EFareType UPDATED_TYPE = EFareType.BUSINESS;

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;

    @Autowired
    private FareRepository fareRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFareMockMvc;

    private Fare fare;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fare createEntity(EntityManager em) {
        Fare fare = new Fare()
            .type(DEFAULT_TYPE)
            .price(DEFAULT_PRICE);
        return fare;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fare createUpdatedEntity(EntityManager em) {
        Fare fare = new Fare()
            .type(UPDATED_TYPE)
            .price(UPDATED_PRICE);
        return fare;
    }

    @BeforeEach
    public void initTest() {
        fare = createEntity(em);
    }

    @Test
    @Transactional
    public void createFare() throws Exception {
        int databaseSizeBeforeCreate = fareRepository.findAll().size();
        // Create the Fare
        restFareMockMvc.perform(post("/api/fares")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fare)))
            .andExpect(status().isCreated());

        // Validate the Fare in the database
        List<Fare> fareList = fareRepository.findAll();
        assertThat(fareList).hasSize(databaseSizeBeforeCreate + 1);
        Fare testFare = fareList.get(fareList.size() - 1);
        assertThat(testFare.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testFare.getPrice()).isEqualTo(DEFAULT_PRICE);
    }

    @Test
    @Transactional
    public void createFareWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = fareRepository.findAll().size();

        // Create the Fare with an existing ID
        fare.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFareMockMvc.perform(post("/api/fares")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fare)))
            .andExpect(status().isBadRequest());

        // Validate the Fare in the database
        List<Fare> fareList = fareRepository.findAll();
        assertThat(fareList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = fareRepository.findAll().size();
        // set the field null
        fare.setType(null);

        // Create the Fare, which fails.


        restFareMockMvc.perform(post("/api/fares")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fare)))
            .andExpect(status().isBadRequest());

        List<Fare> fareList = fareRepository.findAll();
        assertThat(fareList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = fareRepository.findAll().size();
        // set the field null
        fare.setPrice(null);

        // Create the Fare, which fails.


        restFareMockMvc.perform(post("/api/fares")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fare)))
            .andExpect(status().isBadRequest());

        List<Fare> fareList = fareRepository.findAll();
        assertThat(fareList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFares() throws Exception {
        // Initialize the database
        fareRepository.saveAndFlush(fare);

        // Get all the fareList
        restFareMockMvc.perform(get("/api/fares?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fare.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getFare() throws Exception {
        // Initialize the database
        fareRepository.saveAndFlush(fare);

        // Get the fare
        restFareMockMvc.perform(get("/api/fares/{id}", fare.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fare.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()));
    }
    @Test
    @Transactional
    public void getNonExistingFare() throws Exception {
        // Get the fare
        restFareMockMvc.perform(get("/api/fares/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFare() throws Exception {
        // Initialize the database
        fareRepository.saveAndFlush(fare);

        int databaseSizeBeforeUpdate = fareRepository.findAll().size();

        // Update the fare
        Fare updatedFare = fareRepository.findById(fare.getId()).get();
        // Disconnect from session so that the updates on updatedFare are not directly saved in db
        em.detach(updatedFare);
        updatedFare
            .type(UPDATED_TYPE)
            .price(UPDATED_PRICE);

        restFareMockMvc.perform(put("/api/fares")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedFare)))
            .andExpect(status().isOk());

        // Validate the Fare in the database
        List<Fare> fareList = fareRepository.findAll();
        assertThat(fareList).hasSize(databaseSizeBeforeUpdate);
        Fare testFare = fareList.get(fareList.size() - 1);
        assertThat(testFare.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testFare.getPrice()).isEqualTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void updateNonExistingFare() throws Exception {
        int databaseSizeBeforeUpdate = fareRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFareMockMvc.perform(put("/api/fares")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fare)))
            .andExpect(status().isBadRequest());

        // Validate the Fare in the database
        List<Fare> fareList = fareRepository.findAll();
        assertThat(fareList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFare() throws Exception {
        // Initialize the database
        fareRepository.saveAndFlush(fare);

        int databaseSizeBeforeDelete = fareRepository.findAll().size();

        // Delete the fare
        restFareMockMvc.perform(delete("/api/fares/{id}", fare.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Fare> fareList = fareRepository.findAll();
        assertThat(fareList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
