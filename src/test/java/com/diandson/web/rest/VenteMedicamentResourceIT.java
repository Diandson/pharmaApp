package com.diandson.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.diandson.IntegrationTest;
import com.diandson.domain.VenteMedicament;
import com.diandson.repository.VenteMedicamentRepository;
import com.diandson.service.dto.VenteMedicamentDTO;
import com.diandson.service.mapper.VenteMedicamentMapper;
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
 * Integration tests for the {@link VenteMedicamentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VenteMedicamentResourceIT {

    private static final Long DEFAULT_QUANTITE = 1L;
    private static final Long UPDATED_QUANTITE = 2L;

    private static final String DEFAULT_MONTANT = "AAAAAAAAAA";
    private static final String UPDATED_MONTANT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/vente-medicaments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VenteMedicamentRepository venteMedicamentRepository;

    @Autowired
    private VenteMedicamentMapper venteMedicamentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVenteMedicamentMockMvc;

    private VenteMedicament venteMedicament;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VenteMedicament createEntity(EntityManager em) {
        VenteMedicament venteMedicament = new VenteMedicament().quantite(DEFAULT_QUANTITE).montant(DEFAULT_MONTANT);
        return venteMedicament;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VenteMedicament createUpdatedEntity(EntityManager em) {
        VenteMedicament venteMedicament = new VenteMedicament().quantite(UPDATED_QUANTITE).montant(UPDATED_MONTANT);
        return venteMedicament;
    }

    @BeforeEach
    public void initTest() {
        venteMedicament = createEntity(em);
    }

    @Test
    @Transactional
    void createVenteMedicament() throws Exception {
        int databaseSizeBeforeCreate = venteMedicamentRepository.findAll().size();
        // Create the VenteMedicament
        VenteMedicamentDTO venteMedicamentDTO = venteMedicamentMapper.toDto(venteMedicament);
        restVenteMedicamentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(venteMedicamentDTO))
            )
            .andExpect(status().isCreated());

        // Validate the VenteMedicament in the database
        List<VenteMedicament> venteMedicamentList = venteMedicamentRepository.findAll();
        assertThat(venteMedicamentList).hasSize(databaseSizeBeforeCreate + 1);
        VenteMedicament testVenteMedicament = venteMedicamentList.get(venteMedicamentList.size() - 1);
        assertThat(testVenteMedicament.getQuantite()).isEqualTo(DEFAULT_QUANTITE);
        assertThat(testVenteMedicament.getMontant()).isEqualTo(DEFAULT_MONTANT);
    }

    @Test
    @Transactional
    void createVenteMedicamentWithExistingId() throws Exception {
        // Create the VenteMedicament with an existing ID
        venteMedicament.setId(1L);
        VenteMedicamentDTO venteMedicamentDTO = venteMedicamentMapper.toDto(venteMedicament);

        int databaseSizeBeforeCreate = venteMedicamentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVenteMedicamentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(venteMedicamentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VenteMedicament in the database
        List<VenteMedicament> venteMedicamentList = venteMedicamentRepository.findAll();
        assertThat(venteMedicamentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllVenteMedicaments() throws Exception {
        // Initialize the database
        venteMedicamentRepository.saveAndFlush(venteMedicament);

        // Get all the venteMedicamentList
        restVenteMedicamentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(venteMedicament.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantite").value(hasItem(DEFAULT_QUANTITE.intValue())))
            .andExpect(jsonPath("$.[*].montant").value(hasItem(DEFAULT_MONTANT)));
    }

    @Test
    @Transactional
    void getVenteMedicament() throws Exception {
        // Initialize the database
        venteMedicamentRepository.saveAndFlush(venteMedicament);

        // Get the venteMedicament
        restVenteMedicamentMockMvc
            .perform(get(ENTITY_API_URL_ID, venteMedicament.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(venteMedicament.getId().intValue()))
            .andExpect(jsonPath("$.quantite").value(DEFAULT_QUANTITE.intValue()))
            .andExpect(jsonPath("$.montant").value(DEFAULT_MONTANT));
    }

    @Test
    @Transactional
    void getNonExistingVenteMedicament() throws Exception {
        // Get the venteMedicament
        restVenteMedicamentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewVenteMedicament() throws Exception {
        // Initialize the database
        venteMedicamentRepository.saveAndFlush(venteMedicament);

        int databaseSizeBeforeUpdate = venteMedicamentRepository.findAll().size();

        // Update the venteMedicament
        VenteMedicament updatedVenteMedicament = venteMedicamentRepository.findById(venteMedicament.getId()).get();
        // Disconnect from session so that the updates on updatedVenteMedicament are not directly saved in db
        em.detach(updatedVenteMedicament);
        updatedVenteMedicament.quantite(UPDATED_QUANTITE).montant(UPDATED_MONTANT);
        VenteMedicamentDTO venteMedicamentDTO = venteMedicamentMapper.toDto(updatedVenteMedicament);

        restVenteMedicamentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, venteMedicamentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(venteMedicamentDTO))
            )
            .andExpect(status().isOk());

        // Validate the VenteMedicament in the database
        List<VenteMedicament> venteMedicamentList = venteMedicamentRepository.findAll();
        assertThat(venteMedicamentList).hasSize(databaseSizeBeforeUpdate);
        VenteMedicament testVenteMedicament = venteMedicamentList.get(venteMedicamentList.size() - 1);
        assertThat(testVenteMedicament.getQuantite()).isEqualTo(UPDATED_QUANTITE);
        assertThat(testVenteMedicament.getMontant()).isEqualTo(UPDATED_MONTANT);
    }

    @Test
    @Transactional
    void putNonExistingVenteMedicament() throws Exception {
        int databaseSizeBeforeUpdate = venteMedicamentRepository.findAll().size();
        venteMedicament.setId(count.incrementAndGet());

        // Create the VenteMedicament
        VenteMedicamentDTO venteMedicamentDTO = venteMedicamentMapper.toDto(venteMedicament);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVenteMedicamentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, venteMedicamentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(venteMedicamentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VenteMedicament in the database
        List<VenteMedicament> venteMedicamentList = venteMedicamentRepository.findAll();
        assertThat(venteMedicamentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVenteMedicament() throws Exception {
        int databaseSizeBeforeUpdate = venteMedicamentRepository.findAll().size();
        venteMedicament.setId(count.incrementAndGet());

        // Create the VenteMedicament
        VenteMedicamentDTO venteMedicamentDTO = venteMedicamentMapper.toDto(venteMedicament);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVenteMedicamentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(venteMedicamentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VenteMedicament in the database
        List<VenteMedicament> venteMedicamentList = venteMedicamentRepository.findAll();
        assertThat(venteMedicamentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVenteMedicament() throws Exception {
        int databaseSizeBeforeUpdate = venteMedicamentRepository.findAll().size();
        venteMedicament.setId(count.incrementAndGet());

        // Create the VenteMedicament
        VenteMedicamentDTO venteMedicamentDTO = venteMedicamentMapper.toDto(venteMedicament);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVenteMedicamentMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(venteMedicamentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the VenteMedicament in the database
        List<VenteMedicament> venteMedicamentList = venteMedicamentRepository.findAll();
        assertThat(venteMedicamentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVenteMedicamentWithPatch() throws Exception {
        // Initialize the database
        venteMedicamentRepository.saveAndFlush(venteMedicament);

        int databaseSizeBeforeUpdate = venteMedicamentRepository.findAll().size();

        // Update the venteMedicament using partial update
        VenteMedicament partialUpdatedVenteMedicament = new VenteMedicament();
        partialUpdatedVenteMedicament.setId(venteMedicament.getId());

        partialUpdatedVenteMedicament.montant(UPDATED_MONTANT);

        restVenteMedicamentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVenteMedicament.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVenteMedicament))
            )
            .andExpect(status().isOk());

        // Validate the VenteMedicament in the database
        List<VenteMedicament> venteMedicamentList = venteMedicamentRepository.findAll();
        assertThat(venteMedicamentList).hasSize(databaseSizeBeforeUpdate);
        VenteMedicament testVenteMedicament = venteMedicamentList.get(venteMedicamentList.size() - 1);
        assertThat(testVenteMedicament.getQuantite()).isEqualTo(DEFAULT_QUANTITE);
        assertThat(testVenteMedicament.getMontant()).isEqualTo(UPDATED_MONTANT);
    }

    @Test
    @Transactional
    void fullUpdateVenteMedicamentWithPatch() throws Exception {
        // Initialize the database
        venteMedicamentRepository.saveAndFlush(venteMedicament);

        int databaseSizeBeforeUpdate = venteMedicamentRepository.findAll().size();

        // Update the venteMedicament using partial update
        VenteMedicament partialUpdatedVenteMedicament = new VenteMedicament();
        partialUpdatedVenteMedicament.setId(venteMedicament.getId());

        partialUpdatedVenteMedicament.quantite(UPDATED_QUANTITE).montant(UPDATED_MONTANT);

        restVenteMedicamentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVenteMedicament.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVenteMedicament))
            )
            .andExpect(status().isOk());

        // Validate the VenteMedicament in the database
        List<VenteMedicament> venteMedicamentList = venteMedicamentRepository.findAll();
        assertThat(venteMedicamentList).hasSize(databaseSizeBeforeUpdate);
        VenteMedicament testVenteMedicament = venteMedicamentList.get(venteMedicamentList.size() - 1);
        assertThat(testVenteMedicament.getQuantite()).isEqualTo(UPDATED_QUANTITE);
        assertThat(testVenteMedicament.getMontant()).isEqualTo(UPDATED_MONTANT);
    }

    @Test
    @Transactional
    void patchNonExistingVenteMedicament() throws Exception {
        int databaseSizeBeforeUpdate = venteMedicamentRepository.findAll().size();
        venteMedicament.setId(count.incrementAndGet());

        // Create the VenteMedicament
        VenteMedicamentDTO venteMedicamentDTO = venteMedicamentMapper.toDto(venteMedicament);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVenteMedicamentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, venteMedicamentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(venteMedicamentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VenteMedicament in the database
        List<VenteMedicament> venteMedicamentList = venteMedicamentRepository.findAll();
        assertThat(venteMedicamentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVenteMedicament() throws Exception {
        int databaseSizeBeforeUpdate = venteMedicamentRepository.findAll().size();
        venteMedicament.setId(count.incrementAndGet());

        // Create the VenteMedicament
        VenteMedicamentDTO venteMedicamentDTO = venteMedicamentMapper.toDto(venteMedicament);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVenteMedicamentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(venteMedicamentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VenteMedicament in the database
        List<VenteMedicament> venteMedicamentList = venteMedicamentRepository.findAll();
        assertThat(venteMedicamentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVenteMedicament() throws Exception {
        int databaseSizeBeforeUpdate = venteMedicamentRepository.findAll().size();
        venteMedicament.setId(count.incrementAndGet());

        // Create the VenteMedicament
        VenteMedicamentDTO venteMedicamentDTO = venteMedicamentMapper.toDto(venteMedicament);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVenteMedicamentMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(venteMedicamentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the VenteMedicament in the database
        List<VenteMedicament> venteMedicamentList = venteMedicamentRepository.findAll();
        assertThat(venteMedicamentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVenteMedicament() throws Exception {
        // Initialize the database
        venteMedicamentRepository.saveAndFlush(venteMedicament);

        int databaseSizeBeforeDelete = venteMedicamentRepository.findAll().size();

        // Delete the venteMedicament
        restVenteMedicamentMockMvc
            .perform(delete(ENTITY_API_URL_ID, venteMedicament.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<VenteMedicament> venteMedicamentList = venteMedicamentRepository.findAll();
        assertThat(venteMedicamentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
