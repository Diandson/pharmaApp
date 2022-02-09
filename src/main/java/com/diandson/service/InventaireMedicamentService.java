package com.diandson.service;

import com.diandson.domain.InventaireMedicament;
import com.diandson.repository.InventaireMedicamentRepository;
import com.diandson.service.dto.InventaireMedicamentDTO;
import com.diandson.service.mapper.InventaireMedicamentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link InventaireMedicament}.
 */
@Service
@Transactional
public class InventaireMedicamentService {

    private final Logger log = LoggerFactory.getLogger(InventaireMedicamentService.class);

    private final InventaireMedicamentRepository inventaireMedicamentRepository;

    private final InventaireMedicamentMapper inventaireMedicamentMapper;

    public InventaireMedicamentService(
        InventaireMedicamentRepository inventaireMedicamentRepository,
        InventaireMedicamentMapper inventaireMedicamentMapper
    ) {
        this.inventaireMedicamentRepository = inventaireMedicamentRepository;
        this.inventaireMedicamentMapper = inventaireMedicamentMapper;
    }

    /**
     * Save a inventaireMedicament.
     *
     * @param inventaireMedicamentDTO the entity to save.
     * @return the persisted entity.
     */
    public InventaireMedicamentDTO save(InventaireMedicamentDTO inventaireMedicamentDTO) {
        log.debug("Request to save InventaireMedicament : {}", inventaireMedicamentDTO);
        InventaireMedicament inventaireMedicament = inventaireMedicamentMapper.toEntity(inventaireMedicamentDTO);
        inventaireMedicament = inventaireMedicamentRepository.save(inventaireMedicament);
        return inventaireMedicamentMapper.toDto(inventaireMedicament);
    }

    /**
     * Partially update a inventaireMedicament.
     *
     * @param inventaireMedicamentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<InventaireMedicamentDTO> partialUpdate(InventaireMedicamentDTO inventaireMedicamentDTO) {
        log.debug("Request to partially update InventaireMedicament : {}", inventaireMedicamentDTO);

        return inventaireMedicamentRepository
            .findById(inventaireMedicamentDTO.getId())
            .map(existingInventaireMedicament -> {
                inventaireMedicamentMapper.partialUpdate(existingInventaireMedicament, inventaireMedicamentDTO);

                return existingInventaireMedicament;
            })
            .map(inventaireMedicamentRepository::save)
            .map(inventaireMedicamentMapper::toDto);
    }

    /**
     * Get all the inventaireMedicaments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<InventaireMedicamentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all InventaireMedicaments");
        return inventaireMedicamentRepository.findAll(pageable).map(inventaireMedicamentMapper::toDto);
    }

    /**
     * Get one inventaireMedicament by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InventaireMedicamentDTO> findOne(Long id) {
        log.debug("Request to get InventaireMedicament : {}", id);
        return inventaireMedicamentRepository.findById(id).map(inventaireMedicamentMapper::toDto);
    }

    /**
     * Delete the inventaireMedicament by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete InventaireMedicament : {}", id);
        inventaireMedicamentRepository.deleteById(id);
    }
}
