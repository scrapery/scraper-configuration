package io.github.scrapery.setting.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.scrapery.setting.domain.ScrapeResult;
import io.github.scrapery.setting.service.ScrapeResultService;
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
 * REST controller for managing ScrapeResult.
 */
@RestController
@RequestMapping("/api")
public class ScrapeResultResource {

    private final Logger log = LoggerFactory.getLogger(ScrapeResultResource.class);

    private static final String ENTITY_NAME = "scrapeResult";

    private final ScrapeResultService scrapeResultService;

    public ScrapeResultResource(ScrapeResultService scrapeResultService) {
        this.scrapeResultService = scrapeResultService;
    }

    /**
     * POST  /scrape-results : Create a new scrapeResult.
     *
     * @param scrapeResult the scrapeResult to create
     * @return the ResponseEntity with status 201 (Created) and with body the new scrapeResult, or with status 400 (Bad Request) if the scrapeResult has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/scrape-results")
    @Timed
    public ResponseEntity<ScrapeResult> createScrapeResult(@RequestBody ScrapeResult scrapeResult) throws URISyntaxException {
        log.debug("REST request to save ScrapeResult : {}", scrapeResult);
        if (scrapeResult.getId() != null) {
            throw new BadRequestAlertException("A new scrapeResult cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ScrapeResult result = scrapeResultService.save(scrapeResult);
        return ResponseEntity.created(new URI("/api/scrape-results/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /scrape-results : Updates an existing scrapeResult.
     *
     * @param scrapeResult the scrapeResult to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated scrapeResult,
     * or with status 400 (Bad Request) if the scrapeResult is not valid,
     * or with status 500 (Internal Server Error) if the scrapeResult couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/scrape-results")
    @Timed
    public ResponseEntity<ScrapeResult> updateScrapeResult(@RequestBody ScrapeResult scrapeResult) throws URISyntaxException {
        log.debug("REST request to update ScrapeResult : {}", scrapeResult);
        if (scrapeResult.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ScrapeResult result = scrapeResultService.save(scrapeResult);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, scrapeResult.getId().toString()))
            .body(result);
    }

    /**
     * GET  /scrape-results : get all the scrapeResults.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of scrapeResults in body
     */
    @GetMapping("/scrape-results")
    @Timed
    public ResponseEntity<List<ScrapeResult>> getAllScrapeResults(Pageable pageable) {
        log.debug("REST request to get a page of ScrapeResults");
        Page<ScrapeResult> page = scrapeResultService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/scrape-results");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /scrape-results/:id : get the "id" scrapeResult.
     *
     * @param id the id of the scrapeResult to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the scrapeResult, or with status 404 (Not Found)
     */
    @GetMapping("/scrape-results/{id}")
    @Timed
    public ResponseEntity<ScrapeResult> getScrapeResult(@PathVariable String id) {
        log.debug("REST request to get ScrapeResult : {}", id);
        Optional<ScrapeResult> scrapeResult = scrapeResultService.findOne(id);
        return ResponseUtil.wrapOrNotFound(scrapeResult);
    }

    /**
     * DELETE  /scrape-results/:id : delete the "id" scrapeResult.
     *
     * @param id the id of the scrapeResult to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/scrape-results/{id}")
    @Timed
    public ResponseEntity<Void> deleteScrapeResult(@PathVariable String id) {
        log.debug("REST request to delete ScrapeResult : {}", id);
        scrapeResultService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }

    /**
     * SEARCH  /_search/scrape-results?query=:query : search for the scrapeResult corresponding
     * to the query.
     *
     * @param query the query of the scrapeResult search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/scrape-results")
    @Timed
    public ResponseEntity<List<ScrapeResult>> searchScrapeResults(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ScrapeResults for query {}", query);
        Page<ScrapeResult> page = scrapeResultService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/scrape-results");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
