package flights.web.rest;

import flights.FlightsApp;
import flights.domain.Plane;
import flights.repository.PlaneRepository;

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
 * Integration tests for the {@link PlaneResource} REST controller.
 */
@SpringBootTest(classes = FlightsApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class PlaneResourceIT {

    private static final String DEFAULT_MANUFACTURER = "AAAAAAAAAA";
    private static final String UPDATED_MANUFACTURER = "BBBBBBBBBB";

    private static final String DEFAULT_MODEL_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_MODEL_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_REGISTRATION_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_REGISTRATION_NUMBER = "BBBBBBBBBB";

    @Autowired
    private PlaneRepository planeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPlaneMockMvc;

    private Plane plane;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Plane createEntity(EntityManager em) {
        Plane plane = new Plane()
            .manufacturer(DEFAULT_MANUFACTURER)
            .modelNumber(DEFAULT_MODEL_NUMBER)
            .registrationNumber(DEFAULT_REGISTRATION_NUMBER);
        return plane;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Plane createUpdatedEntity(EntityManager em) {
        Plane plane = new Plane()
            .manufacturer(UPDATED_MANUFACTURER)
            .modelNumber(UPDATED_MODEL_NUMBER)
            .registrationNumber(UPDATED_REGISTRATION_NUMBER);
        return plane;
    }

    @BeforeEach
    public void initTest() {
        plane = createEntity(em);
    }

    @Test
    @Transactional
    public void createPlane() throws Exception {
        int databaseSizeBeforeCreate = planeRepository.findAll().size();
        // Create the Plane
        restPlaneMockMvc.perform(post("/api/planes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(plane)))
            .andExpect(status().isCreated());

        // Validate the Plane in the database
        List<Plane> planeList = planeRepository.findAll();
        assertThat(planeList).hasSize(databaseSizeBeforeCreate + 1);
        Plane testPlane = planeList.get(planeList.size() - 1);
        assertThat(testPlane.getManufacturer()).isEqualTo(DEFAULT_MANUFACTURER);
        assertThat(testPlane.getModelNumber()).isEqualTo(DEFAULT_MODEL_NUMBER);
        assertThat(testPlane.getRegistrationNumber()).isEqualTo(DEFAULT_REGISTRATION_NUMBER);
    }

    @Test
    @Transactional
    public void createPlaneWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = planeRepository.findAll().size();

        // Create the Plane with an existing ID
        plane.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlaneMockMvc.perform(post("/api/planes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(plane)))
            .andExpect(status().isBadRequest());

        // Validate the Plane in the database
        List<Plane> planeList = planeRepository.findAll();
        assertThat(planeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkRegistrationNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = planeRepository.findAll().size();
        // set the field null
        plane.setRegistrationNumber(null);

        // Create the Plane, which fails.


        restPlaneMockMvc.perform(post("/api/planes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(plane)))
            .andExpect(status().isBadRequest());

        List<Plane> planeList = planeRepository.findAll();
        assertThat(planeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPlanes() throws Exception {
        // Initialize the database
        planeRepository.saveAndFlush(plane);

        // Get all the planeList
        restPlaneMockMvc.perform(get("/api/planes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(plane.getId().intValue())))
            .andExpect(jsonPath("$.[*].manufacturer").value(hasItem(DEFAULT_MANUFACTURER)))
            .andExpect(jsonPath("$.[*].modelNumber").value(hasItem(DEFAULT_MODEL_NUMBER)))
            .andExpect(jsonPath("$.[*].registrationNumber").value(hasItem(DEFAULT_REGISTRATION_NUMBER)));
    }
    
    @Test
    @Transactional
    public void getPlane() throws Exception {
        // Initialize the database
        planeRepository.saveAndFlush(plane);

        // Get the plane
        restPlaneMockMvc.perform(get("/api/planes/{id}", plane.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(plane.getId().intValue()))
            .andExpect(jsonPath("$.manufacturer").value(DEFAULT_MANUFACTURER))
            .andExpect(jsonPath("$.modelNumber").value(DEFAULT_MODEL_NUMBER))
            .andExpect(jsonPath("$.registrationNumber").value(DEFAULT_REGISTRATION_NUMBER));
    }
    @Test
    @Transactional
    public void getNonExistingPlane() throws Exception {
        // Get the plane
        restPlaneMockMvc.perform(get("/api/planes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePlane() throws Exception {
        // Initialize the database
        planeRepository.saveAndFlush(plane);

        int databaseSizeBeforeUpdate = planeRepository.findAll().size();

        // Update the plane
        Plane updatedPlane = planeRepository.findById(plane.getId()).get();
        // Disconnect from session so that the updates on updatedPlane are not directly saved in db
        em.detach(updatedPlane);
        updatedPlane
            .manufacturer(UPDATED_MANUFACTURER)
            .modelNumber(UPDATED_MODEL_NUMBER)
            .registrationNumber(UPDATED_REGISTRATION_NUMBER);

        restPlaneMockMvc.perform(put("/api/planes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedPlane)))
            .andExpect(status().isOk());

        // Validate the Plane in the database
        List<Plane> planeList = planeRepository.findAll();
        assertThat(planeList).hasSize(databaseSizeBeforeUpdate);
        Plane testPlane = planeList.get(planeList.size() - 1);
        assertThat(testPlane.getManufacturer()).isEqualTo(UPDATED_MANUFACTURER);
        assertThat(testPlane.getModelNumber()).isEqualTo(UPDATED_MODEL_NUMBER);
        assertThat(testPlane.getRegistrationNumber()).isEqualTo(UPDATED_REGISTRATION_NUMBER);
    }

    @Test
    @Transactional
    public void updateNonExistingPlane() throws Exception {
        int databaseSizeBeforeUpdate = planeRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlaneMockMvc.perform(put("/api/planes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(plane)))
            .andExpect(status().isBadRequest());

        // Validate the Plane in the database
        List<Plane> planeList = planeRepository.findAll();
        assertThat(planeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePlane() throws Exception {
        // Initialize the database
        planeRepository.saveAndFlush(plane);

        int databaseSizeBeforeDelete = planeRepository.findAll().size();

        // Delete the plane
        restPlaneMockMvc.perform(delete("/api/planes/{id}", plane.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Plane> planeList = planeRepository.findAll();
        assertThat(planeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
