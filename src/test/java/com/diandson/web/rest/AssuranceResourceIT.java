package com.diandson.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.diandson.IntegrationTest;
import com.diandson.domain.Assurance;
import com.diandson.repository.AssuranceRepository;
import com.diandson.service.dto.AssuranceDTO;
import com.diandson.service.mapper.AssuranceMapper;
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
 * Integration tests for the {@link AssuranceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AssuranceResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final Long DEFAULT_TAUX = 1L;
    private static final Long UPDATED_TAUX = 2L;

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/assurances";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AssuranceRepository assuranceRepository;

    @Autowired
    private AssuranceMapper assuranceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAssuranceMockMvc;

    private Assurance assurance;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Assurance createEntity(EntityManager em) {
        Assurance assurance = new Assurance().libelle(DEFAULT_LIBELLE).taux(DEFAULT_TAUX).email(DEFAULT_EMAIL);
        return assurance;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Assurance createUpdatedEntity(EntityManager em) {
        Assurance assurance = new Assurance().libelle(UPDATED_LIBELLE).taux(UPDATED_TAUX).email(UPDATED_EMAIL);
        return assurance;
    }

    @BeforeEach
    public void initTest() {
        assurance = createEntity(em);
    }

    @Test
    @Transactional
    void createAssurance() throws Exception {
        int databaseSizeBeforeCreate = assuranceRepository.findAll().size();
        // Create the Assurance
        AssuranceDTO assuranceDTO = assuranceMapper.toDto(assurance);
        restAssuranceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assuranceDTO)))
            .andExpect(status().isCreated());

        // Validate the Assurance in the database
        List<Assurance> assuranceList = assuranceRepository.findAll();
        assertThat(assuranceList).hasSize(databaseSizeBeforeCreate + 1);
        Assurance testAssurance = assuranceList.get(assuranceList.size() - 1);
        assertThat(testAssurance.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testAssurance.getTaux()).isEqualTo(DEFAULT_TAUX);
        assertThat(testAssurance.getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void createAssuranceWithExistingId() throws Exception {
        // Create the Assurance with an existing ID
        assurance.setId(1L);
        AssuranceDTO assuranceDTO = assuranceMapper.toDto(assurance);

        int databaseSizeBeforeCreate = assuranceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAssuranceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assuranceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Assurance in the database
        List<Assurance> assuranceList = assuranceRepository.findAll();
        assertThat(assuranceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = assuranceRepository.findAll().size();
        // set the field null
        assurance.setLibelle(null);

        // Create the Assurance, which fails.
        AssuranceDTO assuranceDTO = assuranceMapper.toDto(assurance);

        restAssuranceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assuranceDTO)))
            .andExpect(status().isBadRequest());

        List<Assurance> assuranceList = assuranceRepository.findAll();
        assertThat(assuranceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAssurances() throws Exception {
        // Initialize the database
        assuranceRepository.saveAndFlush(assurance);

        // Get all the assuranceList
        restAssuranceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assurance.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].taux").value(hasItem(DEFAULT_TAUX.intValue())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));
    }

    @Test
    @Transactional
    void getAssurance() throws Exception {
        // Initialize the database
        assuranceRepository.saveAndFlush(assurance);

        // Get the assurance
        restAssuranceMockMvc
            .perform(get(ENTITY_API_URL_ID, assurance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(assurance.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.taux").value(DEFAULT_TAUX.intValue()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL));
    }

    @Test
    @Transactional
    void getNonExistingAssurance() throws Exception {
        // Get the assurance
        restAssuranceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAssurance() throws Exception {
        // Initialize the database
        assuranceRepository.saveAndFlush(assurance);

        int databaseSizeBeforeUpdate = assuranceRepository.findAll().size();

        // Update the assurance
        Assurance updatedAssurance = assuranceRepository.findById(assurance.getId()).get();
        // Disconnect from session so that the updates on updatedAssurance are not directly saved in db
        em.detach(updatedAssurance);
        updatedAssurance.libelle(UPDATED_LIBELLE).taux(UPDATED_TAUX).email(UPDATED_EMAIL);
        AssuranceDTO assuranceDTO = assuranceMapper.toDto(updatedAssurance);

        restAssuranceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, assuranceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assuranceDTO))
            )
            .andExpect(status().isOk());

        // Validate the Assurance in the database
        List<Assurance> assuranceList = assuranceRepository.findAll();
        assertThat(assuranceList).hasSize(databaseSizeBeforeUpdate);
        Assurance testAssurance = assuranceList.get(assuranceList.size() - 1);
        assertThat(testAssurance.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testAssurance.getTaux()).isEqualTo(UPDATED_TAUX);
        assertThat(testAssurance.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void putNonExistingAssurance() throws Exception {
        int databaseSizeBeforeUpdate = assuranceRepository.findAll().size();
        assurance.setId(count.incrementAndGet());

        // Create the Assurance
        AssuranceDTO assuranceDTO = assuranceMapper.toDto(assurance);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssuranceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, assuranceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assuranceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Assurance in the database
        List<Assurance> assuranceList = assuranceRepository.findAll();
        assertThat(assuranceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAssurance() throws Exception {
        int databaseSizeBeforeUpdate = assuranceRepository.findAll().size();
        assurance.setId(count.incrementAndGet());

        // Create the Assurance
        AssuranceDTO assuranceDTO = assuranceMapper.toDto(assurance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssuranceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assuranceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Assurance in the database
        List<Assurance> assuranceList = assuranceRepository.findAll();
        assertThat(assuranceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAssurance() throws Exception {
        int databaseSizeBeforeUpdate = assuranceRepository.findAll().size();
        assurance.setId(count.incrementAndGet());

        // Create the Assurance
        AssuranceDTO assuranceDTO = assuranceMapper.toDto(assurance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssuranceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assuranceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Assurance in the database
        List<Assurance> assuranceList = assuranceRepository.findAll();
        assertThat(assuranceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAssuranceWithPatch() throws Exception {
        // Initialize the database
        assuranceRepository.saveAndFlush(assurance);

        int databaseSizeBeforeUpdate = assuranceRepository.findAll().size();

        // Update the assurance using partial update
        Assurance partialUpdatedAssurance = new Assurance();
        partialUpdatedAssurance.setId(assurance.getId());

        restAssuranceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssurance.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAssurance))
            )
            .andExpect(status().isOk());

        // Validate the Assurance in the database
        List<Assurance> assuranceList = assuranceRepository.findAll();
        assertThat(assuranceList).hasSize(databaseSizeBeforeUpdate);
        Assurance testAssurance = assuranceList.get(assuranceList.size() - 1);
        assertThat(testAssurance.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testAssurance.getTaux()).isEqualTo(DEFAULT_TAUX);
        assertThat(testAssurance.getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void fullUpdateAssuranceWithPatch() throws Exception {
        // Initialize the database
        assuranceRepository.saveAndFlush(assurance);

        int databaseSizeBeforeUpdate = assuranceRepository.findAll().size();

        // Update the assurance using partial update
        Assurance partialUpdatedAssurance = new Assurance();
        partialUpdatedAssurance.setId(assurance.getId());

        partialUpdatedAssurance.libelle(UPDATED_LIBELLE).taux(UPDATED_TAUX).email(UPDATED_EMAIL);

        restAssuranceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssurance.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAssurance))
            )
            .andExpect(status().isOk());

        // Validate the Assurance in the database
        List<Assurance> assuranceList = assuranceRepository.findAll();
        assertThat(assuranceList).hasSize(databaseSizeBeforeUpdate);
        Assurance testAssurance = assuranceList.get(assuranceList.size() - 1);
        assertThat(testAssurance.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testAssurance.getTaux()).isEqualTo(UPDATED_TAUX);
        assertThat(testAssurance.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void patchNonExistingAssurance() throws Exception {
        int databaseSizeBeforeUpdate = assuranceRepository.findAll().size();
        assurance.setId(count.incrementAndGet());

        // Create the Assurance
        AssuranceDTO assuranceDTO = assuranceMapper.toDto(assurance);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssuranceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, assuranceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assuranceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Assurance in the database
        List<Assurance> assuranceList = assuranceRepository.findAll();
        assertThat(assuranceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAssurance() throws Exception {
        int databaseSizeBeforeUpdate = assuranceRepository.findAll().size();
        assurance.setId(count.incrementAndGet());

        // Create the Assurance
        AssuranceDTO assuranceDTO = assuranceMapper.toDto(assurance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssuranceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assuranceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Assurance in the database
        List<Assurance> assuranceList = assuranceRepository.findAll();
        assertThat(assuranceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAssurance() throws Exception {
        int databaseSizeBeforeUpdate = assuranceRepository.findAll().size();
        assurance.setId(count.incrementAndGet());

        // Create the Assurance
        AssuranceDTO assuranceDTO = assuranceMapper.toDto(assurance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssuranceMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(assuranceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Assurance in the database
        List<Assurance> assuranceList = assuranceRepository.findAll();
        assertThat(assuranceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAssurance() throws Exception {
        // Initialize the database
        assuranceRepository.saveAndFlush(assurance);

        int databaseSizeBeforeDelete = assuranceRepository.findAll().size();

        // Delete the assurance
        restAssuranceMockMvc
            .perform(delete(ENTITY_API_URL_ID, assurance.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Assurance> assuranceList = assuranceRepository.findAll();
        assertThat(assuranceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
