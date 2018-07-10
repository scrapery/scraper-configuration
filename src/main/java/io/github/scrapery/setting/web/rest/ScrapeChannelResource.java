package io.github.scrapery.setting.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.scrapery.setting.domain.ScrapeChannel;
import io.github.scrapery.setting.service.ScrapeChannelService;
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
 * REST controller for managing ScrapeChannel.
 */
@RestController
@RequestMapping("/api")
public class ScrapeChannelResource {

    private final Logger log = LoggerFactory.getLogger(ScrapeChannelResource.class);

    private static final String ENTITY_NAME = "scrapeChannel";

    private final ScrapeChannelService scrapeChannelService;

    public ScrapeChannelResource(ScrapeChannelService scrapeChannelService) {
        this.scrapeChannelService = scrapeChannelService;
    }

    /**
     * POST  /scrape-channels : Create a new scrapeChannel.
     *
     * @param scrapeChannel the scrapeChannel to create
     * @return the ResponseEntity with status 201 (Created) and with body the new scrapeChannel, or with status 400 (Bad Request) if the scrapeChannel has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/scrape-channels")
    @Timed
    public ResponseEntity<ScrapeChannel> createScrapeChannel(@RequestBody ScrapeChannel scrapeChannel) throws URISyntaxException {
        log.debug("REST request to save ScrapeChannel : {}", scrapeChannel);
        if (scrapeChannel.getId() != null) {
            throw new BadRequestAlertException("A new scrapeChannel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ScrapeChannel result = scrapeChannelService.save(scrapeChannel);
        return ResponseEntity.created(new URI("/api/scrape-channels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /scrape-channels : Updates an existing scrapeChannel.
     *
     * @param scrapeChannel the scrapeChannel to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated scrapeChannel,
     * or with status 400 (Bad Request) if the scrapeChannel is not valid,
     * or with status 500 (Internal Server Error) if the scrapeChannel couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/scrape-channels")
    @Timed
    public ResponseEntity<ScrapeChannel> updateScrapeChannel(@RequestBody ScrapeChannel scrapeChannel) throws URISyntaxException {
        log.debug("REST request to update ScrapeChannel : {}", scrapeChannel);
        if (scrapeChannel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ScrapeChannel result = scrapeChannelService.save(scrapeChannel);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, scrapeChannel.getId().toString()))
            .body(result);
    }

    /**
     * GET  /scrape-channels : get all the scrapeChannels.
     *
     * @param pageable the pagination information
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many)
     * @return the ResponseEntity with status 200 (OK) and the list of scrapeChannels in body
     */
    @GetMapping("/scrape-channels")
    @Timed
    public ResponseEntity<List<ScrapeChannel>> getAllScrapeChannels(Pageable pageable, @RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get a page of ScrapeChannels");
        Page<ScrapeChannel> page;
        if (eagerload) {
            page = scrapeChannelService.findAllWithEagerRelationships(pageable);
        } else {
            page = scrapeChannelService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, String.format("/api/scrape-channels?eagerload=%b", eagerload));
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /scrape-channels/:id : get the "id" scrapeChannel.
     *
     * @param id the id of the scrapeChannel to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the scrapeChannel, or with status 404 (Not Found)
     */
    @GetMapping("/scrape-channels/{id}")
    @Timed
    public ResponseEntity<ScrapeChannel> getScrapeChannel(@PathVariable String id) {
        log.debug("REST request to get ScrapeChannel : {}", id);
        Optional<ScrapeChannel> scrapeChannel = scrapeChannelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(scrapeChannel);
    }

    /**
     * DELETE  /scrape-channels/:id : delete the "id" scrapeChannel.
     *
     * @param id the id of the scrapeChannel to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/scrape-channels/{id}")
    @Timed
    public ResponseEntity<Void> deleteScrapeChannel(@PathVariable String id) {
        log.debug("REST request to delete ScrapeChannel : {}", id);
        scrapeChannelService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }

    /**
     * SEARCH  /_search/scrape-channels?query=:query : search for the scrapeChannel corresponding
     * to the query.
     *
     * @param query the query of the scrapeChannel search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/scrape-channels")
    @Timed
    public ResponseEntity<List<ScrapeChannel>> searchScrapeChannels(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ScrapeChannels for query {}", query);
        Page<ScrapeChannel> page = scrapeChannelService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/scrape-channels");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
