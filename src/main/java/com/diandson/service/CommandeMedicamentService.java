package com.diandson.service;

import com.diandson.domain.CommandeMedicament;
import com.diandson.repository.CommandeMedicamentRepository;
import com.diandson.service.dto.CommandeMedicamentDTO;
import com.diandson.service.mapper.CommandeMedicamentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CommandeMedicament}.
 */
@Service
@Transactional
public class CommandeMedicamentService {

    private final Logger log = LoggerFactory.getLogger(CommandeMedicamentService.class);

    private final CommandeMedicamentRepository commandeMedicamentRepository;

    private final CommandeMedicamentMapper commandeMedicamentMapper;

    public CommandeMedicamentService(
        CommandeMedicamentRepository commandeMedicamentRepository,
        CommandeMedicamentMapper commandeMedicamentMapper
    ) {
        this.commandeMedicamentRepository = commandeMedicamentRepository;
        this.commandeMedicamentMapper = commandeMedicamentMapper;
    }

    /**
     * Save a commandeMedicament.
     *
     * @param commandeMedicamentDTO the entity to save.
     * @return the persisted entity.
     */
    public CommandeMedicamentDTO save(CommandeMedicamentDTO commandeMedicamentDTO) {
        log.debug("Request to save CommandeMedicament : {}", commandeMedicamentDTO);
        CommandeMedicament commandeMedicament = commandeMedicamentMapper.toEntity(commandeMedicamentDTO);
        commandeMedicament = commandeMedicamentRepository.save(commandeMedicament);
        return commandeMedicamentMapper.toDto(commandeMedicament);
    }

    /**
     * Partially update a commandeMedicament.
     *
     * @param commandeMedicamentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CommandeMedicamentDTO> partialUpdate(CommandeMedicamentDTO commandeMedicamentDTO) {
        log.debug("Request to partially update CommandeMedicament : {}", commandeMedicamentDTO);

        return commandeMedicamentRepository
            .findById(commandeMedicamentDTO.getId())
            .map(existingCommandeMedicament -> {
                commandeMedicamentMapper.partialUpdate(existingCommandeMedicament, commandeMedicamentDTO);

                return existingCommandeMedicament;
            })
            .map(commandeMedicamentRepository::save)
            .map(commandeMedicamentMapper::toDto);
    }

    /**
     * Get all the commandeMedicaments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CommandeMedicamentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CommandeMedicaments");
        return commandeMedicamentRepository.findAll(pageable).map(commandeMedicamentMapper::toDto);
    }

    /**
     * Get one commandeMedicament by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CommandeMedicamentDTO> findOne(Long id) {
        log.debug("Request to get CommandeMedicament : {}", id);
        return commandeMedicamentRepository.findById(id).map(commandeMedicamentMapper::toDto);
    }

    /**
     * Delete the commandeMedicament by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CommandeMedicament : {}", id);
        commandeMedicamentRepository.deleteById(id);
    }
}
