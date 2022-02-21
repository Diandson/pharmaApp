package com.diandson.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.diandson.IntegrationTest;
import com.diandson.domain.MotifListeDepense;
import com.diandson.repository.MotifListeDepenseRepository;
import com.diandson.service.dto.MotifListeDepenseDTO;
import com.diandson.service.mapper.MotifListeDepenseMapper;
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
 * Integration tests for the {@link MotifListeDepenseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MotifListeDepenseResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final Long DEFAULT_MONTANT = 1L;
    private static final Long UPDATED_MONTANT = 2L;

    private static final String ENTITY_API_URL = "/api/motif-liste-depenses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MotifListeDepenseRepository motifListeDepenseRepository;

    @Autowired
    private MotifListeDepenseMapper motifListeDepenseMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMotifListeDepenseMockMvc;

    private MotifListeDepense motifListeDepense;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MotifListeDepense createEntity(EntityManager em) {
        MotifListeDepense motifListeDepense = new MotifListeDepense().libelle(DEFAULT_LIBELLE).montant(DEFAULT_MONTANT);
        return motifListeDepense;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MotifListeDepense createUpdatedEntity(EntityManager em) {
        MotifListeDepense motifListeDepense = new MotifListeDepense().libelle(UPDATED_LIBELLE).montant(UPDATED_MONTANT);
        return motifListeDepense;
    }

    @BeforeEach
    public void initTest() {
        motifListeDepense = createEntity(em);
    }

    @Test
    @Transactional
    void createMotifListeDepense() throws Exception {
        int databaseSizeBeforeCreate = motifListeDepenseRepository.findAll().size();
        // Create the MotifListeDepense
        MotifListeDepenseDTO motifListeDepenseDTO = motifListeDepenseMapper.toDto(motifListeDepense);
        restMotifListeDepenseMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(motifListeDepenseDTO))
            )
            .andExpect(status().isCreated());

        // Validate the MotifListeDepense in the database
        List<MotifListeDepense> motifListeDepenseList = motifListeDepenseRepository.findAll();
        assertThat(motifListeDepenseList).hasSize(databaseSizeBeforeCreate + 1);
        MotifListeDepense testMotifListeDepense = motifListeDepenseList.get(motifListeDepenseList.size() - 1);
        assertThat(testMotifListeDepense.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testMotifListeDepense.getMontant()).isEqualTo(DEFAULT_MONTANT);
    }

    @Test
    @Transactional
    void createMotifListeDepenseWithExistingId() throws Exception {
        // Create the MotifListeDepense with an existing ID
        motifListeDepense.setId(1L);
        MotifListeDepenseDTO motifListeDepenseDTO = motifListeDepenseMapper.toDto(motifListeDepense);

        int databaseSizeBeforeCreate = motifListeDepenseRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMotifListeDepenseMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(motifListeDepenseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MotifListeDepense in the database
        List<MotifListeDepense> motifListeDepenseList = motifListeDepenseRepository.findAll();
        assertThat(motifListeDepenseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMotifListeDepenses() throws Exception {
        // Initialize the database
        motifListeDepenseRepository.saveAndFlush(motifListeDepense);

        // Get all the motifListeDepenseList
        restMotifListeDepenseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(motifListeDepense.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].montant").value(hasItem(DEFAULT_MONTANT.intValue())));
    }

    @Test
    @Transactional
    void getMotifListeDepense() throws Exception {
        // Initialize the database
        motifListeDepenseRepository.saveAndFlush(motifListeDepense);

        // Get the motifListeDepense
        restMotifListeDepenseMockMvc
            .perform(get(ENTITY_API_URL_ID, motifListeDepense.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(motifListeDepense.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.montant").value(DEFAULT_MONTANT.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingMotifListeDepense() throws Exception {
        // Get the motifListeDepense
        restMotifListeDepenseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMotifListeDepense() throws Exception {
        // Initialize the database
        motifListeDepenseRepository.saveAndFlush(motifListeDepense);

        int databaseSizeBeforeUpdate = motifListeDepenseRepository.findAll().size();

        // Update the motifListeDepense
        MotifListeDepense updatedMotifListeDepense = motifListeDepenseRepository.findById(motifListeDepense.getId()).get();
        // Disconnect from session so that the updates on updatedMotifListeDepense are not directly saved in db
        em.detach(updatedMotifListeDepense);
        updatedMotifListeDepense.libelle(UPDATED_LIBELLE).montant(UPDATED_MONTANT);
        MotifListeDepenseDTO motifListeDepenseDTO = motifListeDepenseMapper.toDto(updatedMotifListeDepense);

        restMotifListeDepenseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, motifListeDepenseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(motifListeDepenseDTO))
            )
            .andExpect(status().isOk());

        // Validate the MotifListeDepense in the database
        List<MotifListeDepense> motifListeDepenseList = motifListeDepenseRepository.findAll();
        assertThat(motifListeDepenseList).hasSize(databaseSizeBeforeUpdate);
        MotifListeDepense testMotifListeDepense = motifListeDepenseList.get(motifListeDepenseList.size() - 1);
        assertThat(testMotifListeDepense.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testMotifListeDepense.getMontant()).isEqualTo(UPDATED_MONTANT);
    }

    @Test
    @Transactional
    void putNonExistingMotifListeDepense() throws Exception {
        int databaseSizeBeforeUpdate = motifListeDepenseRepository.findAll().size();
        motifListeDepense.setId(count.incrementAndGet());

        // Create the MotifListeDepense
        MotifListeDepenseDTO motifListeDepenseDTO = motifListeDepenseMapper.toDto(motifListeDepense);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMotifListeDepenseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, motifListeDepenseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(motifListeDepenseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MotifListeDepense in the database
        List<MotifListeDepense> motifListeDepenseList = motifListeDepenseRepository.findAll();
        assertThat(motifListeDepenseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMotifListeDepense() throws Exception {
        int databaseSizeBeforeUpdate = motifListeDepenseRepository.findAll().size();
        motifListeDepense.setId(count.incrementAndGet());

        // Create the MotifListeDepense
        MotifListeDepenseDTO motifListeDepenseDTO = motifListeDepenseMapper.toDto(motifListeDepense);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMotifListeDepenseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(motifListeDepenseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MotifListeDepense in the database
        List<MotifListeDepense> motifListeDepenseList = motifListeDepenseRepository.findAll();
        assertThat(motifListeDepenseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMotifListeDepense() throws Exception {
        int databaseSizeBeforeUpdate = motifListeDepenseRepository.findAll().size();
        motifListeDepense.setId(count.incrementAndGet());

        // Create the MotifListeDepense
        MotifListeDepenseDTO motifListeDepenseDTO = motifListeDepenseMapper.toDto(motifListeDepense);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMotifListeDepenseMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(motifListeDepenseDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MotifListeDepense in the database
        List<MotifListeDepense> motifListeDepenseList = motifListeDepenseRepository.findAll();
        assertThat(motifListeDepenseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMotifListeDepenseWithPatch() throws Exception {
        // Initialize the database
        motifListeDepenseRepository.saveAndFlush(motifListeDepense);

        int databaseSizeBeforeUpdate = motifListeDepenseRepository.findAll().size();

        // Update the motifListeDepense using partial update
        MotifListeDepense partialUpdatedMotifListeDepense = new MotifListeDepense();
        partialUpdatedMotifListeDepense.setId(motifListeDepense.getId());

        restMotifListeDepenseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMotifListeDepense.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMotifListeDepense))
            )
            .andExpect(status().isOk());

        // Validate the MotifListeDepense in the database
        List<MotifListeDepense> motifListeDepenseList = motifListeDepenseRepository.findAll();
        assertThat(motifListeDepenseList).hasSize(databaseSizeBeforeUpdate);
        MotifListeDepense testMotifListeDepense = motifListeDepenseList.get(motifListeDepenseList.size() - 1);
        assertThat(testMotifListeDepense.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testMotifListeDepense.getMontant()).isEqualTo(DEFAULT_MONTANT);
    }

    @Test
    @Transactional
    void fullUpdateMotifListeDepenseWithPatch() throws Exception {
        // Initialize the database
        motifListeDepenseRepository.saveAndFlush(motifListeDepense);

        int databaseSizeBeforeUpdate = motifListeDepenseRepository.findAll().size();

        // Update the motifListeDepense using partial update
        MotifListeDepense partialUpdatedMotifListeDepense = new MotifListeDepense();
        partialUpdatedMotifListeDepense.setId(motifListeDepense.getId());

        partialUpdatedMotifListeDepense.libelle(UPDATED_LIBELLE).montant(UPDATED_MONTANT);

        restMotifListeDepenseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMotifListeDepense.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMotifListeDepense))
            )
            .andExpect(status().isOk());

        // Validate the MotifListeDepense in the database
        List<MotifListeDepense> motifListeDepenseList = motifListeDepenseRepository.findAll();
        assertThat(motifListeDepenseList).hasSize(databaseSizeBeforeUpdate);
        MotifListeDepense testMotifListeDepense = motifListeDepenseList.get(motifListeDepenseList.size() - 1);
        assertThat(testMotifListeDepense.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testMotifListeDepense.getMontant()).isEqualTo(UPDATED_MONTANT);
    }

    @Test
    @Transactional
    void patchNonExistingMotifListeDepense() throws Exception {
        int databaseSizeBeforeUpdate = motifListeDepenseRepository.findAll().size();
        motifListeDepense.setId(count.incrementAndGet());

        // Create the MotifListeDepense
        MotifListeDepenseDTO motifListeDepenseDTO = motifListeDepenseMapper.toDto(motifListeDepense);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMotifListeDepenseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, motifListeDepenseDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(motifListeDepenseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MotifListeDepense in the database
        List<MotifListeDepense> motifListeDepenseList = motifListeDepenseRepository.findAll();
        assertThat(motifListeDepenseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMotifListeDepense() throws Exception {
        int databaseSizeBeforeUpdate = motifListeDepenseRepository.findAll().size();
        motifListeDepense.setId(count.incrementAndGet());

        // Create the MotifListeDepense
        MotifListeDepenseDTO motifListeDepenseDTO = motifListeDepenseMapper.toDto(motifListeDepense);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMotifListeDepenseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(motifListeDepenseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MotifListeDepense in the database
        List<MotifListeDepense> motifListeDepenseList = motifListeDepenseRepository.findAll();
        assertThat(motifListeDepenseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMotifListeDepense() throws Exception {
        int databaseSizeBeforeUpdate = motifListeDepenseRepository.findAll().size();
        motifListeDepense.setId(count.incrementAndGet());

        // Create the MotifListeDepense
        MotifListeDepenseDTO motifListeDepenseDTO = motifListeDepenseMapper.toDto(motifListeDepense);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMotifListeDepenseMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(motifListeDepenseDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MotifListeDepense in the database
        List<MotifListeDepense> motifListeDepenseList = motifListeDepenseRepository.findAll();
        assertThat(motifListeDepenseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMotifListeDepense() throws Exception {
        // Initialize the database
        motifListeDepenseRepository.saveAndFlush(motifListeDepense);

        int databaseSizeBeforeDelete = motifListeDepenseRepository.findAll().size();

        // Delete the motifListeDepense
        restMotifListeDepenseMockMvc
            .perform(delete(ENTITY_API_URL_ID, motifListeDepense.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MotifListeDepense> motifListeDepenseList = motifListeDepenseRepository.findAll();
        assertThat(motifListeDepenseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
