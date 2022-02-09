package com.diandson.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.diandson.IntegrationTest;
import com.diandson.domain.TypePack;
import com.diandson.repository.TypePackRepository;
import com.diandson.service.dto.TypePackDTO;
import com.diandson.service.mapper.TypePackMapper;
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
 * Integration tests for the {@link TypePackResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TypePackResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final Long DEFAULT_DURER = 1L;
    private static final Long UPDATED_DURER = 2L;

    private static final Long DEFAULT_PRIX = 1L;
    private static final Long UPDATED_PRIX = 2L;

    private static final Long DEFAULT_ANNEXE = 1L;
    private static final Long UPDATED_ANNEXE = 2L;

    private static final String ENTITY_API_URL = "/api/type-packs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TypePackRepository typePackRepository;

    @Autowired
    private TypePackMapper typePackMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTypePackMockMvc;

    private TypePack typePack;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypePack createEntity(EntityManager em) {
        TypePack typePack = new TypePack().libelle(DEFAULT_LIBELLE).durer(DEFAULT_DURER).prix(DEFAULT_PRIX).annexe(DEFAULT_ANNEXE);
        return typePack;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypePack createUpdatedEntity(EntityManager em) {
        TypePack typePack = new TypePack().libelle(UPDATED_LIBELLE).durer(UPDATED_DURER).prix(UPDATED_PRIX).annexe(UPDATED_ANNEXE);
        return typePack;
    }

    @BeforeEach
    public void initTest() {
        typePack = createEntity(em);
    }

    @Test
    @Transactional
    void createTypePack() throws Exception {
        int databaseSizeBeforeCreate = typePackRepository.findAll().size();
        // Create the TypePack
        TypePackDTO typePackDTO = typePackMapper.toDto(typePack);
        restTypePackMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typePackDTO)))
            .andExpect(status().isCreated());

        // Validate the TypePack in the database
        List<TypePack> typePackList = typePackRepository.findAll();
        assertThat(typePackList).hasSize(databaseSizeBeforeCreate + 1);
        TypePack testTypePack = typePackList.get(typePackList.size() - 1);
        assertThat(testTypePack.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testTypePack.getDurer()).isEqualTo(DEFAULT_DURER);
        assertThat(testTypePack.getPrix()).isEqualTo(DEFAULT_PRIX);
        assertThat(testTypePack.getAnnexe()).isEqualTo(DEFAULT_ANNEXE);
    }

    @Test
    @Transactional
    void createTypePackWithExistingId() throws Exception {
        // Create the TypePack with an existing ID
        typePack.setId(1L);
        TypePackDTO typePackDTO = typePackMapper.toDto(typePack);

        int databaseSizeBeforeCreate = typePackRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTypePackMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typePackDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TypePack in the database
        List<TypePack> typePackList = typePackRepository.findAll();
        assertThat(typePackList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTypePacks() throws Exception {
        // Initialize the database
        typePackRepository.saveAndFlush(typePack);

        // Get all the typePackList
        restTypePackMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typePack.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].durer").value(hasItem(DEFAULT_DURER.intValue())))
            .andExpect(jsonPath("$.[*].prix").value(hasItem(DEFAULT_PRIX.intValue())))
            .andExpect(jsonPath("$.[*].annexe").value(hasItem(DEFAULT_ANNEXE.intValue())));
    }

    @Test
    @Transactional
    void getTypePack() throws Exception {
        // Initialize the database
        typePackRepository.saveAndFlush(typePack);

        // Get the typePack
        restTypePackMockMvc
            .perform(get(ENTITY_API_URL_ID, typePack.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(typePack.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.durer").value(DEFAULT_DURER.intValue()))
            .andExpect(jsonPath("$.prix").value(DEFAULT_PRIX.intValue()))
            .andExpect(jsonPath("$.annexe").value(DEFAULT_ANNEXE.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingTypePack() throws Exception {
        // Get the typePack
        restTypePackMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTypePack() throws Exception {
        // Initialize the database
        typePackRepository.saveAndFlush(typePack);

        int databaseSizeBeforeUpdate = typePackRepository.findAll().size();

        // Update the typePack
        TypePack updatedTypePack = typePackRepository.findById(typePack.getId()).get();
        // Disconnect from session so that the updates on updatedTypePack are not directly saved in db
        em.detach(updatedTypePack);
        updatedTypePack.libelle(UPDATED_LIBELLE).durer(UPDATED_DURER).prix(UPDATED_PRIX).annexe(UPDATED_ANNEXE);
        TypePackDTO typePackDTO = typePackMapper.toDto(updatedTypePack);

        restTypePackMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typePackDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typePackDTO))
            )
            .andExpect(status().isOk());

        // Validate the TypePack in the database
        List<TypePack> typePackList = typePackRepository.findAll();
        assertThat(typePackList).hasSize(databaseSizeBeforeUpdate);
        TypePack testTypePack = typePackList.get(typePackList.size() - 1);
        assertThat(testTypePack.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testTypePack.getDurer()).isEqualTo(UPDATED_DURER);
        assertThat(testTypePack.getPrix()).isEqualTo(UPDATED_PRIX);
        assertThat(testTypePack.getAnnexe()).isEqualTo(UPDATED_ANNEXE);
    }

    @Test
    @Transactional
    void putNonExistingTypePack() throws Exception {
        int databaseSizeBeforeUpdate = typePackRepository.findAll().size();
        typePack.setId(count.incrementAndGet());

        // Create the TypePack
        TypePackDTO typePackDTO = typePackMapper.toDto(typePack);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypePackMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typePackDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typePackDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypePack in the database
        List<TypePack> typePackList = typePackRepository.findAll();
        assertThat(typePackList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTypePack() throws Exception {
        int databaseSizeBeforeUpdate = typePackRepository.findAll().size();
        typePack.setId(count.incrementAndGet());

        // Create the TypePack
        TypePackDTO typePackDTO = typePackMapper.toDto(typePack);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypePackMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typePackDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypePack in the database
        List<TypePack> typePackList = typePackRepository.findAll();
        assertThat(typePackList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTypePack() throws Exception {
        int databaseSizeBeforeUpdate = typePackRepository.findAll().size();
        typePack.setId(count.incrementAndGet());

        // Create the TypePack
        TypePackDTO typePackDTO = typePackMapper.toDto(typePack);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypePackMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typePackDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypePack in the database
        List<TypePack> typePackList = typePackRepository.findAll();
        assertThat(typePackList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTypePackWithPatch() throws Exception {
        // Initialize the database
        typePackRepository.saveAndFlush(typePack);

        int databaseSizeBeforeUpdate = typePackRepository.findAll().size();

        // Update the typePack using partial update
        TypePack partialUpdatedTypePack = new TypePack();
        partialUpdatedTypePack.setId(typePack.getId());

        partialUpdatedTypePack.libelle(UPDATED_LIBELLE).durer(UPDATED_DURER).annexe(UPDATED_ANNEXE);

        restTypePackMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypePack.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTypePack))
            )
            .andExpect(status().isOk());

        // Validate the TypePack in the database
        List<TypePack> typePackList = typePackRepository.findAll();
        assertThat(typePackList).hasSize(databaseSizeBeforeUpdate);
        TypePack testTypePack = typePackList.get(typePackList.size() - 1);
        assertThat(testTypePack.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testTypePack.getDurer()).isEqualTo(UPDATED_DURER);
        assertThat(testTypePack.getPrix()).isEqualTo(DEFAULT_PRIX);
        assertThat(testTypePack.getAnnexe()).isEqualTo(UPDATED_ANNEXE);
    }

    @Test
    @Transactional
    void fullUpdateTypePackWithPatch() throws Exception {
        // Initialize the database
        typePackRepository.saveAndFlush(typePack);

        int databaseSizeBeforeUpdate = typePackRepository.findAll().size();

        // Update the typePack using partial update
        TypePack partialUpdatedTypePack = new TypePack();
        partialUpdatedTypePack.setId(typePack.getId());

        partialUpdatedTypePack.libelle(UPDATED_LIBELLE).durer(UPDATED_DURER).prix(UPDATED_PRIX).annexe(UPDATED_ANNEXE);

        restTypePackMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypePack.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTypePack))
            )
            .andExpect(status().isOk());

        // Validate the TypePack in the database
        List<TypePack> typePackList = typePackRepository.findAll();
        assertThat(typePackList).hasSize(databaseSizeBeforeUpdate);
        TypePack testTypePack = typePackList.get(typePackList.size() - 1);
        assertThat(testTypePack.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testTypePack.getDurer()).isEqualTo(UPDATED_DURER);
        assertThat(testTypePack.getPrix()).isEqualTo(UPDATED_PRIX);
        assertThat(testTypePack.getAnnexe()).isEqualTo(UPDATED_ANNEXE);
    }

    @Test
    @Transactional
    void patchNonExistingTypePack() throws Exception {
        int databaseSizeBeforeUpdate = typePackRepository.findAll().size();
        typePack.setId(count.incrementAndGet());

        // Create the TypePack
        TypePackDTO typePackDTO = typePackMapper.toDto(typePack);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypePackMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, typePackDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typePackDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypePack in the database
        List<TypePack> typePackList = typePackRepository.findAll();
        assertThat(typePackList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTypePack() throws Exception {
        int databaseSizeBeforeUpdate = typePackRepository.findAll().size();
        typePack.setId(count.incrementAndGet());

        // Create the TypePack
        TypePackDTO typePackDTO = typePackMapper.toDto(typePack);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypePackMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typePackDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypePack in the database
        List<TypePack> typePackList = typePackRepository.findAll();
        assertThat(typePackList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTypePack() throws Exception {
        int databaseSizeBeforeUpdate = typePackRepository.findAll().size();
        typePack.setId(count.incrementAndGet());

        // Create the TypePack
        TypePackDTO typePackDTO = typePackMapper.toDto(typePack);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypePackMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(typePackDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypePack in the database
        List<TypePack> typePackList = typePackRepository.findAll();
        assertThat(typePackList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTypePack() throws Exception {
        // Initialize the database
        typePackRepository.saveAndFlush(typePack);

        int databaseSizeBeforeDelete = typePackRepository.findAll().size();

        // Delete the typePack
        restTypePackMockMvc
            .perform(delete(ENTITY_API_URL_ID, typePack.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TypePack> typePackList = typePackRepository.findAll();
        assertThat(typePackList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
