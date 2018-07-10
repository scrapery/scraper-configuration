package io.github.scrapery.setting.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.scrapery.setting.domain.ScrapeData;
import io.github.scrapery.setting.service.ScrapeDataService;
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
 * REST controller for managing ScrapeData.
 */
@RestController
@RequestMapping("/api")
public class ScrapeDataResource {

    private final Logger log = LoggerFactory.getLogger(ScrapeDataResource.class);

    private static final String ENTITY_NAME = "scrapeData";

    private final ScrapeDataService scrapeDataService;

    public ScrapeDataResource(ScrapeDataService scrapeDataService) {
        this.scrapeDataService = scrapeDataService;
    }

    /**
     * POST  /scrape-data : Create a new scrapeData.
     *
     * @param scrapeData the scrapeData to create
     * @return the ResponseEntity with status 201 (Created) and with body the new scrapeData, or with status 400 (Bad Request) if the scrapeData has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/scrape-data")
    @Timed
    public ResponseEntity<ScrapeData> createScrapeData(@RequestBody ScrapeData scrapeData) throws URISyntaxException {
        log.debug("REST request to save ScrapeData : {}", scrapeData);
        if (scrapeData.getId() != null) {
            throw new BadRequestAlertException("A new scrapeData cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ScrapeData result = scrapeDataService.save(scrapeData);
        return ResponseEntity.created(new URI("/api/scrape-data/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /scrape-data : Updates an existing scrapeData.
     *
     * @param scrapeData the scrapeData to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated scrapeData,
     * or with status 400 (Bad Request) if the scrapeData is not valid,
     * or with status 500 (Internal Server Error) if the scrapeData couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/scrape-data")
    @Timed
    public ResponseEntity<ScrapeData> updateScrapeData(@RequestBody ScrapeData scrapeData) throws URISyntaxException {
        log.debug("REST request to update ScrapeData : {}", scrapeData);
        if (scrapeData.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ScrapeData result = scrapeDataService.save(scrapeData);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, scrapeData.getId().toString()))
            .body(result);
    }

    /**
     * GET  /scrape-data : get all the scrapeData.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of scrapeData in body
     */
    @GetMapping("/scrape-data")
    @Timed
    public ResponseEntity<List<ScrapeData>> getAllScrapeData(Pageable pageable) {
        log.debug("REST request to get a page of ScrapeData");
        Page<ScrapeData> page = scrapeDataService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/scrape-data");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /scrape-data/:id : get the "id" scrapeData.
     *
     * @param id the id of the scrapeData to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the scrapeData, or with status 404 (Not Found)
     */
    @GetMapping("/scrape-data/{id}")
    @Timed
    public ResponseEntity<ScrapeData> getScrapeData(@PathVariable String id) {
        log.debug("REST request to get ScrapeData : {}", id);
        Optional<ScrapeData> scrapeData = scrapeDataService.findOne(id);
        return ResponseUtil.wrapOrNotFound(scrapeData);
    }

    /**
     * DELETE  /scrape-data/:id : delete the "id" scrapeData.
     *
     * @param id the id of the scrapeData to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/scrape-data/{id}")
    @Timed
    public ResponseEntity<Void> deleteScrapeData(@PathVariable String id) {
        log.debug("REST request to delete ScrapeData : {}", id);
        scrapeDataService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }

    /**
     * SEARCH  /_search/scrape-data?query=:query : search for the scrapeData corresponding
     * to the query.
     *
     * @param query the query of the scrapeData search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/scrape-data")
    @Timed
    public ResponseEntity<List<ScrapeData>> searchScrapeData(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ScrapeData for query {}", query);
        Page<ScrapeData> page = scrapeDataService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/scrape-data");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
