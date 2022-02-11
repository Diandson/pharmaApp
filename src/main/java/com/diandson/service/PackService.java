package com.diandson.service;

import com.diandson.domain.Pack;
import com.diandson.domain.TypePack;
import com.diandson.repository.PackRepository;
import com.diandson.repository.TypePackRepository;
import com.diandson.service.dto.PackDTO;
import com.diandson.service.mapper.PackMapper;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.diandson.service.mapper.TypePackMapper;
import com.diandson.service.util.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Pack}.
 */
@Service
@Transactional
public class PackService {

    private final Logger log = LoggerFactory.getLogger(PackService.class);

    private final PackRepository packRepository;

    private final PackMapper packMapper;
    @Autowired
    private TypePackRepository typePackRepository;
    @Autowired
    private TypePackMapper typePackMapper;

    public PackService(PackRepository packRepository, PackMapper packMapper) {
        this.packRepository = packRepository;
        this.packMapper = packMapper;
    }

    /**
     * Save a pack.
     *
     * @param packDTO the entity to save.
     * @return the persisted entity.
     */
    public PackDTO save(PackDTO packDTO) {
        log.debug("Request to save Pack : {}", packDTO);
        Pack pack = packMapper.toEntity(packDTO);
        pack = packRepository.save(pack);
        return packMapper.toDto(pack);
    }

    /**
     * Partially update a pack.
     *
     * @param packDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PackDTO> partialUpdate(PackDTO packDTO) {
        log.debug("Request to partially update Pack : {}", packDTO);

        return packRepository
            .findById(packDTO.getId())
            .map(existingPack -> {
                packMapper.partialUpdate(existingPack, packDTO);

                return existingPack;
            })
            .map(packRepository::save)
            .map(packMapper::toDto);
    }

    /**
     * Get all the packs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PackDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Packs");
        return packRepository.findAll(pageable).map(packMapper::toDto);
    }

    /**
     * Get one pack by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PackDTO> findOne(Long id) {
        log.debug("Request to get Pack : {}", id);
        return packRepository.findById(id).map(packMapper::toDto);
    }

    /**
     * Delete the pack by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Pack : {}", id);
        packRepository.deleteById(id);
    }

    public PackDTO findOneGenerer(Long id) {
        TypePack typePack = typePackRepository.getOne(id);

        List<Pack> typePackList = new ArrayList<>();
        for (int i = 0; i<=10; i++){
            Pack pack = new Pack();
            String serialKeys = RandomUtil.generateRandomAlphanumericString();
            if (packRepository.existsByLibelle(serialKeys)) {
                serialKeys = RandomUtil.generateRandomAlphanumericString();
            }
            pack.setLibelle(serialKeys.toUpperCase());
            pack.setDateRenew(ZonedDateTime.now());
            pack.setDurer(typePack.getDurer());
            pack.setValide(false);
            pack.setType(typePack);
            typePackList.add(pack);
        }

        return packMapper.toDto(packRepository.saveAll(typePackList).get(0));
    }

    public PackDTO findOneKeys(String id) {
        System.out.println("Pack key " + id.toUpperCase().replace("-", ""));
        Pack pack = packRepository.getByLibelle(id.trim().toUpperCase().replace("-", ""));
        PackDTO packDTO = packMapper.toDto(pack);
        packDTO.setType(typePackMapper.toDto(pack.getType()));
        return packDTO;
    }
}
