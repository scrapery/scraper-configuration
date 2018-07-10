package io.github.scrapery.setting.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.scrapery.setting.domain.SiteChannel;
import io.github.scrapery.setting.service.SiteChannelService;
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
 * REST controller for managing SiteChannel.
 */
@RestController
@RequestMapping("/api")
public class SiteChannelResource {

    private final Logger log = LoggerFactory.getLogger(SiteChannelResource.class);

    private static final String ENTITY_NAME = "siteChannel";

    private final SiteChannelService siteChannelService;

    public SiteChannelResource(SiteChannelService siteChannelService) {
        this.siteChannelService = siteChannelService;
    }

    /**
     * POST  /site-channels : Create a new siteChannel.
     *
     * @param siteChannel the siteChannel to create
     * @return the ResponseEntity with status 201 (Created) and with body the new siteChannel, or with status 400 (Bad Request) if the siteChannel has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/site-channels")
    @Timed
    public ResponseEntity<SiteChannel> createSiteChannel(@RequestBody SiteChannel siteChannel) throws URISyntaxException {
        log.debug("REST request to save SiteChannel : {}", siteChannel);
        if (siteChannel.getId() != null) {
            throw new BadRequestAlertException("A new siteChannel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SiteChannel result = siteChannelService.save(siteChannel);
        return ResponseEntity.created(new URI("/api/site-channels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /site-channels : Updates an existing siteChannel.
     *
     * @param siteChannel the siteChannel to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated siteChannel,
     * or with status 400 (Bad Request) if the siteChannel is not valid,
     * or with status 500 (Internal Server Error) if the siteChannel couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/site-channels")
    @Timed
    public ResponseEntity<SiteChannel> updateSiteChannel(@RequestBody SiteChannel siteChannel) throws URISyntaxException {
        log.debug("REST request to update SiteChannel : {}", siteChannel);
        if (siteChannel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SiteChannel result = siteChannelService.save(siteChannel);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, siteChannel.getId().toString()))
            .body(result);
    }

    /**
     * GET  /site-channels : get all the siteChannels.
     *
     * @param pageable the pagination information
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many)
     * @return the ResponseEntity with status 200 (OK) and the list of siteChannels in body
     */
    @GetMapping("/site-channels")
    @Timed
    public ResponseEntity<List<SiteChannel>> getAllSiteChannels(Pageable pageable, @RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get a page of SiteChannels");
        Page<SiteChannel> page;
        if (eagerload) {
            page = siteChannelService.findAllWithEagerRelationships(pageable);
        } else {
            page = siteChannelService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, String.format("/api/site-channels?eagerload=%b", eagerload));
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /site-channels/:id : get the "id" siteChannel.
     *
     * @param id the id of the siteChannel to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the siteChannel, or with status 404 (Not Found)
     */
    @GetMapping("/site-channels/{id}")
    @Timed
    public ResponseEntity<SiteChannel> getSiteChannel(@PathVariable String id) {
        log.debug("REST request to get SiteChannel : {}", id);
        Optional<SiteChannel> siteChannel = siteChannelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(siteChannel);
    }

    /**
     * DELETE  /site-channels/:id : delete the "id" siteChannel.
     *
     * @param id the id of the siteChannel to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/site-channels/{id}")
    @Timed
    public ResponseEntity<Void> deleteSiteChannel(@PathVariable String id) {
        log.debug("REST request to delete SiteChannel : {}", id);
        siteChannelService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }

    /**
     * SEARCH  /_search/site-channels?query=:query : search for the siteChannel corresponding
     * to the query.
     *
     * @param query the query of the siteChannel search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/site-channels")
    @Timed
    public ResponseEntity<List<SiteChannel>> searchSiteChannels(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of SiteChannels for query {}", query);
        Page<SiteChannel> page = siteChannelService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/site-channels");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
