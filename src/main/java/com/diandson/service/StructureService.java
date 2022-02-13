package com.diandson.service;

import com.diandson.domain.*;
import com.diandson.repository.*;
import com.diandson.security.AuthoritiesConstants;
import com.diandson.security.SecurityUtils;
import com.diandson.service.dto.PersonneDTO;
import com.diandson.service.dto.StructureDTO;
import com.diandson.service.mapper.PackMapper;
import com.diandson.service.mapper.PersonneMapper;
import com.diandson.service.mapper.StructureMapper;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.diandson.service.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.security.RandomUtil;

/**
 * Service Implementation for managing {@link Structure}.
 */
@Service
@Transactional
public class StructureService {

    private final Logger log = LoggerFactory.getLogger(StructureService.class);

    private final StructureRepository structureRepository;

    private final StructureMapper structureMapper;
    @Autowired
    private PersonneMapper personneMapper;
    @Autowired
    private PersonneRepository personneRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PackMapper packMapper;
    @Autowired
    private PackRepository packRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthorityRepository authorityRepository;

    public StructureService(StructureRepository structureRepository, StructureMapper structureMapper) {
        this.structureRepository = structureRepository;
        this.structureMapper = structureMapper;
    }

    /**
     * Save a structure.
     *
     * @param structureDTO the entity to save.
     * @return the persisted entity.
     */
    public StructureDTO save(StructureDTO structureDTO) {
        log.debug("Request to save Structure : {}", structureDTO);
        Structure structure = structureMapper.toEntity(structureDTO);
        structure.setDenomination(structureDTO.getDenomination().toLowerCase());
        Personne personne = personneMapper.toEntity(structureDTO.getPersonne());
        personne.setNom(structureDTO.getPersonne().getNom().toLowerCase());
        personne.setPrenom(structureDTO.getPersonne().getPrenom().toLowerCase());
        User user = userMapper.userDTOToUser(structureDTO.getUserDTO());
        Pack pack = packMapper.toEntity(structureDTO.getPackDTO());
        String encryptedPassword = passwordEncoder.encode(structureDTO.getUserDTO().getPassword());
        // new user gets initially a generated password
        user.setPassword(encryptedPassword);
        user.setActivationKey(RandomUtil.generateActivationKey());
        user.setFirstName(personne.getNom());
        user.setLastName(personne.getPrenom());
        user.setActivated(true);
        structure.setPdg(personne.getNom() + " " + personne.getPrenom());
        structure = structureRepository.save(structure);
        pack.setStructure(structure);
        pack.setDateRenew(ZonedDateTime.now());
        pack = packRepository.save(pack);
        switch (structure.getType().name()) {
            case "SIEGE":
                user.getAuthorities().add(authorityRepository.getById(AuthoritiesConstants.USER));
                user.getAuthorities().add(authorityRepository.getById(AuthoritiesConstants.STRUCTURE_ADMIN));
                break;
            case "AGENCE":
                user.getAuthorities().add(authorityRepository.getById(AuthoritiesConstants.USER));
                user.getAuthorities().add(authorityRepository.getById(AuthoritiesConstants.AGENCE_ADMIN));
                break;
            case "MAGASIN":
                user.getAuthorities().add(authorityRepository.getById(AuthoritiesConstants.USER));
                user.getAuthorities().add(authorityRepository.getById(AuthoritiesConstants.MAGASIN_ADMIN));
                break;
        }
        user = userRepository.save(user);
        personne.setUser(user);
        personne.setStructure(structure);
        personne = personneRepository.save(personne);

        return structureMapper.toDto(structure);
    }

    /**
     * Partially update a structure.
     *
     * @param structureDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<StructureDTO> partialUpdate(StructureDTO structureDTO) {
        log.debug("Request to partially update Structure : {}", structureDTO);

        return structureRepository
            .findById(structureDTO.getId())
            .map(existingStructure -> {
                structureMapper.partialUpdate(existingStructure, structureDTO);

                return existingStructure;
            })
            .map(structureRepository::save)
            .map(structureMapper::toDto);
    }

    /**
     * Get all the structures.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<StructureDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Structures");
        return structureRepository.findAll(pageable).map(structureMapper::toDto);
    }

    /**
     * Get one structure by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<StructureDTO> findOne(Long id) {
        log.debug("Request to get Structure : {}", id);
        return structureRepository.findById(id).map(structureMapper::toDto);
    }

    /**
     * Delete the structure by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Structure : {}", id);
        structureRepository.deleteById(id);
    }

    public StructureDTO findOneOnly() {
        List<Structure> structureList = structureRepository.findAll();
        if (structureList.size() == 1) {
            return structureList.stream().map(structureMapper::toDto)
                .collect(Collectors.toList()).get(0);
        }else return null;
    }
    public StructureDTO findOneOnlyAuth() {
        List<Structure> structureList = structureRepository.findAll();
        if (structureList.size() == 1) {
            return structureList.stream().map(structureMapper::toDto)
                .peek(structureDTO ->
                    structureDTO.setPersonne(personneMapper.toDto(personneRepository.findByUserLogin(SecurityUtils.getCurrentUserLogin().get()))))
                .collect(Collectors.toList()).get(0);
        }else return null;
    }
}
