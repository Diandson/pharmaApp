package com.diandson.service;

import com.diandson.domain.Livraison;
import com.diandson.repository.LivraisonRepository;
import com.diandson.service.dto.LivraisonDTO;
import com.diandson.service.mapper.LivraisonMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Livraison}.
 */
@Service
@Transactional
public class LivraisonService {

    private final Logger log = LoggerFactory.getLogger(LivraisonService.class);

    private final LivraisonRepository livraisonRepository;

    private final LivraisonMapper livraisonMapper;

    public LivraisonService(LivraisonRepository livraisonRepository, LivraisonMapper livraisonMapper) {
        this.livraisonRepository = livraisonRepository;
        this.livraisonMapper = livraisonMapper;
    }

    /**
     * Save a livraison.
     *
     * @param livraisonDTO the entity to save.
     * @return the persisted entity.
     */
    public LivraisonDTO save(LivraisonDTO livraisonDTO) {
        log.debug("Request to save Livraison : {}", livraisonDTO);
        Livraison livraison = livraisonMapper.toEntity(livraisonDTO);
        livraison = livraisonRepository.save(livraison);
        return livraisonMapper.toDto(livraison);
    }

    /**
     * Partially update a livraison.
     *
     * @param livraisonDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<LivraisonDTO> partialUpdate(LivraisonDTO livraisonDTO) {
        log.debug("Request to partially update Livraison : {}", livraisonDTO);

        return livraisonRepository
            .findById(livraisonDTO.getId())
            .map(existingLivraison -> {
                livraisonMapper.partialUpdate(existingLivraison, livraisonDTO);

                return existingLivraison;
            })
            .map(livraisonRepository::save)
            .map(livraisonMapper::toDto);
    }

    /**
     * Get all the livraisons.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<LivraisonDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Livraisons");
        return livraisonRepository.findAll(pageable).map(livraisonMapper::toDto);
    }

    /**
     *  Get all the livraisons where Commande is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<LivraisonDTO> findAllWhereCommandeIsNull() {
        log.debug("Request to get all livraisons where Commande is null");
        return StreamSupport
            .stream(livraisonRepository.findAll().spliterator(), false)
            .filter(livraison -> livraison.getCommande() == null)
            .map(livraisonMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one livraison by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LivraisonDTO> findOne(Long id) {
        log.debug("Request to get Livraison : {}", id);
        return livraisonRepository.findById(id).map(livraisonMapper::toDto);
    }

    /**
     * Delete the livraison by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Livraison : {}", id);
        livraisonRepository.deleteById(id);
    }
}
