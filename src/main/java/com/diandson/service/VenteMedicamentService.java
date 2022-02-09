package com.diandson.service;

import com.diandson.domain.VenteMedicament;
import com.diandson.repository.VenteMedicamentRepository;
import com.diandson.service.dto.VenteMedicamentDTO;
import com.diandson.service.mapper.VenteMedicamentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link VenteMedicament}.
 */
@Service
@Transactional
public class VenteMedicamentService {

    private final Logger log = LoggerFactory.getLogger(VenteMedicamentService.class);

    private final VenteMedicamentRepository venteMedicamentRepository;

    private final VenteMedicamentMapper venteMedicamentMapper;

    public VenteMedicamentService(VenteMedicamentRepository venteMedicamentRepository, VenteMedicamentMapper venteMedicamentMapper) {
        this.venteMedicamentRepository = venteMedicamentRepository;
        this.venteMedicamentMapper = venteMedicamentMapper;
    }

    /**
     * Save a venteMedicament.
     *
     * @param venteMedicamentDTO the entity to save.
     * @return the persisted entity.
     */
    public VenteMedicamentDTO save(VenteMedicamentDTO venteMedicamentDTO) {
        log.debug("Request to save VenteMedicament : {}", venteMedicamentDTO);
        VenteMedicament venteMedicament = venteMedicamentMapper.toEntity(venteMedicamentDTO);
        venteMedicament = venteMedicamentRepository.save(venteMedicament);
        return venteMedicamentMapper.toDto(venteMedicament);
    }

    /**
     * Partially update a venteMedicament.
     *
     * @param venteMedicamentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<VenteMedicamentDTO> partialUpdate(VenteMedicamentDTO venteMedicamentDTO) {
        log.debug("Request to partially update VenteMedicament : {}", venteMedicamentDTO);

        return venteMedicamentRepository
            .findById(venteMedicamentDTO.getId())
            .map(existingVenteMedicament -> {
                venteMedicamentMapper.partialUpdate(existingVenteMedicament, venteMedicamentDTO);

                return existingVenteMedicament;
            })
            .map(venteMedicamentRepository::save)
            .map(venteMedicamentMapper::toDto);
    }

    /**
     * Get all the venteMedicaments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<VenteMedicamentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all VenteMedicaments");
        return venteMedicamentRepository.findAll(pageable).map(venteMedicamentMapper::toDto);
    }

    /**
     * Get one venteMedicament by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<VenteMedicamentDTO> findOne(Long id) {
        log.debug("Request to get VenteMedicament : {}", id);
        return venteMedicamentRepository.findById(id).map(venteMedicamentMapper::toDto);
    }

    /**
     * Delete the venteMedicament by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete VenteMedicament : {}", id);
        venteMedicamentRepository.deleteById(id);
    }
}
