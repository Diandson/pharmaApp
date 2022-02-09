package com.diandson.web.rest;

import static com.diandson.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.diandson.IntegrationTest;
import com.diandson.domain.Vente;
import com.diandson.repository.VenteRepository;
import com.diandson.service.dto.VenteDTO;
import com.diandson.service.mapper.VenteMapper;
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
 * Integration tests for the {@link VenteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VenteResourceIT {

    private static final String DEFAULT_NUMERO = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE_VENTE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_VENTE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Long DEFAULT_MONTANT = 1L;
    private static final Long UPDATED_MONTANT = 2L;

    private static final Long DEFAULT_MONTANT_ASSURANCE = 1L;
    private static final Long UPDATED_MONTANT_ASSURANCE = 2L;

    private static final Long DEFAULT_SOMME_DONNE = 1L;
    private static final Long UPDATED_SOMME_DONNE = 2L;

    private static final Long DEFAULT_SOMME_RENDU = 1L;
    private static final Long UPDATED_SOMME_RENDU = 2L;

    private static final Long DEFAULT_AVOIR = 1L;
    private static final Long UPDATED_AVOIR = 2L;

    private static final String ENTITY_API_URL = "/api/ventes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VenteRepository venteRepository;

    @Autowired
    private VenteMapper venteMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVenteMockMvc;

    private Vente vente;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vente createEntity(EntityManager em) {
        Vente vente = new Vente()
            .numero(DEFAULT_NUMERO)
            .dateVente(DEFAULT_DATE_VENTE)
            .montant(DEFAULT_MONTANT)
            .montantAssurance(DEFAULT_MONTANT_ASSURANCE)
            .sommeDonne(DEFAULT_SOMME_DONNE)
            .sommeRendu(DEFAULT_SOMME_RENDU)
            .avoir(DEFAULT_AVOIR);
        return vente;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vente createUpdatedEntity(EntityManager em) {
        Vente vente = new Vente()
            .numero(UPDATED_NUMERO)
            .dateVente(UPDATED_DATE_VENTE)
            .montant(UPDATED_MONTANT)
            .montantAssurance(UPDATED_MONTANT_ASSURANCE)
            .sommeDonne(UPDATED_SOMME_DONNE)
            .sommeRendu(UPDATED_SOMME_RENDU)
            .avoir(UPDATED_AVOIR);
        return vente;
    }

    @BeforeEach
    public void initTest() {
        vente = createEntity(em);
    }

    @Test
    @Transactional
    void createVente() throws Exception {
        int databaseSizeBeforeCreate = venteRepository.findAll().size();
        // Create the Vente
        VenteDTO venteDTO = venteMapper.toDto(vente);
        restVenteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(venteDTO)))
            .andExpect(status().isCreated());

        // Validate the Vente in the database
        List<Vente> venteList = venteRepository.findAll();
        assertThat(venteList).hasSize(databaseSizeBeforeCreate + 1);
        Vente testVente = venteList.get(venteList.size() - 1);
        assertThat(testVente.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testVente.getDateVente()).isEqualTo(DEFAULT_DATE_VENTE);
        assertThat(testVente.getMontant()).isEqualTo(DEFAULT_MONTANT);
        assertThat(testVente.getMontantAssurance()).isEqualTo(DEFAULT_MONTANT_ASSURANCE);
        assertThat(testVente.getSommeDonne()).isEqualTo(DEFAULT_SOMME_DONNE);
        assertThat(testVente.getSommeRendu()).isEqualTo(DEFAULT_SOMME_RENDU);
        assertThat(testVente.getAvoir()).isEqualTo(DEFAULT_AVOIR);
    }

    @Test
    @Transactional
    void createVenteWithExistingId() throws Exception {
        // Create the Vente with an existing ID
        vente.setId(1L);
        VenteDTO venteDTO = venteMapper.toDto(vente);

        int databaseSizeBeforeCreate = venteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVenteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(venteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Vente in the database
        List<Vente> venteList = venteRepository.findAll();
        assertThat(venteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllVentes() throws Exception {
        // Initialize the database
        venteRepository.saveAndFlush(vente);

        // Get all the venteList
        restVenteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vente.getId().intValue())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].dateVente").value(hasItem(sameInstant(DEFAULT_DATE_VENTE))))
            .andExpect(jsonPath("$.[*].montant").value(hasItem(DEFAULT_MONTANT.intValue())))
            .andExpect(jsonPath("$.[*].montantAssurance").value(hasItem(DEFAULT_MONTANT_ASSURANCE.intValue())))
            .andExpect(jsonPath("$.[*].sommeDonne").value(hasItem(DEFAULT_SOMME_DONNE.intValue())))
            .andExpect(jsonPath("$.[*].sommeRendu").value(hasItem(DEFAULT_SOMME_RENDU.intValue())))
            .andExpect(jsonPath("$.[*].avoir").value(hasItem(DEFAULT_AVOIR.intValue())));
    }

    @Test
    @Transactional
    void getVente() throws Exception {
        // Initialize the database
        venteRepository.saveAndFlush(vente);

        // Get the vente
        restVenteMockMvc
            .perform(get(ENTITY_API_URL_ID, vente.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vente.getId().intValue()))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO))
            .andExpect(jsonPath("$.dateVente").value(sameInstant(DEFAULT_DATE_VENTE)))
            .andExpect(jsonPath("$.montant").value(DEFAULT_MONTANT.intValue()))
            .andExpect(jsonPath("$.montantAssurance").value(DEFAULT_MONTANT_ASSURANCE.intValue()))
            .andExpect(jsonPath("$.sommeDonne").value(DEFAULT_SOMME_DONNE.intValue()))
            .andExpect(jsonPath("$.sommeRendu").value(DEFAULT_SOMME_RENDU.intValue()))
            .andExpect(jsonPath("$.avoir").value(DEFAULT_AVOIR.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingVente() throws Exception {
        // Get the vente
        restVenteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewVente() throws Exception {
        // Initialize the database
        venteRepository.saveAndFlush(vente);

        int databaseSizeBeforeUpdate = venteRepository.findAll().size();

        // Update the vente
        Vente updatedVente = venteRepository.findById(vente.getId()).get();
        // Disconnect from session so that the updates on updatedVente are not directly saved in db
        em.detach(updatedVente);
        updatedVente
            .numero(UPDATED_NUMERO)
            .dateVente(UPDATED_DATE_VENTE)
            .montant(UPDATED_MONTANT)
            .montantAssurance(UPDATED_MONTANT_ASSURANCE)
            .sommeDonne(UPDATED_SOMME_DONNE)
            .sommeRendu(UPDATED_SOMME_RENDU)
            .avoir(UPDATED_AVOIR);
        VenteDTO venteDTO = venteMapper.toDto(updatedVente);

        restVenteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, venteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(venteDTO))
            )
            .andExpect(status().isOk());

        // Validate the Vente in the database
        List<Vente> venteList = venteRepository.findAll();
        assertThat(venteList).hasSize(databaseSizeBeforeUpdate);
        Vente testVente = venteList.get(venteList.size() - 1);
        assertThat(testVente.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testVente.getDateVente()).isEqualTo(UPDATED_DATE_VENTE);
        assertThat(testVente.getMontant()).isEqualTo(UPDATED_MONTANT);
        assertThat(testVente.getMontantAssurance()).isEqualTo(UPDATED_MONTANT_ASSURANCE);
        assertThat(testVente.getSommeDonne()).isEqualTo(UPDATED_SOMME_DONNE);
        assertThat(testVente.getSommeRendu()).isEqualTo(UPDATED_SOMME_RENDU);
        assertThat(testVente.getAvoir()).isEqualTo(UPDATED_AVOIR);
    }

    @Test
    @Transactional
    void putNonExistingVente() throws Exception {
        int databaseSizeBeforeUpdate = venteRepository.findAll().size();
        vente.setId(count.incrementAndGet());

        // Create the Vente
        VenteDTO venteDTO = venteMapper.toDto(vente);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVenteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, venteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(venteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vente in the database
        List<Vente> venteList = venteRepository.findAll();
        assertThat(venteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVente() throws Exception {
        int databaseSizeBeforeUpdate = venteRepository.findAll().size();
        vente.setId(count.incrementAndGet());

        // Create the Vente
        VenteDTO venteDTO = venteMapper.toDto(vente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVenteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(venteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vente in the database
        List<Vente> venteList = venteRepository.findAll();
        assertThat(venteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVente() throws Exception {
        int databaseSizeBeforeUpdate = venteRepository.findAll().size();
        vente.setId(count.incrementAndGet());

        // Create the Vente
        VenteDTO venteDTO = venteMapper.toDto(vente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVenteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(venteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vente in the database
        List<Vente> venteList = venteRepository.findAll();
        assertThat(venteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVenteWithPatch() throws Exception {
        // Initialize the database
        venteRepository.saveAndFlush(vente);

        int databaseSizeBeforeUpdate = venteRepository.findAll().size();

        // Update the vente using partial update
        Vente partialUpdatedVente = new Vente();
        partialUpdatedVente.setId(vente.getId());

        partialUpdatedVente.numero(UPDATED_NUMERO).montant(UPDATED_MONTANT).sommeRendu(UPDATED_SOMME_RENDU);

        restVenteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVente.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVente))
            )
            .andExpect(status().isOk());

        // Validate the Vente in the database
        List<Vente> venteList = venteRepository.findAll();
        assertThat(venteList).hasSize(databaseSizeBeforeUpdate);
        Vente testVente = venteList.get(venteList.size() - 1);
        assertThat(testVente.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testVente.getDateVente()).isEqualTo(DEFAULT_DATE_VENTE);
        assertThat(testVente.getMontant()).isEqualTo(UPDATED_MONTANT);
        assertThat(testVente.getMontantAssurance()).isEqualTo(DEFAULT_MONTANT_ASSURANCE);
        assertThat(testVente.getSommeDonne()).isEqualTo(DEFAULT_SOMME_DONNE);
        assertThat(testVente.getSommeRendu()).isEqualTo(UPDATED_SOMME_RENDU);
        assertThat(testVente.getAvoir()).isEqualTo(DEFAULT_AVOIR);
    }

    @Test
    @Transactional
    void fullUpdateVenteWithPatch() throws Exception {
        // Initialize the database
        venteRepository.saveAndFlush(vente);

        int databaseSizeBeforeUpdate = venteRepository.findAll().size();

        // Update the vente using partial update
        Vente partialUpdatedVente = new Vente();
        partialUpdatedVente.setId(vente.getId());

        partialUpdatedVente
            .numero(UPDATED_NUMERO)
            .dateVente(UPDATED_DATE_VENTE)
            .montant(UPDATED_MONTANT)
            .montantAssurance(UPDATED_MONTANT_ASSURANCE)
            .sommeDonne(UPDATED_SOMME_DONNE)
            .sommeRendu(UPDATED_SOMME_RENDU)
            .avoir(UPDATED_AVOIR);

        restVenteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVente.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVente))
            )
            .andExpect(status().isOk());

        // Validate the Vente in the database
        List<Vente> venteList = venteRepository.findAll();
        assertThat(venteList).hasSize(databaseSizeBeforeUpdate);
        Vente testVente = venteList.get(venteList.size() - 1);
        assertThat(testVente.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testVente.getDateVente()).isEqualTo(UPDATED_DATE_VENTE);
        assertThat(testVente.getMontant()).isEqualTo(UPDATED_MONTANT);
        assertThat(testVente.getMontantAssurance()).isEqualTo(UPDATED_MONTANT_ASSURANCE);
        assertThat(testVente.getSommeDonne()).isEqualTo(UPDATED_SOMME_DONNE);
        assertThat(testVente.getSommeRendu()).isEqualTo(UPDATED_SOMME_RENDU);
        assertThat(testVente.getAvoir()).isEqualTo(UPDATED_AVOIR);
    }

    @Test
    @Transactional
    void patchNonExistingVente() throws Exception {
        int databaseSizeBeforeUpdate = venteRepository.findAll().size();
        vente.setId(count.incrementAndGet());

        // Create the Vente
        VenteDTO venteDTO = venteMapper.toDto(vente);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVenteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, venteDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(venteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vente in the database
        List<Vente> venteList = venteRepository.findAll();
        assertThat(venteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVente() throws Exception {
        int databaseSizeBeforeUpdate = venteRepository.findAll().size();
        vente.setId(count.incrementAndGet());

        // Create the Vente
        VenteDTO venteDTO = venteMapper.toDto(vente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVenteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(venteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vente in the database
        List<Vente> venteList = venteRepository.findAll();
        assertThat(venteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVente() throws Exception {
        int databaseSizeBeforeUpdate = venteRepository.findAll().size();
        vente.setId(count.incrementAndGet());

        // Create the Vente
        VenteDTO venteDTO = venteMapper.toDto(vente);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVenteMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(venteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vente in the database
        List<Vente> venteList = venteRepository.findAll();
        assertThat(venteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVente() throws Exception {
        // Initialize the database
        venteRepository.saveAndFlush(vente);

        int databaseSizeBeforeDelete = venteRepository.findAll().size();

        // Delete the vente
        restVenteMockMvc
            .perform(delete(ENTITY_API_URL_ID, vente.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Vente> venteList = venteRepository.findAll();
        assertThat(venteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
