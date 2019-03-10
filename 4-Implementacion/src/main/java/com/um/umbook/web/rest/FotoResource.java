package com.um.umbook.web.rest;
import com.um.umbook.domain.Foto;
import com.um.umbook.repository.FotoRepository;
import com.um.umbook.repository.search.FotoSearchRepository;
import com.um.umbook.web.rest.errors.BadRequestAlertException;
import com.um.umbook.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Foto.
 */
@RestController
@RequestMapping("/api")
public class FotoResource {

    private final Logger log = LoggerFactory.getLogger(FotoResource.class);

    private static final String ENTITY_NAME = "foto";

    private final FotoRepository fotoRepository;

    private final FotoSearchRepository fotoSearchRepository;

    public FotoResource(FotoRepository fotoRepository, FotoSearchRepository fotoSearchRepository) {
        this.fotoRepository = fotoRepository;
        this.fotoSearchRepository = fotoSearchRepository;
    }

    /**
     * POST  /fotos : Create a new foto.
     *
     * @param foto the foto to create
     * @return the ResponseEntity with status 201 (Created) and with body the new foto, or with status 400 (Bad Request) if the foto has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/fotos")
    public ResponseEntity<Foto> createFoto(@RequestBody Foto foto) throws URISyntaxException {
        log.debug("REST request to save Foto : {}", foto);
        if (foto.getId() != null) {
            throw new BadRequestAlertException("A new foto cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Foto result = fotoRepository.save(foto);
        fotoSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/fotos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /fotos : Updates an existing foto.
     *
     * @param foto the foto to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated foto,
     * or with status 400 (Bad Request) if the foto is not valid,
     * or with status 500 (Internal Server Error) if the foto couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/fotos")
    public ResponseEntity<Foto> updateFoto(@RequestBody Foto foto) throws URISyntaxException {
        log.debug("REST request to update Foto : {}", foto);
        if (foto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Foto result = fotoRepository.save(foto);
        fotoSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, foto.getId().toString()))
            .body(result);
    }

    /**
     * GET  /fotos : get all the fotos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of fotos in body
     */
    @GetMapping("/fotos")
    public List<Foto> getAllFotos() {
        log.debug("REST request to get all Fotos");
        return fotoRepository.findAll();
    }

    /**
     * GET  /fotos/:id : get the "id" foto.
     *
     * @param id the id of the foto to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the foto, or with status 404 (Not Found)
     */
    @GetMapping("/fotos/{id}")
    public ResponseEntity<Foto> getFoto(@PathVariable Long id) {
        log.debug("REST request to get Foto : {}", id);
        Optional<Foto> foto = fotoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(foto);
    }

    /**
     * DELETE  /fotos/:id : delete the "id" foto.
     *
     * @param id the id of the foto to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/fotos/{id}")
    public ResponseEntity<Void> deleteFoto(@PathVariable Long id) {
        log.debug("REST request to delete Foto : {}", id);
        fotoRepository.deleteById(id);
        fotoSearchRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/fotos?query=:query : search for the foto corresponding
     * to the query.
     *
     * @param query the query of the foto search
     * @return the result of the search
     */
    @GetMapping("/_search/fotos")
    public List<Foto> searchFotos(@RequestParam String query) {
        log.debug("REST request to search Fotos for query {}", query);
        return StreamSupport
            .stream(fotoSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
