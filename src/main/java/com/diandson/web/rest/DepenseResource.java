package com.diandson.web.rest;

import com.diandson.repository.DepenseRepository;
import com.diandson.service.DepenseService;
import com.diandson.service.dto.DepenseDTO;
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
 * REST controller for managing {@link com.diandson.domain.Depense}.
 */
@RestController
@RequestMapping("/api")
public class DepenseResource {

    private final Logger log = LoggerFactory.getLogger(DepenseResource.class);

    private static final String ENTITY_NAME = "depense";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DepenseService depenseService;

    private final DepenseRepository depenseRepository;

    public DepenseResource(DepenseService depenseService, DepenseRepository depenseRepository) {
        this.depenseService = depenseService;
        this.depenseRepository = depenseRepository;
    }

    /**
     * {@code POST  /depenses} : Create a new depense.
     *
     * @param depenseDTO the depenseDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new depenseDTO, or with status {@code 400 (Bad Request)} if the depense has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/depenses")
    public ResponseEntity<DepenseDTO> createDepense(@RequestBody DepenseDTO depenseDTO) throws URISyntaxException {
        log.debug("REST request to save Depense : {}", depenseDTO);
        if (depenseDTO.getId() != null) {
            throw new BadRequestAlertException("A new depense cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DepenseDTO result = depenseService.save(depenseDTO);
        return ResponseEntity
            .created(new URI("/api/depenses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /depenses/:id} : Updates an existing depense.
     *
     * @param id the id of the depenseDTO to save.
     * @param depenseDTO the depenseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated depenseDTO,
     * or with status {@code 400 (Bad Request)} if the depenseDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the depenseDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/depenses/{id}")
    public ResponseEntity<DepenseDTO> updateDepense(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DepenseDTO depenseDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Depense : {}, {}", id, depenseDTO);
        if (depenseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, depenseDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!depenseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DepenseDTO result = depenseService.save(depenseDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, depenseDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /depenses/:id} : Partial updates given fields of an existing depense, field will ignore if it is null
     *
     * @param id the id of the depenseDTO to save.
     * @param depenseDTO the depenseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated depenseDTO,
     * or with status {@code 400 (Bad Request)} if the depenseDTO is not valid,
     * or with status {@code 404 (Not Found)} if the depenseDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the depenseDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/depenses/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DepenseDTO> partialUpdateDepense(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DepenseDTO depenseDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Depense partially : {}, {}", id, depenseDTO);
        if (depenseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, depenseDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!depenseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DepenseDTO> result = depenseService.partialUpdate(depenseDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, depenseDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /depenses} : get all the depenses.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of depenses in body.
     */
    @GetMapping("/depenses")
    public ResponseEntity<List<DepenseDTO>> getAllDepenses(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Depenses");
        List<DepenseDTO> page = depenseService.findAll(pageable);
        return ResponseEntity.ok().body(page);
    }

    /**
     * {@code GET  /depenses/:id} : get the "id" depense.
     *
     * @param id the id of the depenseDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the depenseDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/depenses/{id}")
    public ResponseEntity<DepenseDTO> getDepense(@PathVariable Long id) {
        log.debug("REST request to get Depense : {}", id);
        Optional<DepenseDTO> depenseDTO = depenseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(depenseDTO);
    }

    /**
     * {@code DELETE  /depenses/:id} : delete the "id" depense.
     *
     * @param id the id of the depenseDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/depenses/{id}")
    public ResponseEntity<Void> deleteDepense(@PathVariable Long id) {
        log.debug("REST request to delete Depense : {}", id);
        depenseService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
