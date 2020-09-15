package passengers.web.rest;

import passengers.PassengersApp;
import passengers.domain.NotificationRepo;
import passengers.repository.NotificationRepoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link NotificationRepoResource} REST controller.
 */
@SpringBootTest(classes = PassengersApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class NotificationRepoResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private NotificationRepoRepository notificationRepoRepository;

    @Mock
    private NotificationRepoRepository notificationRepoRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNotificationRepoMockMvc;

    private NotificationRepo notificationRepo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NotificationRepo createEntity(EntityManager em) {
        NotificationRepo notificationRepo = new NotificationRepo("rand", "rand")
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION);
        return notificationRepo;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NotificationRepo createUpdatedEntity(EntityManager em) {
        NotificationRepo notificationRepo = new NotificationRepo("rand", "rand")
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);
        return notificationRepo;
    }

    @BeforeEach
    public void initTest() {
        notificationRepo = createEntity(em);
    }

    @Test
    @Transactional
    public void createNotificationRepo() throws Exception {
        int databaseSizeBeforeCreate = notificationRepoRepository.findAll().size();
        // Create the NotificationRepo
        restNotificationRepoMockMvc.perform(post("/api/notification-repos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(notificationRepo)))
            .andExpect(status().isCreated());

        // Validate the NotificationRepo in the database
        List<NotificationRepo> notificationRepoList = notificationRepoRepository.findAll();
        assertThat(notificationRepoList).hasSize(databaseSizeBeforeCreate + 1);
        NotificationRepo testNotificationRepo = notificationRepoList.get(notificationRepoList.size() - 1);
        assertThat(testNotificationRepo.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testNotificationRepo.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createNotificationRepoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = notificationRepoRepository.findAll().size();

        // Create the NotificationRepo with an existing ID
        notificationRepo.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restNotificationRepoMockMvc.perform(post("/api/notification-repos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(notificationRepo)))
            .andExpect(status().isBadRequest());

        // Validate the NotificationRepo in the database
        List<NotificationRepo> notificationRepoList = notificationRepoRepository.findAll();
        assertThat(notificationRepoList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = notificationRepoRepository.findAll().size();
        // set the field null
        notificationRepo.setName(null);

        // Create the NotificationRepo, which fails.


        restNotificationRepoMockMvc.perform(post("/api/notification-repos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(notificationRepo)))
            .andExpect(status().isBadRequest());

        List<NotificationRepo> notificationRepoList = notificationRepoRepository.findAll();
        assertThat(notificationRepoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = notificationRepoRepository.findAll().size();
        // set the field null
        notificationRepo.setDescription(null);

        // Create the NotificationRepo, which fails.


        restNotificationRepoMockMvc.perform(post("/api/notification-repos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(notificationRepo)))
            .andExpect(status().isBadRequest());

        List<NotificationRepo> notificationRepoList = notificationRepoRepository.findAll();
        assertThat(notificationRepoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllNotificationRepos() throws Exception {
        // Initialize the database
        notificationRepoRepository.saveAndFlush(notificationRepo);

        // Get all the notificationRepoList
        restNotificationRepoMockMvc.perform(get("/api/notification-repos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notificationRepo.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllNotificationReposWithEagerRelationshipsIsEnabled() throws Exception {
        when(notificationRepoRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNotificationRepoMockMvc.perform(get("/api/notification-repos?eagerload=true"))
            .andExpect(status().isOk());

        verify(notificationRepoRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllNotificationReposWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(notificationRepoRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNotificationRepoMockMvc.perform(get("/api/notification-repos?eagerload=true"))
            .andExpect(status().isOk());

        verify(notificationRepoRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getNotificationRepo() throws Exception {
        // Initialize the database
        notificationRepoRepository.saveAndFlush(notificationRepo);

        // Get the notificationRepo
        restNotificationRepoMockMvc.perform(get("/api/notification-repos/{id}", notificationRepo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(notificationRepo.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }
    @Test
    @Transactional
    public void getNonExistingNotificationRepo() throws Exception {
        // Get the notificationRepo
        restNotificationRepoMockMvc.perform(get("/api/notification-repos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNotificationRepo() throws Exception {
        // Initialize the database
        notificationRepoRepository.saveAndFlush(notificationRepo);

        int databaseSizeBeforeUpdate = notificationRepoRepository.findAll().size();

        // Update the notificationRepo
        NotificationRepo updatedNotificationRepo = notificationRepoRepository.findById(notificationRepo.getId()).get();
        // Disconnect from session so that the updates on updatedNotificationRepo are not directly saved in db
        em.detach(updatedNotificationRepo);
        updatedNotificationRepo
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);

        restNotificationRepoMockMvc.perform(put("/api/notification-repos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedNotificationRepo)))
            .andExpect(status().isOk());

        // Validate the NotificationRepo in the database
        List<NotificationRepo> notificationRepoList = notificationRepoRepository.findAll();
        assertThat(notificationRepoList).hasSize(databaseSizeBeforeUpdate);
        NotificationRepo testNotificationRepo = notificationRepoList.get(notificationRepoList.size() - 1);
        assertThat(testNotificationRepo.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testNotificationRepo.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingNotificationRepo() throws Exception {
        int databaseSizeBeforeUpdate = notificationRepoRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotificationRepoMockMvc.perform(put("/api/notification-repos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(notificationRepo)))
            .andExpect(status().isBadRequest());

        // Validate the NotificationRepo in the database
        List<NotificationRepo> notificationRepoList = notificationRepoRepository.findAll();
        assertThat(notificationRepoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteNotificationRepo() throws Exception {
        // Initialize the database
        notificationRepoRepository.saveAndFlush(notificationRepo);

        int databaseSizeBeforeDelete = notificationRepoRepository.findAll().size();

        // Delete the notificationRepo
        restNotificationRepoMockMvc.perform(delete("/api/notification-repos/{id}", notificationRepo.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<NotificationRepo> notificationRepoList = notificationRepoRepository.findAll();
        assertThat(notificationRepoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
