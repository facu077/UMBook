package com.um.umbook.web.rest;
import com.um.umbook.domain.Mensaje;
import com.um.umbook.repository.MensajeRepository;
import com.um.umbook.repository.search.MensajeSearchRepository;
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
 * REST controller for managing Mensaje.
 */
@RestController
@RequestMapping("/api")
public class MensajeResource {

    private final Logger log = LoggerFactory.getLogger(MensajeResource.class);

    private static final String ENTITY_NAME = "mensaje";

    private final MensajeRepository mensajeRepository;

    private final MensajeSearchRepository mensajeSearchRepository;

    public MensajeResource(MensajeRepository mensajeRepository, MensajeSearchRepository mensajeSearchRepository) {
        this.mensajeRepository = mensajeRepository;
        this.mensajeSearchRepository = mensajeSearchRepository;
    }

    /**
     * POST  /mensajes : Create a new mensaje.
     *
     * @param mensaje the mensaje to create
     * @return the ResponseEntity with status 201 (Created) and with body the new mensaje, or with status 400 (Bad Request) if the mensaje has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/mensajes")
    public ResponseEntity<Mensaje> createMensaje(@RequestBody Mensaje mensaje) throws URISyntaxException {
        log.debug("REST request to save Mensaje : {}", mensaje);
        if (mensaje.getId() != null) {
            throw new BadRequestAlertException("A new mensaje cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Mensaje result = mensajeRepository.save(mensaje);
        mensajeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/mensajes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /mensajes : Updates an existing mensaje.
     *
     * @param mensaje the mensaje to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated mensaje,
     * or with status 400 (Bad Request) if the mensaje is not valid,
     * or with status 500 (Internal Server Error) if the mensaje couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/mensajes")
    public ResponseEntity<Mensaje> updateMensaje(@RequestBody Mensaje mensaje) throws URISyntaxException {
        log.debug("REST request to update Mensaje : {}", mensaje);
        if (mensaje.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Mensaje result = mensajeRepository.save(mensaje);
        mensajeSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, mensaje.getId().toString()))
            .body(result);
    }

    /**
     * GET  /mensajes : get all the mensajes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of mensajes in body
     */
    @GetMapping("/mensajes")
    public List<Mensaje> getAllMensajes() {
        log.debug("REST request to get all Mensajes");
        return mensajeRepository.findAll();
    }

    /**
     * GET  /mensajes/:id : get the "id" mensaje.
     *
     * @param id the id of the mensaje to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the mensaje, or with status 404 (Not Found)
     */
    @GetMapping("/mensajes/{id}")
    public ResponseEntity<Mensaje> getMensaje(@PathVariable Long id) {
        log.debug("REST request to get Mensaje : {}", id);
        Optional<Mensaje> mensaje = mensajeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(mensaje);
    }

    /**
     * DELETE  /mensajes/:id : delete the "id" mensaje.
     *
     * @param id the id of the mensaje to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/mensajes/{id}")
    public ResponseEntity<Void> deleteMensaje(@PathVariable Long id) {
        log.debug("REST request to delete Mensaje : {}", id);
        mensajeRepository.deleteById(id);
        mensajeSearchRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/mensajes?query=:query : search for the mensaje corresponding
     * to the query.
     *
     * @param query the query of the mensaje search
     * @return the result of the search
     */
    @GetMapping("/_search/mensajes")
    public List<Mensaje> searchMensajes(@RequestParam String query) {
        log.debug("REST request to search Mensajes for query {}", query);
        return StreamSupport
            .stream(mensajeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
