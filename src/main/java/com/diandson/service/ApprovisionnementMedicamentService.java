package com.diandson.service;

import com.diandson.domain.ApprovisionnementMedicament;
import com.diandson.repository.ApprovisionnementMedicamentRepository;
import com.diandson.service.dto.ApprovisionnementMedicamentDTO;
import com.diandson.service.mapper.ApprovisionnementMedicamentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ApprovisionnementMedicament}.
 */
@Service
@Transactional
public class ApprovisionnementMedicamentService {

    private final Logger log = LoggerFactory.getLogger(ApprovisionnementMedicamentService.class);

    private final ApprovisionnementMedicamentRepository approvisionnementMedicamentRepository;

    private final ApprovisionnementMedicamentMapper approvisionnementMedicamentMapper;

    public ApprovisionnementMedicamentService(
        ApprovisionnementMedicamentRepository approvisionnementMedicamentRepository,
        ApprovisionnementMedicamentMapper approvisionnementMedicamentMapper
    ) {
        this.approvisionnementMedicamentRepository = approvisionnementMedicamentRepository;
        this.approvisionnementMedicamentMapper = approvisionnementMedicamentMapper;
    }

    /**
     * Save a approvisionnementMedicament.
     *
     * @param approvisionnementMedicamentDTO the entity to save.
     * @return the persisted entity.
     */
    public ApprovisionnementMedicamentDTO save(ApprovisionnementMedicamentDTO approvisionnementMedicamentDTO) {
        log.debug("Request to save ApprovisionnementMedicament : {}", approvisionnementMedicamentDTO);
        ApprovisionnementMedicament approvisionnementMedicament = approvisionnementMedicamentMapper.toEntity(
            approvisionnementMedicamentDTO
        );
        approvisionnementMedicament = approvisionnementMedicamentRepository.save(approvisionnementMedicament);
        return approvisionnementMedicamentMapper.toDto(approvisionnementMedicament);
    }

    /**
     * Partially update a approvisionnementMedicament.
     *
     * @param approvisionnementMedicamentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ApprovisionnementMedicamentDTO> partialUpdate(ApprovisionnementMedicamentDTO approvisionnementMedicamentDTO) {
        log.debug("Request to partially update ApprovisionnementMedicament : {}", approvisionnementMedicamentDTO);

        return approvisionnementMedicamentRepository
            .findById(approvisionnementMedicamentDTO.getId())
            .map(existingApprovisionnementMedicament -> {
                approvisionnementMedicamentMapper.partialUpdate(existingApprovisionnementMedicament, approvisionnementMedicamentDTO);

                return existingApprovisionnementMedicament;
            })
            .map(approvisionnementMedicamentRepository::save)
            .map(approvisionnementMedicamentMapper::toDto);
    }

    /**
     * Get all the approvisionnementMedicaments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ApprovisionnementMedicamentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ApprovisionnementMedicaments");
        return approvisionnementMedicamentRepository.findAll(pageable).map(approvisionnementMedicamentMapper::toDto);
    }

    /**
     * Get one approvisionnementMedicament by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ApprovisionnementMedicamentDTO> findOne(Long id) {
        log.debug("Request to get ApprovisionnementMedicament : {}", id);
        return approvisionnementMedicamentRepository.findById(id).map(approvisionnementMedicamentMapper::toDto);
    }

    /**
     * Delete the approvisionnementMedicament by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ApprovisionnementMedicament : {}", id);
        approvisionnementMedicamentRepository.deleteById(id);
    }
}
