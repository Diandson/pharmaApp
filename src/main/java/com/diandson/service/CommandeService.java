package com.diandson.service;

import com.diandson.domain.Commande;
import com.diandson.domain.CommandeMedicament;
import com.diandson.domain.Medicament;
import com.diandson.repository.*;
import com.diandson.security.SecurityUtils;
import com.diandson.service.dto.CommandeDTO;
import com.diandson.service.mapper.CommandeMapper;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.diandson.service.mapper.CommandeMedicamentMapper;
import com.diandson.service.mapper.MedicamentMapper;
import com.diandson.service.util.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Commande}.
 */
@Service
@Transactional
public class CommandeService {

    private final Logger log = LoggerFactory.getLogger(CommandeService.class);

    private final CommandeRepository commandeRepository;

    private final CommandeMapper commandeMapper;
    @Autowired
    private MedicamentRepository medicamentRepository;
    @Autowired
    private MedicamentMapper medicamentMapper;
    @Autowired
    private CommandeMedicamentRepository commandeMedicamentRepository;
    @Autowired
    private CommandeMedicamentMapper commandeMedicamentMapper;
    @Autowired
    private PersonneRepository personneRepository;
    @Autowired
    private StructureRepository structureRepository;

    public CommandeService(CommandeRepository commandeRepository, CommandeMapper commandeMapper) {
        this.commandeRepository = commandeRepository;
        this.commandeMapper = commandeMapper;
    }

    /**
     * Save a commande.
     *
     * @param commandeDTO the entity to save.
     * @return the persisted entity.
     */
    public CommandeDTO save(CommandeDTO commandeDTO) {
        log.debug("Request to save Commande : {}", commandeDTO);
        Commande commande = commandeMapper.toEntity(commandeDTO);
        commande = commandeRepository.save(commande);
        return commandeMapper.toDto(commande);
    }

    /**
     * Partially update a commande.
     *
     * @param commandeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CommandeDTO> partialUpdate(CommandeDTO commandeDTO) {
        log.debug("Request to partially update Commande : {}", commandeDTO);

        return commandeRepository
            .findById(commandeDTO.getId())
            .map(existingCommande -> {
                commandeMapper.partialUpdate(existingCommande, commandeDTO);

                return existingCommande;
            })
            .map(commandeRepository::save)
            .map(commandeMapper::toDto);
    }

    /**
     * Get all the commandes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CommandeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Commandes");
        return commandeRepository.findAll(pageable).map(commandeMapper::toDto);
    }

    /**
     * Get one commande by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CommandeDTO> findOne(Long id) {
        log.debug("Request to get Commande : {}", id);
        return commandeRepository.findById(id).map(commandeMapper::toDto);
    }

    /**
     * Delete the commande by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Commande : {}", id);
        commandeRepository.deleteById(id);
    }

    public void saveImprimer(CommandeDTO commandeDTO) {
        Commande commande = commandeMapper.toEntity(commandeDTO);
        commande.setOperateur(personneRepository.findByUserLogin(SecurityUtils.getCurrentUserLogin().get()));
        commande.setNumero(RandomUtil.generateRandomSerialNumericPaiementStringc());
        commande.setDateCommande(ZonedDateTime.now());
        commande = commandeRepository.save(commande);

        for (Medicament medicament : commandeDTO.getMedicament().stream()
            .map(medicamentMapper::toEntity).collect(Collectors.toList())){

//            Medicament medicamentToUpdate = medicamentRepository.getById(medicament.getId());
//            medicamentToUpdate.setStockTheorique(medicamentToUpdate.getStockTheorique() - medicament.getStockTheorique());
//            medicamentToUpdate = medicamentRepository.save(medicamentToUpdate);

            CommandeMedicament cm = new CommandeMedicament();
            cm.setCommande(commande);
            cm.setQuantite(medicament.getStockTheorique());
            cm.setMedicament(medicament);
            cm = commandeMedicamentRepository.save(cm);
        }

    }
}
