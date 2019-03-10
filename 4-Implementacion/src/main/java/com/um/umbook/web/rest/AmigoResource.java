package com.um.umbook.web.rest;
import com.um.umbook.domain.Amigo;
import com.um.umbook.repository.AmigoRepository;
import com.um.umbook.repository.search.AmigoSearchRepository;
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
 * REST controller for managing Amigo.
 */
@RestController
@RequestMapping("/api")
public class AmigoResource {

    private final Logger log = LoggerFactory.getLogger(AmigoResource.class);

    private static final String ENTITY_NAME = "amigo";

    private final AmigoRepository amigoRepository;

    private final AmigoSearchRepository amigoSearchRepository;

    public AmigoResource(AmigoRepository amigoRepository, AmigoSearchRepository amigoSearchRepository) {
        this.amigoRepository = amigoRepository;
        this.amigoSearchRepository = amigoSearchRepository;
    }

    /**
     * POST  /amigos : Create a new amigo.
     *
     * @param amigo the amigo to create
     * @return the ResponseEntity with status 201 (Created) and with body the new amigo, or with status 400 (Bad Request) if the amigo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/amigos")
    public ResponseEntity<Amigo> createAmigo(@RequestBody Amigo amigo) throws URISyntaxException {
        log.debug("REST request to save Amigo : {}", amigo);
        if (amigo.getId() != null) {
            throw new BadRequestAlertException("A new amigo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Amigo result = amigoRepository.save(amigo);
        amigoSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/amigos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /amigos : Updates an existing amigo.
     *
     * @param amigo the amigo to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated amigo,
     * or with status 400 (Bad Request) if the amigo is not valid,
     * or with status 500 (Internal Server Error) if the amigo couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/amigos")
    public ResponseEntity<Amigo> updateAmigo(@RequestBody Amigo amigo) throws URISyntaxException {
        log.debug("REST request to update Amigo : {}", amigo);
        if (amigo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Amigo result = amigoRepository.save(amigo);
        amigoSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, amigo.getId().toString()))
            .body(result);
    }

    /**
     * GET  /amigos : get all the amigos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of amigos in body
     */
    @GetMapping("/amigos")
    public List<Amigo> getAllAmigos() {
        log.debug("REST request to get all Amigos");
        return amigoRepository.findAll();
    }

    /**
     * GET  /amigos/:id : get the "id" amigo.
     *
     * @param id the id of the amigo to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the amigo, or with status 404 (Not Found)
     */
    @GetMapping("/amigos/{id}")
    public ResponseEntity<Amigo> getAmigo(@PathVariable Long id) {
        log.debug("REST request to get Amigo : {}", id);
        Optional<Amigo> amigo = amigoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(amigo);
    }

    /**
     * DELETE  /amigos/:id : delete the "id" amigo.
     *
     * @param id the id of the amigo to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/amigos/{id}")
    public ResponseEntity<Void> deleteAmigo(@PathVariable Long id) {
        log.debug("REST request to delete Amigo : {}", id);
        amigoRepository.deleteById(id);
        amigoSearchRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/amigos?query=:query : search for the amigo corresponding
     * to the query.
     *
     * @param query the query of the amigo search
     * @return the result of the search
     */
    @GetMapping("/_search/amigos")
    public List<Amigo> searchAmigos(@RequestParam String query) {
        log.debug("REST request to search Amigos for query {}", query);
        return StreamSupport
            .stream(amigoSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
