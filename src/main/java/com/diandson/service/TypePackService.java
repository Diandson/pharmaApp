package com.diandson.service;

import com.diandson.domain.TypePack;
import com.diandson.repository.TypePackRepository;
import com.diandson.service.dto.TypePackDTO;
import com.diandson.service.mapper.TypePackMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TypePack}.
 */
@Service
@Transactional
public class TypePackService {

    private final Logger log = LoggerFactory.getLogger(TypePackService.class);

    private final TypePackRepository typePackRepository;

    private final TypePackMapper typePackMapper;

    public TypePackService(TypePackRepository typePackRepository, TypePackMapper typePackMapper) {
        this.typePackRepository = typePackRepository;
        this.typePackMapper = typePackMapper;
    }

    /**
     * Save a typePack.
     *
     * @param typePackDTO the entity to save.
     * @return the persisted entity.
     */
    public TypePackDTO save(TypePackDTO typePackDTO) {
        log.debug("Request to save TypePack : {}", typePackDTO);
        TypePack typePack = typePackMapper.toEntity(typePackDTO);
        typePack = typePackRepository.save(typePack);
        return typePackMapper.toDto(typePack);
    }

    /**
     * Partially update a typePack.
     *
     * @param typePackDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TypePackDTO> partialUpdate(TypePackDTO typePackDTO) {
        log.debug("Request to partially update TypePack : {}", typePackDTO);

        return typePackRepository
            .findById(typePackDTO.getId())
            .map(existingTypePack -> {
                typePackMapper.partialUpdate(existingTypePack, typePackDTO);

                return existingTypePack;
            })
            .map(typePackRepository::save)
            .map(typePackMapper::toDto);
    }

    /**
     * Get all the typePacks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TypePackDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TypePacks");
        return typePackRepository.findAll(pageable).map(typePackMapper::toDto);
    }

    /**
     * Get one typePack by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TypePackDTO> findOne(Long id) {
        log.debug("Request to get TypePack : {}", id);
        return typePackRepository.findById(id).map(typePackMapper::toDto);
    }

    /**
     * Delete the typePack by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TypePack : {}", id);
        typePackRepository.deleteById(id);
    }
}
