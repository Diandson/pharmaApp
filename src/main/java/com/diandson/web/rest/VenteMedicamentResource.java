package com.diandson.web.rest;

import com.diandson.repository.VenteMedicamentRepository;
import com.diandson.service.VenteMedicamentService;
import com.diandson.service.dto.VenteMedicamentDTO;
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
 * REST controller for managing {@link com.diandson.domain.VenteMedicament}.
 */
@RestController
@RequestMapping("/api")
public class VenteMedicamentResource {

    private final Logger log = LoggerFactory.getLogger(VenteMedicamentResource.class);

    private static final String ENTITY_NAME = "venteMedicament";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VenteMedicamentService venteMedicamentService;

    private final VenteMedicamentRepository venteMedicamentRepository;

    public VenteMedicamentResource(VenteMedicamentService venteMedicamentService, VenteMedicamentRepository venteMedicamentRepository) {
        this.venteMedicamentService = venteMedicamentService;
        this.venteMedicamentRepository = venteMedicamentRepository;
    }

    /**
     * {@code POST  /vente-medicaments} : Create a new venteMedicament.
     *
     * @param venteMedicamentDTO the venteMedicamentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new venteMedicamentDTO, or with status {@code 400 (Bad Request)} if the venteMedicament has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/vente-medicaments")
    public ResponseEntity<VenteMedicamentDTO> createVenteMedicament(@RequestBody VenteMedicamentDTO venteMedicamentDTO)
        throws URISyntaxException {
        log.debug("REST request to save VenteMedicament : {}", venteMedicamentDTO);
        if (venteMedicamentDTO.getId() != null) {
            throw new BadRequestAlertException("A new venteMedicament cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VenteMedicamentDTO result = venteMedicamentService.save(venteMedicamentDTO);
        return ResponseEntity
            .created(new URI("/api/vente-medicaments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /vente-medicaments/:id} : Updates an existing venteMedicament.
     *
     * @param id the id of the venteMedicamentDTO to save.
     * @param venteMedicamentDTO the venteMedicamentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated venteMedicamentDTO,
     * or with status {@code 400 (Bad Request)} if the venteMedicamentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the venteMedicamentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/vente-medicaments/{id}")
    public ResponseEntity<VenteMedicamentDTO> updateVenteMedicament(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VenteMedicamentDTO venteMedicamentDTO
    ) throws URISyntaxException {
        log.debug("REST request to update VenteMedicament : {}, {}", id, venteMedicamentDTO);
        if (venteMedicamentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, venteMedicamentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!venteMedicamentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        VenteMedicamentDTO result = venteMedicamentService.save(venteMedicamentDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, venteMedicamentDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /vente-medicaments/:id} : Partial updates given fields of an existing venteMedicament, field will ignore if it is null
     *
     * @param id the id of the venteMedicamentDTO to save.
     * @param venteMedicamentDTO the venteMedicamentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated venteMedicamentDTO,
     * or with status {@code 400 (Bad Request)} if the venteMedicamentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the venteMedicamentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the venteMedicamentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/vente-medicaments/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VenteMedicamentDTO> partialUpdateVenteMedicament(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VenteMedicamentDTO venteMedicamentDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update VenteMedicament partially : {}, {}", id, venteMedicamentDTO);
        if (venteMedicamentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, venteMedicamentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!venteMedicamentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VenteMedicamentDTO> result = venteMedicamentService.partialUpdate(venteMedicamentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, venteMedicamentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /vente-medicaments} : get all the venteMedicaments.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of venteMedicaments in body.
     */
    @GetMapping("/vente-medicaments")
    public ResponseEntity<List<VenteMedicamentDTO>> getAllVenteMedicaments(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of VenteMedicaments");
        Page<VenteMedicamentDTO> page = venteMedicamentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /vente-medicaments/:id} : get the "id" venteMedicament.
     *
     * @param id the id of the venteMedicamentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the venteMedicamentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/vente-medicaments/{id}")
    public ResponseEntity<VenteMedicamentDTO> getVenteMedicament(@PathVariable Long id) {
        log.debug("REST request to get VenteMedicament : {}", id);
        Optional<VenteMedicamentDTO> venteMedicamentDTO = venteMedicamentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(venteMedicamentDTO);
    }

    /**
     * {@code DELETE  /vente-medicaments/:id} : delete the "id" venteMedicament.
     *
     * @param id the id of the venteMedicamentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/vente-medicaments/{id}")
    public ResponseEntity<Void> deleteVenteMedicament(@PathVariable Long id) {
        log.debug("REST request to delete VenteMedicament : {}", id);
        venteMedicamentService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
