package com.diandson.web.rest;

import com.diandson.repository.AssuranceRepository;
import com.diandson.service.AssuranceService;
import com.diandson.service.dto.AssuranceDTO;
import com.diandson.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
 * REST controller for managing {@link com.diandson.domain.Assurance}.
 */
@RestController
@RequestMapping("/api")
public class AssuranceResource {

    private final Logger log = LoggerFactory.getLogger(AssuranceResource.class);

    private static final String ENTITY_NAME = "assurance";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AssuranceService assuranceService;

    private final AssuranceRepository assuranceRepository;

    public AssuranceResource(AssuranceService assuranceService, AssuranceRepository assuranceRepository) {
        this.assuranceService = assuranceService;
        this.assuranceRepository = assuranceRepository;
    }

    /**
     * {@code POST  /assurances} : Create a new assurance.
     *
     * @param assuranceDTO the assuranceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new assuranceDTO, or with status {@code 400 (Bad Request)} if the assurance has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/assurances")
    public ResponseEntity<AssuranceDTO> createAssurance(@Valid @RequestBody AssuranceDTO assuranceDTO) throws URISyntaxException {
        log.debug("REST request to save Assurance : {}", assuranceDTO);
        if (assuranceDTO.getId() != null) {
            throw new BadRequestAlertException("A new assurance cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AssuranceDTO result = assuranceService.save(assuranceDTO);
        return ResponseEntity
            .created(new URI("/api/assurances/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /assurances/:id} : Updates an existing assurance.
     *
     * @param id the id of the assuranceDTO to save.
     * @param assuranceDTO the assuranceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assuranceDTO,
     * or with status {@code 400 (Bad Request)} if the assuranceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the assuranceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/assurances/{id}")
    public ResponseEntity<AssuranceDTO> updateAssurance(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AssuranceDTO assuranceDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Assurance : {}, {}", id, assuranceDTO);
        if (assuranceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assuranceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!assuranceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AssuranceDTO result = assuranceService.save(assuranceDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, assuranceDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /assurances/:id} : Partial updates given fields of an existing assurance, field will ignore if it is null
     *
     * @param id the id of the assuranceDTO to save.
     * @param assuranceDTO the assuranceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assuranceDTO,
     * or with status {@code 400 (Bad Request)} if the assuranceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the assuranceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the assuranceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/assurances/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AssuranceDTO> partialUpdateAssurance(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AssuranceDTO assuranceDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Assurance partially : {}, {}", id, assuranceDTO);
        if (assuranceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assuranceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!assuranceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AssuranceDTO> result = assuranceService.partialUpdate(assuranceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, assuranceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /assurances} : get all the assurances.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of assurances in body.
     */
    @GetMapping("/assurances")
    public ResponseEntity<List<AssuranceDTO>> getAllAssurances(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Assurances");
        Page<AssuranceDTO> page = assuranceService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /assurances/:id} : get the "id" assurance.
     *
     * @param id the id of the assuranceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the assuranceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/assurances/{id}")
    public ResponseEntity<AssuranceDTO> getAssurance(@PathVariable Long id) {
        log.debug("REST request to get Assurance : {}", id);
        Optional<AssuranceDTO> assuranceDTO = assuranceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(assuranceDTO);
    }

    /**
     * {@code DELETE  /assurances/:id} : delete the "id" assurance.
     *
     * @param id the id of the assuranceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/assurances/{id}")
    public ResponseEntity<Void> deleteAssurance(@PathVariable Long id) {
        log.debug("REST request to delete Assurance : {}", id);
        assuranceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
