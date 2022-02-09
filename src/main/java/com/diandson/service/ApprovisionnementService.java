package com.diandson.service;

import com.diandson.domain.Approvisionnement;
import com.diandson.repository.ApprovisionnementRepository;
import com.diandson.service.dto.ApprovisionnementDTO;
import com.diandson.service.mapper.ApprovisionnementMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Approvisionnement}.
 */
@Service
@Transactional
public class ApprovisionnementService {

    private final Logger log = LoggerFactory.getLogger(ApprovisionnementService.class);

    private final ApprovisionnementRepository approvisionnementRepository;

    private final ApprovisionnementMapper approvisionnementMapper;

    public ApprovisionnementService(
        ApprovisionnementRepository approvisionnementRepository,
        ApprovisionnementMapper approvisionnementMapper
    ) {
        this.approvisionnementRepository = approvisionnementRepository;
        this.approvisionnementMapper = approvisionnementMapper;
    }

    /**
     * Save a approvisionnement.
     *
     * @param approvisionnementDTO the entity to save.
     * @return the persisted entity.
     */
    public ApprovisionnementDTO save(ApprovisionnementDTO approvisionnementDTO) {
        log.debug("Request to save Approvisionnement : {}", approvisionnementDTO);
        Approvisionnement approvisionnement = approvisionnementMapper.toEntity(approvisionnementDTO);
        approvisionnement = approvisionnementRepository.save(approvisionnement);
        return approvisionnementMapper.toDto(approvisionnement);
    }

    /**
     * Partially update a approvisionnement.
     *
     * @param approvisionnementDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ApprovisionnementDTO> partialUpdate(ApprovisionnementDTO approvisionnementDTO) {
        log.debug("Request to partially update Approvisionnement : {}", approvisionnementDTO);

        return approvisionnementRepository
            .findById(approvisionnementDTO.getId())
            .map(existingApprovisionnement -> {
                approvisionnementMapper.partialUpdate(existingApprovisionnement, approvisionnementDTO);

                return existingApprovisionnement;
            })
            .map(approvisionnementRepository::save)
            .map(approvisionnementMapper::toDto);
    }

    /**
     * Get all the approvisionnements.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ApprovisionnementDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Approvisionnements");
        return approvisionnementRepository.findAll(pageable).map(approvisionnementMapper::toDto);
    }

    /**
     * Get one approvisionnement by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ApprovisionnementDTO> findOne(Long id) {
        log.debug("Request to get Approvisionnement : {}", id);
        return approvisionnementRepository.findById(id).map(approvisionnementMapper::toDto);
    }

    /**
     * Delete the approvisionnement by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Approvisionnement : {}", id);
        approvisionnementRepository.deleteById(id);
    }
}
