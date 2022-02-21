package com.diandson.web.rest;

import static com.diandson.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.diandson.IntegrationTest;
import com.diandson.domain.Depense;
import com.diandson.repository.DepenseRepository;
import com.diandson.service.dto.DepenseDTO;
import com.diandson.service.mapper.DepenseMapper;
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
 * Integration tests for the {@link DepenseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DepenseResourceIT {

    private static final String DEFAULT_NUMERO = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO = "BBBBBBBBBB";

    private static final String DEFAULT_MOTIF_DEPENSE = "AAAAAAAAAA";
    private static final String UPDATED_MOTIF_DEPENSE = "BBBBBBBBBB";

    private static final String DEFAULT_ORDONNATEUR = "AAAAAAAAAA";
    private static final String UPDATED_ORDONNATEUR = "BBBBBBBBBB";

    private static final String DEFAULT_JUSTIFICATIF = "AAAAAAAAAA";
    private static final String UPDATED_JUSTIFICATIF = "BBBBBBBBBB";

    private static final Long DEFAULT_MONTANT = 1L;
    private static final Long UPDATED_MONTANT = 2L;

    private static final ZonedDateTime DEFAULT_DATE_DEPENSE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_DEPENSE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/depenses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DepenseRepository depenseRepository;

    @Autowired
    private DepenseMapper depenseMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDepenseMockMvc;

    private Depense depense;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Depense createEntity(EntityManager em) {
        Depense depense = new Depense()
            .numero(DEFAULT_NUMERO)
            .motifDepense(DEFAULT_MOTIF_DEPENSE)
            .ordonnateur(DEFAULT_ORDONNATEUR)
            .justificatif(DEFAULT_JUSTIFICATIF)
            .montant(DEFAULT_MONTANT)
            .dateDepense(DEFAULT_DATE_DEPENSE);
        return depense;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Depense createUpdatedEntity(EntityManager em) {
        Depense depense = new Depense()
            .numero(UPDATED_NUMERO)
            .motifDepense(UPDATED_MOTIF_DEPENSE)
            .ordonnateur(UPDATED_ORDONNATEUR)
            .justificatif(UPDATED_JUSTIFICATIF)
            .montant(UPDATED_MONTANT)
            .dateDepense(UPDATED_DATE_DEPENSE);
        return depense;
    }

    @BeforeEach
    public void initTest() {
        depense = createEntity(em);
    }

    @Test
    @Transactional
    void createDepense() throws Exception {
        int databaseSizeBeforeCreate = depenseRepository.findAll().size();
        // Create the Depense
        DepenseDTO depenseDTO = depenseMapper.toDto(depense);
        restDepenseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(depenseDTO)))
            .andExpect(status().isCreated());

        // Validate the Depense in the database
        List<Depense> depenseList = depenseRepository.findAll();
        assertThat(depenseList).hasSize(databaseSizeBeforeCreate + 1);
        Depense testDepense = depenseList.get(depenseList.size() - 1);
        assertThat(testDepense.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testDepense.getMotifDepense()).isEqualTo(DEFAULT_MOTIF_DEPENSE);
        assertThat(testDepense.getOrdonnateur()).isEqualTo(DEFAULT_ORDONNATEUR);
        assertThat(testDepense.getJustificatif()).isEqualTo(DEFAULT_JUSTIFICATIF);
        assertThat(testDepense.getMontant()).isEqualTo(DEFAULT_MONTANT);
        assertThat(testDepense.getDateDepense()).isEqualTo(DEFAULT_DATE_DEPENSE);
    }

    @Test
    @Transactional
    void createDepenseWithExistingId() throws Exception {
        // Create the Depense with an existing ID
        depense.setId(1L);
        DepenseDTO depenseDTO = depenseMapper.toDto(depense);

        int databaseSizeBeforeCreate = depenseRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDepenseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(depenseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Depense in the database
        List<Depense> depenseList = depenseRepository.findAll();
        assertThat(depenseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDepenses() throws Exception {
        // Initialize the database
        depenseRepository.saveAndFlush(depense);

        // Get all the depenseList
        restDepenseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(depense.getId().intValue())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].motifDepense").value(hasItem(DEFAULT_MOTIF_DEPENSE)))
            .andExpect(jsonPath("$.[*].ordonnateur").value(hasItem(DEFAULT_ORDONNATEUR)))
            .andExpect(jsonPath("$.[*].justificatif").value(hasItem(DEFAULT_JUSTIFICATIF)))
            .andExpect(jsonPath("$.[*].montant").value(hasItem(DEFAULT_MONTANT.intValue())))
            .andExpect(jsonPath("$.[*].dateDepense").value(hasItem(sameInstant(DEFAULT_DATE_DEPENSE))));
    }

    @Test
    @Transactional
    void getDepense() throws Exception {
        // Initialize the database
        depenseRepository.saveAndFlush(depense);

        // Get the depense
        restDepenseMockMvc
            .perform(get(ENTITY_API_URL_ID, depense.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(depense.getId().intValue()))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO))
            .andExpect(jsonPath("$.motifDepense").value(DEFAULT_MOTIF_DEPENSE))
            .andExpect(jsonPath("$.ordonnateur").value(DEFAULT_ORDONNATEUR))
            .andExpect(jsonPath("$.justificatif").value(DEFAULT_JUSTIFICATIF))
            .andExpect(jsonPath("$.montant").value(DEFAULT_MONTANT.intValue()))
            .andExpect(jsonPath("$.dateDepense").value(sameInstant(DEFAULT_DATE_DEPENSE)));
    }

    @Test
    @Transactional
    void getNonExistingDepense() throws Exception {
        // Get the depense
        restDepenseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDepense() throws Exception {
        // Initialize the database
        depenseRepository.saveAndFlush(depense);

        int databaseSizeBeforeUpdate = depenseRepository.findAll().size();

        // Update the depense
        Depense updatedDepense = depenseRepository.findById(depense.getId()).get();
        // Disconnect from session so that the updates on updatedDepense are not directly saved in db
        em.detach(updatedDepense);
        updatedDepense
            .numero(UPDATED_NUMERO)
            .motifDepense(UPDATED_MOTIF_DEPENSE)
            .ordonnateur(UPDATED_ORDONNATEUR)
            .justificatif(UPDATED_JUSTIFICATIF)
            .montant(UPDATED_MONTANT)
            .dateDepense(UPDATED_DATE_DEPENSE);
        DepenseDTO depenseDTO = depenseMapper.toDto(updatedDepense);

        restDepenseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, depenseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(depenseDTO))
            )
            .andExpect(status().isOk());

        // Validate the Depense in the database
        List<Depense> depenseList = depenseRepository.findAll();
        assertThat(depenseList).hasSize(databaseSizeBeforeUpdate);
        Depense testDepense = depenseList.get(depenseList.size() - 1);
        assertThat(testDepense.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testDepense.getMotifDepense()).isEqualTo(UPDATED_MOTIF_DEPENSE);
        assertThat(testDepense.getOrdonnateur()).isEqualTo(UPDATED_ORDONNATEUR);
        assertThat(testDepense.getJustificatif()).isEqualTo(UPDATED_JUSTIFICATIF);
        assertThat(testDepense.getMontant()).isEqualTo(UPDATED_MONTANT);
        assertThat(testDepense.getDateDepense()).isEqualTo(UPDATED_DATE_DEPENSE);
    }

    @Test
    @Transactional
    void putNonExistingDepense() throws Exception {
        int databaseSizeBeforeUpdate = depenseRepository.findAll().size();
        depense.setId(count.incrementAndGet());

        // Create the Depense
        DepenseDTO depenseDTO = depenseMapper.toDto(depense);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepenseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, depenseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(depenseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Depense in the database
        List<Depense> depenseList = depenseRepository.findAll();
        assertThat(depenseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDepense() throws Exception {
        int databaseSizeBeforeUpdate = depenseRepository.findAll().size();
        depense.setId(count.incrementAndGet());

        // Create the Depense
        DepenseDTO depenseDTO = depenseMapper.toDto(depense);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepenseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(depenseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Depense in the database
        List<Depense> depenseList = depenseRepository.findAll();
        assertThat(depenseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDepense() throws Exception {
        int databaseSizeBeforeUpdate = depenseRepository.findAll().size();
        depense.setId(count.incrementAndGet());

        // Create the Depense
        DepenseDTO depenseDTO = depenseMapper.toDto(depense);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepenseMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(depenseDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Depense in the database
        List<Depense> depenseList = depenseRepository.findAll();
        assertThat(depenseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDepenseWithPatch() throws Exception {
        // Initialize the database
        depenseRepository.saveAndFlush(depense);

        int databaseSizeBeforeUpdate = depenseRepository.findAll().size();

        // Update the depense using partial update
        Depense partialUpdatedDepense = new Depense();
        partialUpdatedDepense.setId(depense.getId());

        partialUpdatedDepense
            .numero(UPDATED_NUMERO)
            .motifDepense(UPDATED_MOTIF_DEPENSE)
            .ordonnateur(UPDATED_ORDONNATEUR)
            .justificatif(UPDATED_JUSTIFICATIF)
            .montant(UPDATED_MONTANT);

        restDepenseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDepense.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDepense))
            )
            .andExpect(status().isOk());

        // Validate the Depense in the database
        List<Depense> depenseList = depenseRepository.findAll();
        assertThat(depenseList).hasSize(databaseSizeBeforeUpdate);
        Depense testDepense = depenseList.get(depenseList.size() - 1);
        assertThat(testDepense.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testDepense.getMotifDepense()).isEqualTo(UPDATED_MOTIF_DEPENSE);
        assertThat(testDepense.getOrdonnateur()).isEqualTo(UPDATED_ORDONNATEUR);
        assertThat(testDepense.getJustificatif()).isEqualTo(UPDATED_JUSTIFICATIF);
        assertThat(testDepense.getMontant()).isEqualTo(UPDATED_MONTANT);
        assertThat(testDepense.getDateDepense()).isEqualTo(DEFAULT_DATE_DEPENSE);
    }

    @Test
    @Transactional
    void fullUpdateDepenseWithPatch() throws Exception {
        // Initialize the database
        depenseRepository.saveAndFlush(depense);

        int databaseSizeBeforeUpdate = depenseRepository.findAll().size();

        // Update the depense using partial update
        Depense partialUpdatedDepense = new Depense();
        partialUpdatedDepense.setId(depense.getId());

        partialUpdatedDepense
            .numero(UPDATED_NUMERO)
            .motifDepense(UPDATED_MOTIF_DEPENSE)
            .ordonnateur(UPDATED_ORDONNATEUR)
            .justificatif(UPDATED_JUSTIFICATIF)
            .montant(UPDATED_MONTANT)
            .dateDepense(UPDATED_DATE_DEPENSE);

        restDepenseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDepense.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDepense))
            )
            .andExpect(status().isOk());

        // Validate the Depense in the database
        List<Depense> depenseList = depenseRepository.findAll();
        assertThat(depenseList).hasSize(databaseSizeBeforeUpdate);
        Depense testDepense = depenseList.get(depenseList.size() - 1);
        assertThat(testDepense.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testDepense.getMotifDepense()).isEqualTo(UPDATED_MOTIF_DEPENSE);
        assertThat(testDepense.getOrdonnateur()).isEqualTo(UPDATED_ORDONNATEUR);
        assertThat(testDepense.getJustificatif()).isEqualTo(UPDATED_JUSTIFICATIF);
        assertThat(testDepense.getMontant()).isEqualTo(UPDATED_MONTANT);
        assertThat(testDepense.getDateDepense()).isEqualTo(UPDATED_DATE_DEPENSE);
    }

    @Test
    @Transactional
    void patchNonExistingDepense() throws Exception {
        int databaseSizeBeforeUpdate = depenseRepository.findAll().size();
        depense.setId(count.incrementAndGet());

        // Create the Depense
        DepenseDTO depenseDTO = depenseMapper.toDto(depense);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepenseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, depenseDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(depenseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Depense in the database
        List<Depense> depenseList = depenseRepository.findAll();
        assertThat(depenseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDepense() throws Exception {
        int databaseSizeBeforeUpdate = depenseRepository.findAll().size();
        depense.setId(count.incrementAndGet());

        // Create the Depense
        DepenseDTO depenseDTO = depenseMapper.toDto(depense);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepenseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(depenseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Depense in the database
        List<Depense> depenseList = depenseRepository.findAll();
        assertThat(depenseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDepense() throws Exception {
        int databaseSizeBeforeUpdate = depenseRepository.findAll().size();
        depense.setId(count.incrementAndGet());

        // Create the Depense
        DepenseDTO depenseDTO = depenseMapper.toDto(depense);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepenseMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(depenseDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Depense in the database
        List<Depense> depenseList = depenseRepository.findAll();
        assertThat(depenseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDepense() throws Exception {
        // Initialize the database
        depenseRepository.saveAndFlush(depense);

        int databaseSizeBeforeDelete = depenseRepository.findAll().size();

        // Delete the depense
        restDepenseMockMvc
            .perform(delete(ENTITY_API_URL_ID, depense.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Depense> depenseList = depenseRepository.findAll();
        assertThat(depenseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
