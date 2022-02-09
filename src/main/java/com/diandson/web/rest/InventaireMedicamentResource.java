package com.diandson.web.rest;

import com.diandson.repository.InventaireMedicamentRepository;
import com.diandson.service.InventaireMedicamentService;
import com.diandson.service.dto.InventaireMedicamentDTO;
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
 * REST controller for managing {@link com.diandson.domain.InventaireMedicament}.
 */
@RestController
@RequestMapping("/api")
public class InventaireMedicamentResource {

    private final Logger log = LoggerFactory.getLogger(InventaireMedicamentResource.class);

    private static final String ENTITY_NAME = "inventaireMedicament";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InventaireMedicamentService inventaireMedicamentService;

    private final InventaireMedicamentRepository inventaireMedicamentRepository;

    public InventaireMedicamentResource(
        InventaireMedicamentService inventaireMedicamentService,
        InventaireMedicamentRepository inventaireMedicamentRepository
    ) {
        this.inventaireMedicamentService = inventaireMedicamentService;
        this.inventaireMedicamentRepository = inventaireMedicamentRepository;
    }

    /**
     * {@code POST  /inventaire-medicaments} : Create a new inventaireMedicament.
     *
     * @param inventaireMedicamentDTO the inventaireMedicamentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new inventaireMedicamentDTO, or with status {@code 400 (Bad Request)} if the inventaireMedicament has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/inventaire-medicaments")
    public ResponseEntity<InventaireMedicamentDTO> createInventaireMedicament(@RequestBody InventaireMedicamentDTO inventaireMedicamentDTO)
        throws URISyntaxException {
        log.debug("REST request to save InventaireMedicament : {}", inventaireMedicamentDTO);
        if (inventaireMedicamentDTO.getId() != null) {
            throw new BadRequestAlertException("A new inventaireMedicament cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InventaireMedicamentDTO result = inventaireMedicamentService.save(inventaireMedicamentDTO);
        return ResponseEntity
            .created(new URI("/api/inventaire-medicaments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /inventaire-medicaments/:id} : Updates an existing inventaireMedicament.
     *
     * @param id the id of the inventaireMedicamentDTO to save.
     * @param inventaireMedicamentDTO the inventaireMedicamentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inventaireMedicamentDTO,
     * or with status {@code 400 (Bad Request)} if the inventaireMedicamentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the inventaireMedicamentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/inventaire-medicaments/{id}")
    public ResponseEntity<InventaireMedicamentDTO> updateInventaireMedicament(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody InventaireMedicamentDTO inventaireMedicamentDTO
    ) throws URISyntaxException {
        log.debug("REST request to update InventaireMedicament : {}, {}", id, inventaireMedicamentDTO);
        if (inventaireMedicamentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inventaireMedicamentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!inventaireMedicamentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        InventaireMedicamentDTO result = inventaireMedicamentService.save(inventaireMedicamentDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, inventaireMedicamentDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /inventaire-medicaments/:id} : Partial updates given fields of an existing inventaireMedicament, field will ignore if it is null
     *
     * @param id the id of the inventaireMedicamentDTO to save.
     * @param inventaireMedicamentDTO the inventaireMedicamentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inventaireMedicamentDTO,
     * or with status {@code 400 (Bad Request)} if the inventaireMedicamentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the inventaireMedicamentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the inventaireMedicamentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/inventaire-medicaments/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InventaireMedicamentDTO> partialUpdateInventaireMedicament(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody InventaireMedicamentDTO inventaireMedicamentDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update InventaireMedicament partially : {}, {}", id, inventaireMedicamentDTO);
        if (inventaireMedicamentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inventaireMedicamentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!inventaireMedicamentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InventaireMedicamentDTO> result = inventaireMedicamentService.partialUpdate(inventaireMedicamentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, inventaireMedicamentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /inventaire-medicaments} : get all the inventaireMedicaments.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of inventaireMedicaments in body.
     */
    @GetMapping("/inventaire-medicaments")
    public ResponseEntity<List<InventaireMedicamentDTO>> getAllInventaireMedicaments(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of InventaireMedicaments");
        Page<InventaireMedicamentDTO> page = inventaireMedicamentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /inventaire-medicaments/:id} : get the "id" inventaireMedicament.
     *
     * @param id the id of the inventaireMedicamentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the inventaireMedicamentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/inventaire-medicaments/{id}")
    public ResponseEntity<InventaireMedicamentDTO> getInventaireMedicament(@PathVariable Long id) {
        log.debug("REST request to get InventaireMedicament : {}", id);
        Optional<InventaireMedicamentDTO> inventaireMedicamentDTO = inventaireMedicamentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(inventaireMedicamentDTO);
    }

    /**
     * {@code DELETE  /inventaire-medicaments/:id} : delete the "id" inventaireMedicament.
     *
     * @param id the id of the inventaireMedicamentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/inventaire-medicaments/{id}")
    public ResponseEntity<Void> deleteInventaireMedicament(@PathVariable Long id) {
        log.debug("REST request to delete InventaireMedicament : {}", id);
        inventaireMedicamentService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
