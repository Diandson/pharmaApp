package com.diandson.web.rest;

import static com.diandson.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.diandson.IntegrationTest;
import com.diandson.domain.Paiement;
import com.diandson.repository.PaiementRepository;
import com.diandson.service.dto.PaiementDTO;
import com.diandson.service.mapper.PaiementMapper;
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
 * Integration tests for the {@link PaiementResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PaiementResourceIT {

    private static final String DEFAULT_NUMERO = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO = "BBBBBBBBBB";

    private static final String DEFAULT_NUMERO_VENTE = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_VENTE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE_PAIEMENT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_PAIEMENT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Long DEFAULT_SOMME_RECU = 1L;
    private static final Long UPDATED_SOMME_RECU = 2L;

    private static final Long DEFAULT_SOMME_DONNER = 1L;
    private static final Long UPDATED_SOMME_DONNER = 2L;

    private static final Long DEFAULT_AVOIR = 1L;
    private static final Long UPDATED_AVOIR = 2L;

    private static final String ENTITY_API_URL = "/api/paiements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PaiementRepository paiementRepository;

    @Autowired
    private PaiementMapper paiementMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaiementMockMvc;

    private Paiement paiement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Paiement createEntity(EntityManager em) {
        Paiement paiement = new Paiement()
            .numero(DEFAULT_NUMERO)
            .numeroVente(DEFAULT_NUMERO_VENTE)
            .datePaiement(DEFAULT_DATE_PAIEMENT)
            .sommeRecu(DEFAULT_SOMME_RECU)
            .sommeDonner(DEFAULT_SOMME_DONNER)
            .avoir(DEFAULT_AVOIR);
        return paiement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Paiement createUpdatedEntity(EntityManager em) {
        Paiement paiement = new Paiement()
            .numero(UPDATED_NUMERO)
            .numeroVente(UPDATED_NUMERO_VENTE)
            .datePaiement(UPDATED_DATE_PAIEMENT)
            .sommeRecu(UPDATED_SOMME_RECU)
            .sommeDonner(UPDATED_SOMME_DONNER)
            .avoir(UPDATED_AVOIR);
        return paiement;
    }

    @BeforeEach
    public void initTest() {
        paiement = createEntity(em);
    }

    @Test
    @Transactional
    void createPaiement() throws Exception {
        int databaseSizeBeforeCreate = paiementRepository.findAll().size();
        // Create the Paiement
        PaiementDTO paiementDTO = paiementMapper.toDto(paiement);
        restPaiementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paiementDTO)))
            .andExpect(status().isCreated());

        // Validate the Paiement in the database
        List<Paiement> paiementList = paiementRepository.findAll();
        assertThat(paiementList).hasSize(databaseSizeBeforeCreate + 1);
        Paiement testPaiement = paiementList.get(paiementList.size() - 1);
        assertThat(testPaiement.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testPaiement.getNumeroVente()).isEqualTo(DEFAULT_NUMERO_VENTE);
        assertThat(testPaiement.getDatePaiement()).isEqualTo(DEFAULT_DATE_PAIEMENT);
        assertThat(testPaiement.getSommeRecu()).isEqualTo(DEFAULT_SOMME_RECU);
        assertThat(testPaiement.getSommeDonner()).isEqualTo(DEFAULT_SOMME_DONNER);
        assertThat(testPaiement.getAvoir()).isEqualTo(DEFAULT_AVOIR);
    }

    @Test
    @Transactional
    void createPaiementWithExistingId() throws Exception {
        // Create the Paiement with an existing ID
        paiement.setId(1L);
        PaiementDTO paiementDTO = paiementMapper.toDto(paiement);

        int databaseSizeBeforeCreate = paiementRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaiementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paiementDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Paiement in the database
        List<Paiement> paiementList = paiementRepository.findAll();
        assertThat(paiementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPaiements() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList
        restPaiementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paiement.getId().intValue())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].numeroVente").value(hasItem(DEFAULT_NUMERO_VENTE)))
            .andExpect(jsonPath("$.[*].datePaiement").value(hasItem(sameInstant(DEFAULT_DATE_PAIEMENT))))
            .andExpect(jsonPath("$.[*].sommeRecu").value(hasItem(DEFAULT_SOMME_RECU.intValue())))
            .andExpect(jsonPath("$.[*].sommeDonner").value(hasItem(DEFAULT_SOMME_DONNER.intValue())))
            .andExpect(jsonPath("$.[*].avoir").value(hasItem(DEFAULT_AVOIR.intValue())));
    }

    @Test
    @Transactional
    void getPaiement() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        // Get the paiement
        restPaiementMockMvc
            .perform(get(ENTITY_API_URL_ID, paiement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paiement.getId().intValue()))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO))
            .andExpect(jsonPath("$.numeroVente").value(DEFAULT_NUMERO_VENTE))
            .andExpect(jsonPath("$.datePaiement").value(sameInstant(DEFAULT_DATE_PAIEMENT)))
            .andExpect(jsonPath("$.sommeRecu").value(DEFAULT_SOMME_RECU.intValue()))
            .andExpect(jsonPath("$.sommeDonner").value(DEFAULT_SOMME_DONNER.intValue()))
            .andExpect(jsonPath("$.avoir").value(DEFAULT_AVOIR.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingPaiement() throws Exception {
        // Get the paiement
        restPaiementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPaiement() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        int databaseSizeBeforeUpdate = paiementRepository.findAll().size();

        // Update the paiement
        Paiement updatedPaiement = paiementRepository.findById(paiement.getId()).get();
        // Disconnect from session so that the updates on updatedPaiement are not directly saved in db
        em.detach(updatedPaiement);
        updatedPaiement
            .numero(UPDATED_NUMERO)
            .numeroVente(UPDATED_NUMERO_VENTE)
            .datePaiement(UPDATED_DATE_PAIEMENT)
            .sommeRecu(UPDATED_SOMME_RECU)
            .sommeDonner(UPDATED_SOMME_DONNER)
            .avoir(UPDATED_AVOIR);
        PaiementDTO paiementDTO = paiementMapper.toDto(updatedPaiement);

        restPaiementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paiementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paiementDTO))
            )
            .andExpect(status().isOk());

        // Validate the Paiement in the database
        List<Paiement> paiementList = paiementRepository.findAll();
        assertThat(paiementList).hasSize(databaseSizeBeforeUpdate);
        Paiement testPaiement = paiementList.get(paiementList.size() - 1);
        assertThat(testPaiement.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testPaiement.getNumeroVente()).isEqualTo(UPDATED_NUMERO_VENTE);
        assertThat(testPaiement.getDatePaiement()).isEqualTo(UPDATED_DATE_PAIEMENT);
        assertThat(testPaiement.getSommeRecu()).isEqualTo(UPDATED_SOMME_RECU);
        assertThat(testPaiement.getSommeDonner()).isEqualTo(UPDATED_SOMME_DONNER);
        assertThat(testPaiement.getAvoir()).isEqualTo(UPDATED_AVOIR);
    }

    @Test
    @Transactional
    void putNonExistingPaiement() throws Exception {
        int databaseSizeBeforeUpdate = paiementRepository.findAll().size();
        paiement.setId(count.incrementAndGet());

        // Create the Paiement
        PaiementDTO paiementDTO = paiementMapper.toDto(paiement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaiementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paiementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paiementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Paiement in the database
        List<Paiement> paiementList = paiementRepository.findAll();
        assertThat(paiementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPaiement() throws Exception {
        int databaseSizeBeforeUpdate = paiementRepository.findAll().size();
        paiement.setId(count.incrementAndGet());

        // Create the Paiement
        PaiementDTO paiementDTO = paiementMapper.toDto(paiement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaiementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paiementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Paiement in the database
        List<Paiement> paiementList = paiementRepository.findAll();
        assertThat(paiementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPaiement() throws Exception {
        int databaseSizeBeforeUpdate = paiementRepository.findAll().size();
        paiement.setId(count.incrementAndGet());

        // Create the Paiement
        PaiementDTO paiementDTO = paiementMapper.toDto(paiement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaiementMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paiementDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Paiement in the database
        List<Paiement> paiementList = paiementRepository.findAll();
        assertThat(paiementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePaiementWithPatch() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        int databaseSizeBeforeUpdate = paiementRepository.findAll().size();

        // Update the paiement using partial update
        Paiement partialUpdatedPaiement = new Paiement();
        partialUpdatedPaiement.setId(paiement.getId());

        partialUpdatedPaiement.numeroVente(UPDATED_NUMERO_VENTE).datePaiement(UPDATED_DATE_PAIEMENT);

        restPaiementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaiement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPaiement))
            )
            .andExpect(status().isOk());

        // Validate the Paiement in the database
        List<Paiement> paiementList = paiementRepository.findAll();
        assertThat(paiementList).hasSize(databaseSizeBeforeUpdate);
        Paiement testPaiement = paiementList.get(paiementList.size() - 1);
        assertThat(testPaiement.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testPaiement.getNumeroVente()).isEqualTo(UPDATED_NUMERO_VENTE);
        assertThat(testPaiement.getDatePaiement()).isEqualTo(UPDATED_DATE_PAIEMENT);
        assertThat(testPaiement.getSommeRecu()).isEqualTo(DEFAULT_SOMME_RECU);
        assertThat(testPaiement.getSommeDonner()).isEqualTo(DEFAULT_SOMME_DONNER);
        assertThat(testPaiement.getAvoir()).isEqualTo(DEFAULT_AVOIR);
    }

    @Test
    @Transactional
    void fullUpdatePaiementWithPatch() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        int databaseSizeBeforeUpdate = paiementRepository.findAll().size();

        // Update the paiement using partial update
        Paiement partialUpdatedPaiement = new Paiement();
        partialUpdatedPaiement.setId(paiement.getId());

        partialUpdatedPaiement
            .numero(UPDATED_NUMERO)
            .numeroVente(UPDATED_NUMERO_VENTE)
            .datePaiement(UPDATED_DATE_PAIEMENT)
            .sommeRecu(UPDATED_SOMME_RECU)
            .sommeDonner(UPDATED_SOMME_DONNER)
            .avoir(UPDATED_AVOIR);

        restPaiementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaiement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPaiement))
            )
            .andExpect(status().isOk());

        // Validate the Paiement in the database
        List<Paiement> paiementList = paiementRepository.findAll();
        assertThat(paiementList).hasSize(databaseSizeBeforeUpdate);
        Paiement testPaiement = paiementList.get(paiementList.size() - 1);
        assertThat(testPaiement.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testPaiement.getNumeroVente()).isEqualTo(UPDATED_NUMERO_VENTE);
        assertThat(testPaiement.getDatePaiement()).isEqualTo(UPDATED_DATE_PAIEMENT);
        assertThat(testPaiement.getSommeRecu()).isEqualTo(UPDATED_SOMME_RECU);
        assertThat(testPaiement.getSommeDonner()).isEqualTo(UPDATED_SOMME_DONNER);
        assertThat(testPaiement.getAvoir()).isEqualTo(UPDATED_AVOIR);
    }

    @Test
    @Transactional
    void patchNonExistingPaiement() throws Exception {
        int databaseSizeBeforeUpdate = paiementRepository.findAll().size();
        paiement.setId(count.incrementAndGet());

        // Create the Paiement
        PaiementDTO paiementDTO = paiementMapper.toDto(paiement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaiementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, paiementDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paiementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Paiement in the database
        List<Paiement> paiementList = paiementRepository.findAll();
        assertThat(paiementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPaiement() throws Exception {
        int databaseSizeBeforeUpdate = paiementRepository.findAll().size();
        paiement.setId(count.incrementAndGet());

        // Create the Paiement
        PaiementDTO paiementDTO = paiementMapper.toDto(paiement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaiementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paiementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Paiement in the database
        List<Paiement> paiementList = paiementRepository.findAll();
        assertThat(paiementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPaiement() throws Exception {
        int databaseSizeBeforeUpdate = paiementRepository.findAll().size();
        paiement.setId(count.incrementAndGet());

        // Create the Paiement
        PaiementDTO paiementDTO = paiementMapper.toDto(paiement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaiementMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(paiementDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Paiement in the database
        List<Paiement> paiementList = paiementRepository.findAll();
        assertThat(paiementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePaiement() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        int databaseSizeBeforeDelete = paiementRepository.findAll().size();

        // Delete the paiement
        restPaiementMockMvc
            .perform(delete(ENTITY_API_URL_ID, paiement.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Paiement> paiementList = paiementRepository.findAll();
        assertThat(paiementList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
