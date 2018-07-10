package io.github.scrapery.setting.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.scrapery.setting.domain.Demo;
import io.github.scrapery.setting.service.DemoService;
import io.github.scrapery.setting.web.rest.errors.BadRequestAlertException;
import io.github.scrapery.setting.web.rest.util.HeaderUtil;
import io.github.scrapery.setting.web.rest.util.PaginationUtil;
import io.github.simlife.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Demo.
 */
@RestController
@RequestMapping("/api")
public class DemoResource {

    private final Logger log = LoggerFactory.getLogger(DemoResource.class);

    private static final String ENTITY_NAME = "demo";

    private final DemoService demoService;

    public DemoResource(DemoService demoService) {
        this.demoService = demoService;
    }

    /**
     * POST  /demos : Create a new demo.
     *
     * @param demo the demo to create
     * @return the ResponseEntity with status 201 (Created) and with body the new demo, or with status 400 (Bad Request) if the demo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/demos")
    @Timed
    public ResponseEntity<Demo> createDemo(@RequestBody Demo demo) throws URISyntaxException {
        log.debug("REST request to save Demo : {}", demo);
        if (demo.getId() != null) {
            throw new BadRequestAlertException("A new demo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Demo result = demoService.save(demo);
        return ResponseEntity.created(new URI("/api/demos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /demos : Updates an existing demo.
     *
     * @param demo the demo to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated demo,
     * or with status 400 (Bad Request) if the demo is not valid,
     * or with status 500 (Internal Server Error) if the demo couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/demos")
    @Timed
    public ResponseEntity<Demo> updateDemo(@RequestBody Demo demo) throws URISyntaxException {
        log.debug("REST request to update Demo : {}", demo);
        if (demo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Demo result = demoService.save(demo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, demo.getId().toString()))
            .body(result);
    }

    /**
     * GET  /demos : get all the demos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of demos in body
     */
    @GetMapping("/demos")
    @Timed
    public ResponseEntity<List<Demo>> getAllDemos(Pageable pageable) {
        log.debug("REST request to get a page of Demos");
        Page<Demo> page = demoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/demos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /demos/:id : get the "id" demo.
     *
     * @param id the id of the demo to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the demo, or with status 404 (Not Found)
     */
    @GetMapping("/demos/{id}")
    @Timed
    public ResponseEntity<Demo> getDemo(@PathVariable String id) {
        log.debug("REST request to get Demo : {}", id);
        Optional<Demo> demo = demoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(demo);
    }

    /**
     * DELETE  /demos/:id : delete the "id" demo.
     *
     * @param id the id of the demo to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/demos/{id}")
    @Timed
    public ResponseEntity<Void> deleteDemo(@PathVariable String id) {
        log.debug("REST request to delete Demo : {}", id);
        demoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }

    /**
     * SEARCH  /_search/demos?query=:query : search for the demo corresponding
     * to the query.
     *
     * @param query the query of the demo search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/demos")
    @Timed
    public ResponseEntity<List<Demo>> searchDemos(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Demos for query {}", query);
        Page<Demo> page = demoService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/demos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
