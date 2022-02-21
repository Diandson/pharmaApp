package com.diandson.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.diandson.IntegrationTest;
import com.diandson.domain.Versement;
import com.diandson.repository.VersementRepository;
import com.diandson.service.dto.VersementDTO;
import com.diandson.service.mapper.VersementMapper;
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
 * Integration tests for the {@link VersementResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VersementResourceIT {

    private static final String DEFAULT_NUMERO = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENTAIRE = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTAIRE = "BBBBBBBBBB";

    private static final Long DEFAULT_MONTANT = 1L;
    private static final Long UPDATED_MONTANT = 2L;

    private static final Long DEFAULT_RESTE_A_VERSER = 1L;
    private static final Long UPDATED_RESTE_A_VERSER = 2L;

    private static final String DEFAULT_LIEU_VERSEMENT = "AAAAAAAAAA";
    private static final String UPDATED_LIEU_VERSEMENT = "BBBBBBBBBB";

    private static final String DEFAULT_REFERENCE_VERSEMENT = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE_VERSEMENT = "BBBBBBBBBB";

    private static final String DEFAULT_IDENTITE_RECEVEUR = "AAAAAAAAAA";
    private static final String UPDATED_IDENTITE_RECEVEUR = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/versements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VersementRepository versementRepository;

    @Autowired
    private VersementMapper versementMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVersementMockMvc;

    private Versement versement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Versement createEntity(EntityManager em) {
        Versement versement = new Versement()
            .numero(DEFAULT_NUMERO)
            .commentaire(DEFAULT_COMMENTAIRE)
            .montant(DEFAULT_MONTANT)
            .resteAVerser(DEFAULT_RESTE_A_VERSER)
            .lieuVersement(DEFAULT_LIEU_VERSEMENT)
            .referenceVersement(DEFAULT_REFERENCE_VERSEMENT)
            .identiteReceveur(DEFAULT_IDENTITE_RECEVEUR);
        return versement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Versement createUpdatedEntity(EntityManager em) {
        Versement versement = new Versement()
            .numero(UPDATED_NUMERO)
            .commentaire(UPDATED_COMMENTAIRE)
            .montant(UPDATED_MONTANT)
            .resteAVerser(UPDATED_RESTE_A_VERSER)
            .lieuVersement(UPDATED_LIEU_VERSEMENT)
            .referenceVersement(UPDATED_REFERENCE_VERSEMENT)
            .identiteReceveur(UPDATED_IDENTITE_RECEVEUR);
        return versement;
    }

    @BeforeEach
    public void initTest() {
        versement = createEntity(em);
    }

    @Test
    @Transactional
    void createVersement() throws Exception {
        int databaseSizeBeforeCreate = versementRepository.findAll().size();
        // Create the Versement
        VersementDTO versementDTO = versementMapper.toDto(versement);
        restVersementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(versementDTO)))
            .andExpect(status().isCreated());

        // Validate the Versement in the database
        List<Versement> versementList = versementRepository.findAll();
        assertThat(versementList).hasSize(databaseSizeBeforeCreate + 1);
        Versement testVersement = versementList.get(versementList.size() - 1);
        assertThat(testVersement.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testVersement.getCommentaire()).isEqualTo(DEFAULT_COMMENTAIRE);
        assertThat(testVersement.getMontant()).isEqualTo(DEFAULT_MONTANT);
        assertThat(testVersement.getResteAVerser()).isEqualTo(DEFAULT_RESTE_A_VERSER);
        assertThat(testVersement.getLieuVersement()).isEqualTo(DEFAULT_LIEU_VERSEMENT);
        assertThat(testVersement.getReferenceVersement()).isEqualTo(DEFAULT_REFERENCE_VERSEMENT);
        assertThat(testVersement.getIdentiteReceveur()).isEqualTo(DEFAULT_IDENTITE_RECEVEUR);
    }

    @Test
    @Transactional
    void createVersementWithExistingId() throws Exception {
        // Create the Versement with an existing ID
        versement.setId(1L);
        VersementDTO versementDTO = versementMapper.toDto(versement);

        int databaseSizeBeforeCreate = versementRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVersementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(versementDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Versement in the database
        List<Versement> versementList = versementRepository.findAll();
        assertThat(versementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllVersements() throws Exception {
        // Initialize the database
        versementRepository.saveAndFlush(versement);

        // Get all the versementList
        restVersementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(versement.getId().intValue())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].commentaire").value(hasItem(DEFAULT_COMMENTAIRE)))
            .andExpect(jsonPath("$.[*].montant").value(hasItem(DEFAULT_MONTANT.intValue())))
            .andExpect(jsonPath("$.[*].resteAVerser").value(hasItem(DEFAULT_RESTE_A_VERSER.intValue())))
            .andExpect(jsonPath("$.[*].lieuVersement").value(hasItem(DEFAULT_LIEU_VERSEMENT)))
            .andExpect(jsonPath("$.[*].referenceVersement").value(hasItem(DEFAULT_REFERENCE_VERSEMENT)))
            .andExpect(jsonPath("$.[*].identiteReceveur").value(hasItem(DEFAULT_IDENTITE_RECEVEUR)));
    }

    @Test
    @Transactional
    void getVersement() throws Exception {
        // Initialize the database
        versementRepository.saveAndFlush(versement);

        // Get the versement
        restVersementMockMvc
            .perform(get(ENTITY_API_URL_ID, versement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(versement.getId().intValue()))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO))
            .andExpect(jsonPath("$.commentaire").value(DEFAULT_COMMENTAIRE))
            .andExpect(jsonPath("$.montant").value(DEFAULT_MONTANT.intValue()))
            .andExpect(jsonPath("$.resteAVerser").value(DEFAULT_RESTE_A_VERSER.intValue()))
            .andExpect(jsonPath("$.lieuVersement").value(DEFAULT_LIEU_VERSEMENT))
            .andExpect(jsonPath("$.referenceVersement").value(DEFAULT_REFERENCE_VERSEMENT))
            .andExpect(jsonPath("$.identiteReceveur").value(DEFAULT_IDENTITE_RECEVEUR));
    }

    @Test
    @Transactional
    void getNonExistingVersement() throws Exception {
        // Get the versement
        restVersementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewVersement() throws Exception {
        // Initialize the database
        versementRepository.saveAndFlush(versement);

        int databaseSizeBeforeUpdate = versementRepository.findAll().size();

        // Update the versement
        Versement updatedVersement = versementRepository.findById(versement.getId()).get();
        // Disconnect from session so that the updates on updatedVersement are not directly saved in db
        em.detach(updatedVersement);
        updatedVersement
            .numero(UPDATED_NUMERO)
            .commentaire(UPDATED_COMMENTAIRE)
            .montant(UPDATED_MONTANT)
            .resteAVerser(UPDATED_RESTE_A_VERSER)
            .lieuVersement(UPDATED_LIEU_VERSEMENT)
            .referenceVersement(UPDATED_REFERENCE_VERSEMENT)
            .identiteReceveur(UPDATED_IDENTITE_RECEVEUR);
        VersementDTO versementDTO = versementMapper.toDto(updatedVersement);

        restVersementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, versementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(versementDTO))
            )
            .andExpect(status().isOk());

        // Validate the Versement in the database
        List<Versement> versementList = versementRepository.findAll();
        assertThat(versementList).hasSize(databaseSizeBeforeUpdate);
        Versement testVersement = versementList.get(versementList.size() - 1);
        assertThat(testVersement.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testVersement.getCommentaire()).isEqualTo(UPDATED_COMMENTAIRE);
        assertThat(testVersement.getMontant()).isEqualTo(UPDATED_MONTANT);
        assertThat(testVersement.getResteAVerser()).isEqualTo(UPDATED_RESTE_A_VERSER);
        assertThat(testVersement.getLieuVersement()).isEqualTo(UPDATED_LIEU_VERSEMENT);
        assertThat(testVersement.getReferenceVersement()).isEqualTo(UPDATED_REFERENCE_VERSEMENT);
        assertThat(testVersement.getIdentiteReceveur()).isEqualTo(UPDATED_IDENTITE_RECEVEUR);
    }

    @Test
    @Transactional
    void putNonExistingVersement() throws Exception {
        int databaseSizeBeforeUpdate = versementRepository.findAll().size();
        versement.setId(count.incrementAndGet());

        // Create the Versement
        VersementDTO versementDTO = versementMapper.toDto(versement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVersementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, versementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(versementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Versement in the database
        List<Versement> versementList = versementRepository.findAll();
        assertThat(versementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVersement() throws Exception {
        int databaseSizeBeforeUpdate = versementRepository.findAll().size();
        versement.setId(count.incrementAndGet());

        // Create the Versement
        VersementDTO versementDTO = versementMapper.toDto(versement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVersementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(versementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Versement in the database
        List<Versement> versementList = versementRepository.findAll();
        assertThat(versementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVersement() throws Exception {
        int databaseSizeBeforeUpdate = versementRepository.findAll().size();
        versement.setId(count.incrementAndGet());

        // Create the Versement
        VersementDTO versementDTO = versementMapper.toDto(versement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVersementMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(versementDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Versement in the database
        List<Versement> versementList = versementRepository.findAll();
        assertThat(versementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVersementWithPatch() throws Exception {
        // Initialize the database
        versementRepository.saveAndFlush(versement);

        int databaseSizeBeforeUpdate = versementRepository.findAll().size();

        // Update the versement using partial update
        Versement partialUpdatedVersement = new Versement();
        partialUpdatedVersement.setId(versement.getId());

        partialUpdatedVersement.numero(UPDATED_NUMERO).montant(UPDATED_MONTANT).referenceVersement(UPDATED_REFERENCE_VERSEMENT);

        restVersementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVersement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVersement))
            )
            .andExpect(status().isOk());

        // Validate the Versement in the database
        List<Versement> versementList = versementRepository.findAll();
        assertThat(versementList).hasSize(databaseSizeBeforeUpdate);
        Versement testVersement = versementList.get(versementList.size() - 1);
        assertThat(testVersement.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testVersement.getCommentaire()).isEqualTo(DEFAULT_COMMENTAIRE);
        assertThat(testVersement.getMontant()).isEqualTo(UPDATED_MONTANT);
        assertThat(testVersement.getResteAVerser()).isEqualTo(DEFAULT_RESTE_A_VERSER);
        assertThat(testVersement.getLieuVersement()).isEqualTo(DEFAULT_LIEU_VERSEMENT);
        assertThat(testVersement.getReferenceVersement()).isEqualTo(UPDATED_REFERENCE_VERSEMENT);
        assertThat(testVersement.getIdentiteReceveur()).isEqualTo(DEFAULT_IDENTITE_RECEVEUR);
    }

    @Test
    @Transactional
    void fullUpdateVersementWithPatch() throws Exception {
        // Initialize the database
        versementRepository.saveAndFlush(versement);

        int databaseSizeBeforeUpdate = versementRepository.findAll().size();

        // Update the versement using partial update
        Versement partialUpdatedVersement = new Versement();
        partialUpdatedVersement.setId(versement.getId());

        partialUpdatedVersement
            .numero(UPDATED_NUMERO)
            .commentaire(UPDATED_COMMENTAIRE)
            .montant(UPDATED_MONTANT)
            .resteAVerser(UPDATED_RESTE_A_VERSER)
            .lieuVersement(UPDATED_LIEU_VERSEMENT)
            .referenceVersement(UPDATED_REFERENCE_VERSEMENT)
            .identiteReceveur(UPDATED_IDENTITE_RECEVEUR);

        restVersementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVersement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVersement))
            )
            .andExpect(status().isOk());

        // Validate the Versement in the database
        List<Versement> versementList = versementRepository.findAll();
        assertThat(versementList).hasSize(databaseSizeBeforeUpdate);
        Versement testVersement = versementList.get(versementList.size() - 1);
        assertThat(testVersement.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testVersement.getCommentaire()).isEqualTo(UPDATED_COMMENTAIRE);
        assertThat(testVersement.getMontant()).isEqualTo(UPDATED_MONTANT);
        assertThat(testVersement.getResteAVerser()).isEqualTo(UPDATED_RESTE_A_VERSER);
        assertThat(testVersement.getLieuVersement()).isEqualTo(UPDATED_LIEU_VERSEMENT);
        assertThat(testVersement.getReferenceVersement()).isEqualTo(UPDATED_REFERENCE_VERSEMENT);
        assertThat(testVersement.getIdentiteReceveur()).isEqualTo(UPDATED_IDENTITE_RECEVEUR);
    }

    @Test
    @Transactional
    void patchNonExistingVersement() throws Exception {
        int databaseSizeBeforeUpdate = versementRepository.findAll().size();
        versement.setId(count.incrementAndGet());

        // Create the Versement
        VersementDTO versementDTO = versementMapper.toDto(versement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVersementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, versementDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(versementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Versement in the database
        List<Versement> versementList = versementRepository.findAll();
        assertThat(versementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVersement() throws Exception {
        int databaseSizeBeforeUpdate = versementRepository.findAll().size();
        versement.setId(count.incrementAndGet());

        // Create the Versement
        VersementDTO versementDTO = versementMapper.toDto(versement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVersementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(versementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Versement in the database
        List<Versement> versementList = versementRepository.findAll();
        assertThat(versementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVersement() throws Exception {
        int databaseSizeBeforeUpdate = versementRepository.findAll().size();
        versement.setId(count.incrementAndGet());

        // Create the Versement
        VersementDTO versementDTO = versementMapper.toDto(versement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVersementMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(versementDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Versement in the database
        List<Versement> versementList = versementRepository.findAll();
        assertThat(versementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVersement() throws Exception {
        // Initialize the database
        versementRepository.saveAndFlush(versement);

        int databaseSizeBeforeDelete = versementRepository.findAll().size();

        // Delete the versement
        restVersementMockMvc
            .perform(delete(ENTITY_API_URL_ID, versement.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Versement> versementList = versementRepository.findAll();
        assertThat(versementList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
