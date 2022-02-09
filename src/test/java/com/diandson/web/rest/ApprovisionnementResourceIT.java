package com.diandson.web.rest;

import static com.diandson.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.diandson.IntegrationTest;
import com.diandson.domain.Approvisionnement;
import com.diandson.repository.ApprovisionnementRepository;
import com.diandson.service.dto.ApprovisionnementDTO;
import com.diandson.service.mapper.ApprovisionnementMapper;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ApprovisionnementResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ApprovisionnementResourceIT {

    private static final String DEFAULT_NUMERO = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO = "BBBBBBBBBB";

    private static final String DEFAULT_AGENCE_APP = "AAAAAAAAAA";
    private static final String UPDATED_AGENCE_APP = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENTAIRE = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTAIRE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE_COMMANDE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_COMMANDE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/approvisionnements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ApprovisionnementRepository approvisionnementRepository;

    @Autowired
    private ApprovisionnementMapper approvisionnementMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restApprovisionnementMockMvc;

    private Approvisionnement approvisionnement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Approvisionnement createEntity(EntityManager em) {
        Approvisionnement approvisionnement = new Approvisionnement()
            .numero(DEFAULT_NUMERO)
            .agenceApp(DEFAULT_AGENCE_APP)
            .commentaire(DEFAULT_COMMENTAIRE)
            .dateCommande(DEFAULT_DATE_COMMANDE);
        return approvisionnement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Approvisionnement createUpdatedEntity(EntityManager em) {
        Approvisionnement approvisionnement = new Approvisionnement()
            .numero(UPDATED_NUMERO)
            .agenceApp(UPDATED_AGENCE_APP)
            .commentaire(UPDATED_COMMENTAIRE)
            .dateCommande(UPDATED_DATE_COMMANDE);
        return approvisionnement;
    }

    @BeforeEach
    public void initTest() {
        approvisionnement = createEntity(em);
    }

    @Test
    @Transactional
    void createApprovisionnement() throws Exception {
        int databaseSizeBeforeCreate = approvisionnementRepository.findAll().size();
        // Create the Approvisionnement
        ApprovisionnementDTO approvisionnementDTO = approvisionnementMapper.toDto(approvisionnement);
        restApprovisionnementMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(approvisionnementDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Approvisionnement in the database
        List<Approvisionnement> approvisionnementList = approvisionnementRepository.findAll();
        assertThat(approvisionnementList).hasSize(databaseSizeBeforeCreate + 1);
        Approvisionnement testApprovisionnement = approvisionnementList.get(approvisionnementList.size() - 1);
        assertThat(testApprovisionnement.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testApprovisionnement.getAgenceApp()).isEqualTo(DEFAULT_AGENCE_APP);
        assertThat(testApprovisionnement.getCommentaire()).isEqualTo(DEFAULT_COMMENTAIRE);
        assertThat(testApprovisionnement.getDateCommande()).isEqualTo(DEFAULT_DATE_COMMANDE);
    }

    @Test
    @Transactional
    void createApprovisionnementWithExistingId() throws Exception {
        // Create the Approvisionnement with an existing ID
        approvisionnement.setId(1L);
        ApprovisionnementDTO approvisionnementDTO = approvisionnementMapper.toDto(approvisionnement);

        int databaseSizeBeforeCreate = approvisionnementRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restApprovisionnementMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(approvisionnementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Approvisionnement in the database
        List<Approvisionnement> approvisionnementList = approvisionnementRepository.findAll();
        assertThat(approvisionnementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllApprovisionnements() throws Exception {
        // Initialize the database
        approvisionnementRepository.saveAndFlush(approvisionnement);

        // Get all the approvisionnementList
        restApprovisionnementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(approvisionnement.getId().intValue())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].agenceApp").value(hasItem(DEFAULT_AGENCE_APP)))
            .andExpect(jsonPath("$.[*].commentaire").value(hasItem(DEFAULT_COMMENTAIRE)))
            .andExpect(jsonPath("$.[*].dateCommande").value(hasItem(sameInstant(DEFAULT_DATE_COMMANDE))));
    }

    @Test
    @Transactional
    void getApprovisionnement() throws Exception {
        // Initialize the database
        approvisionnementRepository.saveAndFlush(approvisionnement);

        // Get the approvisionnement
        restApprovisionnementMockMvc
            .perform(get(ENTITY_API_URL_ID, approvisionnement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(approvisionnement.getId().intValue()))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO))
            .andExpect(jsonPath("$.agenceApp").value(DEFAULT_AGENCE_APP))
            .andExpect(jsonPath("$.commentaire").value(DEFAULT_COMMENTAIRE))
            .andExpect(jsonPath("$.dateCommande").value(sameInstant(DEFAULT_DATE_COMMANDE)));
    }

    @Test
    @Transactional
    void getNonExistingApprovisionnement() throws Exception {
        // Get the approvisionnement
        restApprovisionnementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewApprovisionnement() throws Exception {
        // Initialize the database
        approvisionnementRepository.saveAndFlush(approvisionnement);

        int databaseSizeBeforeUpdate = approvisionnementRepository.findAll().size();

        // Update the approvisionnement
        Approvisionnement updatedApprovisionnement = approvisionnementRepository.findById(approvisionnement.getId()).get();
        // Disconnect from session so that the updates on updatedApprovisionnement are not directly saved in db
        em.detach(updatedApprovisionnement);
        updatedApprovisionnement
            .numero(UPDATED_NUMERO)
            .agenceApp(UPDATED_AGENCE_APP)
            .commentaire(UPDATED_COMMENTAIRE)
            .dateCommande(UPDATED_DATE_COMMANDE);
        ApprovisionnementDTO approvisionnementDTO = approvisionnementMapper.toDto(updatedApprovisionnement);

        restApprovisionnementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, approvisionnementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(approvisionnementDTO))
            )
            .andExpect(status().isOk());

        // Validate the Approvisionnement in the database
        List<Approvisionnement> approvisionnementList = approvisionnementRepository.findAll();
        assertThat(approvisionnementList).hasSize(databaseSizeBeforeUpdate);
        Approvisionnement testApprovisionnement = approvisionnementList.get(approvisionnementList.size() - 1);
        assertThat(testApprovisionnement.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testApprovisionnement.getAgenceApp()).isEqualTo(UPDATED_AGENCE_APP);
        assertThat(testApprovisionnement.getCommentaire()).isEqualTo(UPDATED_COMMENTAIRE);
        assertThat(testApprovisionnement.getDateCommande()).isEqualTo(UPDATED_DATE_COMMANDE);
    }

    @Test
    @Transactional
    void putNonExistingApprovisionnement() throws Exception {
        int databaseSizeBeforeUpdate = approvisionnementRepository.findAll().size();
        approvisionnement.setId(count.incrementAndGet());

        // Create the Approvisionnement
        ApprovisionnementDTO approvisionnementDTO = approvisionnementMapper.toDto(approvisionnement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApprovisionnementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, approvisionnementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(approvisionnementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Approvisionnement in the database
        List<Approvisionnement> approvisionnementList = approvisionnementRepository.findAll();
        assertThat(approvisionnementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchApprovisionnement() throws Exception {
        int databaseSizeBeforeUpdate = approvisionnementRepository.findAll().size();
        approvisionnement.setId(count.incrementAndGet());

        // Create the Approvisionnement
        ApprovisionnementDTO approvisionnementDTO = approvisionnementMapper.toDto(approvisionnement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApprovisionnementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(approvisionnementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Approvisionnement in the database
        List<Approvisionnement> approvisionnementList = approvisionnementRepository.findAll();
        assertThat(approvisionnementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamApprovisionnement() throws Exception {
        int databaseSizeBeforeUpdate = approvisionnementRepository.findAll().size();
        approvisionnement.setId(count.incrementAndGet());

        // Create the Approvisionnement
        ApprovisionnementDTO approvisionnementDTO = approvisionnementMapper.toDto(approvisionnement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApprovisionnementMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(approvisionnementDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Approvisionnement in the database
        List<Approvisionnement> approvisionnementList = approvisionnementRepository.findAll();
        assertThat(approvisionnementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateApprovisionnementWithPatch() throws Exception {
        // Initialize the database
        approvisionnementRepository.saveAndFlush(approvisionnement);

        int databaseSizeBeforeUpdate = approvisionnementRepository.findAll().size();

        // Update the approvisionnement using partial update
        Approvisionnement partialUpdatedApprovisionnement = new Approvisionnement();
        partialUpdatedApprovisionnement.setId(approvisionnement.getId());

        partialUpdatedApprovisionnement.commentaire(UPDATED_COMMENTAIRE).dateCommande(UPDATED_DATE_COMMANDE);

        restApprovisionnementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApprovisionnement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApprovisionnement))
            )
            .andExpect(status().isOk());

        // Validate the Approvisionnement in the database
        List<Approvisionnement> approvisionnementList = approvisionnementRepository.findAll();
        assertThat(approvisionnementList).hasSize(databaseSizeBeforeUpdate);
        Approvisionnement testApprovisionnement = approvisionnementList.get(approvisionnementList.size() - 1);
        assertThat(testApprovisionnement.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testApprovisionnement.getAgenceApp()).isEqualTo(DEFAULT_AGENCE_APP);
        assertThat(testApprovisionnement.getCommentaire()).isEqualTo(UPDATED_COMMENTAIRE);
        assertThat(testApprovisionnement.getDateCommande()).isEqualTo(UPDATED_DATE_COMMANDE);
    }

    @Test
    @Transactional
    void fullUpdateApprovisionnementWithPatch() throws Exception {
        // Initialize the database
        approvisionnementRepository.saveAndFlush(approvisionnement);

        int databaseSizeBeforeUpdate = approvisionnementRepository.findAll().size();

        // Update the approvisionnement using partial update
        Approvisionnement partialUpdatedApprovisionnement = new Approvisionnement();
        partialUpdatedApprovisionnement.setId(approvisionnement.getId());

        partialUpdatedApprovisionnement
            .numero(UPDATED_NUMERO)
            .agenceApp(UPDATED_AGENCE_APP)
            .commentaire(UPDATED_COMMENTAIRE)
            .dateCommande(UPDATED_DATE_COMMANDE);

        restApprovisionnementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApprovisionnement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApprovisionnement))
            )
            .andExpect(status().isOk());

        // Validate the Approvisionnement in the database
        List<Approvisionnement> approvisionnementList = approvisionnementRepository.findAll();
        assertThat(approvisionnementList).hasSize(databaseSizeBeforeUpdate);
        Approvisionnement testApprovisionnement = approvisionnementList.get(approvisionnementList.size() - 1);
        assertThat(testApprovisionnement.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testApprovisionnement.getAgenceApp()).isEqualTo(UPDATED_AGENCE_APP);
        assertThat(testApprovisionnement.getCommentaire()).isEqualTo(UPDATED_COMMENTAIRE);
        assertThat(testApprovisionnement.getDateCommande()).isEqualTo(UPDATED_DATE_COMMANDE);
    }

    @Test
    @Transactional
    void patchNonExistingApprovisionnement() throws Exception {
        int databaseSizeBeforeUpdate = approvisionnementRepository.findAll().size();
        approvisionnement.setId(count.incrementAndGet());

        // Create the Approvisionnement
        ApprovisionnementDTO approvisionnementDTO = approvisionnementMapper.toDto(approvisionnement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApprovisionnementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, approvisionnementDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(approvisionnementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Approvisionnement in the database
        List<Approvisionnement> approvisionnementList = approvisionnementRepository.findAll();
        assertThat(approvisionnementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchApprovisionnement() throws Exception {
        int databaseSizeBeforeUpdate = approvisionnementRepository.findAll().size();
        approvisionnement.setId(count.incrementAndGet());

        // Create the Approvisionnement
        ApprovisionnementDTO approvisionnementDTO = approvisionnementMapper.toDto(approvisionnement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApprovisionnementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(approvisionnementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Approvisionnement in the database
        List<Approvisionnement> approvisionnementList = approvisionnementRepository.findAll();
        assertThat(approvisionnementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamApprovisionnement() throws Exception {
        int databaseSizeBeforeUpdate = approvisionnementRepository.findAll().size();
        approvisionnement.setId(count.incrementAndGet());

        // Create the Approvisionnement
        ApprovisionnementDTO approvisionnementDTO = approvisionnementMapper.toDto(approvisionnement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApprovisionnementMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(approvisionnementDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Approvisionnement in the database
        List<Approvisionnement> approvisionnementList = approvisionnementRepository.findAll();
        assertThat(approvisionnementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteApprovisionnement() throws Exception {
        // Initialize the database
        approvisionnementRepository.saveAndFlush(approvisionnement);

        int databaseSizeBeforeDelete = approvisionnementRepository.findAll().size();

        // Delete the approvisionnement
        restApprovisionnementMockMvc
            .perform(delete(ENTITY_API_URL_ID, approvisionnement.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Approvisionnement> approvisionnementList = approvisionnementRepository.findAll();
        assertThat(approvisionnementList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
