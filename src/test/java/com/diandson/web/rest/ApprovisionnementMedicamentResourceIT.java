package com.diandson.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.diandson.IntegrationTest;
import com.diandson.domain.ApprovisionnementMedicament;
import com.diandson.repository.ApprovisionnementMedicamentRepository;
import com.diandson.service.dto.ApprovisionnementMedicamentDTO;
import com.diandson.service.mapper.ApprovisionnementMedicamentMapper;
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
 * Integration tests for the {@link ApprovisionnementMedicamentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ApprovisionnementMedicamentResourceIT {

    private static final Long DEFAULT_QUANTITE = 1L;
    private static final Long UPDATED_QUANTITE = 2L;

    private static final String ENTITY_API_URL = "/api/approvisionnement-medicaments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ApprovisionnementMedicamentRepository approvisionnementMedicamentRepository;

    @Autowired
    private ApprovisionnementMedicamentMapper approvisionnementMedicamentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restApprovisionnementMedicamentMockMvc;

    private ApprovisionnementMedicament approvisionnementMedicament;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApprovisionnementMedicament createEntity(EntityManager em) {
        ApprovisionnementMedicament approvisionnementMedicament = new ApprovisionnementMedicament().quantite(DEFAULT_QUANTITE);
        return approvisionnementMedicament;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApprovisionnementMedicament createUpdatedEntity(EntityManager em) {
        ApprovisionnementMedicament approvisionnementMedicament = new ApprovisionnementMedicament().quantite(UPDATED_QUANTITE);
        return approvisionnementMedicament;
    }

    @BeforeEach
    public void initTest() {
        approvisionnementMedicament = createEntity(em);
    }

    @Test
    @Transactional
    void createApprovisionnementMedicament() throws Exception {
        int databaseSizeBeforeCreate = approvisionnementMedicamentRepository.findAll().size();
        // Create the ApprovisionnementMedicament
        ApprovisionnementMedicamentDTO approvisionnementMedicamentDTO = approvisionnementMedicamentMapper.toDto(
            approvisionnementMedicament
        );
        restApprovisionnementMedicamentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(approvisionnementMedicamentDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ApprovisionnementMedicament in the database
        List<ApprovisionnementMedicament> approvisionnementMedicamentList = approvisionnementMedicamentRepository.findAll();
        assertThat(approvisionnementMedicamentList).hasSize(databaseSizeBeforeCreate + 1);
        ApprovisionnementMedicament testApprovisionnementMedicament = approvisionnementMedicamentList.get(
            approvisionnementMedicamentList.size() - 1
        );
        assertThat(testApprovisionnementMedicament.getQuantite()).isEqualTo(DEFAULT_QUANTITE);
    }

    @Test
    @Transactional
    void createApprovisionnementMedicamentWithExistingId() throws Exception {
        // Create the ApprovisionnementMedicament with an existing ID
        approvisionnementMedicament.setId(1L);
        ApprovisionnementMedicamentDTO approvisionnementMedicamentDTO = approvisionnementMedicamentMapper.toDto(
            approvisionnementMedicament
        );

        int databaseSizeBeforeCreate = approvisionnementMedicamentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restApprovisionnementMedicamentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(approvisionnementMedicamentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApprovisionnementMedicament in the database
        List<ApprovisionnementMedicament> approvisionnementMedicamentList = approvisionnementMedicamentRepository.findAll();
        assertThat(approvisionnementMedicamentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllApprovisionnementMedicaments() throws Exception {
        // Initialize the database
        approvisionnementMedicamentRepository.saveAndFlush(approvisionnementMedicament);

        // Get all the approvisionnementMedicamentList
        restApprovisionnementMedicamentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(approvisionnementMedicament.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantite").value(hasItem(DEFAULT_QUANTITE.intValue())));
    }

    @Test
    @Transactional
    void getApprovisionnementMedicament() throws Exception {
        // Initialize the database
        approvisionnementMedicamentRepository.saveAndFlush(approvisionnementMedicament);

        // Get the approvisionnementMedicament
        restApprovisionnementMedicamentMockMvc
            .perform(get(ENTITY_API_URL_ID, approvisionnementMedicament.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(approvisionnementMedicament.getId().intValue()))
            .andExpect(jsonPath("$.quantite").value(DEFAULT_QUANTITE.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingApprovisionnementMedicament() throws Exception {
        // Get the approvisionnementMedicament
        restApprovisionnementMedicamentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewApprovisionnementMedicament() throws Exception {
        // Initialize the database
        approvisionnementMedicamentRepository.saveAndFlush(approvisionnementMedicament);

        int databaseSizeBeforeUpdate = approvisionnementMedicamentRepository.findAll().size();

        // Update the approvisionnementMedicament
        ApprovisionnementMedicament updatedApprovisionnementMedicament = approvisionnementMedicamentRepository
            .findById(approvisionnementMedicament.getId())
            .get();
        // Disconnect from session so that the updates on updatedApprovisionnementMedicament are not directly saved in db
        em.detach(updatedApprovisionnementMedicament);
        updatedApprovisionnementMedicament.quantite(UPDATED_QUANTITE);
        ApprovisionnementMedicamentDTO approvisionnementMedicamentDTO = approvisionnementMedicamentMapper.toDto(
            updatedApprovisionnementMedicament
        );

        restApprovisionnementMedicamentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, approvisionnementMedicamentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(approvisionnementMedicamentDTO))
            )
            .andExpect(status().isOk());

        // Validate the ApprovisionnementMedicament in the database
        List<ApprovisionnementMedicament> approvisionnementMedicamentList = approvisionnementMedicamentRepository.findAll();
        assertThat(approvisionnementMedicamentList).hasSize(databaseSizeBeforeUpdate);
        ApprovisionnementMedicament testApprovisionnementMedicament = approvisionnementMedicamentList.get(
            approvisionnementMedicamentList.size() - 1
        );
        assertThat(testApprovisionnementMedicament.getQuantite()).isEqualTo(UPDATED_QUANTITE);
    }

    @Test
    @Transactional
    void putNonExistingApprovisionnementMedicament() throws Exception {
        int databaseSizeBeforeUpdate = approvisionnementMedicamentRepository.findAll().size();
        approvisionnementMedicament.setId(count.incrementAndGet());

        // Create the ApprovisionnementMedicament
        ApprovisionnementMedicamentDTO approvisionnementMedicamentDTO = approvisionnementMedicamentMapper.toDto(
            approvisionnementMedicament
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApprovisionnementMedicamentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, approvisionnementMedicamentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(approvisionnementMedicamentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApprovisionnementMedicament in the database
        List<ApprovisionnementMedicament> approvisionnementMedicamentList = approvisionnementMedicamentRepository.findAll();
        assertThat(approvisionnementMedicamentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchApprovisionnementMedicament() throws Exception {
        int databaseSizeBeforeUpdate = approvisionnementMedicamentRepository.findAll().size();
        approvisionnementMedicament.setId(count.incrementAndGet());

        // Create the ApprovisionnementMedicament
        ApprovisionnementMedicamentDTO approvisionnementMedicamentDTO = approvisionnementMedicamentMapper.toDto(
            approvisionnementMedicament
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApprovisionnementMedicamentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(approvisionnementMedicamentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApprovisionnementMedicament in the database
        List<ApprovisionnementMedicament> approvisionnementMedicamentList = approvisionnementMedicamentRepository.findAll();
        assertThat(approvisionnementMedicamentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamApprovisionnementMedicament() throws Exception {
        int databaseSizeBeforeUpdate = approvisionnementMedicamentRepository.findAll().size();
        approvisionnementMedicament.setId(count.incrementAndGet());

        // Create the ApprovisionnementMedicament
        ApprovisionnementMedicamentDTO approvisionnementMedicamentDTO = approvisionnementMedicamentMapper.toDto(
            approvisionnementMedicament
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApprovisionnementMedicamentMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(approvisionnementMedicamentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ApprovisionnementMedicament in the database
        List<ApprovisionnementMedicament> approvisionnementMedicamentList = approvisionnementMedicamentRepository.findAll();
        assertThat(approvisionnementMedicamentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateApprovisionnementMedicamentWithPatch() throws Exception {
        // Initialize the database
        approvisionnementMedicamentRepository.saveAndFlush(approvisionnementMedicament);

        int databaseSizeBeforeUpdate = approvisionnementMedicamentRepository.findAll().size();

        // Update the approvisionnementMedicament using partial update
        ApprovisionnementMedicament partialUpdatedApprovisionnementMedicament = new ApprovisionnementMedicament();
        partialUpdatedApprovisionnementMedicament.setId(approvisionnementMedicament.getId());

        restApprovisionnementMedicamentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApprovisionnementMedicament.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApprovisionnementMedicament))
            )
            .andExpect(status().isOk());

        // Validate the ApprovisionnementMedicament in the database
        List<ApprovisionnementMedicament> approvisionnementMedicamentList = approvisionnementMedicamentRepository.findAll();
        assertThat(approvisionnementMedicamentList).hasSize(databaseSizeBeforeUpdate);
        ApprovisionnementMedicament testApprovisionnementMedicament = approvisionnementMedicamentList.get(
            approvisionnementMedicamentList.size() - 1
        );
        assertThat(testApprovisionnementMedicament.getQuantite()).isEqualTo(DEFAULT_QUANTITE);
    }

    @Test
    @Transactional
    void fullUpdateApprovisionnementMedicamentWithPatch() throws Exception {
        // Initialize the database
        approvisionnementMedicamentRepository.saveAndFlush(approvisionnementMedicament);

        int databaseSizeBeforeUpdate = approvisionnementMedicamentRepository.findAll().size();

        // Update the approvisionnementMedicament using partial update
        ApprovisionnementMedicament partialUpdatedApprovisionnementMedicament = new ApprovisionnementMedicament();
        partialUpdatedApprovisionnementMedicament.setId(approvisionnementMedicament.getId());

        partialUpdatedApprovisionnementMedicament.quantite(UPDATED_QUANTITE);

        restApprovisionnementMedicamentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApprovisionnementMedicament.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApprovisionnementMedicament))
            )
            .andExpect(status().isOk());

        // Validate the ApprovisionnementMedicament in the database
        List<ApprovisionnementMedicament> approvisionnementMedicamentList = approvisionnementMedicamentRepository.findAll();
        assertThat(approvisionnementMedicamentList).hasSize(databaseSizeBeforeUpdate);
        ApprovisionnementMedicament testApprovisionnementMedicament = approvisionnementMedicamentList.get(
            approvisionnementMedicamentList.size() - 1
        );
        assertThat(testApprovisionnementMedicament.getQuantite()).isEqualTo(UPDATED_QUANTITE);
    }

    @Test
    @Transactional
    void patchNonExistingApprovisionnementMedicament() throws Exception {
        int databaseSizeBeforeUpdate = approvisionnementMedicamentRepository.findAll().size();
        approvisionnementMedicament.setId(count.incrementAndGet());

        // Create the ApprovisionnementMedicament
        ApprovisionnementMedicamentDTO approvisionnementMedicamentDTO = approvisionnementMedicamentMapper.toDto(
            approvisionnementMedicament
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApprovisionnementMedicamentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, approvisionnementMedicamentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(approvisionnementMedicamentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApprovisionnementMedicament in the database
        List<ApprovisionnementMedicament> approvisionnementMedicamentList = approvisionnementMedicamentRepository.findAll();
        assertThat(approvisionnementMedicamentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchApprovisionnementMedicament() throws Exception {
        int databaseSizeBeforeUpdate = approvisionnementMedicamentRepository.findAll().size();
        approvisionnementMedicament.setId(count.incrementAndGet());

        // Create the ApprovisionnementMedicament
        ApprovisionnementMedicamentDTO approvisionnementMedicamentDTO = approvisionnementMedicamentMapper.toDto(
            approvisionnementMedicament
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApprovisionnementMedicamentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(approvisionnementMedicamentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApprovisionnementMedicament in the database
        List<ApprovisionnementMedicament> approvisionnementMedicamentList = approvisionnementMedicamentRepository.findAll();
        assertThat(approvisionnementMedicamentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamApprovisionnementMedicament() throws Exception {
        int databaseSizeBeforeUpdate = approvisionnementMedicamentRepository.findAll().size();
        approvisionnementMedicament.setId(count.incrementAndGet());

        // Create the ApprovisionnementMedicament
        ApprovisionnementMedicamentDTO approvisionnementMedicamentDTO = approvisionnementMedicamentMapper.toDto(
            approvisionnementMedicament
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApprovisionnementMedicamentMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(approvisionnementMedicamentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ApprovisionnementMedicament in the database
        List<ApprovisionnementMedicament> approvisionnementMedicamentList = approvisionnementMedicamentRepository.findAll();
        assertThat(approvisionnementMedicamentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteApprovisionnementMedicament() throws Exception {
        // Initialize the database
        approvisionnementMedicamentRepository.saveAndFlush(approvisionnementMedicament);

        int databaseSizeBeforeDelete = approvisionnementMedicamentRepository.findAll().size();

        // Delete the approvisionnementMedicament
        restApprovisionnementMedicamentMockMvc
            .perform(delete(ENTITY_API_URL_ID, approvisionnementMedicament.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ApprovisionnementMedicament> approvisionnementMedicamentList = approvisionnementMedicamentRepository.findAll();
        assertThat(approvisionnementMedicamentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
