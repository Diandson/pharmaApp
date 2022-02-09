package com.diandson.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.diandson.IntegrationTest;
import com.diandson.domain.Inventaire;
import com.diandson.repository.InventaireRepository;
import com.diandson.service.dto.InventaireDTO;
import com.diandson.service.mapper.InventaireMapper;
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
 * Integration tests for the {@link InventaireResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InventaireResourceIT {

    private static final String DEFAULT_NUMERO = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO = "BBBBBBBBBB";

    private static final String DEFAULT_DATE_INVENTAIRE = "AAAAAAAAAA";
    private static final String UPDATED_DATE_INVENTAIRE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/inventaires";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InventaireRepository inventaireRepository;

    @Autowired
    private InventaireMapper inventaireMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInventaireMockMvc;

    private Inventaire inventaire;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Inventaire createEntity(EntityManager em) {
        Inventaire inventaire = new Inventaire().numero(DEFAULT_NUMERO).dateInventaire(DEFAULT_DATE_INVENTAIRE);
        return inventaire;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Inventaire createUpdatedEntity(EntityManager em) {
        Inventaire inventaire = new Inventaire().numero(UPDATED_NUMERO).dateInventaire(UPDATED_DATE_INVENTAIRE);
        return inventaire;
    }

    @BeforeEach
    public void initTest() {
        inventaire = createEntity(em);
    }

    @Test
    @Transactional
    void createInventaire() throws Exception {
        int databaseSizeBeforeCreate = inventaireRepository.findAll().size();
        // Create the Inventaire
        InventaireDTO inventaireDTO = inventaireMapper.toDto(inventaire);
        restInventaireMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(inventaireDTO)))
            .andExpect(status().isCreated());

        // Validate the Inventaire in the database
        List<Inventaire> inventaireList = inventaireRepository.findAll();
        assertThat(inventaireList).hasSize(databaseSizeBeforeCreate + 1);
        Inventaire testInventaire = inventaireList.get(inventaireList.size() - 1);
        assertThat(testInventaire.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testInventaire.getDateInventaire()).isEqualTo(DEFAULT_DATE_INVENTAIRE);
    }

    @Test
    @Transactional
    void createInventaireWithExistingId() throws Exception {
        // Create the Inventaire with an existing ID
        inventaire.setId(1L);
        InventaireDTO inventaireDTO = inventaireMapper.toDto(inventaire);

        int databaseSizeBeforeCreate = inventaireRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInventaireMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(inventaireDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Inventaire in the database
        List<Inventaire> inventaireList = inventaireRepository.findAll();
        assertThat(inventaireList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllInventaires() throws Exception {
        // Initialize the database
        inventaireRepository.saveAndFlush(inventaire);

        // Get all the inventaireList
        restInventaireMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inventaire.getId().intValue())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].dateInventaire").value(hasItem(DEFAULT_DATE_INVENTAIRE)));
    }

    @Test
    @Transactional
    void getInventaire() throws Exception {
        // Initialize the database
        inventaireRepository.saveAndFlush(inventaire);

        // Get the inventaire
        restInventaireMockMvc
            .perform(get(ENTITY_API_URL_ID, inventaire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(inventaire.getId().intValue()))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO))
            .andExpect(jsonPath("$.dateInventaire").value(DEFAULT_DATE_INVENTAIRE));
    }

    @Test
    @Transactional
    void getNonExistingInventaire() throws Exception {
        // Get the inventaire
        restInventaireMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewInventaire() throws Exception {
        // Initialize the database
        inventaireRepository.saveAndFlush(inventaire);

        int databaseSizeBeforeUpdate = inventaireRepository.findAll().size();

        // Update the inventaire
        Inventaire updatedInventaire = inventaireRepository.findById(inventaire.getId()).get();
        // Disconnect from session so that the updates on updatedInventaire are not directly saved in db
        em.detach(updatedInventaire);
        updatedInventaire.numero(UPDATED_NUMERO).dateInventaire(UPDATED_DATE_INVENTAIRE);
        InventaireDTO inventaireDTO = inventaireMapper.toDto(updatedInventaire);

        restInventaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inventaireDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inventaireDTO))
            )
            .andExpect(status().isOk());

        // Validate the Inventaire in the database
        List<Inventaire> inventaireList = inventaireRepository.findAll();
        assertThat(inventaireList).hasSize(databaseSizeBeforeUpdate);
        Inventaire testInventaire = inventaireList.get(inventaireList.size() - 1);
        assertThat(testInventaire.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testInventaire.getDateInventaire()).isEqualTo(UPDATED_DATE_INVENTAIRE);
    }

    @Test
    @Transactional
    void putNonExistingInventaire() throws Exception {
        int databaseSizeBeforeUpdate = inventaireRepository.findAll().size();
        inventaire.setId(count.incrementAndGet());

        // Create the Inventaire
        InventaireDTO inventaireDTO = inventaireMapper.toDto(inventaire);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInventaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inventaireDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inventaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inventaire in the database
        List<Inventaire> inventaireList = inventaireRepository.findAll();
        assertThat(inventaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInventaire() throws Exception {
        int databaseSizeBeforeUpdate = inventaireRepository.findAll().size();
        inventaire.setId(count.incrementAndGet());

        // Create the Inventaire
        InventaireDTO inventaireDTO = inventaireMapper.toDto(inventaire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inventaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inventaire in the database
        List<Inventaire> inventaireList = inventaireRepository.findAll();
        assertThat(inventaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInventaire() throws Exception {
        int databaseSizeBeforeUpdate = inventaireRepository.findAll().size();
        inventaire.setId(count.incrementAndGet());

        // Create the Inventaire
        InventaireDTO inventaireDTO = inventaireMapper.toDto(inventaire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventaireMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(inventaireDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Inventaire in the database
        List<Inventaire> inventaireList = inventaireRepository.findAll();
        assertThat(inventaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInventaireWithPatch() throws Exception {
        // Initialize the database
        inventaireRepository.saveAndFlush(inventaire);

        int databaseSizeBeforeUpdate = inventaireRepository.findAll().size();

        // Update the inventaire using partial update
        Inventaire partialUpdatedInventaire = new Inventaire();
        partialUpdatedInventaire.setId(inventaire.getId());

        restInventaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInventaire.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInventaire))
            )
            .andExpect(status().isOk());

        // Validate the Inventaire in the database
        List<Inventaire> inventaireList = inventaireRepository.findAll();
        assertThat(inventaireList).hasSize(databaseSizeBeforeUpdate);
        Inventaire testInventaire = inventaireList.get(inventaireList.size() - 1);
        assertThat(testInventaire.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testInventaire.getDateInventaire()).isEqualTo(DEFAULT_DATE_INVENTAIRE);
    }

    @Test
    @Transactional
    void fullUpdateInventaireWithPatch() throws Exception {
        // Initialize the database
        inventaireRepository.saveAndFlush(inventaire);

        int databaseSizeBeforeUpdate = inventaireRepository.findAll().size();

        // Update the inventaire using partial update
        Inventaire partialUpdatedInventaire = new Inventaire();
        partialUpdatedInventaire.setId(inventaire.getId());

        partialUpdatedInventaire.numero(UPDATED_NUMERO).dateInventaire(UPDATED_DATE_INVENTAIRE);

        restInventaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInventaire.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInventaire))
            )
            .andExpect(status().isOk());

        // Validate the Inventaire in the database
        List<Inventaire> inventaireList = inventaireRepository.findAll();
        assertThat(inventaireList).hasSize(databaseSizeBeforeUpdate);
        Inventaire testInventaire = inventaireList.get(inventaireList.size() - 1);
        assertThat(testInventaire.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testInventaire.getDateInventaire()).isEqualTo(UPDATED_DATE_INVENTAIRE);
    }

    @Test
    @Transactional
    void patchNonExistingInventaire() throws Exception {
        int databaseSizeBeforeUpdate = inventaireRepository.findAll().size();
        inventaire.setId(count.incrementAndGet());

        // Create the Inventaire
        InventaireDTO inventaireDTO = inventaireMapper.toDto(inventaire);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInventaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, inventaireDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(inventaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inventaire in the database
        List<Inventaire> inventaireList = inventaireRepository.findAll();
        assertThat(inventaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInventaire() throws Exception {
        int databaseSizeBeforeUpdate = inventaireRepository.findAll().size();
        inventaire.setId(count.incrementAndGet());

        // Create the Inventaire
        InventaireDTO inventaireDTO = inventaireMapper.toDto(inventaire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(inventaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inventaire in the database
        List<Inventaire> inventaireList = inventaireRepository.findAll();
        assertThat(inventaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInventaire() throws Exception {
        int databaseSizeBeforeUpdate = inventaireRepository.findAll().size();
        inventaire.setId(count.incrementAndGet());

        // Create the Inventaire
        InventaireDTO inventaireDTO = inventaireMapper.toDto(inventaire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventaireMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(inventaireDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Inventaire in the database
        List<Inventaire> inventaireList = inventaireRepository.findAll();
        assertThat(inventaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInventaire() throws Exception {
        // Initialize the database
        inventaireRepository.saveAndFlush(inventaire);

        int databaseSizeBeforeDelete = inventaireRepository.findAll().size();

        // Delete the inventaire
        restInventaireMockMvc
            .perform(delete(ENTITY_API_URL_ID, inventaire.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Inventaire> inventaireList = inventaireRepository.findAll();
        assertThat(inventaireList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
