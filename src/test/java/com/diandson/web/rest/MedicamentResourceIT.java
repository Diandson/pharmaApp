package com.diandson.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.diandson.IntegrationTest;
import com.diandson.domain.Medicament;
import com.diandson.repository.MedicamentRepository;
import com.diandson.service.dto.MedicamentDTO;
import com.diandson.service.mapper.MedicamentMapper;
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
 * Integration tests for the {@link MedicamentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MedicamentResourceIT {

    private static final String DEFAULT_DENOMINATION = "AAAAAAAAAA";
    private static final String UPDATED_DENOMINATION = "BBBBBBBBBB";

    private static final String DEFAULT_DCI = "AAAAAAAAAA";
    private static final String UPDATED_DCI = "BBBBBBBBBB";

    private static final String DEFAULT_FORME = "AAAAAAAAAA";
    private static final String UPDATED_FORME = "BBBBBBBBBB";

    private static final String DEFAULT_DOSAGE = "AAAAAAAAAA";
    private static final String UPDATED_DOSAGE = "BBBBBBBBBB";

    private static final String DEFAULT_CLASSE = "AAAAAAAAAA";
    private static final String UPDATED_CLASSE = "BBBBBBBBBB";

    private static final String DEFAULT_CODE_BARE = "AAAAAAAAAA";
    private static final String UPDATED_CODE_BARE = "BBBBBBBBBB";

    private static final Long DEFAULT_PRIX_ACHAT = 1L;
    private static final Long UPDATED_PRIX_ACHAT = 2L;

    private static final Long DEFAULT_PRIX_PUBLIC = 1L;
    private static final Long UPDATED_PRIX_PUBLIC = 2L;

    private static final Long DEFAULT_STOCK_ALERTE = 1L;
    private static final Long UPDATED_STOCK_ALERTE = 2L;

    private static final Long DEFAULT_STOCK_SECURITE = 1L;
    private static final Long UPDATED_STOCK_SECURITE = 2L;

    private static final Long DEFAULT_STOCK_THEORIQUE = 1L;
    private static final Long UPDATED_STOCK_THEORIQUE = 2L;

    private static final LocalDate DEFAULT_DATE_FABRICATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FABRICATION = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_EXPIRATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_EXPIRATION = LocalDate.now(ZoneId.systemDefault());

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/medicaments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MedicamentRepository medicamentRepository;

    @Autowired
    private MedicamentMapper medicamentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMedicamentMockMvc;

    private Medicament medicament;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Medicament createEntity(EntityManager em) {
        Medicament medicament = new Medicament()
            .denomination(DEFAULT_DENOMINATION)
            .dci(DEFAULT_DCI)
            .forme(DEFAULT_FORME)
            .dosage(DEFAULT_DOSAGE)
            .classe(DEFAULT_CLASSE)
            .codeBare(DEFAULT_CODE_BARE)
            .prixAchat(DEFAULT_PRIX_ACHAT)
            .prixPublic(DEFAULT_PRIX_PUBLIC)
            .stockAlerte(DEFAULT_STOCK_ALERTE)
            .stockSecurite(DEFAULT_STOCK_SECURITE)
            .stockTheorique(DEFAULT_STOCK_THEORIQUE)
            .dateFabrication(DEFAULT_DATE_FABRICATION)
            .dateExpiration(DEFAULT_DATE_EXPIRATION)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE);
        return medicament;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Medicament createUpdatedEntity(EntityManager em) {
        Medicament medicament = new Medicament()
            .denomination(UPDATED_DENOMINATION)
            .dci(UPDATED_DCI)
            .forme(UPDATED_FORME)
            .dosage(UPDATED_DOSAGE)
            .classe(UPDATED_CLASSE)
            .codeBare(UPDATED_CODE_BARE)
            .prixAchat(UPDATED_PRIX_ACHAT)
            .prixPublic(UPDATED_PRIX_PUBLIC)
            .stockAlerte(UPDATED_STOCK_ALERTE)
            .stockSecurite(UPDATED_STOCK_SECURITE)
            .stockTheorique(UPDATED_STOCK_THEORIQUE)
            .dateFabrication(UPDATED_DATE_FABRICATION)
            .dateExpiration(UPDATED_DATE_EXPIRATION)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);
        return medicament;
    }

    @BeforeEach
    public void initTest() {
        medicament = createEntity(em);
    }

    @Test
    @Transactional
    void createMedicament() throws Exception {
        int databaseSizeBeforeCreate = medicamentRepository.findAll().size();
        // Create the Medicament
        MedicamentDTO medicamentDTO = medicamentMapper.toDto(medicament);
        restMedicamentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(medicamentDTO)))
            .andExpect(status().isCreated());

        // Validate the Medicament in the database
        List<Medicament> medicamentList = medicamentRepository.findAll();
        assertThat(medicamentList).hasSize(databaseSizeBeforeCreate + 1);
        Medicament testMedicament = medicamentList.get(medicamentList.size() - 1);
        assertThat(testMedicament.getDenomination()).isEqualTo(DEFAULT_DENOMINATION);
        assertThat(testMedicament.getDci()).isEqualTo(DEFAULT_DCI);
        assertThat(testMedicament.getForme()).isEqualTo(DEFAULT_FORME);
        assertThat(testMedicament.getDosage()).isEqualTo(DEFAULT_DOSAGE);
        assertThat(testMedicament.getClasse()).isEqualTo(DEFAULT_CLASSE);
        assertThat(testMedicament.getCodeBare()).isEqualTo(DEFAULT_CODE_BARE);
        assertThat(testMedicament.getPrixAchat()).isEqualTo(DEFAULT_PRIX_ACHAT);
        assertThat(testMedicament.getPrixPublic()).isEqualTo(DEFAULT_PRIX_PUBLIC);
        assertThat(testMedicament.getStockAlerte()).isEqualTo(DEFAULT_STOCK_ALERTE);
        assertThat(testMedicament.getStockSecurite()).isEqualTo(DEFAULT_STOCK_SECURITE);
        assertThat(testMedicament.getStockTheorique()).isEqualTo(DEFAULT_STOCK_THEORIQUE);
        assertThat(testMedicament.getDateFabrication()).isEqualTo(DEFAULT_DATE_FABRICATION);
        assertThat(testMedicament.getDateExpiration()).isEqualTo(DEFAULT_DATE_EXPIRATION);
        assertThat(testMedicament.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testMedicament.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createMedicamentWithExistingId() throws Exception {
        // Create the Medicament with an existing ID
        medicament.setId(1L);
        MedicamentDTO medicamentDTO = medicamentMapper.toDto(medicament);

        int databaseSizeBeforeCreate = medicamentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMedicamentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(medicamentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Medicament in the database
        List<Medicament> medicamentList = medicamentRepository.findAll();
        assertThat(medicamentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMedicaments() throws Exception {
        // Initialize the database
        medicamentRepository.saveAndFlush(medicament);

        // Get all the medicamentList
        restMedicamentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medicament.getId().intValue())))
            .andExpect(jsonPath("$.[*].denomination").value(hasItem(DEFAULT_DENOMINATION)))
            .andExpect(jsonPath("$.[*].dci").value(hasItem(DEFAULT_DCI)))
            .andExpect(jsonPath("$.[*].forme").value(hasItem(DEFAULT_FORME)))
            .andExpect(jsonPath("$.[*].dosage").value(hasItem(DEFAULT_DOSAGE)))
            .andExpect(jsonPath("$.[*].classe").value(hasItem(DEFAULT_CLASSE)))
            .andExpect(jsonPath("$.[*].codeBare").value(hasItem(DEFAULT_CODE_BARE)))
            .andExpect(jsonPath("$.[*].prixAchat").value(hasItem(DEFAULT_PRIX_ACHAT.intValue())))
            .andExpect(jsonPath("$.[*].prixPublic").value(hasItem(DEFAULT_PRIX_PUBLIC.intValue())))
            .andExpect(jsonPath("$.[*].stockAlerte").value(hasItem(DEFAULT_STOCK_ALERTE.intValue())))
            .andExpect(jsonPath("$.[*].stockSecurite").value(hasItem(DEFAULT_STOCK_SECURITE.intValue())))
            .andExpect(jsonPath("$.[*].stockTheorique").value(hasItem(DEFAULT_STOCK_THEORIQUE.intValue())))
            .andExpect(jsonPath("$.[*].dateFabrication").value(hasItem(DEFAULT_DATE_FABRICATION.toString())))
            .andExpect(jsonPath("$.[*].dateExpiration").value(hasItem(DEFAULT_DATE_EXPIRATION.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));
    }

    @Test
    @Transactional
    void getMedicament() throws Exception {
        // Initialize the database
        medicamentRepository.saveAndFlush(medicament);

        // Get the medicament
        restMedicamentMockMvc
            .perform(get(ENTITY_API_URL_ID, medicament.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(medicament.getId().intValue()))
            .andExpect(jsonPath("$.denomination").value(DEFAULT_DENOMINATION))
            .andExpect(jsonPath("$.dci").value(DEFAULT_DCI))
            .andExpect(jsonPath("$.forme").value(DEFAULT_FORME))
            .andExpect(jsonPath("$.dosage").value(DEFAULT_DOSAGE))
            .andExpect(jsonPath("$.classe").value(DEFAULT_CLASSE))
            .andExpect(jsonPath("$.codeBare").value(DEFAULT_CODE_BARE))
            .andExpect(jsonPath("$.prixAchat").value(DEFAULT_PRIX_ACHAT.intValue()))
            .andExpect(jsonPath("$.prixPublic").value(DEFAULT_PRIX_PUBLIC.intValue()))
            .andExpect(jsonPath("$.stockAlerte").value(DEFAULT_STOCK_ALERTE.intValue()))
            .andExpect(jsonPath("$.stockSecurite").value(DEFAULT_STOCK_SECURITE.intValue()))
            .andExpect(jsonPath("$.stockTheorique").value(DEFAULT_STOCK_THEORIQUE.intValue()))
            .andExpect(jsonPath("$.dateFabrication").value(DEFAULT_DATE_FABRICATION.toString()))
            .andExpect(jsonPath("$.dateExpiration").value(DEFAULT_DATE_EXPIRATION.toString()))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)));
    }

    @Test
    @Transactional
    void getNonExistingMedicament() throws Exception {
        // Get the medicament
        restMedicamentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMedicament() throws Exception {
        // Initialize the database
        medicamentRepository.saveAndFlush(medicament);

        int databaseSizeBeforeUpdate = medicamentRepository.findAll().size();

        // Update the medicament
        Medicament updatedMedicament = medicamentRepository.findById(medicament.getId()).get();
        // Disconnect from session so that the updates on updatedMedicament are not directly saved in db
        em.detach(updatedMedicament);
        updatedMedicament
            .denomination(UPDATED_DENOMINATION)
            .dci(UPDATED_DCI)
            .forme(UPDATED_FORME)
            .dosage(UPDATED_DOSAGE)
            .classe(UPDATED_CLASSE)
            .codeBare(UPDATED_CODE_BARE)
            .prixAchat(UPDATED_PRIX_ACHAT)
            .prixPublic(UPDATED_PRIX_PUBLIC)
            .stockAlerte(UPDATED_STOCK_ALERTE)
            .stockSecurite(UPDATED_STOCK_SECURITE)
            .stockTheorique(UPDATED_STOCK_THEORIQUE)
            .dateFabrication(UPDATED_DATE_FABRICATION)
            .dateExpiration(UPDATED_DATE_EXPIRATION)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);
        MedicamentDTO medicamentDTO = medicamentMapper.toDto(updatedMedicament);

        restMedicamentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, medicamentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(medicamentDTO))
            )
            .andExpect(status().isOk());

        // Validate the Medicament in the database
        List<Medicament> medicamentList = medicamentRepository.findAll();
        assertThat(medicamentList).hasSize(databaseSizeBeforeUpdate);
        Medicament testMedicament = medicamentList.get(medicamentList.size() - 1);
        assertThat(testMedicament.getDenomination()).isEqualTo(UPDATED_DENOMINATION);
        assertThat(testMedicament.getDci()).isEqualTo(UPDATED_DCI);
        assertThat(testMedicament.getForme()).isEqualTo(UPDATED_FORME);
        assertThat(testMedicament.getDosage()).isEqualTo(UPDATED_DOSAGE);
        assertThat(testMedicament.getClasse()).isEqualTo(UPDATED_CLASSE);
        assertThat(testMedicament.getCodeBare()).isEqualTo(UPDATED_CODE_BARE);
        assertThat(testMedicament.getPrixAchat()).isEqualTo(UPDATED_PRIX_ACHAT);
        assertThat(testMedicament.getPrixPublic()).isEqualTo(UPDATED_PRIX_PUBLIC);
        assertThat(testMedicament.getStockAlerte()).isEqualTo(UPDATED_STOCK_ALERTE);
        assertThat(testMedicament.getStockSecurite()).isEqualTo(UPDATED_STOCK_SECURITE);
        assertThat(testMedicament.getStockTheorique()).isEqualTo(UPDATED_STOCK_THEORIQUE);
        assertThat(testMedicament.getDateFabrication()).isEqualTo(UPDATED_DATE_FABRICATION);
        assertThat(testMedicament.getDateExpiration()).isEqualTo(UPDATED_DATE_EXPIRATION);
        assertThat(testMedicament.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testMedicament.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingMedicament() throws Exception {
        int databaseSizeBeforeUpdate = medicamentRepository.findAll().size();
        medicament.setId(count.incrementAndGet());

        // Create the Medicament
        MedicamentDTO medicamentDTO = medicamentMapper.toDto(medicament);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedicamentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, medicamentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(medicamentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medicament in the database
        List<Medicament> medicamentList = medicamentRepository.findAll();
        assertThat(medicamentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMedicament() throws Exception {
        int databaseSizeBeforeUpdate = medicamentRepository.findAll().size();
        medicament.setId(count.incrementAndGet());

        // Create the Medicament
        MedicamentDTO medicamentDTO = medicamentMapper.toDto(medicament);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicamentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(medicamentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medicament in the database
        List<Medicament> medicamentList = medicamentRepository.findAll();
        assertThat(medicamentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMedicament() throws Exception {
        int databaseSizeBeforeUpdate = medicamentRepository.findAll().size();
        medicament.setId(count.incrementAndGet());

        // Create the Medicament
        MedicamentDTO medicamentDTO = medicamentMapper.toDto(medicament);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicamentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(medicamentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Medicament in the database
        List<Medicament> medicamentList = medicamentRepository.findAll();
        assertThat(medicamentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMedicamentWithPatch() throws Exception {
        // Initialize the database
        medicamentRepository.saveAndFlush(medicament);

        int databaseSizeBeforeUpdate = medicamentRepository.findAll().size();

        // Update the medicament using partial update
        Medicament partialUpdatedMedicament = new Medicament();
        partialUpdatedMedicament.setId(medicament.getId());

        partialUpdatedMedicament
            .dci(UPDATED_DCI)
            .classe(UPDATED_CLASSE)
            .prixPublic(UPDATED_PRIX_PUBLIC)
            .dateFabrication(UPDATED_DATE_FABRICATION);

        restMedicamentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedicament.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMedicament))
            )
            .andExpect(status().isOk());

        // Validate the Medicament in the database
        List<Medicament> medicamentList = medicamentRepository.findAll();
        assertThat(medicamentList).hasSize(databaseSizeBeforeUpdate);
        Medicament testMedicament = medicamentList.get(medicamentList.size() - 1);
        assertThat(testMedicament.getDenomination()).isEqualTo(DEFAULT_DENOMINATION);
        assertThat(testMedicament.getDci()).isEqualTo(UPDATED_DCI);
        assertThat(testMedicament.getForme()).isEqualTo(DEFAULT_FORME);
        assertThat(testMedicament.getDosage()).isEqualTo(DEFAULT_DOSAGE);
        assertThat(testMedicament.getClasse()).isEqualTo(UPDATED_CLASSE);
        assertThat(testMedicament.getCodeBare()).isEqualTo(DEFAULT_CODE_BARE);
        assertThat(testMedicament.getPrixAchat()).isEqualTo(DEFAULT_PRIX_ACHAT);
        assertThat(testMedicament.getPrixPublic()).isEqualTo(UPDATED_PRIX_PUBLIC);
        assertThat(testMedicament.getStockAlerte()).isEqualTo(DEFAULT_STOCK_ALERTE);
        assertThat(testMedicament.getStockSecurite()).isEqualTo(DEFAULT_STOCK_SECURITE);
        assertThat(testMedicament.getStockTheorique()).isEqualTo(DEFAULT_STOCK_THEORIQUE);
        assertThat(testMedicament.getDateFabrication()).isEqualTo(UPDATED_DATE_FABRICATION);
        assertThat(testMedicament.getDateExpiration()).isEqualTo(DEFAULT_DATE_EXPIRATION);
        assertThat(testMedicament.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testMedicament.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateMedicamentWithPatch() throws Exception {
        // Initialize the database
        medicamentRepository.saveAndFlush(medicament);

        int databaseSizeBeforeUpdate = medicamentRepository.findAll().size();

        // Update the medicament using partial update
        Medicament partialUpdatedMedicament = new Medicament();
        partialUpdatedMedicament.setId(medicament.getId());

        partialUpdatedMedicament
            .denomination(UPDATED_DENOMINATION)
            .dci(UPDATED_DCI)
            .forme(UPDATED_FORME)
            .dosage(UPDATED_DOSAGE)
            .classe(UPDATED_CLASSE)
            .codeBare(UPDATED_CODE_BARE)
            .prixAchat(UPDATED_PRIX_ACHAT)
            .prixPublic(UPDATED_PRIX_PUBLIC)
            .stockAlerte(UPDATED_STOCK_ALERTE)
            .stockSecurite(UPDATED_STOCK_SECURITE)
            .stockTheorique(UPDATED_STOCK_THEORIQUE)
            .dateFabrication(UPDATED_DATE_FABRICATION)
            .dateExpiration(UPDATED_DATE_EXPIRATION)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restMedicamentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedicament.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMedicament))
            )
            .andExpect(status().isOk());

        // Validate the Medicament in the database
        List<Medicament> medicamentList = medicamentRepository.findAll();
        assertThat(medicamentList).hasSize(databaseSizeBeforeUpdate);
        Medicament testMedicament = medicamentList.get(medicamentList.size() - 1);
        assertThat(testMedicament.getDenomination()).isEqualTo(UPDATED_DENOMINATION);
        assertThat(testMedicament.getDci()).isEqualTo(UPDATED_DCI);
        assertThat(testMedicament.getForme()).isEqualTo(UPDATED_FORME);
        assertThat(testMedicament.getDosage()).isEqualTo(UPDATED_DOSAGE);
        assertThat(testMedicament.getClasse()).isEqualTo(UPDATED_CLASSE);
        assertThat(testMedicament.getCodeBare()).isEqualTo(UPDATED_CODE_BARE);
        assertThat(testMedicament.getPrixAchat()).isEqualTo(UPDATED_PRIX_ACHAT);
        assertThat(testMedicament.getPrixPublic()).isEqualTo(UPDATED_PRIX_PUBLIC);
        assertThat(testMedicament.getStockAlerte()).isEqualTo(UPDATED_STOCK_ALERTE);
        assertThat(testMedicament.getStockSecurite()).isEqualTo(UPDATED_STOCK_SECURITE);
        assertThat(testMedicament.getStockTheorique()).isEqualTo(UPDATED_STOCK_THEORIQUE);
        assertThat(testMedicament.getDateFabrication()).isEqualTo(UPDATED_DATE_FABRICATION);
        assertThat(testMedicament.getDateExpiration()).isEqualTo(UPDATED_DATE_EXPIRATION);
        assertThat(testMedicament.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testMedicament.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingMedicament() throws Exception {
        int databaseSizeBeforeUpdate = medicamentRepository.findAll().size();
        medicament.setId(count.incrementAndGet());

        // Create the Medicament
        MedicamentDTO medicamentDTO = medicamentMapper.toDto(medicament);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedicamentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, medicamentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(medicamentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medicament in the database
        List<Medicament> medicamentList = medicamentRepository.findAll();
        assertThat(medicamentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMedicament() throws Exception {
        int databaseSizeBeforeUpdate = medicamentRepository.findAll().size();
        medicament.setId(count.incrementAndGet());

        // Create the Medicament
        MedicamentDTO medicamentDTO = medicamentMapper.toDto(medicament);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicamentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(medicamentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medicament in the database
        List<Medicament> medicamentList = medicamentRepository.findAll();
        assertThat(medicamentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMedicament() throws Exception {
        int databaseSizeBeforeUpdate = medicamentRepository.findAll().size();
        medicament.setId(count.incrementAndGet());

        // Create the Medicament
        MedicamentDTO medicamentDTO = medicamentMapper.toDto(medicament);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicamentMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(medicamentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Medicament in the database
        List<Medicament> medicamentList = medicamentRepository.findAll();
        assertThat(medicamentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMedicament() throws Exception {
        // Initialize the database
        medicamentRepository.saveAndFlush(medicament);

        int databaseSizeBeforeDelete = medicamentRepository.findAll().size();

        // Delete the medicament
        restMedicamentMockMvc
            .perform(delete(ENTITY_API_URL_ID, medicament.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Medicament> medicamentList = medicamentRepository.findAll();
        assertThat(medicamentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
