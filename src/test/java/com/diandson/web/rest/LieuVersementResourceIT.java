package com.diandson.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.diandson.IntegrationTest;
import com.diandson.domain.LieuVersement;
import com.diandson.repository.LieuVersementRepository;
import com.diandson.service.dto.LieuVersementDTO;
import com.diandson.service.mapper.LieuVersementMapper;
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
 * Integration tests for the {@link LieuVersementResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LieuVersementResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/lieu-versements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LieuVersementRepository lieuVersementRepository;

    @Autowired
    private LieuVersementMapper lieuVersementMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLieuVersementMockMvc;

    private LieuVersement lieuVersement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LieuVersement createEntity(EntityManager em) {
        LieuVersement lieuVersement = new LieuVersement().libelle(DEFAULT_LIBELLE);
        return lieuVersement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LieuVersement createUpdatedEntity(EntityManager em) {
        LieuVersement lieuVersement = new LieuVersement().libelle(UPDATED_LIBELLE);
        return lieuVersement;
    }

    @BeforeEach
    public void initTest() {
        lieuVersement = createEntity(em);
    }

    @Test
    @Transactional
    void createLieuVersement() throws Exception {
        int databaseSizeBeforeCreate = lieuVersementRepository.findAll().size();
        // Create the LieuVersement
        LieuVersementDTO lieuVersementDTO = lieuVersementMapper.toDto(lieuVersement);
        restLieuVersementMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lieuVersementDTO))
            )
            .andExpect(status().isCreated());

        // Validate the LieuVersement in the database
        List<LieuVersement> lieuVersementList = lieuVersementRepository.findAll();
        assertThat(lieuVersementList).hasSize(databaseSizeBeforeCreate + 1);
        LieuVersement testLieuVersement = lieuVersementList.get(lieuVersementList.size() - 1);
        assertThat(testLieuVersement.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
    }

    @Test
    @Transactional
    void createLieuVersementWithExistingId() throws Exception {
        // Create the LieuVersement with an existing ID
        lieuVersement.setId(1L);
        LieuVersementDTO lieuVersementDTO = lieuVersementMapper.toDto(lieuVersement);

        int databaseSizeBeforeCreate = lieuVersementRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLieuVersementMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lieuVersementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LieuVersement in the database
        List<LieuVersement> lieuVersementList = lieuVersementRepository.findAll();
        assertThat(lieuVersementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLieuVersements() throws Exception {
        // Initialize the database
        lieuVersementRepository.saveAndFlush(lieuVersement);

        // Get all the lieuVersementList
        restLieuVersementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lieuVersement.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)));
    }

    @Test
    @Transactional
    void getLieuVersement() throws Exception {
        // Initialize the database
        lieuVersementRepository.saveAndFlush(lieuVersement);

        // Get the lieuVersement
        restLieuVersementMockMvc
            .perform(get(ENTITY_API_URL_ID, lieuVersement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(lieuVersement.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE));
    }

    @Test
    @Transactional
    void getNonExistingLieuVersement() throws Exception {
        // Get the lieuVersement
        restLieuVersementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLieuVersement() throws Exception {
        // Initialize the database
        lieuVersementRepository.saveAndFlush(lieuVersement);

        int databaseSizeBeforeUpdate = lieuVersementRepository.findAll().size();

        // Update the lieuVersement
        LieuVersement updatedLieuVersement = lieuVersementRepository.findById(lieuVersement.getId()).get();
        // Disconnect from session so that the updates on updatedLieuVersement are not directly saved in db
        em.detach(updatedLieuVersement);
        updatedLieuVersement.libelle(UPDATED_LIBELLE);
        LieuVersementDTO lieuVersementDTO = lieuVersementMapper.toDto(updatedLieuVersement);

        restLieuVersementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, lieuVersementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lieuVersementDTO))
            )
            .andExpect(status().isOk());

        // Validate the LieuVersement in the database
        List<LieuVersement> lieuVersementList = lieuVersementRepository.findAll();
        assertThat(lieuVersementList).hasSize(databaseSizeBeforeUpdate);
        LieuVersement testLieuVersement = lieuVersementList.get(lieuVersementList.size() - 1);
        assertThat(testLieuVersement.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void putNonExistingLieuVersement() throws Exception {
        int databaseSizeBeforeUpdate = lieuVersementRepository.findAll().size();
        lieuVersement.setId(count.incrementAndGet());

        // Create the LieuVersement
        LieuVersementDTO lieuVersementDTO = lieuVersementMapper.toDto(lieuVersement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLieuVersementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, lieuVersementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lieuVersementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LieuVersement in the database
        List<LieuVersement> lieuVersementList = lieuVersementRepository.findAll();
        assertThat(lieuVersementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLieuVersement() throws Exception {
        int databaseSizeBeforeUpdate = lieuVersementRepository.findAll().size();
        lieuVersement.setId(count.incrementAndGet());

        // Create the LieuVersement
        LieuVersementDTO lieuVersementDTO = lieuVersementMapper.toDto(lieuVersement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLieuVersementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lieuVersementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LieuVersement in the database
        List<LieuVersement> lieuVersementList = lieuVersementRepository.findAll();
        assertThat(lieuVersementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLieuVersement() throws Exception {
        int databaseSizeBeforeUpdate = lieuVersementRepository.findAll().size();
        lieuVersement.setId(count.incrementAndGet());

        // Create the LieuVersement
        LieuVersementDTO lieuVersementDTO = lieuVersementMapper.toDto(lieuVersement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLieuVersementMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lieuVersementDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LieuVersement in the database
        List<LieuVersement> lieuVersementList = lieuVersementRepository.findAll();
        assertThat(lieuVersementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLieuVersementWithPatch() throws Exception {
        // Initialize the database
        lieuVersementRepository.saveAndFlush(lieuVersement);

        int databaseSizeBeforeUpdate = lieuVersementRepository.findAll().size();

        // Update the lieuVersement using partial update
        LieuVersement partialUpdatedLieuVersement = new LieuVersement();
        partialUpdatedLieuVersement.setId(lieuVersement.getId());

        restLieuVersementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLieuVersement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLieuVersement))
            )
            .andExpect(status().isOk());

        // Validate the LieuVersement in the database
        List<LieuVersement> lieuVersementList = lieuVersementRepository.findAll();
        assertThat(lieuVersementList).hasSize(databaseSizeBeforeUpdate);
        LieuVersement testLieuVersement = lieuVersementList.get(lieuVersementList.size() - 1);
        assertThat(testLieuVersement.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
    }

    @Test
    @Transactional
    void fullUpdateLieuVersementWithPatch() throws Exception {
        // Initialize the database
        lieuVersementRepository.saveAndFlush(lieuVersement);

        int databaseSizeBeforeUpdate = lieuVersementRepository.findAll().size();

        // Update the lieuVersement using partial update
        LieuVersement partialUpdatedLieuVersement = new LieuVersement();
        partialUpdatedLieuVersement.setId(lieuVersement.getId());

        partialUpdatedLieuVersement.libelle(UPDATED_LIBELLE);

        restLieuVersementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLieuVersement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLieuVersement))
            )
            .andExpect(status().isOk());

        // Validate the LieuVersement in the database
        List<LieuVersement> lieuVersementList = lieuVersementRepository.findAll();
        assertThat(lieuVersementList).hasSize(databaseSizeBeforeUpdate);
        LieuVersement testLieuVersement = lieuVersementList.get(lieuVersementList.size() - 1);
        assertThat(testLieuVersement.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void patchNonExistingLieuVersement() throws Exception {
        int databaseSizeBeforeUpdate = lieuVersementRepository.findAll().size();
        lieuVersement.setId(count.incrementAndGet());

        // Create the LieuVersement
        LieuVersementDTO lieuVersementDTO = lieuVersementMapper.toDto(lieuVersement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLieuVersementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, lieuVersementDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lieuVersementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LieuVersement in the database
        List<LieuVersement> lieuVersementList = lieuVersementRepository.findAll();
        assertThat(lieuVersementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLieuVersement() throws Exception {
        int databaseSizeBeforeUpdate = lieuVersementRepository.findAll().size();
        lieuVersement.setId(count.incrementAndGet());

        // Create the LieuVersement
        LieuVersementDTO lieuVersementDTO = lieuVersementMapper.toDto(lieuVersement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLieuVersementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lieuVersementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LieuVersement in the database
        List<LieuVersement> lieuVersementList = lieuVersementRepository.findAll();
        assertThat(lieuVersementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLieuVersement() throws Exception {
        int databaseSizeBeforeUpdate = lieuVersementRepository.findAll().size();
        lieuVersement.setId(count.incrementAndGet());

        // Create the LieuVersement
        LieuVersementDTO lieuVersementDTO = lieuVersementMapper.toDto(lieuVersement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLieuVersementMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lieuVersementDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LieuVersement in the database
        List<LieuVersement> lieuVersementList = lieuVersementRepository.findAll();
        assertThat(lieuVersementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLieuVersement() throws Exception {
        // Initialize the database
        lieuVersementRepository.saveAndFlush(lieuVersement);

        int databaseSizeBeforeDelete = lieuVersementRepository.findAll().size();

        // Delete the lieuVersement
        restLieuVersementMockMvc
            .perform(delete(ENTITY_API_URL_ID, lieuVersement.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LieuVersement> lieuVersementList = lieuVersementRepository.findAll();
        assertThat(lieuVersementList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
