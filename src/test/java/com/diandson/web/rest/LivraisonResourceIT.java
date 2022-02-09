package com.diandson.web.rest;

import static com.diandson.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.diandson.IntegrationTest;
import com.diandson.domain.Livraison;
import com.diandson.repository.LivraisonRepository;
import com.diandson.service.dto.LivraisonDTO;
import com.diandson.service.mapper.LivraisonMapper;
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
 * Integration tests for the {@link LivraisonResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LivraisonResourceIT {

    private static final String DEFAULT_NUMERO = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE_LIVRAISON = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_LIVRAISON = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/livraisons";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LivraisonRepository livraisonRepository;

    @Autowired
    private LivraisonMapper livraisonMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLivraisonMockMvc;

    private Livraison livraison;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Livraison createEntity(EntityManager em) {
        Livraison livraison = new Livraison().numero(DEFAULT_NUMERO).dateLivraison(DEFAULT_DATE_LIVRAISON);
        return livraison;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Livraison createUpdatedEntity(EntityManager em) {
        Livraison livraison = new Livraison().numero(UPDATED_NUMERO).dateLivraison(UPDATED_DATE_LIVRAISON);
        return livraison;
    }

    @BeforeEach
    public void initTest() {
        livraison = createEntity(em);
    }

    @Test
    @Transactional
    void createLivraison() throws Exception {
        int databaseSizeBeforeCreate = livraisonRepository.findAll().size();
        // Create the Livraison
        LivraisonDTO livraisonDTO = livraisonMapper.toDto(livraison);
        restLivraisonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(livraisonDTO)))
            .andExpect(status().isCreated());

        // Validate the Livraison in the database
        List<Livraison> livraisonList = livraisonRepository.findAll();
        assertThat(livraisonList).hasSize(databaseSizeBeforeCreate + 1);
        Livraison testLivraison = livraisonList.get(livraisonList.size() - 1);
        assertThat(testLivraison.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testLivraison.getDateLivraison()).isEqualTo(DEFAULT_DATE_LIVRAISON);
    }

    @Test
    @Transactional
    void createLivraisonWithExistingId() throws Exception {
        // Create the Livraison with an existing ID
        livraison.setId(1L);
        LivraisonDTO livraisonDTO = livraisonMapper.toDto(livraison);

        int databaseSizeBeforeCreate = livraisonRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLivraisonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(livraisonDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Livraison in the database
        List<Livraison> livraisonList = livraisonRepository.findAll();
        assertThat(livraisonList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLivraisons() throws Exception {
        // Initialize the database
        livraisonRepository.saveAndFlush(livraison);

        // Get all the livraisonList
        restLivraisonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(livraison.getId().intValue())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].dateLivraison").value(hasItem(sameInstant(DEFAULT_DATE_LIVRAISON))));
    }

    @Test
    @Transactional
    void getLivraison() throws Exception {
        // Initialize the database
        livraisonRepository.saveAndFlush(livraison);

        // Get the livraison
        restLivraisonMockMvc
            .perform(get(ENTITY_API_URL_ID, livraison.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(livraison.getId().intValue()))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO))
            .andExpect(jsonPath("$.dateLivraison").value(sameInstant(DEFAULT_DATE_LIVRAISON)));
    }

    @Test
    @Transactional
    void getNonExistingLivraison() throws Exception {
        // Get the livraison
        restLivraisonMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLivraison() throws Exception {
        // Initialize the database
        livraisonRepository.saveAndFlush(livraison);

        int databaseSizeBeforeUpdate = livraisonRepository.findAll().size();

        // Update the livraison
        Livraison updatedLivraison = livraisonRepository.findById(livraison.getId()).get();
        // Disconnect from session so that the updates on updatedLivraison are not directly saved in db
        em.detach(updatedLivraison);
        updatedLivraison.numero(UPDATED_NUMERO).dateLivraison(UPDATED_DATE_LIVRAISON);
        LivraisonDTO livraisonDTO = livraisonMapper.toDto(updatedLivraison);

        restLivraisonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, livraisonDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(livraisonDTO))
            )
            .andExpect(status().isOk());

        // Validate the Livraison in the database
        List<Livraison> livraisonList = livraisonRepository.findAll();
        assertThat(livraisonList).hasSize(databaseSizeBeforeUpdate);
        Livraison testLivraison = livraisonList.get(livraisonList.size() - 1);
        assertThat(testLivraison.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testLivraison.getDateLivraison()).isEqualTo(UPDATED_DATE_LIVRAISON);
    }

    @Test
    @Transactional
    void putNonExistingLivraison() throws Exception {
        int databaseSizeBeforeUpdate = livraisonRepository.findAll().size();
        livraison.setId(count.incrementAndGet());

        // Create the Livraison
        LivraisonDTO livraisonDTO = livraisonMapper.toDto(livraison);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLivraisonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, livraisonDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(livraisonDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Livraison in the database
        List<Livraison> livraisonList = livraisonRepository.findAll();
        assertThat(livraisonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLivraison() throws Exception {
        int databaseSizeBeforeUpdate = livraisonRepository.findAll().size();
        livraison.setId(count.incrementAndGet());

        // Create the Livraison
        LivraisonDTO livraisonDTO = livraisonMapper.toDto(livraison);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLivraisonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(livraisonDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Livraison in the database
        List<Livraison> livraisonList = livraisonRepository.findAll();
        assertThat(livraisonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLivraison() throws Exception {
        int databaseSizeBeforeUpdate = livraisonRepository.findAll().size();
        livraison.setId(count.incrementAndGet());

        // Create the Livraison
        LivraisonDTO livraisonDTO = livraisonMapper.toDto(livraison);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLivraisonMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(livraisonDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Livraison in the database
        List<Livraison> livraisonList = livraisonRepository.findAll();
        assertThat(livraisonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLivraisonWithPatch() throws Exception {
        // Initialize the database
        livraisonRepository.saveAndFlush(livraison);

        int databaseSizeBeforeUpdate = livraisonRepository.findAll().size();

        // Update the livraison using partial update
        Livraison partialUpdatedLivraison = new Livraison();
        partialUpdatedLivraison.setId(livraison.getId());

        restLivraisonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLivraison.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLivraison))
            )
            .andExpect(status().isOk());

        // Validate the Livraison in the database
        List<Livraison> livraisonList = livraisonRepository.findAll();
        assertThat(livraisonList).hasSize(databaseSizeBeforeUpdate);
        Livraison testLivraison = livraisonList.get(livraisonList.size() - 1);
        assertThat(testLivraison.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testLivraison.getDateLivraison()).isEqualTo(DEFAULT_DATE_LIVRAISON);
    }

    @Test
    @Transactional
    void fullUpdateLivraisonWithPatch() throws Exception {
        // Initialize the database
        livraisonRepository.saveAndFlush(livraison);

        int databaseSizeBeforeUpdate = livraisonRepository.findAll().size();

        // Update the livraison using partial update
        Livraison partialUpdatedLivraison = new Livraison();
        partialUpdatedLivraison.setId(livraison.getId());

        partialUpdatedLivraison.numero(UPDATED_NUMERO).dateLivraison(UPDATED_DATE_LIVRAISON);

        restLivraisonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLivraison.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLivraison))
            )
            .andExpect(status().isOk());

        // Validate the Livraison in the database
        List<Livraison> livraisonList = livraisonRepository.findAll();
        assertThat(livraisonList).hasSize(databaseSizeBeforeUpdate);
        Livraison testLivraison = livraisonList.get(livraisonList.size() - 1);
        assertThat(testLivraison.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testLivraison.getDateLivraison()).isEqualTo(UPDATED_DATE_LIVRAISON);
    }

    @Test
    @Transactional
    void patchNonExistingLivraison() throws Exception {
        int databaseSizeBeforeUpdate = livraisonRepository.findAll().size();
        livraison.setId(count.incrementAndGet());

        // Create the Livraison
        LivraisonDTO livraisonDTO = livraisonMapper.toDto(livraison);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLivraisonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, livraisonDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(livraisonDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Livraison in the database
        List<Livraison> livraisonList = livraisonRepository.findAll();
        assertThat(livraisonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLivraison() throws Exception {
        int databaseSizeBeforeUpdate = livraisonRepository.findAll().size();
        livraison.setId(count.incrementAndGet());

        // Create the Livraison
        LivraisonDTO livraisonDTO = livraisonMapper.toDto(livraison);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLivraisonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(livraisonDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Livraison in the database
        List<Livraison> livraisonList = livraisonRepository.findAll();
        assertThat(livraisonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLivraison() throws Exception {
        int databaseSizeBeforeUpdate = livraisonRepository.findAll().size();
        livraison.setId(count.incrementAndGet());

        // Create the Livraison
        LivraisonDTO livraisonDTO = livraisonMapper.toDto(livraison);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLivraisonMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(livraisonDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Livraison in the database
        List<Livraison> livraisonList = livraisonRepository.findAll();
        assertThat(livraisonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLivraison() throws Exception {
        // Initialize the database
        livraisonRepository.saveAndFlush(livraison);

        int databaseSizeBeforeDelete = livraisonRepository.findAll().size();

        // Delete the livraison
        restLivraisonMockMvc
            .perform(delete(ENTITY_API_URL_ID, livraison.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Livraison> livraisonList = livraisonRepository.findAll();
        assertThat(livraisonList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
