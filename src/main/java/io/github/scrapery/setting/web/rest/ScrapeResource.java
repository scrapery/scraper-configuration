package io.github.scrapery.setting.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.scrapery.setting.domain.Scrape;
import io.github.scrapery.setting.service.ScrapeService;
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
 * REST controller for managing Scrape.
 */
@RestController
@RequestMapping("/api")
public class ScrapeResource {

    private final Logger log = LoggerFactory.getLogger(ScrapeResource.class);

    private static final String ENTITY_NAME = "scrape";

    private final ScrapeService scrapeService;

    public ScrapeResource(ScrapeService scrapeService) {
        this.scrapeService = scrapeService;
    }

    /**
     * POST  /scrapes : Create a new scrape.
     *
     * @param scrape the scrape to create
     * @return the ResponseEntity with status 201 (Created) and with body the new scrape, or with status 400 (Bad Request) if the scrape has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/scrapes")
    @Timed
    public ResponseEntity<Scrape> createScrape(@RequestBody Scrape scrape) throws URISyntaxException {
        log.debug("REST request to save Scrape : {}", scrape);
        if (scrape.getId() != null) {
            throw new BadRequestAlertException("A new scrape cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Scrape result = scrapeService.save(scrape);
        return ResponseEntity.created(new URI("/api/scrapes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /scrapes : Updates an existing scrape.
     *
     * @param scrape the scrape to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated scrape,
     * or with status 400 (Bad Request) if the scrape is not valid,
     * or with status 500 (Internal Server Error) if the scrape couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/scrapes")
    @Timed
    public ResponseEntity<Scrape> updateScrape(@RequestBody Scrape scrape) throws URISyntaxException {
        log.debug("REST request to update Scrape : {}", scrape);
        if (scrape.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Scrape result = scrapeService.save(scrape);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, scrape.getId().toString()))
            .body(result);
    }

    /**
     * GET  /scrapes : get all the scrapes.
     *
     * @param pageable the pagination information
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many)
     * @return the ResponseEntity with status 200 (OK) and the list of scrapes in body
     */
    @GetMapping("/scrapes")
    @Timed
    public ResponseEntity<List<Scrape>> getAllScrapes(Pageable pageable, @RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get a page of Scrapes");
        Page<Scrape> page;
        if (eagerload) {
            page = scrapeService.findAllWithEagerRelationships(pageable);
        } else {
            page = scrapeService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, String.format("/api/scrapes?eagerload=%b", eagerload));
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /scrapes/:id : get the "id" scrape.
     *
     * @param id the id of the scrape to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the scrape, or with status 404 (Not Found)
     */
    @GetMapping("/scrapes/{id}")
    @Timed
    public ResponseEntity<Scrape> getScrape(@PathVariable String id) {
        log.debug("REST request to get Scrape : {}", id);
        Optional<Scrape> scrape = scrapeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(scrape);
    }

    /**
     * DELETE  /scrapes/:id : delete the "id" scrape.
     *
     * @param id the id of the scrape to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/scrapes/{id}")
    @Timed
    public ResponseEntity<Void> deleteScrape(@PathVariable String id) {
        log.debug("REST request to delete Scrape : {}", id);
        scrapeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }

    /**
     * SEARCH  /_search/scrapes?query=:query : search for the scrape corresponding
     * to the query.
     *
     * @param query the query of the scrape search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/scrapes")
    @Timed
    public ResponseEntity<List<Scrape>> searchScrapes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Scrapes for query {}", query);
        Page<Scrape> page = scrapeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/scrapes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
