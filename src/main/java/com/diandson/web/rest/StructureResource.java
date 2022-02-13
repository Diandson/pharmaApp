package com.diandson.web.rest;

import com.diandson.repository.StructureRepository;
import com.diandson.service.StructureService;
import com.diandson.service.dto.StructureDTO;
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
 * REST controller for managing {@link com.diandson.domain.Structure}.
 */
@RestController
@RequestMapping("/api")
public class StructureResource {

    private final Logger log = LoggerFactory.getLogger(StructureResource.class);

    private static final String ENTITY_NAME = "structure";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StructureService structureService;

    private final StructureRepository structureRepository;

    public StructureResource(StructureService structureService, StructureRepository structureRepository) {
        this.structureService = structureService;
        this.structureRepository = structureRepository;
    }

    /**
     * {@code POST  /structures} : Create a new structure.
     *
     * @param structureDTO the structureDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new structureDTO, or with status {@code 400 (Bad Request)} if the structure has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/structures")
    public ResponseEntity<StructureDTO> createStructure(@RequestBody StructureDTO structureDTO) throws URISyntaxException {
        log.debug("REST request to save Structure : {}", structureDTO);
        if (structureDTO.getId() != null) {
            throw new BadRequestAlertException("A new structure cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StructureDTO result = structureService.save(structureDTO);
        return ResponseEntity
            .created(new URI("/api/structures/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /structures/:id} : Updates an existing structure.
     *
     * @param id the id of the structureDTO to save.
     * @param structureDTO the structureDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated structureDTO,
     * or with status {@code 400 (Bad Request)} if the structureDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the structureDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/structures/{id}")
    public ResponseEntity<StructureDTO> updateStructure(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StructureDTO structureDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Structure : {}, {}", id, structureDTO);
        if (structureDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, structureDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!structureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        StructureDTO result = structureService.save(structureDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, structureDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /structures/:id} : Partial updates given fields of an existing structure, field will ignore if it is null
     *
     * @param id the id of the structureDTO to save.
     * @param structureDTO the structureDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated structureDTO,
     * or with status {@code 400 (Bad Request)} if the structureDTO is not valid,
     * or with status {@code 404 (Not Found)} if the structureDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the structureDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/structures/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StructureDTO> partialUpdateStructure(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StructureDTO structureDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Structure partially : {}, {}", id, structureDTO);
        if (structureDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, structureDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!structureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StructureDTO> result = structureService.partialUpdate(structureDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, structureDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /structures} : get all the structures.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of structures in body.
     */
    @GetMapping("/structures")
    public ResponseEntity<List<StructureDTO>> getAllStructures(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Structures");
        Page<StructureDTO> page = structureService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /structures/:id} : get the "id" structure.
     *
     * @param id the id of the structureDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the structureDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/structures/{id}")
    public ResponseEntity<StructureDTO> getStructure(@PathVariable Long id) {
        log.debug("REST request to get Structure : {}", id);
        Optional<StructureDTO> structureDTO = structureService.findOne(id);
        return ResponseUtil.wrapOrNotFound(structureDTO);
    }

    @GetMapping("/structures/only")
    public StructureDTO getOnlyStructure() {
        return structureService.findOneOnly();
    }
    @GetMapping("/structures/only/auth")
    public StructureDTO getOnlyStructureAuth() {
        return structureService.findOneOnlyAuth();
    }

    /**
     * {@code DELETE  /structures/:id} : delete the "id" structure.
     *
     * @param id the id of the structureDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/structures/{id}")
    public ResponseEntity<Void> deleteStructure(@PathVariable Long id) {
        log.debug("REST request to delete Structure : {}", id);
        structureService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
