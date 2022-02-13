package com.diandson.web.rest;

import static com.diandson.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.diandson.IntegrationTest;
import com.diandson.domain.Structure;
import com.diandson.domain.enumeration.TypeStructure;
import com.diandson.repository.StructureRepository;
import com.diandson.service.dto.StructureDTO;
import com.diandson.service.mapper.StructureMapper;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link StructureResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StructureResourceIT {

    private static final String DEFAULT_DENOMINATION = "AAAAAAAAAA";
    private static final String UPDATED_DENOMINATION = "BBBBBBBBBB";

    private static final String DEFAULT_IFU = "AAAAAAAAAA";
    private static final String UPDATED_IFU = "BBBBBBBBBB";

    private static final String DEFAULT_RCCM = "AAAAAAAAAA";
    private static final String UPDATED_RCCM = "BBBBBBBBBB";

    private static final String DEFAULT_CODE_POSTAL = "AAAAAAAAAA";
    private static final String UPDATED_CODE_POSTAL = "BBBBBBBBBB";

    private static final String DEFAULT_LOCALISATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCALISATION = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT = "BBBBBBBBBB";

    private static final String DEFAULT_REGIME = "AAAAAAAAAA";
    private static final String UPDATED_REGIME = "BBBBBBBBBB";

    private static final String DEFAULT_DIVISION = "AAAAAAAAAA";
    private static final String UPDATED_DIVISION = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final byte[] DEFAULT_LOGO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_LOGO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_LOGO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_LOGO_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_CACHET = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_CACHET = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_CACHET_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_CACHET_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_SIGNATURE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_SIGNATURE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_SIGNATURE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_SIGNATURE_CONTENT_TYPE = "image/png";

    private static final ZonedDateTime DEFAULT_DATE_CONFIG = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_CONFIG = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_PDG = "AAAAAAAAAA";
    private static final String UPDATED_PDG = "BBBBBBBBBB";

    private static final TypeStructure DEFAULT_TYPE = TypeStructure.SIEGE;
    private static final TypeStructure UPDATED_TYPE = TypeStructure.AGENCE;

    private static final String DEFAULT_MERE = "AAAAAAAAAA";
    private static final String UPDATED_MERE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/structures";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StructureRepository structureRepository;

    @Autowired
    private StructureMapper structureMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStructureMockMvc;

    private Structure structure;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Structure createEntity(EntityManager em) {
        Structure structure = new Structure()
            .denomination(DEFAULT_DENOMINATION)
            .ifu(DEFAULT_IFU)
            .rccm(DEFAULT_RCCM)
            .codePostal(DEFAULT_CODE_POSTAL)
            .localisation(DEFAULT_LOCALISATION)
            .contact(DEFAULT_CONTACT)
            .regime(DEFAULT_REGIME)
            .division(DEFAULT_DIVISION)
            .email(DEFAULT_EMAIL)
            .logo(DEFAULT_LOGO)
            .logoContentType(DEFAULT_LOGO_CONTENT_TYPE)
            .cachet(DEFAULT_CACHET)
            .cachetContentType(DEFAULT_CACHET_CONTENT_TYPE)
            .signature(DEFAULT_SIGNATURE)
            .signatureContentType(DEFAULT_SIGNATURE_CONTENT_TYPE)
            .dateConfig(DEFAULT_DATE_CONFIG)
            .pdg(DEFAULT_PDG)
            .type(DEFAULT_TYPE)
            .mere(DEFAULT_MERE);
        return structure;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Structure createUpdatedEntity(EntityManager em) {
        Structure structure = new Structure()
            .denomination(UPDATED_DENOMINATION)
            .ifu(UPDATED_IFU)
            .rccm(UPDATED_RCCM)
            .codePostal(UPDATED_CODE_POSTAL)
            .localisation(UPDATED_LOCALISATION)
            .contact(UPDATED_CONTACT)
            .regime(UPDATED_REGIME)
            .division(UPDATED_DIVISION)
            .email(UPDATED_EMAIL)
            .logo(UPDATED_LOGO)
            .logoContentType(UPDATED_LOGO_CONTENT_TYPE)
            .cachet(UPDATED_CACHET)
            .cachetContentType(UPDATED_CACHET_CONTENT_TYPE)
            .signature(UPDATED_SIGNATURE)
            .signatureContentType(UPDATED_SIGNATURE_CONTENT_TYPE)
            .dateConfig(UPDATED_DATE_CONFIG)
            .pdg(UPDATED_PDG)
            .type(UPDATED_TYPE)
            .mere(UPDATED_MERE);
        return structure;
    }

    @BeforeEach
    public void initTest() {
        structure = createEntity(em);
    }

    @Test
    @Transactional
    void createStructure() throws Exception {
        int databaseSizeBeforeCreate = structureRepository.findAll().size();
        // Create the Structure
        StructureDTO structureDTO = structureMapper.toDto(structure);
        restStructureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(structureDTO)))
            .andExpect(status().isCreated());

        // Validate the Structure in the database
        List<Structure> structureList = structureRepository.findAll();
        assertThat(structureList).hasSize(databaseSizeBeforeCreate + 1);
        Structure testStructure = structureList.get(structureList.size() - 1);
        assertThat(testStructure.getDenomination()).isEqualTo(DEFAULT_DENOMINATION);
        assertThat(testStructure.getIfu()).isEqualTo(DEFAULT_IFU);
        assertThat(testStructure.getRccm()).isEqualTo(DEFAULT_RCCM);
        assertThat(testStructure.getCodePostal()).isEqualTo(DEFAULT_CODE_POSTAL);
        assertThat(testStructure.getLocalisation()).isEqualTo(DEFAULT_LOCALISATION);
        assertThat(testStructure.getContact()).isEqualTo(DEFAULT_CONTACT);
        assertThat(testStructure.getRegime()).isEqualTo(DEFAULT_REGIME);
        assertThat(testStructure.getDivision()).isEqualTo(DEFAULT_DIVISION);
        assertThat(testStructure.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testStructure.getLogo()).isEqualTo(DEFAULT_LOGO);
        assertThat(testStructure.getLogoContentType()).isEqualTo(DEFAULT_LOGO_CONTENT_TYPE);
        assertThat(testStructure.getCachet()).isEqualTo(DEFAULT_CACHET);
        assertThat(testStructure.getCachetContentType()).isEqualTo(DEFAULT_CACHET_CONTENT_TYPE);
        assertThat(testStructure.getSignature()).isEqualTo(DEFAULT_SIGNATURE);
        assertThat(testStructure.getSignatureContentType()).isEqualTo(DEFAULT_SIGNATURE_CONTENT_TYPE);
        assertThat(testStructure.getDateConfig()).isEqualTo(DEFAULT_DATE_CONFIG);
        assertThat(testStructure.getPdg()).isEqualTo(DEFAULT_PDG);
        assertThat(testStructure.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testStructure.getMere()).isEqualTo(DEFAULT_MERE);
    }

    @Test
    @Transactional
    void createStructureWithExistingId() throws Exception {
        // Create the Structure with an existing ID
        structure.setId(1L);
        StructureDTO structureDTO = structureMapper.toDto(structure);

        int databaseSizeBeforeCreate = structureRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStructureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(structureDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Structure in the database
        List<Structure> structureList = structureRepository.findAll();
        assertThat(structureList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllStructures() throws Exception {
        // Initialize the database
        structureRepository.saveAndFlush(structure);

        // Get all the structureList
        restStructureMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(structure.getId().intValue())))
            .andExpect(jsonPath("$.[*].denomination").value(hasItem(DEFAULT_DENOMINATION)))
            .andExpect(jsonPath("$.[*].ifu").value(hasItem(DEFAULT_IFU)))
            .andExpect(jsonPath("$.[*].rccm").value(hasItem(DEFAULT_RCCM)))
            .andExpect(jsonPath("$.[*].codePostal").value(hasItem(DEFAULT_CODE_POSTAL)))
            .andExpect(jsonPath("$.[*].localisation").value(hasItem(DEFAULT_LOCALISATION)))
            .andExpect(jsonPath("$.[*].contact").value(hasItem(DEFAULT_CONTACT)))
            .andExpect(jsonPath("$.[*].regime").value(hasItem(DEFAULT_REGIME)))
            .andExpect(jsonPath("$.[*].division").value(hasItem(DEFAULT_DIVISION)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].logoContentType").value(hasItem(DEFAULT_LOGO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].logo").value(hasItem(Base64Utils.encodeToString(DEFAULT_LOGO))))
            .andExpect(jsonPath("$.[*].cachetContentType").value(hasItem(DEFAULT_CACHET_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].cachet").value(hasItem(Base64Utils.encodeToString(DEFAULT_CACHET))))
            .andExpect(jsonPath("$.[*].signatureContentType").value(hasItem(DEFAULT_SIGNATURE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].signature").value(hasItem(Base64Utils.encodeToString(DEFAULT_SIGNATURE))))
            .andExpect(jsonPath("$.[*].dateConfig").value(hasItem(sameInstant(DEFAULT_DATE_CONFIG))))
            .andExpect(jsonPath("$.[*].pdg").value(hasItem(DEFAULT_PDG)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].mere").value(hasItem(DEFAULT_MERE)));
    }

    @Test
    @Transactional
    void getStructure() throws Exception {
        // Initialize the database
        structureRepository.saveAndFlush(structure);

        // Get the structure
        restStructureMockMvc
            .perform(get(ENTITY_API_URL_ID, structure.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(structure.getId().intValue()))
            .andExpect(jsonPath("$.denomination").value(DEFAULT_DENOMINATION))
            .andExpect(jsonPath("$.ifu").value(DEFAULT_IFU))
            .andExpect(jsonPath("$.rccm").value(DEFAULT_RCCM))
            .andExpect(jsonPath("$.codePostal").value(DEFAULT_CODE_POSTAL))
            .andExpect(jsonPath("$.localisation").value(DEFAULT_LOCALISATION))
            .andExpect(jsonPath("$.contact").value(DEFAULT_CONTACT))
            .andExpect(jsonPath("$.regime").value(DEFAULT_REGIME))
            .andExpect(jsonPath("$.division").value(DEFAULT_DIVISION))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.logoContentType").value(DEFAULT_LOGO_CONTENT_TYPE))
            .andExpect(jsonPath("$.logo").value(Base64Utils.encodeToString(DEFAULT_LOGO)))
            .andExpect(jsonPath("$.cachetContentType").value(DEFAULT_CACHET_CONTENT_TYPE))
            .andExpect(jsonPath("$.cachet").value(Base64Utils.encodeToString(DEFAULT_CACHET)))
            .andExpect(jsonPath("$.signatureContentType").value(DEFAULT_SIGNATURE_CONTENT_TYPE))
            .andExpect(jsonPath("$.signature").value(Base64Utils.encodeToString(DEFAULT_SIGNATURE)))
            .andExpect(jsonPath("$.dateConfig").value(sameInstant(DEFAULT_DATE_CONFIG)))
            .andExpect(jsonPath("$.pdg").value(DEFAULT_PDG))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.mere").value(DEFAULT_MERE));
    }

    @Test
    @Transactional
    void getNonExistingStructure() throws Exception {
        // Get the structure
        restStructureMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewStructure() throws Exception {
        // Initialize the database
        structureRepository.saveAndFlush(structure);

        int databaseSizeBeforeUpdate = structureRepository.findAll().size();

        // Update the structure
        Structure updatedStructure = structureRepository.findById(structure.getId()).get();
        // Disconnect from session so that the updates on updatedStructure are not directly saved in db
        em.detach(updatedStructure);
        updatedStructure
            .denomination(UPDATED_DENOMINATION)
            .ifu(UPDATED_IFU)
            .rccm(UPDATED_RCCM)
            .codePostal(UPDATED_CODE_POSTAL)
            .localisation(UPDATED_LOCALISATION)
            .contact(UPDATED_CONTACT)
            .regime(UPDATED_REGIME)
            .division(UPDATED_DIVISION)
            .email(UPDATED_EMAIL)
            .logo(UPDATED_LOGO)
            .logoContentType(UPDATED_LOGO_CONTENT_TYPE)
            .cachet(UPDATED_CACHET)
            .cachetContentType(UPDATED_CACHET_CONTENT_TYPE)
            .signature(UPDATED_SIGNATURE)
            .signatureContentType(UPDATED_SIGNATURE_CONTENT_TYPE)
            .dateConfig(UPDATED_DATE_CONFIG)
            .pdg(UPDATED_PDG)
            .type(UPDATED_TYPE)
            .mere(UPDATED_MERE);
        StructureDTO structureDTO = structureMapper.toDto(updatedStructure);

        restStructureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, structureDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(structureDTO))
            )
            .andExpect(status().isOk());

        // Validate the Structure in the database
        List<Structure> structureList = structureRepository.findAll();
        assertThat(structureList).hasSize(databaseSizeBeforeUpdate);
        Structure testStructure = structureList.get(structureList.size() - 1);
        assertThat(testStructure.getDenomination()).isEqualTo(UPDATED_DENOMINATION);
        assertThat(testStructure.getIfu()).isEqualTo(UPDATED_IFU);
        assertThat(testStructure.getRccm()).isEqualTo(UPDATED_RCCM);
        assertThat(testStructure.getCodePostal()).isEqualTo(UPDATED_CODE_POSTAL);
        assertThat(testStructure.getLocalisation()).isEqualTo(UPDATED_LOCALISATION);
        assertThat(testStructure.getContact()).isEqualTo(UPDATED_CONTACT);
        assertThat(testStructure.getRegime()).isEqualTo(UPDATED_REGIME);
        assertThat(testStructure.getDivision()).isEqualTo(UPDATED_DIVISION);
        assertThat(testStructure.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testStructure.getLogo()).isEqualTo(UPDATED_LOGO);
        assertThat(testStructure.getLogoContentType()).isEqualTo(UPDATED_LOGO_CONTENT_TYPE);
        assertThat(testStructure.getCachet()).isEqualTo(UPDATED_CACHET);
        assertThat(testStructure.getCachetContentType()).isEqualTo(UPDATED_CACHET_CONTENT_TYPE);
        assertThat(testStructure.getSignature()).isEqualTo(UPDATED_SIGNATURE);
        assertThat(testStructure.getSignatureContentType()).isEqualTo(UPDATED_SIGNATURE_CONTENT_TYPE);
        assertThat(testStructure.getDateConfig()).isEqualTo(UPDATED_DATE_CONFIG);
        assertThat(testStructure.getPdg()).isEqualTo(UPDATED_PDG);
        assertThat(testStructure.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testStructure.getMere()).isEqualTo(UPDATED_MERE);
    }

    @Test
    @Transactional
    void putNonExistingStructure() throws Exception {
        int databaseSizeBeforeUpdate = structureRepository.findAll().size();
        structure.setId(count.incrementAndGet());

        // Create the Structure
        StructureDTO structureDTO = structureMapper.toDto(structure);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStructureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, structureDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(structureDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Structure in the database
        List<Structure> structureList = structureRepository.findAll();
        assertThat(structureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStructure() throws Exception {
        int databaseSizeBeforeUpdate = structureRepository.findAll().size();
        structure.setId(count.incrementAndGet());

        // Create the Structure
        StructureDTO structureDTO = structureMapper.toDto(structure);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStructureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(structureDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Structure in the database
        List<Structure> structureList = structureRepository.findAll();
        assertThat(structureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStructure() throws Exception {
        int databaseSizeBeforeUpdate = structureRepository.findAll().size();
        structure.setId(count.incrementAndGet());

        // Create the Structure
        StructureDTO structureDTO = structureMapper.toDto(structure);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStructureMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(structureDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Structure in the database
        List<Structure> structureList = structureRepository.findAll();
        assertThat(structureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStructureWithPatch() throws Exception {
        // Initialize the database
        structureRepository.saveAndFlush(structure);

        int databaseSizeBeforeUpdate = structureRepository.findAll().size();

        // Update the structure using partial update
        Structure partialUpdatedStructure = new Structure();
        partialUpdatedStructure.setId(structure.getId());

        partialUpdatedStructure
            .ifu(UPDATED_IFU)
            .regime(UPDATED_REGIME)
            .email(UPDATED_EMAIL)
            .logo(UPDATED_LOGO)
            .logoContentType(UPDATED_LOGO_CONTENT_TYPE)
            .cachet(UPDATED_CACHET)
            .cachetContentType(UPDATED_CACHET_CONTENT_TYPE)
            .pdg(UPDATED_PDG)
            .mere(UPDATED_MERE);

        restStructureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStructure.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStructure))
            )
            .andExpect(status().isOk());

        // Validate the Structure in the database
        List<Structure> structureList = structureRepository.findAll();
        assertThat(structureList).hasSize(databaseSizeBeforeUpdate);
        Structure testStructure = structureList.get(structureList.size() - 1);
        assertThat(testStructure.getDenomination()).isEqualTo(DEFAULT_DENOMINATION);
        assertThat(testStructure.getIfu()).isEqualTo(UPDATED_IFU);
        assertThat(testStructure.getRccm()).isEqualTo(DEFAULT_RCCM);
        assertThat(testStructure.getCodePostal()).isEqualTo(DEFAULT_CODE_POSTAL);
        assertThat(testStructure.getLocalisation()).isEqualTo(DEFAULT_LOCALISATION);
        assertThat(testStructure.getContact()).isEqualTo(DEFAULT_CONTACT);
        assertThat(testStructure.getRegime()).isEqualTo(UPDATED_REGIME);
        assertThat(testStructure.getDivision()).isEqualTo(DEFAULT_DIVISION);
        assertThat(testStructure.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testStructure.getLogo()).isEqualTo(UPDATED_LOGO);
        assertThat(testStructure.getLogoContentType()).isEqualTo(UPDATED_LOGO_CONTENT_TYPE);
        assertThat(testStructure.getCachet()).isEqualTo(UPDATED_CACHET);
        assertThat(testStructure.getCachetContentType()).isEqualTo(UPDATED_CACHET_CONTENT_TYPE);
        assertThat(testStructure.getSignature()).isEqualTo(DEFAULT_SIGNATURE);
        assertThat(testStructure.getSignatureContentType()).isEqualTo(DEFAULT_SIGNATURE_CONTENT_TYPE);
        assertThat(testStructure.getDateConfig()).isEqualTo(DEFAULT_DATE_CONFIG);
        assertThat(testStructure.getPdg()).isEqualTo(UPDATED_PDG);
        assertThat(testStructure.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testStructure.getMere()).isEqualTo(UPDATED_MERE);
    }

    @Test
    @Transactional
    void fullUpdateStructureWithPatch() throws Exception {
        // Initialize the database
        structureRepository.saveAndFlush(structure);

        int databaseSizeBeforeUpdate = structureRepository.findAll().size();

        // Update the structure using partial update
        Structure partialUpdatedStructure = new Structure();
        partialUpdatedStructure.setId(structure.getId());

        partialUpdatedStructure
            .denomination(UPDATED_DENOMINATION)
            .ifu(UPDATED_IFU)
            .rccm(UPDATED_RCCM)
            .codePostal(UPDATED_CODE_POSTAL)
            .localisation(UPDATED_LOCALISATION)
            .contact(UPDATED_CONTACT)
            .regime(UPDATED_REGIME)
            .division(UPDATED_DIVISION)
            .email(UPDATED_EMAIL)
            .logo(UPDATED_LOGO)
            .logoContentType(UPDATED_LOGO_CONTENT_TYPE)
            .cachet(UPDATED_CACHET)
            .cachetContentType(UPDATED_CACHET_CONTENT_TYPE)
            .signature(UPDATED_SIGNATURE)
            .signatureContentType(UPDATED_SIGNATURE_CONTENT_TYPE)
            .dateConfig(UPDATED_DATE_CONFIG)
            .pdg(UPDATED_PDG)
            .type(UPDATED_TYPE)
            .mere(UPDATED_MERE);

        restStructureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStructure.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStructure))
            )
            .andExpect(status().isOk());

        // Validate the Structure in the database
        List<Structure> structureList = structureRepository.findAll();
        assertThat(structureList).hasSize(databaseSizeBeforeUpdate);
        Structure testStructure = structureList.get(structureList.size() - 1);
        assertThat(testStructure.getDenomination()).isEqualTo(UPDATED_DENOMINATION);
        assertThat(testStructure.getIfu()).isEqualTo(UPDATED_IFU);
        assertThat(testStructure.getRccm()).isEqualTo(UPDATED_RCCM);
        assertThat(testStructure.getCodePostal()).isEqualTo(UPDATED_CODE_POSTAL);
        assertThat(testStructure.getLocalisation()).isEqualTo(UPDATED_LOCALISATION);
        assertThat(testStructure.getContact()).isEqualTo(UPDATED_CONTACT);
        assertThat(testStructure.getRegime()).isEqualTo(UPDATED_REGIME);
        assertThat(testStructure.getDivision()).isEqualTo(UPDATED_DIVISION);
        assertThat(testStructure.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testStructure.getLogo()).isEqualTo(UPDATED_LOGO);
        assertThat(testStructure.getLogoContentType()).isEqualTo(UPDATED_LOGO_CONTENT_TYPE);
        assertThat(testStructure.getCachet()).isEqualTo(UPDATED_CACHET);
        assertThat(testStructure.getCachetContentType()).isEqualTo(UPDATED_CACHET_CONTENT_TYPE);
        assertThat(testStructure.getSignature()).isEqualTo(UPDATED_SIGNATURE);
        assertThat(testStructure.getSignatureContentType()).isEqualTo(UPDATED_SIGNATURE_CONTENT_TYPE);
        assertThat(testStructure.getDateConfig()).isEqualTo(UPDATED_DATE_CONFIG);
        assertThat(testStructure.getPdg()).isEqualTo(UPDATED_PDG);
        assertThat(testStructure.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testStructure.getMere()).isEqualTo(UPDATED_MERE);
    }

    @Test
    @Transactional
    void patchNonExistingStructure() throws Exception {
        int databaseSizeBeforeUpdate = structureRepository.findAll().size();
        structure.setId(count.incrementAndGet());

        // Create the Structure
        StructureDTO structureDTO = structureMapper.toDto(structure);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStructureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, structureDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(structureDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Structure in the database
        List<Structure> structureList = structureRepository.findAll();
        assertThat(structureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStructure() throws Exception {
        int databaseSizeBeforeUpdate = structureRepository.findAll().size();
        structure.setId(count.incrementAndGet());

        // Create the Structure
        StructureDTO structureDTO = structureMapper.toDto(structure);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStructureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(structureDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Structure in the database
        List<Structure> structureList = structureRepository.findAll();
        assertThat(structureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStructure() throws Exception {
        int databaseSizeBeforeUpdate = structureRepository.findAll().size();
        structure.setId(count.incrementAndGet());

        // Create the Structure
        StructureDTO structureDTO = structureMapper.toDto(structure);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStructureMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(structureDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Structure in the database
        List<Structure> structureList = structureRepository.findAll();
        assertThat(structureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStructure() throws Exception {
        // Initialize the database
        structureRepository.saveAndFlush(structure);

        int databaseSizeBeforeDelete = structureRepository.findAll().size();

        // Delete the structure
        restStructureMockMvc
            .perform(delete(ENTITY_API_URL_ID, structure.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Structure> structureList = structureRepository.findAll();
        assertThat(structureList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
