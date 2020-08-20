package flights.web.rest;

import flights.FlightsApp;
import flights.domain.CreditCard;
import flights.repository.CreditCardRepository;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import flights.domain.enumeration.ECardType;
/**
 * Integration tests for the {@link CreditCardResource} REST controller.
 */
@SpringBootTest(classes = FlightsApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class CreditCardResourceIT {

    private static final ECardType DEFAULT_CARD_TYPE = ECardType.MASTERCARD;
    private static final ECardType UPDATED_CARD_TYPE = ECardType.VISA;

    private static final Integer DEFAULT_CVC = 1;
    private static final Integer UPDATED_CVC = 2;

    private static final Integer DEFAULT_CARD_NUMBER = 1;
    private static final Integer UPDATED_CARD_NUMBER = 2;

    private static final LocalDate DEFAULT_VALIDITY_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_VALIDITY_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCreditCardMockMvc;

    private CreditCard creditCard;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CreditCard createEntity(EntityManager em) {
        CreditCard creditCard = new CreditCard()
            .cardType(DEFAULT_CARD_TYPE)
            .cvc(DEFAULT_CVC)
            .cardNumber(DEFAULT_CARD_NUMBER)
            .validityDate(DEFAULT_VALIDITY_DATE);
        return creditCard;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CreditCard createUpdatedEntity(EntityManager em) {
        CreditCard creditCard = new CreditCard()
            .cardType(UPDATED_CARD_TYPE)
            .cvc(UPDATED_CVC)
            .cardNumber(UPDATED_CARD_NUMBER)
            .validityDate(UPDATED_VALIDITY_DATE);
        return creditCard;
    }

    @BeforeEach
    public void initTest() {
        creditCard = createEntity(em);
    }

    @Test
    @Transactional
    public void createCreditCard() throws Exception {
        int databaseSizeBeforeCreate = creditCardRepository.findAll().size();
        // Create the CreditCard
        restCreditCardMockMvc.perform(post("/api/credit-cards")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(creditCard)))
            .andExpect(status().isCreated());

        // Validate the CreditCard in the database
        List<CreditCard> creditCardList = creditCardRepository.findAll();
        assertThat(creditCardList).hasSize(databaseSizeBeforeCreate + 1);
        CreditCard testCreditCard = creditCardList.get(creditCardList.size() - 1);
        assertThat(testCreditCard.getCardType()).isEqualTo(DEFAULT_CARD_TYPE);
        assertThat(testCreditCard.getCvc()).isEqualTo(DEFAULT_CVC);
        assertThat(testCreditCard.getCardNumber()).isEqualTo(DEFAULT_CARD_NUMBER);
        assertThat(testCreditCard.getValidityDate()).isEqualTo(DEFAULT_VALIDITY_DATE);
    }

    @Test
    @Transactional
    public void createCreditCardWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = creditCardRepository.findAll().size();

        // Create the CreditCard with an existing ID
        creditCard.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCreditCardMockMvc.perform(post("/api/credit-cards")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(creditCard)))
            .andExpect(status().isBadRequest());

        // Validate the CreditCard in the database
        List<CreditCard> creditCardList = creditCardRepository.findAll();
        assertThat(creditCardList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCardTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = creditCardRepository.findAll().size();
        // set the field null
        creditCard.setCardType(null);

        // Create the CreditCard, which fails.


        restCreditCardMockMvc.perform(post("/api/credit-cards")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(creditCard)))
            .andExpect(status().isBadRequest());

        List<CreditCard> creditCardList = creditCardRepository.findAll();
        assertThat(creditCardList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCvcIsRequired() throws Exception {
        int databaseSizeBeforeTest = creditCardRepository.findAll().size();
        // set the field null
        creditCard.setCvc(null);

        // Create the CreditCard, which fails.


        restCreditCardMockMvc.perform(post("/api/credit-cards")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(creditCard)))
            .andExpect(status().isBadRequest());

        List<CreditCard> creditCardList = creditCardRepository.findAll();
        assertThat(creditCardList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCardNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = creditCardRepository.findAll().size();
        // set the field null
        creditCard.setCardNumber(null);

        // Create the CreditCard, which fails.


        restCreditCardMockMvc.perform(post("/api/credit-cards")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(creditCard)))
            .andExpect(status().isBadRequest());

        List<CreditCard> creditCardList = creditCardRepository.findAll();
        assertThat(creditCardList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkValidityDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = creditCardRepository.findAll().size();
        // set the field null
        creditCard.setValidityDate(null);

        // Create the CreditCard, which fails.


        restCreditCardMockMvc.perform(post("/api/credit-cards")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(creditCard)))
            .andExpect(status().isBadRequest());

        List<CreditCard> creditCardList = creditCardRepository.findAll();
        assertThat(creditCardList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCreditCards() throws Exception {
        // Initialize the database
        creditCardRepository.saveAndFlush(creditCard);

        // Get all the creditCardList
        restCreditCardMockMvc.perform(get("/api/credit-cards?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(creditCard.getId().intValue())))
            .andExpect(jsonPath("$.[*].cardType").value(hasItem(DEFAULT_CARD_TYPE.toString())))
            .andExpect(jsonPath("$.[*].cvc").value(hasItem(DEFAULT_CVC)))
            .andExpect(jsonPath("$.[*].cardNumber").value(hasItem(DEFAULT_CARD_NUMBER)))
            .andExpect(jsonPath("$.[*].validityDate").value(hasItem(DEFAULT_VALIDITY_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getCreditCard() throws Exception {
        // Initialize the database
        creditCardRepository.saveAndFlush(creditCard);

        // Get the creditCard
        restCreditCardMockMvc.perform(get("/api/credit-cards/{id}", creditCard.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(creditCard.getId().intValue()))
            .andExpect(jsonPath("$.cardType").value(DEFAULT_CARD_TYPE.toString()))
            .andExpect(jsonPath("$.cvc").value(DEFAULT_CVC))
            .andExpect(jsonPath("$.cardNumber").value(DEFAULT_CARD_NUMBER))
            .andExpect(jsonPath("$.validityDate").value(DEFAULT_VALIDITY_DATE.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingCreditCard() throws Exception {
        // Get the creditCard
        restCreditCardMockMvc.perform(get("/api/credit-cards/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCreditCard() throws Exception {
        // Initialize the database
        creditCardRepository.saveAndFlush(creditCard);

        int databaseSizeBeforeUpdate = creditCardRepository.findAll().size();

        // Update the creditCard
        CreditCard updatedCreditCard = creditCardRepository.findById(creditCard.getId()).get();
        // Disconnect from session so that the updates on updatedCreditCard are not directly saved in db
        em.detach(updatedCreditCard);
        updatedCreditCard
            .cardType(UPDATED_CARD_TYPE)
            .cvc(UPDATED_CVC)
            .cardNumber(UPDATED_CARD_NUMBER)
            .validityDate(UPDATED_VALIDITY_DATE);

        restCreditCardMockMvc.perform(put("/api/credit-cards")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedCreditCard)))
            .andExpect(status().isOk());

        // Validate the CreditCard in the database
        List<CreditCard> creditCardList = creditCardRepository.findAll();
        assertThat(creditCardList).hasSize(databaseSizeBeforeUpdate);
        CreditCard testCreditCard = creditCardList.get(creditCardList.size() - 1);
        assertThat(testCreditCard.getCardType()).isEqualTo(UPDATED_CARD_TYPE);
        assertThat(testCreditCard.getCvc()).isEqualTo(UPDATED_CVC);
        assertThat(testCreditCard.getCardNumber()).isEqualTo(UPDATED_CARD_NUMBER);
        assertThat(testCreditCard.getValidityDate()).isEqualTo(UPDATED_VALIDITY_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingCreditCard() throws Exception {
        int databaseSizeBeforeUpdate = creditCardRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCreditCardMockMvc.perform(put("/api/credit-cards")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(creditCard)))
            .andExpect(status().isBadRequest());

        // Validate the CreditCard in the database
        List<CreditCard> creditCardList = creditCardRepository.findAll();
        assertThat(creditCardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCreditCard() throws Exception {
        // Initialize the database
        creditCardRepository.saveAndFlush(creditCard);

        int databaseSizeBeforeDelete = creditCardRepository.findAll().size();

        // Delete the creditCard
        restCreditCardMockMvc.perform(delete("/api/credit-cards/{id}", creditCard.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CreditCard> creditCardList = creditCardRepository.findAll();
        assertThat(creditCardList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
