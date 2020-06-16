package payments.web.rest;

import payments.PaymentsApp;
import payments.domain.Validator;
import payments.repository.ValidatorRepository;

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
 * Integration tests for the {@link ValidatorResource} REST controller.
 */
@SpringBootTest(classes = PaymentsApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ValidatorResourceIT {

    private static final Boolean DEFAULT_IS_PAYMENT_METHOD_VALID = false;
    private static final Boolean UPDATED_IS_PAYMENT_METHOD_VALID = true;

    private static final Boolean DEFAULT_IS_INVOICE_VALID = false;
    private static final Boolean UPDATED_IS_INVOICE_VALID = true;

    @Autowired
    private ValidatorRepository validatorRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restValidatorMockMvc;

    private Validator validator;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Validator createEntity(EntityManager em) {
        Validator validator = new Validator()
            .isPaymentMethodValid(DEFAULT_IS_PAYMENT_METHOD_VALID)
            .isInvoiceValid(DEFAULT_IS_INVOICE_VALID);
        return validator;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Validator createUpdatedEntity(EntityManager em) {
        Validator validator = new Validator()
            .isPaymentMethodValid(UPDATED_IS_PAYMENT_METHOD_VALID)
            .isInvoiceValid(UPDATED_IS_INVOICE_VALID);
        return validator;
    }

    @BeforeEach
    public void initTest() {
        validator = createEntity(em);
    }

    @Test
    @Transactional
    public void createValidator() throws Exception {
        int databaseSizeBeforeCreate = validatorRepository.findAll().size();
        // Create the Validator
        restValidatorMockMvc.perform(post("/api/validators")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(validator)))
            .andExpect(status().isCreated());

        // Validate the Validator in the database
        List<Validator> validatorList = validatorRepository.findAll();
        assertThat(validatorList).hasSize(databaseSizeBeforeCreate + 1);
        Validator testValidator = validatorList.get(validatorList.size() - 1);
        assertThat(testValidator.isIsPaymentMethodValid()).isEqualTo(DEFAULT_IS_PAYMENT_METHOD_VALID);
        assertThat(testValidator.isIsInvoiceValid()).isEqualTo(DEFAULT_IS_INVOICE_VALID);
    }

    @Test
    @Transactional
    public void createValidatorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = validatorRepository.findAll().size();

        // Create the Validator with an existing ID
        validator.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restValidatorMockMvc.perform(post("/api/validators")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(validator)))
            .andExpect(status().isBadRequest());

        // Validate the Validator in the database
        List<Validator> validatorList = validatorRepository.findAll();
        assertThat(validatorList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllValidators() throws Exception {
        // Initialize the database
        validatorRepository.saveAndFlush(validator);

        // Get all the validatorList
        restValidatorMockMvc.perform(get("/api/validators?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(validator.getId().intValue())))
            .andExpect(jsonPath("$.[*].isPaymentMethodValid").value(hasItem(DEFAULT_IS_PAYMENT_METHOD_VALID.booleanValue())))
            .andExpect(jsonPath("$.[*].isInvoiceValid").value(hasItem(DEFAULT_IS_INVOICE_VALID.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getValidator() throws Exception {
        // Initialize the database
        validatorRepository.saveAndFlush(validator);

        // Get the validator
        restValidatorMockMvc.perform(get("/api/validators/{id}", validator.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(validator.getId().intValue()))
            .andExpect(jsonPath("$.isPaymentMethodValid").value(DEFAULT_IS_PAYMENT_METHOD_VALID.booleanValue()))
            .andExpect(jsonPath("$.isInvoiceValid").value(DEFAULT_IS_INVOICE_VALID.booleanValue()));
    }
    @Test
    @Transactional
    public void getNonExistingValidator() throws Exception {
        // Get the validator
        restValidatorMockMvc.perform(get("/api/validators/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateValidator() throws Exception {
        // Initialize the database
        validatorRepository.saveAndFlush(validator);

        int databaseSizeBeforeUpdate = validatorRepository.findAll().size();

        // Update the validator
        Validator updatedValidator = validatorRepository.findById(validator.getId()).get();
        // Disconnect from session so that the updates on updatedValidator are not directly saved in db
        em.detach(updatedValidator);
        updatedValidator
            .isPaymentMethodValid(UPDATED_IS_PAYMENT_METHOD_VALID)
            .isInvoiceValid(UPDATED_IS_INVOICE_VALID);

        restValidatorMockMvc.perform(put("/api/validators")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedValidator)))
            .andExpect(status().isOk());

        // Validate the Validator in the database
        List<Validator> validatorList = validatorRepository.findAll();
        assertThat(validatorList).hasSize(databaseSizeBeforeUpdate);
        Validator testValidator = validatorList.get(validatorList.size() - 1);
        assertThat(testValidator.isIsPaymentMethodValid()).isEqualTo(UPDATED_IS_PAYMENT_METHOD_VALID);
        assertThat(testValidator.isIsInvoiceValid()).isEqualTo(UPDATED_IS_INVOICE_VALID);
    }

    @Test
    @Transactional
    public void updateNonExistingValidator() throws Exception {
        int databaseSizeBeforeUpdate = validatorRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restValidatorMockMvc.perform(put("/api/validators")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(validator)))
            .andExpect(status().isBadRequest());

        // Validate the Validator in the database
        List<Validator> validatorList = validatorRepository.findAll();
        assertThat(validatorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteValidator() throws Exception {
        // Initialize the database
        validatorRepository.saveAndFlush(validator);

        int databaseSizeBeforeDelete = validatorRepository.findAll().size();

        // Delete the validator
        restValidatorMockMvc.perform(delete("/api/validators/{id}", validator.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Validator> validatorList = validatorRepository.findAll();
        assertThat(validatorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
