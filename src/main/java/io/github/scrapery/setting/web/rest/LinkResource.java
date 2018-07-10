package io.github.scrapery.setting.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.scrapery.setting.domain.Link;
import io.github.scrapery.setting.service.LinkService;
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
 * REST controller for managing Link.
 */
@RestController
@RequestMapping("/api")
public class LinkResource {

    private final Logger log = LoggerFactory.getLogger(LinkResource.class);

    private static final String ENTITY_NAME = "link";

    private final LinkService linkService;

    public LinkResource(LinkService linkService) {
        this.linkService = linkService;
    }

    /**
     * POST  /links : Create a new link.
     *
     * @param link the link to create
     * @return the ResponseEntity with status 201 (Created) and with body the new link, or with status 400 (Bad Request) if the link has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/links")
    @Timed
    public ResponseEntity<Link> createLink(@RequestBody Link link) throws URISyntaxException {
        log.debug("REST request to save Link : {}", link);
        if (link.getId() != null) {
            throw new BadRequestAlertException("A new link cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Link result = linkService.save(link);
        return ResponseEntity.created(new URI("/api/links/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /links : Updates an existing link.
     *
     * @param link the link to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated link,
     * or with status 400 (Bad Request) if the link is not valid,
     * or with status 500 (Internal Server Error) if the link couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/links")
    @Timed
    public ResponseEntity<Link> updateLink(@RequestBody Link link) throws URISyntaxException {
        log.debug("REST request to update Link : {}", link);
        if (link.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Link result = linkService.save(link);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, link.getId().toString()))
            .body(result);
    }

    /**
     * GET  /links : get all the links.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of links in body
     */
    @GetMapping("/links")
    @Timed
    public ResponseEntity<List<Link>> getAllLinks(Pageable pageable) {
        log.debug("REST request to get a page of Links");
        Page<Link> page = linkService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/links");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /links/:id : get the "id" link.
     *
     * @param id the id of the link to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the link, or with status 404 (Not Found)
     */
    @GetMapping("/links/{id}")
    @Timed
    public ResponseEntity<Link> getLink(@PathVariable String id) {
        log.debug("REST request to get Link : {}", id);
        Optional<Link> link = linkService.findOne(id);
        return ResponseUtil.wrapOrNotFound(link);
    }

    /**
     * DELETE  /links/:id : delete the "id" link.
     *
     * @param id the id of the link to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/links/{id}")
    @Timed
    public ResponseEntity<Void> deleteLink(@PathVariable String id) {
        log.debug("REST request to delete Link : {}", id);
        linkService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }

    /**
     * SEARCH  /_search/links?query=:query : search for the link corresponding
     * to the query.
     *
     * @param query the query of the link search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/links")
    @Timed
    public ResponseEntity<List<Link>> searchLinks(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Links for query {}", query);
        Page<Link> page = linkService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/links");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
