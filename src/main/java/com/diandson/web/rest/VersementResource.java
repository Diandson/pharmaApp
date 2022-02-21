package com.diandson.web.rest;

import com.diandson.repository.VersementRepository;
import com.diandson.service.VersementService;
import com.diandson.service.dto.VersementDTO;
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
 * REST controller for managing {@link com.diandson.domain.Versement}.
 */
@RestController
@RequestMapping("/api")
public class VersementResource {

    private final Logger log = LoggerFactory.getLogger(VersementResource.class);

    private static final String ENTITY_NAME = "versement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VersementService versementService;

    private final VersementRepository versementRepository;

    public VersementResource(VersementService versementService, VersementRepository versementRepository) {
        this.versementService = versementService;
        this.versementRepository = versementRepository;
    }

    /**
     * {@code POST  /versements} : Create a new versement.
     *
     * @param versementDTO the versementDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new versementDTO, or with status {@code 400 (Bad Request)} if the versement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/versements")
    public ResponseEntity<VersementDTO> createVersement(@RequestBody VersementDTO versementDTO) throws URISyntaxException {
        log.debug("REST request to save Versement : {}", versementDTO);
        if (versementDTO.getId() != null) {
            throw new BadRequestAlertException("A new versement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VersementDTO result = versementService.save(versementDTO);
        return ResponseEntity
            .created(new URI("/api/versements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /versements/:id} : Updates an existing versement.
     *
     * @param id the id of the versementDTO to save.
     * @param versementDTO the versementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated versementDTO,
     * or with status {@code 400 (Bad Request)} if the versementDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the versementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/versements/{id}")
    public ResponseEntity<VersementDTO> updateVersement(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VersementDTO versementDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Versement : {}, {}", id, versementDTO);
        if (versementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, versementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!versementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        VersementDTO result = versementService.save(versementDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, versementDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /versements/:id} : Partial updates given fields of an existing versement, field will ignore if it is null
     *
     * @param id the id of the versementDTO to save.
     * @param versementDTO the versementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated versementDTO,
     * or with status {@code 400 (Bad Request)} if the versementDTO is not valid,
     * or with status {@code 404 (Not Found)} if the versementDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the versementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/versements/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VersementDTO> partialUpdateVersement(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VersementDTO versementDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Versement partially : {}, {}", id, versementDTO);
        if (versementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, versementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!versementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VersementDTO> result = versementService.partialUpdate(versementDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, versementDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /versements} : get all the versements.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of versements in body.
     */
    @GetMapping("/versements")
    public ResponseEntity<List<VersementDTO>> getAllVersements(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Versements");
        Page<VersementDTO> page = versementService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /versements/:id} : get the "id" versement.
     *
     * @param id the id of the versementDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the versementDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/versements/{id}")
    public ResponseEntity<VersementDTO> getVersement(@PathVariable Long id) {
        log.debug("REST request to get Versement : {}", id);
        Optional<VersementDTO> versementDTO = versementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(versementDTO);
    }

    /**
     * {@code DELETE  /versements/:id} : delete the "id" versement.
     *
     * @param id the id of the versementDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/versements/{id}")
    public ResponseEntity<Void> deleteVersement(@PathVariable Long id) {
        log.debug("REST request to delete Versement : {}", id);
        versementService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
