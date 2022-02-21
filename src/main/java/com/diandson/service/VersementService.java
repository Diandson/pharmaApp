package com.diandson.service;

import com.diandson.domain.Versement;
import com.diandson.repository.VersementRepository;
import com.diandson.service.dto.VersementDTO;
import com.diandson.service.mapper.VersementMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Versement}.
 */
@Service
@Transactional
public class VersementService {

    private final Logger log = LoggerFactory.getLogger(VersementService.class);

    private final VersementRepository versementRepository;

    private final VersementMapper versementMapper;

    public VersementService(VersementRepository versementRepository, VersementMapper versementMapper) {
        this.versementRepository = versementRepository;
        this.versementMapper = versementMapper;
    }

    /**
     * Save a versement.
     *
     * @param versementDTO the entity to save.
     * @return the persisted entity.
     */
    public VersementDTO save(VersementDTO versementDTO) {
        log.debug("Request to save Versement : {}", versementDTO);
        Versement versement = versementMapper.toEntity(versementDTO);
        versement = versementRepository.save(versement);
        return versementMapper.toDto(versement);
    }

    /**
     * Partially update a versement.
     *
     * @param versementDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<VersementDTO> partialUpdate(VersementDTO versementDTO) {
        log.debug("Request to partially update Versement : {}", versementDTO);

        return versementRepository
            .findById(versementDTO.getId())
            .map(existingVersement -> {
                versementMapper.partialUpdate(existingVersement, versementDTO);

                return existingVersement;
            })
            .map(versementRepository::save)
            .map(versementMapper::toDto);
    }

    /**
     * Get all the versements.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<VersementDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Versements");
        return versementRepository.findAll(pageable).map(versementMapper::toDto);
    }

    /**
     * Get one versement by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<VersementDTO> findOne(Long id) {
        log.debug("Request to get Versement : {}", id);
        return versementRepository.findById(id).map(versementMapper::toDto);
    }

    /**
     * Delete the versement by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Versement : {}", id);
        versementRepository.deleteById(id);
    }
}
