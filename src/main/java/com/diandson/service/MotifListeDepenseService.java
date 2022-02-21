package com.diandson.service;

import com.diandson.domain.MotifListeDepense;
import com.diandson.repository.MotifListeDepenseRepository;
import com.diandson.service.dto.MotifListeDepenseDTO;
import com.diandson.service.mapper.MotifListeDepenseMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link MotifListeDepense}.
 */
@Service
@Transactional
public class MotifListeDepenseService {

    private final Logger log = LoggerFactory.getLogger(MotifListeDepenseService.class);

    private final MotifListeDepenseRepository motifListeDepenseRepository;

    private final MotifListeDepenseMapper motifListeDepenseMapper;

    public MotifListeDepenseService(
        MotifListeDepenseRepository motifListeDepenseRepository,
        MotifListeDepenseMapper motifListeDepenseMapper
    ) {
        this.motifListeDepenseRepository = motifListeDepenseRepository;
        this.motifListeDepenseMapper = motifListeDepenseMapper;
    }

    /**
     * Save a motifListeDepense.
     *
     * @param motifListeDepenseDTO the entity to save.
     * @return the persisted entity.
     */
    public MotifListeDepenseDTO save(MotifListeDepenseDTO motifListeDepenseDTO) {
        log.debug("Request to save MotifListeDepense : {}", motifListeDepenseDTO);
        MotifListeDepense motifListeDepense = motifListeDepenseMapper.toEntity(motifListeDepenseDTO);
        motifListeDepense = motifListeDepenseRepository.save(motifListeDepense);
        return motifListeDepenseMapper.toDto(motifListeDepense);
    }

    /**
     * Partially update a motifListeDepense.
     *
     * @param motifListeDepenseDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MotifListeDepenseDTO> partialUpdate(MotifListeDepenseDTO motifListeDepenseDTO) {
        log.debug("Request to partially update MotifListeDepense : {}", motifListeDepenseDTO);

        return motifListeDepenseRepository
            .findById(motifListeDepenseDTO.getId())
            .map(existingMotifListeDepense -> {
                motifListeDepenseMapper.partialUpdate(existingMotifListeDepense, motifListeDepenseDTO);

                return existingMotifListeDepense;
            })
            .map(motifListeDepenseRepository::save)
            .map(motifListeDepenseMapper::toDto);
    }

    /**
     * Get all the motifListeDepenses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MotifListeDepenseDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MotifListeDepenses");
        return motifListeDepenseRepository.findAll(pageable).map(motifListeDepenseMapper::toDto);
    }

    /**
     * Get one motifListeDepense by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MotifListeDepenseDTO> findOne(Long id) {
        log.debug("Request to get MotifListeDepense : {}", id);
        return motifListeDepenseRepository.findById(id).map(motifListeDepenseMapper::toDto);
    }

    /**
     * Delete the motifListeDepense by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete MotifListeDepense : {}", id);
        motifListeDepenseRepository.deleteById(id);
    }
}
