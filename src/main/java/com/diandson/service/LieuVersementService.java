package com.diandson.service;

import com.diandson.domain.LieuVersement;
import com.diandson.repository.LieuVersementRepository;
import com.diandson.service.dto.LieuVersementDTO;
import com.diandson.service.mapper.LieuVersementMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LieuVersement}.
 */
@Service
@Transactional
public class LieuVersementService {

    private final Logger log = LoggerFactory.getLogger(LieuVersementService.class);

    private final LieuVersementRepository lieuVersementRepository;

    private final LieuVersementMapper lieuVersementMapper;

    public LieuVersementService(LieuVersementRepository lieuVersementRepository, LieuVersementMapper lieuVersementMapper) {
        this.lieuVersementRepository = lieuVersementRepository;
        this.lieuVersementMapper = lieuVersementMapper;
    }

    /**
     * Save a lieuVersement.
     *
     * @param lieuVersementDTO the entity to save.
     * @return the persisted entity.
     */
    public LieuVersementDTO save(LieuVersementDTO lieuVersementDTO) {
        log.debug("Request to save LieuVersement : {}", lieuVersementDTO);
        LieuVersement lieuVersement = lieuVersementMapper.toEntity(lieuVersementDTO);
        lieuVersement = lieuVersementRepository.save(lieuVersement);
        return lieuVersementMapper.toDto(lieuVersement);
    }

    /**
     * Partially update a lieuVersement.
     *
     * @param lieuVersementDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<LieuVersementDTO> partialUpdate(LieuVersementDTO lieuVersementDTO) {
        log.debug("Request to partially update LieuVersement : {}", lieuVersementDTO);

        return lieuVersementRepository
            .findById(lieuVersementDTO.getId())
            .map(existingLieuVersement -> {
                lieuVersementMapper.partialUpdate(existingLieuVersement, lieuVersementDTO);

                return existingLieuVersement;
            })
            .map(lieuVersementRepository::save)
            .map(lieuVersementMapper::toDto);
    }

    /**
     * Get all the lieuVersements.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<LieuVersementDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LieuVersements");
        return lieuVersementRepository.findAll(pageable).map(lieuVersementMapper::toDto);
    }

    /**
     * Get one lieuVersement by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LieuVersementDTO> findOne(Long id) {
        log.debug("Request to get LieuVersement : {}", id);
        return lieuVersementRepository.findById(id).map(lieuVersementMapper::toDto);
    }

    /**
     * Delete the lieuVersement by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete LieuVersement : {}", id);
        lieuVersementRepository.deleteById(id);
    }
}
