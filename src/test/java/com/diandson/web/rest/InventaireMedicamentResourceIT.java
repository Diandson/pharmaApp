package com.diandson.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.diandson.IntegrationTest;
import com.diandson.domain.InventaireMedicament;
import com.diandson.repository.InventaireMedicamentRepository;
import com.diandson.service.dto.InventaireMedicamentDTO;
import com.diandson.service.mapper.InventaireMedicamentMapper;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link InventaireMedicamentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InventaireMedicamentResourceIT {

    private static final Long DEFAULT_STOCK_THEORIQUE = 1L;
    private static final Long UPDATED_STOCK_THEORIQUE = 2L;

    private static final Long DEFAULT_STOCK_PHYSIQUE = 1L;
    private static final Long UPDATED_STOCK_PHYSIQUE = 2L;

    private static final Long DEFAULT_STOCK_DIFFERENT = 1L;
    private static final Long UPDATED_STOCK_DIFFERENT = 2L;

    private static final LocalDate DEFAULT_DATE_FABRICATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FABRICATION = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_EXPIRATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_EXPIRATION = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/inventaire-medicaments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InventaireMedicamentRepository inventaireMedicamentRepository;

    @Autowired
    private InventaireMedicamentMapper inventaireMedicamentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInventaireMedicamentMockMvc;

    private InventaireMedicament inventaireMedicament;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InventaireMedicament createEntity(EntityManager em) {
        InventaireMedicament inventaireMedicament = new InventaireMedicament()
            .stockTheorique(DEFAULT_STOCK_THEORIQUE)
            .stockPhysique(DEFAULT_STOCK_PHYSIQUE)
            .stockDifferent(DEFAULT_STOCK_DIFFERENT)
            .dateFabrication(DEFAULT_DATE_FABRICATION)
            .dateExpiration(DEFAULT_DATE_EXPIRATION);
        return inventaireMedicament;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InventaireMedicament createUpdatedEntity(EntityManager em) {
        InventaireMedicament inventaireMedicament = new InventaireMedicament()
            .stockTheorique(UPDATED_STOCK_THEORIQUE)
            .stockPhysique(UPDATED_STOCK_PHYSIQUE)
            .stockDifferent(UPDATED_STOCK_DIFFERENT)
            .dateFabrication(UPDATED_DATE_FABRICATION)
            .dateExpiration(UPDATED_DATE_EXPIRATION);
        return inventaireMedicament;
    }

    @BeforeEach
    public void initTest() {
        inventaireMedicament = createEntity(em);
    }

    @Test
    @Transactional
    void createInventaireMedicament() throws Exception {
        int databaseSizeBeforeCreate = inventaireMedicamentRepository.findAll().size();
        // Create the InventaireMedicament
        InventaireMedicamentDTO inventaireMedicamentDTO = inventaireMedicamentMapper.toDto(inventaireMedicament);
        restInventaireMedicamentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inventaireMedicamentDTO))
            )
            .andExpect(status().isCreated());

        // Validate the InventaireMedicament in the database
        List<InventaireMedicament> inventaireMedicamentList = inventaireMedicamentRepository.findAll();
        assertThat(inventaireMedicamentList).hasSize(databaseSizeBeforeCreate + 1);
        InventaireMedicament testInventaireMedicament = inventaireMedicamentList.get(inventaireMedicamentList.size() - 1);
        assertThat(testInventaireMedicament.getStockTheorique()).isEqualTo(DEFAULT_STOCK_THEORIQUE);
        assertThat(testInventaireMedicament.getStockPhysique()).isEqualTo(DEFAULT_STOCK_PHYSIQUE);
        assertThat(testInventaireMedicament.getStockDifferent()).isEqualTo(DEFAULT_STOCK_DIFFERENT);
        assertThat(testInventaireMedicament.getDateFabrication()).isEqualTo(DEFAULT_DATE_FABRICATION);
        assertThat(testInventaireMedicament.getDateExpiration()).isEqualTo(DEFAULT_DATE_EXPIRATION);
    }

    @Test
    @Transactional
    void createInventaireMedicamentWithExistingId() throws Exception {
        // Create the InventaireMedicament with an existing ID
        inventaireMedicament.setId(1L);
        InventaireMedicamentDTO inventaireMedicamentDTO = inventaireMedicamentMapper.toDto(inventaireMedicament);

        int databaseSizeBeforeCreate = inventaireMedicamentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInventaireMedicamentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inventaireMedicamentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InventaireMedicament in the database
        List<InventaireMedicament> inventaireMedicamentList = inventaireMedicamentRepository.findAll();
        assertThat(inventaireMedicamentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllInventaireMedicaments() throws Exception {
        // Initialize the database
        inventaireMedicamentRepository.saveAndFlush(inventaireMedicament);

        // Get all the inventaireMedicamentList
        restInventaireMedicamentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inventaireMedicament.getId().intValue())))
            .andExpect(jsonPath("$.[*].stockTheorique").value(hasItem(DEFAULT_STOCK_THEORIQUE.intValue())))
            .andExpect(jsonPath("$.[*].stockPhysique").value(hasItem(DEFAULT_STOCK_PHYSIQUE.intValue())))
            .andExpect(jsonPath("$.[*].stockDifferent").value(hasItem(DEFAULT_STOCK_DIFFERENT.intValue())))
            .andExpect(jsonPath("$.[*].dateFabrication").value(hasItem(DEFAULT_DATE_FABRICATION.toString())))
            .andExpect(jsonPath("$.[*].dateExpiration").value(hasItem(DEFAULT_DATE_EXPIRATION.toString())));
    }

    @Test
    @Transactional
    void getInventaireMedicament() throws Exception {
        // Initialize the database
        inventaireMedicamentRepository.saveAndFlush(inventaireMedicament);

        // Get the inventaireMedicament
        restInventaireMedicamentMockMvc
            .perform(get(ENTITY_API_URL_ID, inventaireMedicament.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(inventaireMedicament.getId().intValue()))
            .andExpect(jsonPath("$.stockTheorique").value(DEFAULT_STOCK_THEORIQUE.intValue()))
            .andExpect(jsonPath("$.stockPhysique").value(DEFAULT_STOCK_PHYSIQUE.intValue()))
            .andExpect(jsonPath("$.stockDifferent").value(DEFAULT_STOCK_DIFFERENT.intValue()))
            .andExpect(jsonPath("$.dateFabrication").value(DEFAULT_DATE_FABRICATION.toString()))
            .andExpect(jsonPath("$.dateExpiration").value(DEFAULT_DATE_EXPIRATION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingInventaireMedicament() throws Exception {
        // Get the inventaireMedicament
        restInventaireMedicamentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewInventaireMedicament() throws Exception {
        // Initialize the database
        inventaireMedicamentRepository.saveAndFlush(inventaireMedicament);

        int databaseSizeBeforeUpdate = inventaireMedicamentRepository.findAll().size();

        // Update the inventaireMedicament
        InventaireMedicament updatedInventaireMedicament = inventaireMedicamentRepository.findById(inventaireMedicament.getId()).get();
        // Disconnect from session so that the updates on updatedInventaireMedicament are not directly saved in db
        em.detach(updatedInventaireMedicament);
        updatedInventaireMedicament
            .stockTheorique(UPDATED_STOCK_THEORIQUE)
            .stockPhysique(UPDATED_STOCK_PHYSIQUE)
            .stockDifferent(UPDATED_STOCK_DIFFERENT)
            .dateFabrication(UPDATED_DATE_FABRICATION)
            .dateExpiration(UPDATED_DATE_EXPIRATION);
        InventaireMedicamentDTO inventaireMedicamentDTO = inventaireMedicamentMapper.toDto(updatedInventaireMedicament);

        restInventaireMedicamentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inventaireMedicamentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inventaireMedicamentDTO))
            )
            .andExpect(status().isOk());

        // Validate the InventaireMedicament in the database
        List<InventaireMedicament> inventaireMedicamentList = inventaireMedicamentRepository.findAll();
        assertThat(inventaireMedicamentList).hasSize(databaseSizeBeforeUpdate);
        InventaireMedicament testInventaireMedicament = inventaireMedicamentList.get(inventaireMedicamentList.size() - 1);
        assertThat(testInventaireMedicament.getStockTheorique()).isEqualTo(UPDATED_STOCK_THEORIQUE);
        assertThat(testInventaireMedicament.getStockPhysique()).isEqualTo(UPDATED_STOCK_PHYSIQUE);
        assertThat(testInventaireMedicament.getStockDifferent()).isEqualTo(UPDATED_STOCK_DIFFERENT);
        assertThat(testInventaireMedicament.getDateFabrication()).isEqualTo(UPDATED_DATE_FABRICATION);
        assertThat(testInventaireMedicament.getDateExpiration()).isEqualTo(UPDATED_DATE_EXPIRATION);
    }

    @Test
    @Transactional
    void putNonExistingInventaireMedicament() throws Exception {
        int databaseSizeBeforeUpdate = inventaireMedicamentRepository.findAll().size();
        inventaireMedicament.setId(count.incrementAndGet());

        // Create the InventaireMedicament
        InventaireMedicamentDTO inventaireMedicamentDTO = inventaireMedicamentMapper.toDto(inventaireMedicament);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInventaireMedicamentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inventaireMedicamentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inventaireMedicamentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InventaireMedicament in the database
        List<InventaireMedicament> inventaireMedicamentList = inventaireMedicamentRepository.findAll();
        assertThat(inventaireMedicamentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInventaireMedicament() throws Exception {
        int databaseSizeBeforeUpdate = inventaireMedicamentRepository.findAll().size();
        inventaireMedicament.setId(count.incrementAndGet());

        // Create the InventaireMedicament
        InventaireMedicamentDTO inventaireMedicamentDTO = inventaireMedicamentMapper.toDto(inventaireMedicament);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventaireMedicamentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inventaireMedicamentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InventaireMedicament in the database
        List<InventaireMedicament> inventaireMedicamentList = inventaireMedicamentRepository.findAll();
        assertThat(inventaireMedicamentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInventaireMedicament() throws Exception {
        int databaseSizeBeforeUpdate = inventaireMedicamentRepository.findAll().size();
        inventaireMedicament.setId(count.incrementAndGet());

        // Create the InventaireMedicament
        InventaireMedicamentDTO inventaireMedicamentDTO = inventaireMedicamentMapper.toDto(inventaireMedicament);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventaireMedicamentMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inventaireMedicamentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InventaireMedicament in the database
        List<InventaireMedicament> inventaireMedicamentList = inventaireMedicamentRepository.findAll();
        assertThat(inventaireMedicamentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInventaireMedicamentWithPatch() throws Exception {
        // Initialize the database
        inventaireMedicamentRepository.saveAndFlush(inventaireMedicament);

        int databaseSizeBeforeUpdate = inventaireMedicamentRepository.findAll().size();

        // Update the inventaireMedicament using partial update
        InventaireMedicament partialUpdatedInventaireMedicament = new InventaireMedicament();
        partialUpdatedInventaireMedicament.setId(inventaireMedicament.getId());

        partialUpdatedInventaireMedicament
            .stockTheorique(UPDATED_STOCK_THEORIQUE)
            .stockDifferent(UPDATED_STOCK_DIFFERENT)
            .dateExpiration(UPDATED_DATE_EXPIRATION);

        restInventaireMedicamentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInventaireMedicament.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInventaireMedicament))
            )
            .andExpect(status().isOk());

        // Validate the InventaireMedicament in the database
        List<InventaireMedicament> inventaireMedicamentList = inventaireMedicamentRepository.findAll();
        assertThat(inventaireMedicamentList).hasSize(databaseSizeBeforeUpdate);
        InventaireMedicament testInventaireMedicament = inventaireMedicamentList.get(inventaireMedicamentList.size() - 1);
        assertThat(testInventaireMedicament.getStockTheorique()).isEqualTo(UPDATED_STOCK_THEORIQUE);
        assertThat(testInventaireMedicament.getStockPhysique()).isEqualTo(DEFAULT_STOCK_PHYSIQUE);
        assertThat(testInventaireMedicament.getStockDifferent()).isEqualTo(UPDATED_STOCK_DIFFERENT);
        assertThat(testInventaireMedicament.getDateFabrication()).isEqualTo(DEFAULT_DATE_FABRICATION);
        assertThat(testInventaireMedicament.getDateExpiration()).isEqualTo(UPDATED_DATE_EXPIRATION);
    }

    @Test
    @Transactional
    void fullUpdateInventaireMedicamentWithPatch() throws Exception {
        // Initialize the database
        inventaireMedicamentRepository.saveAndFlush(inventaireMedicament);

        int databaseSizeBeforeUpdate = inventaireMedicamentRepository.findAll().size();

        // Update the inventaireMedicament using partial update
        InventaireMedicament partialUpdatedInventaireMedicament = new InventaireMedicament();
        partialUpdatedInventaireMedicament.setId(inventaireMedicament.getId());

        partialUpdatedInventaireMedicament
            .stockTheorique(UPDATED_STOCK_THEORIQUE)
            .stockPhysique(UPDATED_STOCK_PHYSIQUE)
            .stockDifferent(UPDATED_STOCK_DIFFERENT)
            .dateFabrication(UPDATED_DATE_FABRICATION)
            .dateExpiration(UPDATED_DATE_EXPIRATION);

        restInventaireMedicamentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInventaireMedicament.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInventaireMedicament))
            )
            .andExpect(status().isOk());

        // Validate the InventaireMedicament in the database
        List<InventaireMedicament> inventaireMedicamentList = inventaireMedicamentRepository.findAll();
        assertThat(inventaireMedicamentList).hasSize(databaseSizeBeforeUpdate);
        InventaireMedicament testInventaireMedicament = inventaireMedicamentList.get(inventaireMedicamentList.size() - 1);
        assertThat(testInventaireMedicament.getStockTheorique()).isEqualTo(UPDATED_STOCK_THEORIQUE);
        assertThat(testInventaireMedicament.getStockPhysique()).isEqualTo(UPDATED_STOCK_PHYSIQUE);
        assertThat(testInventaireMedicament.getStockDifferent()).isEqualTo(UPDATED_STOCK_DIFFERENT);
        assertThat(testInventaireMedicament.getDateFabrication()).isEqualTo(UPDATED_DATE_FABRICATION);
        assertThat(testInventaireMedicament.getDateExpiration()).isEqualTo(UPDATED_DATE_EXPIRATION);
    }

    @Test
    @Transactional
    void patchNonExistingInventaireMedicament() throws Exception {
        int databaseSizeBeforeUpdate = inventaireMedicamentRepository.findAll().size();
        inventaireMedicament.setId(count.incrementAndGet());

        // Create the InventaireMedicament
        InventaireMedicamentDTO inventaireMedicamentDTO = inventaireMedicamentMapper.toDto(inventaireMedicament);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInventaireMedicamentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, inventaireMedicamentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(inventaireMedicamentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InventaireMedicament in the database
        List<InventaireMedicament> inventaireMedicamentList = inventaireMedicamentRepository.findAll();
        assertThat(inventaireMedicamentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInventaireMedicament() throws Exception {
        int databaseSizeBeforeUpdate = inventaireMedicamentRepository.findAll().size();
        inventaireMedicament.setId(count.incrementAndGet());

        // Create the InventaireMedicament
        InventaireMedicamentDTO inventaireMedicamentDTO = inventaireMedicamentMapper.toDto(inventaireMedicament);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventaireMedicamentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(inventaireMedicamentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InventaireMedicament in the database
        List<InventaireMedicament> inventaireMedicamentList = inventaireMedicamentRepository.findAll();
        assertThat(inventaireMedicamentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInventaireMedicament() throws Exception {
        int databaseSizeBeforeUpdate = inventaireMedicamentRepository.findAll().size();
        inventaireMedicament.setId(count.incrementAndGet());

        // Create the InventaireMedicament
        InventaireMedicamentDTO inventaireMedicamentDTO = inventaireMedicamentMapper.toDto(inventaireMedicament);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventaireMedicamentMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(inventaireMedicamentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InventaireMedicament in the database
        List<InventaireMedicament> inventaireMedicamentList = inventaireMedicamentRepository.findAll();
        assertThat(inventaireMedicamentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInventaireMedicament() throws Exception {
        // Initialize the database
        inventaireMedicamentRepository.saveAndFlush(inventaireMedicament);

        int databaseSizeBeforeDelete = inventaireMedicamentRepository.findAll().size();

        // Delete the inventaireMedicament
        restInventaireMedicamentMockMvc
            .perform(delete(ENTITY_API_URL_ID, inventaireMedicament.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<InventaireMedicament> inventaireMedicamentList = inventaireMedicamentRepository.findAll();
        assertThat(inventaireMedicamentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
