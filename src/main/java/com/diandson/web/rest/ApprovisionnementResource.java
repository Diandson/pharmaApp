package com.diandson.web.rest;

import com.diandson.repository.ApprovisionnementRepository;
import com.diandson.service.ApprovisionnementService;
import com.diandson.service.dto.ApprovisionnementDTO;
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
 * REST controller for managing {@link com.diandson.domain.Approvisionnement}.
 */
@RestController
@RequestMapping("/api")
public class ApprovisionnementResource {

    private final Logger log = LoggerFactory.getLogger(ApprovisionnementResource.class);

    private static final String ENTITY_NAME = "approvisionnement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ApprovisionnementService approvisionnementService;

    private final ApprovisionnementRepository approvisionnementRepository;

    public ApprovisionnementResource(
        ApprovisionnementService approvisionnementService,
        ApprovisionnementRepository approvisionnementRepository
    ) {
        this.approvisionnementService = approvisionnementService;
        this.approvisionnementRepository = approvisionnementRepository;
    }

    /**
     * {@code POST  /approvisionnements} : Create a new approvisionnement.
     *
     * @param approvisionnementDTO the approvisionnementDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new approvisionnementDTO, or with status {@code 400 (Bad Request)} if the approvisionnement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/approvisionnements")
    public ResponseEntity<ApprovisionnementDTO> createApprovisionnement(@RequestBody ApprovisionnementDTO approvisionnementDTO)
        throws URISyntaxException {
        log.debug("REST request to save Approvisionnement : {}", approvisionnementDTO);
        if (approvisionnementDTO.getId() != null) {
            throw new BadRequestAlertException("A new approvisionnement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ApprovisionnementDTO result = approvisionnementService.save(approvisionnementDTO);
        return ResponseEntity
            .created(new URI("/api/approvisionnements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /approvisionnements/:id} : Updates an existing approvisionnement.
     *
     * @param id the id of the approvisionnementDTO to save.
     * @param approvisionnementDTO the approvisionnementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated approvisionnementDTO,
     * or with status {@code 400 (Bad Request)} if the approvisionnementDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the approvisionnementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/approvisionnements/{id}")
    public ResponseEntity<ApprovisionnementDTO> updateApprovisionnement(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ApprovisionnementDTO approvisionnementDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Approvisionnement : {}, {}", id, approvisionnementDTO);
        if (approvisionnementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, approvisionnementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!approvisionnementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ApprovisionnementDTO result = approvisionnementService.save(approvisionnementDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, approvisionnementDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /approvisionnements/:id} : Partial updates given fields of an existing approvisionnement, field will ignore if it is null
     *
     * @param id the id of the approvisionnementDTO to save.
     * @param approvisionnementDTO the approvisionnementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated approvisionnementDTO,
     * or with status {@code 400 (Bad Request)} if the approvisionnementDTO is not valid,
     * or with status {@code 404 (Not Found)} if the approvisionnementDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the approvisionnementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/approvisionnements/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ApprovisionnementDTO> partialUpdateApprovisionnement(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ApprovisionnementDTO approvisionnementDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Approvisionnement partially : {}, {}", id, approvisionnementDTO);
        if (approvisionnementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, approvisionnementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!approvisionnementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ApprovisionnementDTO> result = approvisionnementService.partialUpdate(approvisionnementDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, approvisionnementDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /approvisionnements} : get all the approvisionnements.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of approvisionnements in body.
     */
    @GetMapping("/approvisionnements")
    public ResponseEntity<List<ApprovisionnementDTO>> getAllApprovisionnements(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of Approvisionnements");
        Page<ApprovisionnementDTO> page = approvisionnementService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /approvisionnements/:id} : get the "id" approvisionnement.
     *
     * @param id the id of the approvisionnementDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the approvisionnementDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/approvisionnements/{id}")
    public ResponseEntity<ApprovisionnementDTO> getApprovisionnement(@PathVariable Long id) {
        log.debug("REST request to get Approvisionnement : {}", id);
        Optional<ApprovisionnementDTO> approvisionnementDTO = approvisionnementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(approvisionnementDTO);
    }

    /**
     * {@code DELETE  /approvisionnements/:id} : delete the "id" approvisionnement.
     *
     * @param id the id of the approvisionnementDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/approvisionnements/{id}")
    public ResponseEntity<Void> deleteApprovisionnement(@PathVariable Long id) {
        log.debug("REST request to delete Approvisionnement : {}", id);
        approvisionnementService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
