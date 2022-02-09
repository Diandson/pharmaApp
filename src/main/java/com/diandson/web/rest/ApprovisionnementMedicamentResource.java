package com.diandson.web.rest;

import com.diandson.repository.ApprovisionnementMedicamentRepository;
import com.diandson.service.ApprovisionnementMedicamentService;
import com.diandson.service.dto.ApprovisionnementMedicamentDTO;
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
 * REST controller for managing {@link com.diandson.domain.ApprovisionnementMedicament}.
 */
@RestController
@RequestMapping("/api")
public class ApprovisionnementMedicamentResource {

    private final Logger log = LoggerFactory.getLogger(ApprovisionnementMedicamentResource.class);

    private static final String ENTITY_NAME = "approvisionnementMedicament";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ApprovisionnementMedicamentService approvisionnementMedicamentService;

    private final ApprovisionnementMedicamentRepository approvisionnementMedicamentRepository;

    public ApprovisionnementMedicamentResource(
        ApprovisionnementMedicamentService approvisionnementMedicamentService,
        ApprovisionnementMedicamentRepository approvisionnementMedicamentRepository
    ) {
        this.approvisionnementMedicamentService = approvisionnementMedicamentService;
        this.approvisionnementMedicamentRepository = approvisionnementMedicamentRepository;
    }

    /**
     * {@code POST  /approvisionnement-medicaments} : Create a new approvisionnementMedicament.
     *
     * @param approvisionnementMedicamentDTO the approvisionnementMedicamentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new approvisionnementMedicamentDTO, or with status {@code 400 (Bad Request)} if the approvisionnementMedicament has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/approvisionnement-medicaments")
    public ResponseEntity<ApprovisionnementMedicamentDTO> createApprovisionnementMedicament(
        @RequestBody ApprovisionnementMedicamentDTO approvisionnementMedicamentDTO
    ) throws URISyntaxException {
        log.debug("REST request to save ApprovisionnementMedicament : {}", approvisionnementMedicamentDTO);
        if (approvisionnementMedicamentDTO.getId() != null) {
            throw new BadRequestAlertException("A new approvisionnementMedicament cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ApprovisionnementMedicamentDTO result = approvisionnementMedicamentService.save(approvisionnementMedicamentDTO);
        return ResponseEntity
            .created(new URI("/api/approvisionnement-medicaments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /approvisionnement-medicaments/:id} : Updates an existing approvisionnementMedicament.
     *
     * @param id the id of the approvisionnementMedicamentDTO to save.
     * @param approvisionnementMedicamentDTO the approvisionnementMedicamentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated approvisionnementMedicamentDTO,
     * or with status {@code 400 (Bad Request)} if the approvisionnementMedicamentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the approvisionnementMedicamentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/approvisionnement-medicaments/{id}")
    public ResponseEntity<ApprovisionnementMedicamentDTO> updateApprovisionnementMedicament(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ApprovisionnementMedicamentDTO approvisionnementMedicamentDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ApprovisionnementMedicament : {}, {}", id, approvisionnementMedicamentDTO);
        if (approvisionnementMedicamentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, approvisionnementMedicamentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!approvisionnementMedicamentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ApprovisionnementMedicamentDTO result = approvisionnementMedicamentService.save(approvisionnementMedicamentDTO);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, approvisionnementMedicamentDTO.getId().toString())
            )
            .body(result);
    }

    /**
     * {@code PATCH  /approvisionnement-medicaments/:id} : Partial updates given fields of an existing approvisionnementMedicament, field will ignore if it is null
     *
     * @param id the id of the approvisionnementMedicamentDTO to save.
     * @param approvisionnementMedicamentDTO the approvisionnementMedicamentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated approvisionnementMedicamentDTO,
     * or with status {@code 400 (Bad Request)} if the approvisionnementMedicamentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the approvisionnementMedicamentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the approvisionnementMedicamentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/approvisionnement-medicaments/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ApprovisionnementMedicamentDTO> partialUpdateApprovisionnementMedicament(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ApprovisionnementMedicamentDTO approvisionnementMedicamentDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ApprovisionnementMedicament partially : {}, {}", id, approvisionnementMedicamentDTO);
        if (approvisionnementMedicamentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, approvisionnementMedicamentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!approvisionnementMedicamentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ApprovisionnementMedicamentDTO> result = approvisionnementMedicamentService.partialUpdate(approvisionnementMedicamentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, approvisionnementMedicamentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /approvisionnement-medicaments} : get all the approvisionnementMedicaments.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of approvisionnementMedicaments in body.
     */
    @GetMapping("/approvisionnement-medicaments")
    public ResponseEntity<List<ApprovisionnementMedicamentDTO>> getAllApprovisionnementMedicaments(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of ApprovisionnementMedicaments");
        Page<ApprovisionnementMedicamentDTO> page = approvisionnementMedicamentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /approvisionnement-medicaments/:id} : get the "id" approvisionnementMedicament.
     *
     * @param id the id of the approvisionnementMedicamentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the approvisionnementMedicamentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/approvisionnement-medicaments/{id}")
    public ResponseEntity<ApprovisionnementMedicamentDTO> getApprovisionnementMedicament(@PathVariable Long id) {
        log.debug("REST request to get ApprovisionnementMedicament : {}", id);
        Optional<ApprovisionnementMedicamentDTO> approvisionnementMedicamentDTO = approvisionnementMedicamentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(approvisionnementMedicamentDTO);
    }

    /**
     * {@code DELETE  /approvisionnement-medicaments/:id} : delete the "id" approvisionnementMedicament.
     *
     * @param id the id of the approvisionnementMedicamentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/approvisionnement-medicaments/{id}")
    public ResponseEntity<Void> deleteApprovisionnementMedicament(@PathVariable Long id) {
        log.debug("REST request to delete ApprovisionnementMedicament : {}", id);
        approvisionnementMedicamentService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
