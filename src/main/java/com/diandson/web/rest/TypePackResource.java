package com.diandson.web.rest;

import com.diandson.repository.TypePackRepository;
import com.diandson.service.TypePackService;
import com.diandson.service.dto.TypePackDTO;
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
 * REST controller for managing {@link com.diandson.domain.TypePack}.
 */
@RestController
@RequestMapping("/api")
public class TypePackResource {

    private final Logger log = LoggerFactory.getLogger(TypePackResource.class);

    private static final String ENTITY_NAME = "typePack";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TypePackService typePackService;

    private final TypePackRepository typePackRepository;

    public TypePackResource(TypePackService typePackService, TypePackRepository typePackRepository) {
        this.typePackService = typePackService;
        this.typePackRepository = typePackRepository;
    }

    /**
     * {@code POST  /type-packs} : Create a new typePack.
     *
     * @param typePackDTO the typePackDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new typePackDTO, or with status {@code 400 (Bad Request)} if the typePack has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/type-packs")
    public ResponseEntity<TypePackDTO> createTypePack(@RequestBody TypePackDTO typePackDTO) throws URISyntaxException {
        log.debug("REST request to save TypePack : {}", typePackDTO);
        if (typePackDTO.getId() != null) {
            throw new BadRequestAlertException("A new typePack cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TypePackDTO result = typePackService.save(typePackDTO);
        return ResponseEntity
            .created(new URI("/api/type-packs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /type-packs/:id} : Updates an existing typePack.
     *
     * @param id the id of the typePackDTO to save.
     * @param typePackDTO the typePackDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typePackDTO,
     * or with status {@code 400 (Bad Request)} if the typePackDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the typePackDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/type-packs/{id}")
    public ResponseEntity<TypePackDTO> updateTypePack(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TypePackDTO typePackDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TypePack : {}, {}", id, typePackDTO);
        if (typePackDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typePackDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typePackRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TypePackDTO result = typePackService.save(typePackDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typePackDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /type-packs/:id} : Partial updates given fields of an existing typePack, field will ignore if it is null
     *
     * @param id the id of the typePackDTO to save.
     * @param typePackDTO the typePackDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typePackDTO,
     * or with status {@code 400 (Bad Request)} if the typePackDTO is not valid,
     * or with status {@code 404 (Not Found)} if the typePackDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the typePackDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/type-packs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TypePackDTO> partialUpdateTypePack(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TypePackDTO typePackDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TypePack partially : {}, {}", id, typePackDTO);
        if (typePackDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typePackDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typePackRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TypePackDTO> result = typePackService.partialUpdate(typePackDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typePackDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /type-packs} : get all the typePacks.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of typePacks in body.
     */
    @GetMapping("/type-packs")
    public ResponseEntity<List<TypePackDTO>> getAllTypePacks(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of TypePacks");
        Page<TypePackDTO> page = typePackService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /type-packs/:id} : get the "id" typePack.
     *
     * @param id the id of the typePackDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the typePackDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/type-packs/{id}")
    public ResponseEntity<TypePackDTO> getTypePack(@PathVariable Long id) {
        log.debug("REST request to get TypePack : {}", id);
        Optional<TypePackDTO> typePackDTO = typePackService.findOne(id);
        return ResponseUtil.wrapOrNotFound(typePackDTO);
    }

    /**
     * {@code DELETE  /type-packs/:id} : delete the "id" typePack.
     *
     * @param id the id of the typePackDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/type-packs/{id}")
    public ResponseEntity<Void> deleteTypePack(@PathVariable Long id) {
        log.debug("REST request to delete TypePack : {}", id);
        typePackService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
