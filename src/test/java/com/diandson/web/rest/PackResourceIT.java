package com.diandson.web.rest;

import static com.diandson.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.diandson.IntegrationTest;
import com.diandson.domain.Pack;
import com.diandson.repository.PackRepository;
import com.diandson.service.dto.PackDTO;
import com.diandson.service.mapper.PackMapper;
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
 * Integration tests for the {@link PackResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PackResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final Long DEFAULT_DURER = 1L;
    private static final Long UPDATED_DURER = 2L;

    private static final Boolean DEFAULT_VALIDE = false;
    private static final Boolean UPDATED_VALIDE = true;

    private static final ZonedDateTime DEFAULT_DATE_RENEW = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_RENEW = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/packs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PackRepository packRepository;

    @Autowired
    private PackMapper packMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPackMockMvc;

    private Pack pack;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pack createEntity(EntityManager em) {
        Pack pack = new Pack().libelle(DEFAULT_LIBELLE).durer(DEFAULT_DURER).valide(DEFAULT_VALIDE).dateRenew(DEFAULT_DATE_RENEW);
        return pack;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pack createUpdatedEntity(EntityManager em) {
        Pack pack = new Pack().libelle(UPDATED_LIBELLE).durer(UPDATED_DURER).valide(UPDATED_VALIDE).dateRenew(UPDATED_DATE_RENEW);
        return pack;
    }

    @BeforeEach
    public void initTest() {
        pack = createEntity(em);
    }

    @Test
    @Transactional
    void createPack() throws Exception {
        int databaseSizeBeforeCreate = packRepository.findAll().size();
        // Create the Pack
        PackDTO packDTO = packMapper.toDto(pack);
        restPackMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(packDTO)))
            .andExpect(status().isCreated());

        // Validate the Pack in the database
        List<Pack> packList = packRepository.findAll();
        assertThat(packList).hasSize(databaseSizeBeforeCreate + 1);
        Pack testPack = packList.get(packList.size() - 1);
        assertThat(testPack.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testPack.getDurer()).isEqualTo(DEFAULT_DURER);
        assertThat(testPack.getValide()).isEqualTo(DEFAULT_VALIDE);
        assertThat(testPack.getDateRenew()).isEqualTo(DEFAULT_DATE_RENEW);
    }

    @Test
    @Transactional
    void createPackWithExistingId() throws Exception {
        // Create the Pack with an existing ID
        pack.setId(1L);
        PackDTO packDTO = packMapper.toDto(pack);

        int databaseSizeBeforeCreate = packRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPackMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(packDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Pack in the database
        List<Pack> packList = packRepository.findAll();
        assertThat(packList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPacks() throws Exception {
        // Initialize the database
        packRepository.saveAndFlush(pack);

        // Get all the packList
        restPackMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pack.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].durer").value(hasItem(DEFAULT_DURER.intValue())))
            .andExpect(jsonPath("$.[*].valide").value(hasItem(DEFAULT_VALIDE.booleanValue())))
            .andExpect(jsonPath("$.[*].dateRenew").value(hasItem(sameInstant(DEFAULT_DATE_RENEW))));
    }

    @Test
    @Transactional
    void getPack() throws Exception {
        // Initialize the database
        packRepository.saveAndFlush(pack);

        // Get the pack
        restPackMockMvc
            .perform(get(ENTITY_API_URL_ID, pack.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pack.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.durer").value(DEFAULT_DURER.intValue()))
            .andExpect(jsonPath("$.valide").value(DEFAULT_VALIDE.booleanValue()))
            .andExpect(jsonPath("$.dateRenew").value(sameInstant(DEFAULT_DATE_RENEW)));
    }

    @Test
    @Transactional
    void getNonExistingPack() throws Exception {
        // Get the pack
        restPackMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPack() throws Exception {
        // Initialize the database
        packRepository.saveAndFlush(pack);

        int databaseSizeBeforeUpdate = packRepository.findAll().size();

        // Update the pack
        Pack updatedPack = packRepository.findById(pack.getId()).get();
        // Disconnect from session so that the updates on updatedPack are not directly saved in db
        em.detach(updatedPack);
        updatedPack.libelle(UPDATED_LIBELLE).durer(UPDATED_DURER).valide(UPDATED_VALIDE).dateRenew(UPDATED_DATE_RENEW);
        PackDTO packDTO = packMapper.toDto(updatedPack);

        restPackMockMvc
            .perform(
                put(ENTITY_API_URL_ID, packDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(packDTO))
            )
            .andExpect(status().isOk());

        // Validate the Pack in the database
        List<Pack> packList = packRepository.findAll();
        assertThat(packList).hasSize(databaseSizeBeforeUpdate);
        Pack testPack = packList.get(packList.size() - 1);
        assertThat(testPack.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testPack.getDurer()).isEqualTo(UPDATED_DURER);
        assertThat(testPack.getValide()).isEqualTo(UPDATED_VALIDE);
        assertThat(testPack.getDateRenew()).isEqualTo(UPDATED_DATE_RENEW);
    }

    @Test
    @Transactional
    void putNonExistingPack() throws Exception {
        int databaseSizeBeforeUpdate = packRepository.findAll().size();
        pack.setId(count.incrementAndGet());

        // Create the Pack
        PackDTO packDTO = packMapper.toDto(pack);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPackMockMvc
            .perform(
                put(ENTITY_API_URL_ID, packDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(packDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pack in the database
        List<Pack> packList = packRepository.findAll();
        assertThat(packList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPack() throws Exception {
        int databaseSizeBeforeUpdate = packRepository.findAll().size();
        pack.setId(count.incrementAndGet());

        // Create the Pack
        PackDTO packDTO = packMapper.toDto(pack);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPackMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(packDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pack in the database
        List<Pack> packList = packRepository.findAll();
        assertThat(packList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPack() throws Exception {
        int databaseSizeBeforeUpdate = packRepository.findAll().size();
        pack.setId(count.incrementAndGet());

        // Create the Pack
        PackDTO packDTO = packMapper.toDto(pack);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPackMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(packDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pack in the database
        List<Pack> packList = packRepository.findAll();
        assertThat(packList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePackWithPatch() throws Exception {
        // Initialize the database
        packRepository.saveAndFlush(pack);

        int databaseSizeBeforeUpdate = packRepository.findAll().size();

        // Update the pack using partial update
        Pack partialUpdatedPack = new Pack();
        partialUpdatedPack.setId(pack.getId());

        partialUpdatedPack.durer(UPDATED_DURER).dateRenew(UPDATED_DATE_RENEW);

        restPackMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPack.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPack))
            )
            .andExpect(status().isOk());

        // Validate the Pack in the database
        List<Pack> packList = packRepository.findAll();
        assertThat(packList).hasSize(databaseSizeBeforeUpdate);
        Pack testPack = packList.get(packList.size() - 1);
        assertThat(testPack.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testPack.getDurer()).isEqualTo(UPDATED_DURER);
        assertThat(testPack.getValide()).isEqualTo(DEFAULT_VALIDE);
        assertThat(testPack.getDateRenew()).isEqualTo(UPDATED_DATE_RENEW);
    }

    @Test
    @Transactional
    void fullUpdatePackWithPatch() throws Exception {
        // Initialize the database
        packRepository.saveAndFlush(pack);

        int databaseSizeBeforeUpdate = packRepository.findAll().size();

        // Update the pack using partial update
        Pack partialUpdatedPack = new Pack();
        partialUpdatedPack.setId(pack.getId());

        partialUpdatedPack.libelle(UPDATED_LIBELLE).durer(UPDATED_DURER).valide(UPDATED_VALIDE).dateRenew(UPDATED_DATE_RENEW);

        restPackMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPack.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPack))
            )
            .andExpect(status().isOk());

        // Validate the Pack in the database
        List<Pack> packList = packRepository.findAll();
        assertThat(packList).hasSize(databaseSizeBeforeUpdate);
        Pack testPack = packList.get(packList.size() - 1);
        assertThat(testPack.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testPack.getDurer()).isEqualTo(UPDATED_DURER);
        assertThat(testPack.getValide()).isEqualTo(UPDATED_VALIDE);
        assertThat(testPack.getDateRenew()).isEqualTo(UPDATED_DATE_RENEW);
    }

    @Test
    @Transactional
    void patchNonExistingPack() throws Exception {
        int databaseSizeBeforeUpdate = packRepository.findAll().size();
        pack.setId(count.incrementAndGet());

        // Create the Pack
        PackDTO packDTO = packMapper.toDto(pack);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPackMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, packDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(packDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pack in the database
        List<Pack> packList = packRepository.findAll();
        assertThat(packList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPack() throws Exception {
        int databaseSizeBeforeUpdate = packRepository.findAll().size();
        pack.setId(count.incrementAndGet());

        // Create the Pack
        PackDTO packDTO = packMapper.toDto(pack);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPackMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(packDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pack in the database
        List<Pack> packList = packRepository.findAll();
        assertThat(packList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPack() throws Exception {
        int databaseSizeBeforeUpdate = packRepository.findAll().size();
        pack.setId(count.incrementAndGet());

        // Create the Pack
        PackDTO packDTO = packMapper.toDto(pack);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPackMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(packDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pack in the database
        List<Pack> packList = packRepository.findAll();
        assertThat(packList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePack() throws Exception {
        // Initialize the database
        packRepository.saveAndFlush(pack);

        int databaseSizeBeforeDelete = packRepository.findAll().size();

        // Delete the pack
        restPackMockMvc
            .perform(delete(ENTITY_API_URL_ID, pack.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Pack> packList = packRepository.findAll();
        assertThat(packList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
