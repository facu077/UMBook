package com.um.umbook.web.rest;
import com.um.umbook.domain.Muro;
import com.um.umbook.repository.MuroRepository;
import com.um.umbook.repository.search.MuroSearchRepository;
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
 * REST controller for managing Muro.
 */
@RestController
@RequestMapping("/api")
public class MuroResource {

    private final Logger log = LoggerFactory.getLogger(MuroResource.class);

    private static final String ENTITY_NAME = "muro";

    private final MuroRepository muroRepository;

    private final MuroSearchRepository muroSearchRepository;

    public MuroResource(MuroRepository muroRepository, MuroSearchRepository muroSearchRepository) {
        this.muroRepository = muroRepository;
        this.muroSearchRepository = muroSearchRepository;
    }

    /**
     * POST  /muros : Create a new muro.
     *
     * @param muro the muro to create
     * @return the ResponseEntity with status 201 (Created) and with body the new muro, or with status 400 (Bad Request) if the muro has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/muros")
    public ResponseEntity<Muro> createMuro(@RequestBody Muro muro) throws URISyntaxException {
        log.debug("REST request to save Muro : {}", muro);
        if (muro.getId() != null) {
            throw new BadRequestAlertException("A new muro cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Muro result = muroRepository.save(muro);
        muroSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/muros/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /muros : Updates an existing muro.
     *
     * @param muro the muro to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated muro,
     * or with status 400 (Bad Request) if the muro is not valid,
     * or with status 500 (Internal Server Error) if the muro couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/muros")
    public ResponseEntity<Muro> updateMuro(@RequestBody Muro muro) throws URISyntaxException {
        log.debug("REST request to update Muro : {}", muro);
        if (muro.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Muro result = muroRepository.save(muro);
        muroSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, muro.getId().toString()))
            .body(result);
    }

    /**
     * GET  /muros : get all the muros.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of muros in body
     */
    @GetMapping("/muros")
    public List<Muro> getAllMuros() {
        log.debug("REST request to get all Muros");
        return muroRepository.findAll();
    }

    /**
     * GET  /muros/:id : get the "id" muro.
     *
     * @param id the id of the muro to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the muro, or with status 404 (Not Found)
     */
    @GetMapping("/muros/{id}")
    public ResponseEntity<Muro> getMuro(@PathVariable Long id) {
        log.debug("REST request to get Muro : {}", id);
        Optional<Muro> muro = muroRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(muro);
    }

    /**
     * DELETE  /muros/:id : delete the "id" muro.
     *
     * @param id the id of the muro to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/muros/{id}")
    public ResponseEntity<Void> deleteMuro(@PathVariable Long id) {
        log.debug("REST request to delete Muro : {}", id);
        muroRepository.deleteById(id);
        muroSearchRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/muros?query=:query : search for the muro corresponding
     * to the query.
     *
     * @param query the query of the muro search
     * @return the result of the search
     */
    @GetMapping("/_search/muros")
    public List<Muro> searchMuros(@RequestParam String query) {
        log.debug("REST request to search Muros for query {}", query);
        return StreamSupport
            .stream(muroSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
