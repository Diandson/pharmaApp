package com.diandson.service;

import com.diandson.domain.Inventaire;
import com.diandson.repository.InventaireRepository;
import com.diandson.service.dto.InventaireDTO;
import com.diandson.service.mapper.InventaireMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Inventaire}.
 */
@Service
@Transactional
public class InventaireService {

    private final Logger log = LoggerFactory.getLogger(InventaireService.class);

    private final InventaireRepository inventaireRepository;

    private final InventaireMapper inventaireMapper;

    public InventaireService(InventaireRepository inventaireRepository, InventaireMapper inventaireMapper) {
        this.inventaireRepository = inventaireRepository;
        this.inventaireMapper = inventaireMapper;
    }

    /**
     * Save a inventaire.
     *
     * @param inventaireDTO the entity to save.
     * @return the persisted entity.
     */
    public InventaireDTO save(InventaireDTO inventaireDTO) {
        log.debug("Request to save Inventaire : {}", inventaireDTO);
        Inventaire inventaire = inventaireMapper.toEntity(inventaireDTO);
        inventaire = inventaireRepository.save(inventaire);
        return inventaireMapper.toDto(inventaire);
    }

    /**
     * Partially update a inventaire.
     *
     * @param inventaireDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<InventaireDTO> partialUpdate(InventaireDTO inventaireDTO) {
        log.debug("Request to partially update Inventaire : {}", inventaireDTO);

        return inventaireRepository
            .findById(inventaireDTO.getId())
            .map(existingInventaire -> {
                inventaireMapper.partialUpdate(existingInventaire, inventaireDTO);

                return existingInventaire;
            })
            .map(inventaireRepository::save)
            .map(inventaireMapper::toDto);
    }

    /**
     * Get all the inventaires.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<InventaireDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Inventaires");
        return inventaireRepository.findAll(pageable).map(inventaireMapper::toDto);
    }

    /**
     * Get one inventaire by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InventaireDTO> findOne(Long id) {
        log.debug("Request to get Inventaire : {}", id);
        return inventaireRepository.findById(id).map(inventaireMapper::toDto);
    }

    /**
     * Delete the inventaire by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Inventaire : {}", id);
        inventaireRepository.deleteById(id);
    }
}
