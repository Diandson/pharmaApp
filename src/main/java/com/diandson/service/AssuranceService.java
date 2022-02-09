package com.diandson.service;

import com.diandson.domain.Assurance;
import com.diandson.repository.AssuranceRepository;
import com.diandson.service.dto.AssuranceDTO;
import com.diandson.service.mapper.AssuranceMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Assurance}.
 */
@Service
@Transactional
public class AssuranceService {

    private final Logger log = LoggerFactory.getLogger(AssuranceService.class);

    private final AssuranceRepository assuranceRepository;

    private final AssuranceMapper assuranceMapper;

    public AssuranceService(AssuranceRepository assuranceRepository, AssuranceMapper assuranceMapper) {
        this.assuranceRepository = assuranceRepository;
        this.assuranceMapper = assuranceMapper;
    }

    /**
     * Save a assurance.
     *
     * @param assuranceDTO the entity to save.
     * @return the persisted entity.
     */
    public AssuranceDTO save(AssuranceDTO assuranceDTO) {
        log.debug("Request to save Assurance : {}", assuranceDTO);
        Assurance assurance = assuranceMapper.toEntity(assuranceDTO);
        assurance = assuranceRepository.save(assurance);
        return assuranceMapper.toDto(assurance);
    }

    /**
     * Partially update a assurance.
     *
     * @param assuranceDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AssuranceDTO> partialUpdate(AssuranceDTO assuranceDTO) {
        log.debug("Request to partially update Assurance : {}", assuranceDTO);

        return assuranceRepository
            .findById(assuranceDTO.getId())
            .map(existingAssurance -> {
                assuranceMapper.partialUpdate(existingAssurance, assuranceDTO);

                return existingAssurance;
            })
            .map(assuranceRepository::save)
            .map(assuranceMapper::toDto);
    }

    /**
     * Get all the assurances.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AssuranceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Assurances");
        return assuranceRepository.findAll(pageable).map(assuranceMapper::toDto);
    }

    /**
     * Get one assurance by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AssuranceDTO> findOne(Long id) {
        log.debug("Request to get Assurance : {}", id);
        return assuranceRepository.findById(id).map(assuranceMapper::toDto);
    }

    /**
     * Delete the assurance by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Assurance : {}", id);
        assuranceRepository.deleteById(id);
    }
}
