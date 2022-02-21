package com.diandson.web.rest;

import com.diandson.repository.MotifListeDepenseRepository;
import com.diandson.service.MotifListeDepenseService;
import com.diandson.service.dto.MotifListeDepenseDTO;
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
 * REST controller for managing {@link com.diandson.domain.MotifListeDepense}.
 */
@RestController
@RequestMapping("/api")
public class MotifListeDepenseResource {

    private final Logger log = LoggerFactory.getLogger(MotifListeDepenseResource.class);

    private static final String ENTITY_NAME = "motifListeDepense";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MotifListeDepenseService motifListeDepenseService;

    private final MotifListeDepenseRepository motifListeDepenseRepository;

    public MotifListeDepenseResource(
        MotifListeDepenseService motifListeDepenseService,
        MotifListeDepenseRepository motifListeDepenseRepository
    ) {
        this.motifListeDepenseService = motifListeDepenseService;
        this.motifListeDepenseRepository = motifListeDepenseRepository;
    }

    /**
     * {@code POST  /motif-liste-depenses} : Create a new motifListeDepense.
     *
     * @param motifListeDepenseDTO the motifListeDepenseDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new motifListeDepenseDTO, or with status {@code 400 (Bad Request)} if the motifListeDepense has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/motif-liste-depenses")
    public ResponseEntity<MotifListeDepenseDTO> createMotifListeDepense(@RequestBody MotifListeDepenseDTO motifListeDepenseDTO)
        throws URISyntaxException {
        log.debug("REST request to save MotifListeDepense : {}", motifListeDepenseDTO);
        if (motifListeDepenseDTO.getId() != null) {
            throw new BadRequestAlertException("A new motifListeDepense cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MotifListeDepenseDTO result = motifListeDepenseService.save(motifListeDepenseDTO);
        return ResponseEntity
            .created(new URI("/api/motif-liste-depenses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /motif-liste-depenses/:id} : Updates an existing motifListeDepense.
     *
     * @param id the id of the motifListeDepenseDTO to save.
     * @param motifListeDepenseDTO the motifListeDepenseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated motifListeDepenseDTO,
     * or with status {@code 400 (Bad Request)} if the motifListeDepenseDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the motifListeDepenseDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/motif-liste-depenses/{id}")
    public ResponseEntity<MotifListeDepenseDTO> updateMotifListeDepense(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MotifListeDepenseDTO motifListeDepenseDTO
    ) throws URISyntaxException {
        log.debug("REST request to update MotifListeDepense : {}, {}", id, motifListeDepenseDTO);
        if (motifListeDepenseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, motifListeDepenseDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!motifListeDepenseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MotifListeDepenseDTO result = motifListeDepenseService.save(motifListeDepenseDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, motifListeDepenseDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /motif-liste-depenses/:id} : Partial updates given fields of an existing motifListeDepense, field will ignore if it is null
     *
     * @param id the id of the motifListeDepenseDTO to save.
     * @param motifListeDepenseDTO the motifListeDepenseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated motifListeDepenseDTO,
     * or with status {@code 400 (Bad Request)} if the motifListeDepenseDTO is not valid,
     * or with status {@code 404 (Not Found)} if the motifListeDepenseDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the motifListeDepenseDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/motif-liste-depenses/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MotifListeDepenseDTO> partialUpdateMotifListeDepense(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MotifListeDepenseDTO motifListeDepenseDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update MotifListeDepense partially : {}, {}", id, motifListeDepenseDTO);
        if (motifListeDepenseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, motifListeDepenseDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!motifListeDepenseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MotifListeDepenseDTO> result = motifListeDepenseService.partialUpdate(motifListeDepenseDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, motifListeDepenseDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /motif-liste-depenses} : get all the motifListeDepenses.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of motifListeDepenses in body.
     */
    @GetMapping("/motif-liste-depenses")
    public ResponseEntity<List<MotifListeDepenseDTO>> getAllMotifListeDepenses(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of MotifListeDepenses");
        Page<MotifListeDepenseDTO> page = motifListeDepenseService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /motif-liste-depenses/:id} : get the "id" motifListeDepense.
     *
     * @param id the id of the motifListeDepenseDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the motifListeDepenseDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/motif-liste-depenses/{id}")
    public ResponseEntity<MotifListeDepenseDTO> getMotifListeDepense(@PathVariable Long id) {
        log.debug("REST request to get MotifListeDepense : {}", id);
        Optional<MotifListeDepenseDTO> motifListeDepenseDTO = motifListeDepenseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(motifListeDepenseDTO);
    }

    /**
     * {@code DELETE  /motif-liste-depenses/:id} : delete the "id" motifListeDepense.
     *
     * @param id the id of the motifListeDepenseDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/motif-liste-depenses/{id}")
    public ResponseEntity<Void> deleteMotifListeDepense(@PathVariable Long id) {
        log.debug("REST request to delete MotifListeDepense : {}", id);
        motifListeDepenseService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
