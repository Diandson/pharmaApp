package com.diandson.service;

import com.diandson.domain.Medicament;
import com.diandson.domain.Paiement;
import com.diandson.domain.Vente;
import com.diandson.repository.MedicamentRepository;
import com.diandson.repository.PaiementRepository;
import com.diandson.repository.PersonneRepository;
import com.diandson.repository.VenteRepository;
import com.diandson.security.SecurityUtils;
import com.diandson.service.dto.PaiementDTO;
import com.diandson.service.mapper.MedicamentMapper;
import com.diandson.service.mapper.PaiementMapper;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.stream.Collectors;

import com.diandson.service.mapper.VenteMapper;
import com.diandson.service.util.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Paiement}.
 */
@Service
@Transactional
public class PaiementService {

    private final Logger log = LoggerFactory.getLogger(PaiementService.class);

    private final PaiementRepository paiementRepository;

    private final PaiementMapper paiementMapper;
    @Autowired
    private VenteMapper venteMapper;
    @Autowired
    private VenteRepository venteRepository;
    @Autowired
    private MedicamentMapper medicamentMapper;
    @Autowired
    private MedicamentRepository medicamentRepository;
    @Autowired
    private PersonneRepository personneRepository;
    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    public PaiementService(PaiementRepository paiementRepository, PaiementMapper paiementMapper) {
        this.paiementRepository = paiementRepository;
        this.paiementMapper = paiementMapper;
    }

    /**
     * Save a paiement.
     *
     * @param paiementDTO the entity to save.
     * @return the persisted entity.
     */
    public PaiementDTO save(PaiementDTO paiementDTO) {
        log.debug("Request to save Paiement : {}", paiementDTO);
        Paiement paiement = paiementMapper.toEntity(paiementDTO);
        paiement.setNumero(RandomUtil.generateRandomSerialNumericPaiementStringc());
        paiement.setDatePaiement(ZonedDateTime.now());
        paiement.setOperateur(personneRepository.findByUserLogin(SecurityUtils.getCurrentUserLogin().get()));
        Vente vente = venteMapper.toEntity(paiementDTO.getVente());
        paiement = paiementRepository.save(paiement);
        vente.setPaiement(paiement);
        vente = venteRepository.save(vente);
//        messagingTemplate.convertAndSend("/topic/vente", venteMapper.toDto(vente));

        return paiementMapper.toDto(paiement);
    }

    /**
     * Partially update a paiement.
     *
     * @param paiementDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PaiementDTO> partialUpdate(PaiementDTO paiementDTO) {
        log.debug("Request to partially update Paiement : {}", paiementDTO);

        return paiementRepository
            .findById(paiementDTO.getId())
            .map(existingPaiement -> {
                paiementMapper.partialUpdate(existingPaiement, paiementDTO);

                return existingPaiement;
            })
            .map(paiementRepository::save)
            .map(paiementMapper::toDto);
    }

    /**
     * Get all the paiements.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PaiementDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Paiements");
        return paiementRepository.findAll(pageable).map(paiementMapper::toDto);
    }

    /**
     * Get one paiement by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PaiementDTO> findOne(Long id) {
        log.debug("Request to get Paiement : {}", id);
        return paiementRepository.findById(id).map(paiementMapper::toDto);
    }

    /**
     * Delete the paiement by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Paiement : {}", id);
        paiementRepository.deleteById(id);
    }
}
