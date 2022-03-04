package com.diandson.service;

import com.diandson.domain.Medicament;
import com.diandson.domain.Paiement;
import com.diandson.domain.Vente;
import com.diandson.domain.VenteMedicament;
import com.diandson.repository.*;
import com.diandson.security.SecurityUtils;
import com.diandson.service.dto.MedicamentDTO;
import com.diandson.service.dto.VenteDTO;
import com.diandson.service.dto.VenteMedicamentDTO;
import com.diandson.service.mapper.MedicamentMapper;
import com.diandson.service.mapper.PaiementMapper;
import com.diandson.service.mapper.VenteMapper;
import com.diandson.service.mapper.VenteMedicamentMapper;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import liquibase.pro.packaged.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Vente}.
 */
@Service
@Transactional
public class VenteService {

    private final Logger log = LoggerFactory.getLogger(VenteService.class);

    private final VenteRepository venteRepository;

    private final VenteMapper venteMapper;

    @Autowired
    private MedicamentMapper medicamentMapper;

    @Autowired
    private MedicamentRepository medicamentRepository;

    @Autowired
    private VenteMedicamentMapper venteMedicamentMapper;

    @Autowired
    private VenteMedicamentRepository venteMedicamentRepository;

    @Autowired
    private PersonneRepository personneRepository;

    @Autowired
    private PaiementRepository paiementRepository;

    @Autowired
    private PaiementMapper paiementMapper;

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    public VenteService(VenteRepository venteRepository, VenteMapper venteMapper) {
        this.venteRepository = venteRepository;
        this.venteMapper = venteMapper;
    }

    /**
     * Save a vente.
     *
     * @param venteDTO the entity to save.
     * @return the persisted entity.
     */
    public VenteDTO save(VenteDTO venteDTO) {
        log.debug("Request to save Vente : {}", venteDTO);
        Vente vente = venteMapper.toEntity(venteDTO);
        vente.setNumero(String.valueOf(venteRepository.count() + 1));
        vente.setDateVente(ZonedDateTime.now());
        vente.setOperateur(personneRepository.findByUserLogin(SecurityUtils.getCurrentUserLogin().get()));
        vente = venteRepository.save(vente);
        List<VenteMedicament> venteMedicamentList = new ArrayList<>();
        List<Medicament> medicamentList = venteDTO.getMedicament().stream().map(medicamentMapper::toEntity).collect(Collectors.toList());
        for (Medicament medicament : medicamentList) {
            VenteMedicament venteMedicament = new VenteMedicament();
            Medicament medicamentOld = medicamentRepository.getById(medicament.getId());
            medicamentOld.setStockTheorique(medicamentOld.getStockTheorique() - medicament.getStockTheorique());

            medicamentOld = medicamentRepository.save(medicamentOld);
            venteMedicament.setMedicament(medicament);
            venteMedicament.setVente(vente);
            venteMedicament.setQuantite(medicament.getStockTheorique());

            venteMedicamentList.add(venteMedicament);
        }
        venteMedicamentRepository.saveAll(venteMedicamentList);
//        messagingTemplate.convertAndSend("/topic/vente", venteDTO);
        return venteMapper.toDto(vente);
    }

    /**
     * Partially update a vente.
     *
     * @param venteDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<VenteDTO> partialUpdate(VenteDTO venteDTO) {
        log.debug("Request to partially update Vente : {}", venteDTO);

        return venteRepository
            .findById(venteDTO.getId())
            .map(existingVente -> {
                venteMapper.partialUpdate(existingVente, venteDTO);

                return existingVente;
            })
            .map(venteRepository::save)
            .map(venteMapper::toDto);
    }

    /**
     * Get all the ventes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<VenteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Ventes");
        return venteRepository.findAll().stream()
            .map(venteMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Get one vente by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<VenteDTO> findOne(Long id) {
        log.debug("Request to get Vente : {}", id);
        return venteRepository.findById(id).map(venteMapper::toDto);
    }

    /**
     * Delete the vente by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Vente : {}", id);
        venteRepository.deleteById(id);
    }

    public List<VenteDTO> findAllNewVentes() {
        return venteRepository
            .findAll()
            .stream()
            .filter(vente -> vente.getPaiement() == null)
            .map(venteMapper::toDto)
            .peek(venteDTO -> {
                List<MedicamentDTO> medicamentDTOList = new ArrayList<>();
               venteMedicamentRepository.findAllByVenteId(venteDTO.getId()).stream()
                   .peek(venteMedicament -> {
                       MedicamentDTO medicamentDTO = medicamentMapper.toDto(venteMedicament.getMedicament());
                       medicamentDTO.setStockTheorique(venteMedicament.getQuantite());
                       medicamentDTOList.add(medicamentDTO);
                   }).collect(Collectors.toList());
               venteDTO.setMedicament(medicamentDTOList);

            }).collect(Collectors.toList());
    }
}
