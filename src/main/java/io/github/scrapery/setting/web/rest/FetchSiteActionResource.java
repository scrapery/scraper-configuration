package io.github.scrapery.setting.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.scrapery.setting.domain.FetchSiteAction;
import io.github.scrapery.setting.service.FetchSiteActionService;
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
 * REST controller for managing FetchSiteAction.
 */
@RestController
@RequestMapping("/api")
public class FetchSiteActionResource {

    private final Logger log = LoggerFactory.getLogger(FetchSiteActionResource.class);

    private static final String ENTITY_NAME = "fetchSiteAction";

    private final FetchSiteActionService fetchSiteActionService;

    public FetchSiteActionResource(FetchSiteActionService fetchSiteActionService) {
        this.fetchSiteActionService = fetchSiteActionService;
    }

    /**
     * POST  /fetch-site-actions : Create a new fetchSiteAction.
     *
     * @param fetchSiteAction the fetchSiteAction to create
     * @return the ResponseEntity with status 201 (Created) and with body the new fetchSiteAction, or with status 400 (Bad Request) if the fetchSiteAction has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/fetch-site-actions")
    @Timed
    public ResponseEntity<FetchSiteAction> createFetchSiteAction(@RequestBody FetchSiteAction fetchSiteAction) throws URISyntaxException {
        log.debug("REST request to save FetchSiteAction : {}", fetchSiteAction);
        if (fetchSiteAction.getId() != null) {
            throw new BadRequestAlertException("A new fetchSiteAction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FetchSiteAction result = fetchSiteActionService.save(fetchSiteAction);
        return ResponseEntity.created(new URI("/api/fetch-site-actions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /fetch-site-actions : Updates an existing fetchSiteAction.
     *
     * @param fetchSiteAction the fetchSiteAction to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated fetchSiteAction,
     * or with status 400 (Bad Request) if the fetchSiteAction is not valid,
     * or with status 500 (Internal Server Error) if the fetchSiteAction couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/fetch-site-actions")
    @Timed
    public ResponseEntity<FetchSiteAction> updateFetchSiteAction(@RequestBody FetchSiteAction fetchSiteAction) throws URISyntaxException {
        log.debug("REST request to update FetchSiteAction : {}", fetchSiteAction);
        if (fetchSiteAction.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FetchSiteAction result = fetchSiteActionService.save(fetchSiteAction);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, fetchSiteAction.getId().toString()))
            .body(result);
    }

    /**
     * GET  /fetch-site-actions : get all the fetchSiteActions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of fetchSiteActions in body
     */
    @GetMapping("/fetch-site-actions")
    @Timed
    public ResponseEntity<List<FetchSiteAction>> getAllFetchSiteActions(Pageable pageable) {
        log.debug("REST request to get a page of FetchSiteActions");
        Page<FetchSiteAction> page = fetchSiteActionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/fetch-site-actions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /fetch-site-actions/:id : get the "id" fetchSiteAction.
     *
     * @param id the id of the fetchSiteAction to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the fetchSiteAction, or with status 404 (Not Found)
     */
    @GetMapping("/fetch-site-actions/{id}")
    @Timed
    public ResponseEntity<FetchSiteAction> getFetchSiteAction(@PathVariable String id) {
        log.debug("REST request to get FetchSiteAction : {}", id);
        Optional<FetchSiteAction> fetchSiteAction = fetchSiteActionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fetchSiteAction);
    }

    /**
     * DELETE  /fetch-site-actions/:id : delete the "id" fetchSiteAction.
     *
     * @param id the id of the fetchSiteAction to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/fetch-site-actions/{id}")
    @Timed
    public ResponseEntity<Void> deleteFetchSiteAction(@PathVariable String id) {
        log.debug("REST request to delete FetchSiteAction : {}", id);
        fetchSiteActionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }

    /**
     * SEARCH  /_search/fetch-site-actions?query=:query : search for the fetchSiteAction corresponding
     * to the query.
     *
     * @param query the query of the fetchSiteAction search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/fetch-site-actions")
    @Timed
    public ResponseEntity<List<FetchSiteAction>> searchFetchSiteActions(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of FetchSiteActions for query {}", query);
        Page<FetchSiteAction> page = fetchSiteActionService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/fetch-site-actions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
