package io.github.scrapery.setting.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.scrapery.setting.domain.ChannelOuterLink;
import io.github.scrapery.setting.service.ChannelOuterLinkService;
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
 * REST controller for managing ChannelOuterLink.
 */
@RestController
@RequestMapping("/api")
public class ChannelOuterLinkResource {

    private final Logger log = LoggerFactory.getLogger(ChannelOuterLinkResource.class);

    private static final String ENTITY_NAME = "channelOuterLink";

    private final ChannelOuterLinkService channelOuterLinkService;

    public ChannelOuterLinkResource(ChannelOuterLinkService channelOuterLinkService) {
        this.channelOuterLinkService = channelOuterLinkService;
    }

    /**
     * POST  /channel-outer-links : Create a new channelOuterLink.
     *
     * @param channelOuterLink the channelOuterLink to create
     * @return the ResponseEntity with status 201 (Created) and with body the new channelOuterLink, or with status 400 (Bad Request) if the channelOuterLink has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/channel-outer-links")
    @Timed
    public ResponseEntity<ChannelOuterLink> createChannelOuterLink(@RequestBody ChannelOuterLink channelOuterLink) throws URISyntaxException {
        log.debug("REST request to save ChannelOuterLink : {}", channelOuterLink);
        if (channelOuterLink.getId() != null) {
            throw new BadRequestAlertException("A new channelOuterLink cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ChannelOuterLink result = channelOuterLinkService.save(channelOuterLink);
        return ResponseEntity.created(new URI("/api/channel-outer-links/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /channel-outer-links : Updates an existing channelOuterLink.
     *
     * @param channelOuterLink the channelOuterLink to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated channelOuterLink,
     * or with status 400 (Bad Request) if the channelOuterLink is not valid,
     * or with status 500 (Internal Server Error) if the channelOuterLink couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/channel-outer-links")
    @Timed
    public ResponseEntity<ChannelOuterLink> updateChannelOuterLink(@RequestBody ChannelOuterLink channelOuterLink) throws URISyntaxException {
        log.debug("REST request to update ChannelOuterLink : {}", channelOuterLink);
        if (channelOuterLink.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ChannelOuterLink result = channelOuterLinkService.save(channelOuterLink);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, channelOuterLink.getId().toString()))
            .body(result);
    }

    /**
     * GET  /channel-outer-links : get all the channelOuterLinks.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of channelOuterLinks in body
     */
    @GetMapping("/channel-outer-links")
    @Timed
    public ResponseEntity<List<ChannelOuterLink>> getAllChannelOuterLinks(Pageable pageable) {
        log.debug("REST request to get a page of ChannelOuterLinks");
        Page<ChannelOuterLink> page = channelOuterLinkService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/channel-outer-links");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /channel-outer-links/:id : get the "id" channelOuterLink.
     *
     * @param id the id of the channelOuterLink to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the channelOuterLink, or with status 404 (Not Found)
     */
    @GetMapping("/channel-outer-links/{id}")
    @Timed
    public ResponseEntity<ChannelOuterLink> getChannelOuterLink(@PathVariable String id) {
        log.debug("REST request to get ChannelOuterLink : {}", id);
        Optional<ChannelOuterLink> channelOuterLink = channelOuterLinkService.findOne(id);
        return ResponseUtil.wrapOrNotFound(channelOuterLink);
    }

    /**
     * DELETE  /channel-outer-links/:id : delete the "id" channelOuterLink.
     *
     * @param id the id of the channelOuterLink to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/channel-outer-links/{id}")
    @Timed
    public ResponseEntity<Void> deleteChannelOuterLink(@PathVariable String id) {
        log.debug("REST request to delete ChannelOuterLink : {}", id);
        channelOuterLinkService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }

    /**
     * SEARCH  /_search/channel-outer-links?query=:query : search for the channelOuterLink corresponding
     * to the query.
     *
     * @param query the query of the channelOuterLink search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/channel-outer-links")
    @Timed
    public ResponseEntity<List<ChannelOuterLink>> searchChannelOuterLinks(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ChannelOuterLinks for query {}", query);
        Page<ChannelOuterLink> page = channelOuterLinkService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/channel-outer-links");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
