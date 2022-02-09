package com.diandson.web.rest;

import com.diandson.repository.CommandeMedicamentRepository;
import com.diandson.service.CommandeMedicamentService;
import com.diandson.service.dto.CommandeMedicamentDTO;
import com.diandson.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.diandson.domain.CommandeMedicament}.
 */
@RestController
@RequestMapping("/api")
public class CommandeMedicamentResource {

    private final Logger log = LoggerFactory.getLogger(CommandeMedicamentResource.class);

    private static final String ENTITY_NAME = "commandeMedicament";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CommandeMedicamentService commandeMedicamentService;

    private final CommandeMedicamentRepository commandeMedicamentRepository;

    public CommandeMedicamentResource(
        CommandeMedicamentService commandeMedicamentService,
        CommandeMedicamentRepository commandeMedicamentRepository
    ) {
        this.commandeMedicamentService = commandeMedicamentService;
        this.commandeMedicamentRepository = commandeMedicamentRepository;
    }

    /**
     * {@code POST  /commande-medicaments} : Create a new commandeMedicament.
     *
     * @param commandeMedicamentDTO the commandeMedicamentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new commandeMedicamentDTO, or with status {@code 400 (Bad Request)} if the commandeMedicament has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/commande-medicaments")
    public ResponseEntity<CommandeMedicamentDTO> createCommandeMedicament(@RequestBody CommandeMedicamentDTO commandeMedicamentDTO)
        throws URISyntaxException {
        log.debug("REST request to save CommandeMedicament : {}", commandeMedicamentDTO);
        if (commandeMedicamentDTO.getId() != null) {
            throw new BadRequestAlertException("A new commandeMedicament cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CommandeMedicamentDTO result = commandeMedicamentService.save(commandeMedicamentDTO);
        return ResponseEntity
            .created(new URI("/api/commande-medicaments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /commande-medicaments/:id} : Updates an existing commandeMedicament.
     *
     * @param id the id of the commandeMedicamentDTO to save.
     * @param commandeMedicamentDTO the commandeMedicamentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commandeMedicamentDTO,
     * or with status {@code 400 (Bad Request)} if the commandeMedicamentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the commandeMedicamentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/commande-medicaments/{id}")
    public ResponseEntity<CommandeMedicamentDTO> updateCommandeMedicament(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CommandeMedicamentDTO commandeMedicamentDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CommandeMedicament : {}, {}", id, commandeMedicamentDTO);
        if (commandeMedicamentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, commandeMedicamentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!commandeMedicamentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CommandeMedicamentDTO result = commandeMedicamentService.save(commandeMedicamentDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, commandeMedicamentDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /commande-medicaments/:id} : Partial updates given fields of an existing commandeMedicament, field will ignore if it is null
     *
     * @param id the id of the commandeMedicamentDTO to save.
     * @param commandeMedicamentDTO the commandeMedicamentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commandeMedicamentDTO,
     * or with status {@code 400 (Bad Request)} if the commandeMedicamentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the commandeMedicamentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the commandeMedicamentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/commande-medicaments/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CommandeMedicamentDTO> partialUpdateCommandeMedicament(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CommandeMedicamentDTO commandeMedicamentDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CommandeMedicament partially : {}, {}", id, commandeMedicamentDTO);
        if (commandeMedicamentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, commandeMedicamentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!commandeMedicamentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CommandeMedicamentDTO> result = commandeMedicamentService.partialUpdate(commandeMedicamentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, commandeMedicamentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /commande-medicaments} : get all the commandeMedicaments.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of commandeMedicaments in body.
     */
    @GetMapping("/commande-medicaments")
    public ResponseEntity<List<CommandeMedicamentDTO>> getAllCommandeMedicaments(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of CommandeMedicaments");
        Page<CommandeMedicamentDTO> page = commandeMedicamentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /commande-medicaments/:id} : get the "id" commandeMedicament.
     *
     * @param id the id of the commandeMedicamentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the commandeMedicamentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/commande-medicaments/{id}")
    public ResponseEntity<CommandeMedicamentDTO> getCommandeMedicament(@PathVariable Long id) {
        log.debug("REST request to get CommandeMedicament : {}", id);
        Optional<CommandeMedicamentDTO> commandeMedicamentDTO = commandeMedicamentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(commandeMedicamentDTO);
    }

    /**
     * {@code DELETE  /commande-medicaments/:id} : delete the "id" commandeMedicament.
     *
     * @param id the id of the commandeMedicamentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/commande-medicaments/{id}")
    public ResponseEntity<Void> deleteCommandeMedicament(@PathVariable Long id) {
        log.debug("REST request to delete CommandeMedicament : {}", id);
        commandeMedicamentService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
