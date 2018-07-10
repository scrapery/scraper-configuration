package io.github.scrapery.setting.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.scrapery.setting.domain.SpiderScheduler;
import io.github.scrapery.setting.service.SpiderSchedulerService;
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
 * REST controller for managing SpiderScheduler.
 */
@RestController
@RequestMapping("/api")
public class SpiderSchedulerResource {

    private final Logger log = LoggerFactory.getLogger(SpiderSchedulerResource.class);

    private static final String ENTITY_NAME = "spiderScheduler";

    private final SpiderSchedulerService spiderSchedulerService;

    public SpiderSchedulerResource(SpiderSchedulerService spiderSchedulerService) {
        this.spiderSchedulerService = spiderSchedulerService;
    }

    /**
     * POST  /spider-schedulers : Create a new spiderScheduler.
     *
     * @param spiderScheduler the spiderScheduler to create
     * @return the ResponseEntity with status 201 (Created) and with body the new spiderScheduler, or with status 400 (Bad Request) if the spiderScheduler has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/spider-schedulers")
    @Timed
    public ResponseEntity<SpiderScheduler> createSpiderScheduler(@RequestBody SpiderScheduler spiderScheduler) throws URISyntaxException {
        log.debug("REST request to save SpiderScheduler : {}", spiderScheduler);
        if (spiderScheduler.getId() != null) {
            throw new BadRequestAlertException("A new spiderScheduler cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SpiderScheduler result = spiderSchedulerService.save(spiderScheduler);
        return ResponseEntity.created(new URI("/api/spider-schedulers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /spider-schedulers : Updates an existing spiderScheduler.
     *
     * @param spiderScheduler the spiderScheduler to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated spiderScheduler,
     * or with status 400 (Bad Request) if the spiderScheduler is not valid,
     * or with status 500 (Internal Server Error) if the spiderScheduler couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/spider-schedulers")
    @Timed
    public ResponseEntity<SpiderScheduler> updateSpiderScheduler(@RequestBody SpiderScheduler spiderScheduler) throws URISyntaxException {
        log.debug("REST request to update SpiderScheduler : {}", spiderScheduler);
        if (spiderScheduler.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SpiderScheduler result = spiderSchedulerService.save(spiderScheduler);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, spiderScheduler.getId().toString()))
            .body(result);
    }

    /**
     * GET  /spider-schedulers : get all the spiderSchedulers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of spiderSchedulers in body
     */
    @GetMapping("/spider-schedulers")
    @Timed
    public ResponseEntity<List<SpiderScheduler>> getAllSpiderSchedulers(Pageable pageable) {
        log.debug("REST request to get a page of SpiderSchedulers");
        Page<SpiderScheduler> page = spiderSchedulerService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/spider-schedulers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /spider-schedulers/:id : get the "id" spiderScheduler.
     *
     * @param id the id of the spiderScheduler to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the spiderScheduler, or with status 404 (Not Found)
     */
    @GetMapping("/spider-schedulers/{id}")
    @Timed
    public ResponseEntity<SpiderScheduler> getSpiderScheduler(@PathVariable String id) {
        log.debug("REST request to get SpiderScheduler : {}", id);
        Optional<SpiderScheduler> spiderScheduler = spiderSchedulerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(spiderScheduler);
    }

    /**
     * DELETE  /spider-schedulers/:id : delete the "id" spiderScheduler.
     *
     * @param id the id of the spiderScheduler to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/spider-schedulers/{id}")
    @Timed
    public ResponseEntity<Void> deleteSpiderScheduler(@PathVariable String id) {
        log.debug("REST request to delete SpiderScheduler : {}", id);
        spiderSchedulerService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }

    /**
     * SEARCH  /_search/spider-schedulers?query=:query : search for the spiderScheduler corresponding
     * to the query.
     *
     * @param query the query of the spiderScheduler search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/spider-schedulers")
    @Timed
    public ResponseEntity<List<SpiderScheduler>> searchSpiderSchedulers(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of SpiderSchedulers for query {}", query);
        Page<SpiderScheduler> page = spiderSchedulerService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/spider-schedulers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
