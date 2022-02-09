package com.diandson.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.diandson.IntegrationTest;
import com.diandson.domain.Personne;
import com.diandson.repository.PersonneRepository;
import com.diandson.service.dto.PersonneDTO;
import com.diandson.service.mapper.PersonneMapper;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link PersonneResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PersonneResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATA_NAISSANCE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_NAISSANCE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_LIEU_NAISSANCE = "AAAAAAAAAA";
    private static final String UPDATED_LIEU_NAISSANCE = "BBBBBBBBBB";

    private static final String DEFAULT_NUMERO_DOC = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_DOC = "BBBBBBBBBB";

    private static final byte[] DEFAULT_PROFIL = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PROFIL = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PROFIL_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PROFIL_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_TELEPHONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEPHONE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/personnes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PersonneRepository personneRepository;

    @Autowired
    private PersonneMapper personneMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPersonneMockMvc;

    private Personne personne;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Personne createEntity(EntityManager em) {
        Personne personne = new Personne()
            .nom(DEFAULT_NOM)
            .prenom(DEFAULT_PRENOM)
            .dataNaissance(DEFAULT_DATA_NAISSANCE)
            .lieuNaissance(DEFAULT_LIEU_NAISSANCE)
            .numeroDoc(DEFAULT_NUMERO_DOC)
            .profil(DEFAULT_PROFIL)
            .profilContentType(DEFAULT_PROFIL_CONTENT_TYPE)
            .telephone(DEFAULT_TELEPHONE);
        return personne;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Personne createUpdatedEntity(EntityManager em) {
        Personne personne = new Personne()
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .dataNaissance(UPDATED_DATA_NAISSANCE)
            .lieuNaissance(UPDATED_LIEU_NAISSANCE)
            .numeroDoc(UPDATED_NUMERO_DOC)
            .profil(UPDATED_PROFIL)
            .profilContentType(UPDATED_PROFIL_CONTENT_TYPE)
            .telephone(UPDATED_TELEPHONE);
        return personne;
    }

    @BeforeEach
    public void initTest() {
        personne = createEntity(em);
    }

    @Test
    @Transactional
    void createPersonne() throws Exception {
        int databaseSizeBeforeCreate = personneRepository.findAll().size();
        // Create the Personne
        PersonneDTO personneDTO = personneMapper.toDto(personne);
        restPersonneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personneDTO)))
            .andExpect(status().isCreated());

        // Validate the Personne in the database
        List<Personne> personneList = personneRepository.findAll();
        assertThat(personneList).hasSize(databaseSizeBeforeCreate + 1);
        Personne testPersonne = personneList.get(personneList.size() - 1);
        assertThat(testPersonne.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testPersonne.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testPersonne.getDataNaissance()).isEqualTo(DEFAULT_DATA_NAISSANCE);
        assertThat(testPersonne.getLieuNaissance()).isEqualTo(DEFAULT_LIEU_NAISSANCE);
        assertThat(testPersonne.getNumeroDoc()).isEqualTo(DEFAULT_NUMERO_DOC);
        assertThat(testPersonne.getProfil()).isEqualTo(DEFAULT_PROFIL);
        assertThat(testPersonne.getProfilContentType()).isEqualTo(DEFAULT_PROFIL_CONTENT_TYPE);
        assertThat(testPersonne.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
    }

    @Test
    @Transactional
    void createPersonneWithExistingId() throws Exception {
        // Create the Personne with an existing ID
        personne.setId(1L);
        PersonneDTO personneDTO = personneMapper.toDto(personne);

        int databaseSizeBeforeCreate = personneRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPersonneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personneDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Personne in the database
        List<Personne> personneList = personneRepository.findAll();
        assertThat(personneList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPersonnes() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList
        restPersonneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(personne.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].dataNaissance").value(hasItem(DEFAULT_DATA_NAISSANCE.toString())))
            .andExpect(jsonPath("$.[*].lieuNaissance").value(hasItem(DEFAULT_LIEU_NAISSANCE)))
            .andExpect(jsonPath("$.[*].numeroDoc").value(hasItem(DEFAULT_NUMERO_DOC)))
            .andExpect(jsonPath("$.[*].profilContentType").value(hasItem(DEFAULT_PROFIL_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].profil").value(hasItem(Base64Utils.encodeToString(DEFAULT_PROFIL))))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)));
    }

    @Test
    @Transactional
    void getPersonne() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get the personne
        restPersonneMockMvc
            .perform(get(ENTITY_API_URL_ID, personne.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(personne.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM))
            .andExpect(jsonPath("$.dataNaissance").value(DEFAULT_DATA_NAISSANCE.toString()))
            .andExpect(jsonPath("$.lieuNaissance").value(DEFAULT_LIEU_NAISSANCE))
            .andExpect(jsonPath("$.numeroDoc").value(DEFAULT_NUMERO_DOC))
            .andExpect(jsonPath("$.profilContentType").value(DEFAULT_PROFIL_CONTENT_TYPE))
            .andExpect(jsonPath("$.profil").value(Base64Utils.encodeToString(DEFAULT_PROFIL)))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE));
    }

    @Test
    @Transactional
    void getNonExistingPersonne() throws Exception {
        // Get the personne
        restPersonneMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPersonne() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        int databaseSizeBeforeUpdate = personneRepository.findAll().size();

        // Update the personne
        Personne updatedPersonne = personneRepository.findById(personne.getId()).get();
        // Disconnect from session so that the updates on updatedPersonne are not directly saved in db
        em.detach(updatedPersonne);
        updatedPersonne
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .dataNaissance(UPDATED_DATA_NAISSANCE)
            .lieuNaissance(UPDATED_LIEU_NAISSANCE)
            .numeroDoc(UPDATED_NUMERO_DOC)
            .profil(UPDATED_PROFIL)
            .profilContentType(UPDATED_PROFIL_CONTENT_TYPE)
            .telephone(UPDATED_TELEPHONE);
        PersonneDTO personneDTO = personneMapper.toDto(updatedPersonne);

        restPersonneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, personneDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personneDTO))
            )
            .andExpect(status().isOk());

        // Validate the Personne in the database
        List<Personne> personneList = personneRepository.findAll();
        assertThat(personneList).hasSize(databaseSizeBeforeUpdate);
        Personne testPersonne = personneList.get(personneList.size() - 1);
        assertThat(testPersonne.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testPersonne.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testPersonne.getDataNaissance()).isEqualTo(UPDATED_DATA_NAISSANCE);
        assertThat(testPersonne.getLieuNaissance()).isEqualTo(UPDATED_LIEU_NAISSANCE);
        assertThat(testPersonne.getNumeroDoc()).isEqualTo(UPDATED_NUMERO_DOC);
        assertThat(testPersonne.getProfil()).isEqualTo(UPDATED_PROFIL);
        assertThat(testPersonne.getProfilContentType()).isEqualTo(UPDATED_PROFIL_CONTENT_TYPE);
        assertThat(testPersonne.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void putNonExistingPersonne() throws Exception {
        int databaseSizeBeforeUpdate = personneRepository.findAll().size();
        personne.setId(count.incrementAndGet());

        // Create the Personne
        PersonneDTO personneDTO = personneMapper.toDto(personne);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, personneDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Personne in the database
        List<Personne> personneList = personneRepository.findAll();
        assertThat(personneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPersonne() throws Exception {
        int databaseSizeBeforeUpdate = personneRepository.findAll().size();
        personne.setId(count.incrementAndGet());

        // Create the Personne
        PersonneDTO personneDTO = personneMapper.toDto(personne);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Personne in the database
        List<Personne> personneList = personneRepository.findAll();
        assertThat(personneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPersonne() throws Exception {
        int databaseSizeBeforeUpdate = personneRepository.findAll().size();
        personne.setId(count.incrementAndGet());

        // Create the Personne
        PersonneDTO personneDTO = personneMapper.toDto(personne);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonneMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personneDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Personne in the database
        List<Personne> personneList = personneRepository.findAll();
        assertThat(personneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePersonneWithPatch() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        int databaseSizeBeforeUpdate = personneRepository.findAll().size();

        // Update the personne using partial update
        Personne partialUpdatedPersonne = new Personne();
        partialUpdatedPersonne.setId(personne.getId());

        partialUpdatedPersonne.nom(UPDATED_NOM).numeroDoc(UPDATED_NUMERO_DOC);

        restPersonneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonne.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPersonne))
            )
            .andExpect(status().isOk());

        // Validate the Personne in the database
        List<Personne> personneList = personneRepository.findAll();
        assertThat(personneList).hasSize(databaseSizeBeforeUpdate);
        Personne testPersonne = personneList.get(personneList.size() - 1);
        assertThat(testPersonne.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testPersonne.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testPersonne.getDataNaissance()).isEqualTo(DEFAULT_DATA_NAISSANCE);
        assertThat(testPersonne.getLieuNaissance()).isEqualTo(DEFAULT_LIEU_NAISSANCE);
        assertThat(testPersonne.getNumeroDoc()).isEqualTo(UPDATED_NUMERO_DOC);
        assertThat(testPersonne.getProfil()).isEqualTo(DEFAULT_PROFIL);
        assertThat(testPersonne.getProfilContentType()).isEqualTo(DEFAULT_PROFIL_CONTENT_TYPE);
        assertThat(testPersonne.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
    }

    @Test
    @Transactional
    void fullUpdatePersonneWithPatch() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        int databaseSizeBeforeUpdate = personneRepository.findAll().size();

        // Update the personne using partial update
        Personne partialUpdatedPersonne = new Personne();
        partialUpdatedPersonne.setId(personne.getId());

        partialUpdatedPersonne
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .dataNaissance(UPDATED_DATA_NAISSANCE)
            .lieuNaissance(UPDATED_LIEU_NAISSANCE)
            .numeroDoc(UPDATED_NUMERO_DOC)
            .profil(UPDATED_PROFIL)
            .profilContentType(UPDATED_PROFIL_CONTENT_TYPE)
            .telephone(UPDATED_TELEPHONE);

        restPersonneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonne.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPersonne))
            )
            .andExpect(status().isOk());

        // Validate the Personne in the database
        List<Personne> personneList = personneRepository.findAll();
        assertThat(personneList).hasSize(databaseSizeBeforeUpdate);
        Personne testPersonne = personneList.get(personneList.size() - 1);
        assertThat(testPersonne.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testPersonne.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testPersonne.getDataNaissance()).isEqualTo(UPDATED_DATA_NAISSANCE);
        assertThat(testPersonne.getLieuNaissance()).isEqualTo(UPDATED_LIEU_NAISSANCE);
        assertThat(testPersonne.getNumeroDoc()).isEqualTo(UPDATED_NUMERO_DOC);
        assertThat(testPersonne.getProfil()).isEqualTo(UPDATED_PROFIL);
        assertThat(testPersonne.getProfilContentType()).isEqualTo(UPDATED_PROFIL_CONTENT_TYPE);
        assertThat(testPersonne.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void patchNonExistingPersonne() throws Exception {
        int databaseSizeBeforeUpdate = personneRepository.findAll().size();
        personne.setId(count.incrementAndGet());

        // Create the Personne
        PersonneDTO personneDTO = personneMapper.toDto(personne);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, personneDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Personne in the database
        List<Personne> personneList = personneRepository.findAll();
        assertThat(personneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPersonne() throws Exception {
        int databaseSizeBeforeUpdate = personneRepository.findAll().size();
        personne.setId(count.incrementAndGet());

        // Create the Personne
        PersonneDTO personneDTO = personneMapper.toDto(personne);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Personne in the database
        List<Personne> personneList = personneRepository.findAll();
        assertThat(personneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPersonne() throws Exception {
        int databaseSizeBeforeUpdate = personneRepository.findAll().size();
        personne.setId(count.incrementAndGet());

        // Create the Personne
        PersonneDTO personneDTO = personneMapper.toDto(personne);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonneMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(personneDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Personne in the database
        List<Personne> personneList = personneRepository.findAll();
        assertThat(personneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePersonne() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        int databaseSizeBeforeDelete = personneRepository.findAll().size();

        // Delete the personne
        restPersonneMockMvc
            .perform(delete(ENTITY_API_URL_ID, personne.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Personne> personneList = personneRepository.findAll();
        assertThat(personneList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
