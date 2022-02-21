package com.diandson.web.rest;

import com.diandson.repository.LieuVersementRepository;
import com.diandson.service.LieuVersementService;
import com.diandson.service.dto.LieuVersementDTO;
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
 * REST controller for managing {@link com.diandson.domain.LieuVersement}.
 */
@RestController
@RequestMapping("/api")
public class LieuVersementResource {

    private final Logger log = LoggerFactory.getLogger(LieuVersementResource.class);

    private static final String ENTITY_NAME = "lieuVersement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LieuVersementService lieuVersementService;

    private final LieuVersementRepository lieuVersementRepository;

    public LieuVersementResource(LieuVersementService lieuVersementService, LieuVersementRepository lieuVersementRepository) {
        this.lieuVersementService = lieuVersementService;
        this.lieuVersementRepository = lieuVersementRepository;
    }

    /**
     * {@code POST  /lieu-versements} : Create a new lieuVersement.
     *
     * @param lieuVersementDTO the lieuVersementDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new lieuVersementDTO, or with status {@code 400 (Bad Request)} if the lieuVersement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/lieu-versements")
    public ResponseEntity<LieuVersementDTO> createLieuVersement(@RequestBody LieuVersementDTO lieuVersementDTO) throws URISyntaxException {
        log.debug("REST request to save LieuVersement : {}", lieuVersementDTO);
        if (lieuVersementDTO.getId() != null) {
            throw new BadRequestAlertException("A new lieuVersement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LieuVersementDTO result = lieuVersementService.save(lieuVersementDTO);
        return ResponseEntity
            .created(new URI("/api/lieu-versements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /lieu-versements/:id} : Updates an existing lieuVersement.
     *
     * @param id the id of the lieuVersementDTO to save.
     * @param lieuVersementDTO the lieuVersementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lieuVersementDTO,
     * or with status {@code 400 (Bad Request)} if the lieuVersementDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the lieuVersementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/lieu-versements/{id}")
    public ResponseEntity<LieuVersementDTO> updateLieuVersement(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LieuVersementDTO lieuVersementDTO
    ) throws URISyntaxException {
        log.debug("REST request to update LieuVersement : {}, {}", id, lieuVersementDTO);
        if (lieuVersementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lieuVersementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lieuVersementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LieuVersementDTO result = lieuVersementService.save(lieuVersementDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lieuVersementDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /lieu-versements/:id} : Partial updates given fields of an existing lieuVersement, field will ignore if it is null
     *
     * @param id the id of the lieuVersementDTO to save.
     * @param lieuVersementDTO the lieuVersementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lieuVersementDTO,
     * or with status {@code 400 (Bad Request)} if the lieuVersementDTO is not valid,
     * or with status {@code 404 (Not Found)} if the lieuVersementDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the lieuVersementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/lieu-versements/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LieuVersementDTO> partialUpdateLieuVersement(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LieuVersementDTO lieuVersementDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update LieuVersement partially : {}, {}", id, lieuVersementDTO);
        if (lieuVersementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lieuVersementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lieuVersementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LieuVersementDTO> result = lieuVersementService.partialUpdate(lieuVersementDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lieuVersementDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /lieu-versements} : get all the lieuVersements.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lieuVersements in body.
     */
    @GetMapping("/lieu-versements")
    public ResponseEntity<List<LieuVersementDTO>> getAllLieuVersements(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of LieuVersements");
        Page<LieuVersementDTO> page = lieuVersementService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /lieu-versements/:id} : get the "id" lieuVersement.
     *
     * @param id the id of the lieuVersementDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lieuVersementDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/lieu-versements/{id}")
    public ResponseEntity<LieuVersementDTO> getLieuVersement(@PathVariable Long id) {
        log.debug("REST request to get LieuVersement : {}", id);
        Optional<LieuVersementDTO> lieuVersementDTO = lieuVersementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(lieuVersementDTO);
    }

    /**
     * {@code DELETE  /lieu-versements/:id} : delete the "id" lieuVersement.
     *
     * @param id the id of the lieuVersementDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/lieu-versements/{id}")
    public ResponseEntity<Void> deleteLieuVersement(@PathVariable Long id) {
        log.debug("REST request to delete LieuVersement : {}", id);
        lieuVersementService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
