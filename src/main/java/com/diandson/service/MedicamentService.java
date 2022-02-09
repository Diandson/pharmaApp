package com.diandson.service;

import com.diandson.domain.Medicament;
import com.diandson.repository.MedicamentRepository;
import com.diandson.service.dto.MedicamentDTO;
import com.diandson.service.mapper.MedicamentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Medicament}.
 */
@Service
@Transactional
public class MedicamentService {

    private final Logger log = LoggerFactory.getLogger(MedicamentService.class);

    private final MedicamentRepository medicamentRepository;

    private final MedicamentMapper medicamentMapper;

    public MedicamentService(MedicamentRepository medicamentRepository, MedicamentMapper medicamentMapper) {
        this.medicamentRepository = medicamentRepository;
        this.medicamentMapper = medicamentMapper;
    }

    /**
     * Save a medicament.
     *
     * @param medicamentDTO the entity to save.
     * @return the persisted entity.
     */
    public MedicamentDTO save(MedicamentDTO medicamentDTO) {
        log.debug("Request to save Medicament : {}", medicamentDTO);
        Medicament medicament = medicamentMapper.toEntity(medicamentDTO);
        medicament = medicamentRepository.save(medicament);
        return medicamentMapper.toDto(medicament);
    }

    /**
     * Partially update a medicament.
     *
     * @param medicamentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MedicamentDTO> partialUpdate(MedicamentDTO medicamentDTO) {
        log.debug("Request to partially update Medicament : {}", medicamentDTO);

        return medicamentRepository
            .findById(medicamentDTO.getId())
            .map(existingMedicament -> {
                medicamentMapper.partialUpdate(existingMedicament, medicamentDTO);

                return existingMedicament;
            })
            .map(medicamentRepository::save)
            .map(medicamentMapper::toDto);
    }

    /**
     * Get all the medicaments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MedicamentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Medicaments");
        return medicamentRepository.findAll(pageable).map(medicamentMapper::toDto);
    }

    /**
     * Get one medicament by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MedicamentDTO> findOne(Long id) {
        log.debug("Request to get Medicament : {}", id);
        return medicamentRepository.findById(id).map(medicamentMapper::toDto);
    }

    /**
     * Delete the medicament by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Medicament : {}", id);
        medicamentRepository.deleteById(id);
    }
}
