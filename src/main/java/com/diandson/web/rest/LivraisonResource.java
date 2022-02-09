package com.diandson.web.rest;

import com.diandson.repository.LivraisonRepository;
import com.diandson.service.LivraisonService;
import com.diandson.service.dto.LivraisonDTO;
import com.diandson.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
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
 * REST controller for managing {@link com.diandson.domain.Livraison}.
 */
@RestController
@RequestMapping("/api")
public class LivraisonResource {

    private final Logger log = LoggerFactory.getLogger(LivraisonResource.class);

    private static final String ENTITY_NAME = "livraison";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LivraisonService livraisonService;

    private final LivraisonRepository livraisonRepository;

    public LivraisonResource(LivraisonService livraisonService, LivraisonRepository livraisonRepository) {
        this.livraisonService = livraisonService;
        this.livraisonRepository = livraisonRepository;
    }

    /**
     * {@code POST  /livraisons} : Create a new livraison.
     *
     * @param livraisonDTO the livraisonDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new livraisonDTO, or with status {@code 400 (Bad Request)} if the livraison has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/livraisons")
    public ResponseEntity<LivraisonDTO> createLivraison(@RequestBody LivraisonDTO livraisonDTO) throws URISyntaxException {
        log.debug("REST request to save Livraison : {}", livraisonDTO);
        if (livraisonDTO.getId() != null) {
            throw new BadRequestAlertException("A new livraison cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LivraisonDTO result = livraisonService.save(livraisonDTO);
        return ResponseEntity
            .created(new URI("/api/livraisons/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /livraisons/:id} : Updates an existing livraison.
     *
     * @param id the id of the livraisonDTO to save.
     * @param livraisonDTO the livraisonDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated livraisonDTO,
     * or with status {@code 400 (Bad Request)} if the livraisonDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the livraisonDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/livraisons/{id}")
    public ResponseEntity<LivraisonDTO> updateLivraison(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LivraisonDTO livraisonDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Livraison : {}, {}", id, livraisonDTO);
        if (livraisonDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, livraisonDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!livraisonRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LivraisonDTO result = livraisonService.save(livraisonDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, livraisonDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /livraisons/:id} : Partial updates given fields of an existing livraison, field will ignore if it is null
     *
     * @param id the id of the livraisonDTO to save.
     * @param livraisonDTO the livraisonDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated livraisonDTO,
     * or with status {@code 400 (Bad Request)} if the livraisonDTO is not valid,
     * or with status {@code 404 (Not Found)} if the livraisonDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the livraisonDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/livraisons/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LivraisonDTO> partialUpdateLivraison(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LivraisonDTO livraisonDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Livraison partially : {}, {}", id, livraisonDTO);
        if (livraisonDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, livraisonDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!livraisonRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LivraisonDTO> result = livraisonService.partialUpdate(livraisonDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, livraisonDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /livraisons} : get all the livraisons.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of livraisons in body.
     */
    @GetMapping("/livraisons")
    public ResponseEntity<List<LivraisonDTO>> getAllLivraisons(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false) String filter
    ) {
        if ("commande-is-null".equals(filter)) {
            log.debug("REST request to get all Livraisons where commande is null");
            return new ResponseEntity<>(livraisonService.findAllWhereCommandeIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of Livraisons");
        Page<LivraisonDTO> page = livraisonService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /livraisons/:id} : get the "id" livraison.
     *
     * @param id the id of the livraisonDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the livraisonDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/livraisons/{id}")
    public ResponseEntity<LivraisonDTO> getLivraison(@PathVariable Long id) {
        log.debug("REST request to get Livraison : {}", id);
        Optional<LivraisonDTO> livraisonDTO = livraisonService.findOne(id);
        return ResponseUtil.wrapOrNotFound(livraisonDTO);
    }

    /**
     * {@code DELETE  /livraisons/:id} : delete the "id" livraison.
     *
     * @param id the id of the livraisonDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/livraisons/{id}")
    public ResponseEntity<Void> deleteLivraison(@PathVariable Long id) {
        log.debug("REST request to delete Livraison : {}", id);
        livraisonService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
