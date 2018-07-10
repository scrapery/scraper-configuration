package io.github.scrapery.setting.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.scrapery.setting.domain.TryParser;
import io.github.scrapery.setting.service.TryParserService;
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
 * REST controller for managing TryParser.
 */
@RestController
@RequestMapping("/api")
public class TryParserResource {

    private final Logger log = LoggerFactory.getLogger(TryParserResource.class);

    private static final String ENTITY_NAME = "tryParser";

    private final TryParserService tryParserService;

    public TryParserResource(TryParserService tryParserService) {
        this.tryParserService = tryParserService;
    }

    /**
     * POST  /try-parsers : Create a new tryParser.
     *
     * @param tryParser the tryParser to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tryParser, or with status 400 (Bad Request) if the tryParser has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/try-parsers")
    @Timed
    public ResponseEntity<TryParser> createTryParser(@RequestBody TryParser tryParser) throws URISyntaxException {
        log.debug("REST request to save TryParser : {}", tryParser);
        if (tryParser.getId() != null) {
            throw new BadRequestAlertException("A new tryParser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TryParser result = tryParserService.save(tryParser);
        return ResponseEntity.created(new URI("/api/try-parsers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /try-parsers : Updates an existing tryParser.
     *
     * @param tryParser the tryParser to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tryParser,
     * or with status 400 (Bad Request) if the tryParser is not valid,
     * or with status 500 (Internal Server Error) if the tryParser couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/try-parsers")
    @Timed
    public ResponseEntity<TryParser> updateTryParser(@RequestBody TryParser tryParser) throws URISyntaxException {
        log.debug("REST request to update TryParser : {}", tryParser);
        if (tryParser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TryParser result = tryParserService.save(tryParser);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tryParser.getId().toString()))
            .body(result);
    }

    /**
     * GET  /try-parsers : get all the tryParsers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of tryParsers in body
     */
    @GetMapping("/try-parsers")
    @Timed
    public ResponseEntity<List<TryParser>> getAllTryParsers(Pageable pageable) {
        log.debug("REST request to get a page of TryParsers");
        Page<TryParser> page = tryParserService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/try-parsers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /try-parsers/:id : get the "id" tryParser.
     *
     * @param id the id of the tryParser to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tryParser, or with status 404 (Not Found)
     */
    @GetMapping("/try-parsers/{id}")
    @Timed
    public ResponseEntity<TryParser> getTryParser(@PathVariable String id) {
        log.debug("REST request to get TryParser : {}", id);
        Optional<TryParser> tryParser = tryParserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tryParser);
    }

    /**
     * DELETE  /try-parsers/:id : delete the "id" tryParser.
     *
     * @param id the id of the tryParser to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/try-parsers/{id}")
    @Timed
    public ResponseEntity<Void> deleteTryParser(@PathVariable String id) {
        log.debug("REST request to delete TryParser : {}", id);
        tryParserService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }

    /**
     * SEARCH  /_search/try-parsers?query=:query : search for the tryParser corresponding
     * to the query.
     *
     * @param query the query of the tryParser search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/try-parsers")
    @Timed
    public ResponseEntity<List<TryParser>> searchTryParsers(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of TryParsers for query {}", query);
        Page<TryParser> page = tryParserService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/try-parsers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
