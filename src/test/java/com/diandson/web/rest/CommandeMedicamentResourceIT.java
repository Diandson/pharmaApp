package com.diandson.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.diandson.IntegrationTest;
import com.diandson.domain.CommandeMedicament;
import com.diandson.repository.CommandeMedicamentRepository;
import com.diandson.service.dto.CommandeMedicamentDTO;
import com.diandson.service.mapper.CommandeMedicamentMapper;
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
 * Integration tests for the {@link CommandeMedicamentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CommandeMedicamentResourceIT {

    private static final Long DEFAULT_QUANTITE = 1L;
    private static final Long UPDATED_QUANTITE = 2L;

    private static final String ENTITY_API_URL = "/api/commande-medicaments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CommandeMedicamentRepository commandeMedicamentRepository;

    @Autowired
    private CommandeMedicamentMapper commandeMedicamentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCommandeMedicamentMockMvc;

    private CommandeMedicament commandeMedicament;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CommandeMedicament createEntity(EntityManager em) {
        CommandeMedicament commandeMedicament = new CommandeMedicament().quantite(DEFAULT_QUANTITE);
        return commandeMedicament;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CommandeMedicament createUpdatedEntity(EntityManager em) {
        CommandeMedicament commandeMedicament = new CommandeMedicament().quantite(UPDATED_QUANTITE);
        return commandeMedicament;
    }

    @BeforeEach
    public void initTest() {
        commandeMedicament = createEntity(em);
    }

    @Test
    @Transactional
    void createCommandeMedicament() throws Exception {
        int databaseSizeBeforeCreate = commandeMedicamentRepository.findAll().size();
        // Create the CommandeMedicament
        CommandeMedicamentDTO commandeMedicamentDTO = commandeMedicamentMapper.toDto(commandeMedicament);
        restCommandeMedicamentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commandeMedicamentDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CommandeMedicament in the database
        List<CommandeMedicament> commandeMedicamentList = commandeMedicamentRepository.findAll();
        assertThat(commandeMedicamentList).hasSize(databaseSizeBeforeCreate + 1);
        CommandeMedicament testCommandeMedicament = commandeMedicamentList.get(commandeMedicamentList.size() - 1);
        assertThat(testCommandeMedicament.getQuantite()).isEqualTo(DEFAULT_QUANTITE);
    }

    @Test
    @Transactional
    void createCommandeMedicamentWithExistingId() throws Exception {
        // Create the CommandeMedicament with an existing ID
        commandeMedicament.setId(1L);
        CommandeMedicamentDTO commandeMedicamentDTO = commandeMedicamentMapper.toDto(commandeMedicament);

        int databaseSizeBeforeCreate = commandeMedicamentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommandeMedicamentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commandeMedicamentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommandeMedicament in the database
        List<CommandeMedicament> commandeMedicamentList = commandeMedicamentRepository.findAll();
        assertThat(commandeMedicamentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCommandeMedicaments() throws Exception {
        // Initialize the database
        commandeMedicamentRepository.saveAndFlush(commandeMedicament);

        // Get all the commandeMedicamentList
        restCommandeMedicamentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commandeMedicament.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantite").value(hasItem(DEFAULT_QUANTITE.intValue())));
    }

    @Test
    @Transactional
    void getCommandeMedicament() throws Exception {
        // Initialize the database
        commandeMedicamentRepository.saveAndFlush(commandeMedicament);

        // Get the commandeMedicament
        restCommandeMedicamentMockMvc
            .perform(get(ENTITY_API_URL_ID, commandeMedicament.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(commandeMedicament.getId().intValue()))
            .andExpect(jsonPath("$.quantite").value(DEFAULT_QUANTITE.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingCommandeMedicament() throws Exception {
        // Get the commandeMedicament
        restCommandeMedicamentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCommandeMedicament() throws Exception {
        // Initialize the database
        commandeMedicamentRepository.saveAndFlush(commandeMedicament);

        int databaseSizeBeforeUpdate = commandeMedicamentRepository.findAll().size();

        // Update the commandeMedicament
        CommandeMedicament updatedCommandeMedicament = commandeMedicamentRepository.findById(commandeMedicament.getId()).get();
        // Disconnect from session so that the updates on updatedCommandeMedicament are not directly saved in db
        em.detach(updatedCommandeMedicament);
        updatedCommandeMedicament.quantite(UPDATED_QUANTITE);
        CommandeMedicamentDTO commandeMedicamentDTO = commandeMedicamentMapper.toDto(updatedCommandeMedicament);

        restCommandeMedicamentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, commandeMedicamentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commandeMedicamentDTO))
            )
            .andExpect(status().isOk());

        // Validate the CommandeMedicament in the database
        List<CommandeMedicament> commandeMedicamentList = commandeMedicamentRepository.findAll();
        assertThat(commandeMedicamentList).hasSize(databaseSizeBeforeUpdate);
        CommandeMedicament testCommandeMedicament = commandeMedicamentList.get(commandeMedicamentList.size() - 1);
        assertThat(testCommandeMedicament.getQuantite()).isEqualTo(UPDATED_QUANTITE);
    }

    @Test
    @Transactional
    void putNonExistingCommandeMedicament() throws Exception {
        int databaseSizeBeforeUpdate = commandeMedicamentRepository.findAll().size();
        commandeMedicament.setId(count.incrementAndGet());

        // Create the CommandeMedicament
        CommandeMedicamentDTO commandeMedicamentDTO = commandeMedicamentMapper.toDto(commandeMedicament);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommandeMedicamentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, commandeMedicamentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commandeMedicamentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommandeMedicament in the database
        List<CommandeMedicament> commandeMedicamentList = commandeMedicamentRepository.findAll();
        assertThat(commandeMedicamentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCommandeMedicament() throws Exception {
        int databaseSizeBeforeUpdate = commandeMedicamentRepository.findAll().size();
        commandeMedicament.setId(count.incrementAndGet());

        // Create the CommandeMedicament
        CommandeMedicamentDTO commandeMedicamentDTO = commandeMedicamentMapper.toDto(commandeMedicament);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommandeMedicamentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commandeMedicamentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommandeMedicament in the database
        List<CommandeMedicament> commandeMedicamentList = commandeMedicamentRepository.findAll();
        assertThat(commandeMedicamentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCommandeMedicament() throws Exception {
        int databaseSizeBeforeUpdate = commandeMedicamentRepository.findAll().size();
        commandeMedicament.setId(count.incrementAndGet());

        // Create the CommandeMedicament
        CommandeMedicamentDTO commandeMedicamentDTO = commandeMedicamentMapper.toDto(commandeMedicament);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommandeMedicamentMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commandeMedicamentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CommandeMedicament in the database
        List<CommandeMedicament> commandeMedicamentList = commandeMedicamentRepository.findAll();
        assertThat(commandeMedicamentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCommandeMedicamentWithPatch() throws Exception {
        // Initialize the database
        commandeMedicamentRepository.saveAndFlush(commandeMedicament);

        int databaseSizeBeforeUpdate = commandeMedicamentRepository.findAll().size();

        // Update the commandeMedicament using partial update
        CommandeMedicament partialUpdatedCommandeMedicament = new CommandeMedicament();
        partialUpdatedCommandeMedicament.setId(commandeMedicament.getId());

        restCommandeMedicamentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommandeMedicament.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommandeMedicament))
            )
            .andExpect(status().isOk());

        // Validate the CommandeMedicament in the database
        List<CommandeMedicament> commandeMedicamentList = commandeMedicamentRepository.findAll();
        assertThat(commandeMedicamentList).hasSize(databaseSizeBeforeUpdate);
        CommandeMedicament testCommandeMedicament = commandeMedicamentList.get(commandeMedicamentList.size() - 1);
        assertThat(testCommandeMedicament.getQuantite()).isEqualTo(DEFAULT_QUANTITE);
    }

    @Test
    @Transactional
    void fullUpdateCommandeMedicamentWithPatch() throws Exception {
        // Initialize the database
        commandeMedicamentRepository.saveAndFlush(commandeMedicament);

        int databaseSizeBeforeUpdate = commandeMedicamentRepository.findAll().size();

        // Update the commandeMedicament using partial update
        CommandeMedicament partialUpdatedCommandeMedicament = new CommandeMedicament();
        partialUpdatedCommandeMedicament.setId(commandeMedicament.getId());

        partialUpdatedCommandeMedicament.quantite(UPDATED_QUANTITE);

        restCommandeMedicamentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommandeMedicament.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommandeMedicament))
            )
            .andExpect(status().isOk());

        // Validate the CommandeMedicament in the database
        List<CommandeMedicament> commandeMedicamentList = commandeMedicamentRepository.findAll();
        assertThat(commandeMedicamentList).hasSize(databaseSizeBeforeUpdate);
        CommandeMedicament testCommandeMedicament = commandeMedicamentList.get(commandeMedicamentList.size() - 1);
        assertThat(testCommandeMedicament.getQuantite()).isEqualTo(UPDATED_QUANTITE);
    }

    @Test
    @Transactional
    void patchNonExistingCommandeMedicament() throws Exception {
        int databaseSizeBeforeUpdate = commandeMedicamentRepository.findAll().size();
        commandeMedicament.setId(count.incrementAndGet());

        // Create the CommandeMedicament
        CommandeMedicamentDTO commandeMedicamentDTO = commandeMedicamentMapper.toDto(commandeMedicament);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommandeMedicamentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, commandeMedicamentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commandeMedicamentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommandeMedicament in the database
        List<CommandeMedicament> commandeMedicamentList = commandeMedicamentRepository.findAll();
        assertThat(commandeMedicamentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCommandeMedicament() throws Exception {
        int databaseSizeBeforeUpdate = commandeMedicamentRepository.findAll().size();
        commandeMedicament.setId(count.incrementAndGet());

        // Create the CommandeMedicament
        CommandeMedicamentDTO commandeMedicamentDTO = commandeMedicamentMapper.toDto(commandeMedicament);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommandeMedicamentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commandeMedicamentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommandeMedicament in the database
        List<CommandeMedicament> commandeMedicamentList = commandeMedicamentRepository.findAll();
        assertThat(commandeMedicamentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCommandeMedicament() throws Exception {
        int databaseSizeBeforeUpdate = commandeMedicamentRepository.findAll().size();
        commandeMedicament.setId(count.incrementAndGet());

        // Create the CommandeMedicament
        CommandeMedicamentDTO commandeMedicamentDTO = commandeMedicamentMapper.toDto(commandeMedicament);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommandeMedicamentMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commandeMedicamentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CommandeMedicament in the database
        List<CommandeMedicament> commandeMedicamentList = commandeMedicamentRepository.findAll();
        assertThat(commandeMedicamentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCommandeMedicament() throws Exception {
        // Initialize the database
        commandeMedicamentRepository.saveAndFlush(commandeMedicament);

        int databaseSizeBeforeDelete = commandeMedicamentRepository.findAll().size();

        // Delete the commandeMedicament
        restCommandeMedicamentMockMvc
            .perform(delete(ENTITY_API_URL_ID, commandeMedicament.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CommandeMedicament> commandeMedicamentList = commandeMedicamentRepository.findAll();
        assertThat(commandeMedicamentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
