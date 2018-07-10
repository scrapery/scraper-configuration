package io.github.scrapery.setting.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.scrapery.setting.domain.ConfigSite;
import io.github.scrapery.setting.service.ConfigSiteService;
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
 * REST controller for managing ConfigSite.
 */
@RestController
@RequestMapping("/api")
public class ConfigSiteResource {

    private final Logger log = LoggerFactory.getLogger(ConfigSiteResource.class);

    private static final String ENTITY_NAME = "configSite";

    private final ConfigSiteService configSiteService;

    public ConfigSiteResource(ConfigSiteService configSiteService) {
        this.configSiteService = configSiteService;
    }

    /**
     * POST  /config-sites : Create a new configSite.
     *
     * @param configSite the configSite to create
     * @return the ResponseEntity with status 201 (Created) and with body the new configSite, or with status 400 (Bad Request) if the configSite has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/config-sites")
    @Timed
    public ResponseEntity<ConfigSite> createConfigSite(@RequestBody ConfigSite configSite) throws URISyntaxException {
        log.debug("REST request to save ConfigSite : {}", configSite);
        if (configSite.getId() != null) {
            throw new BadRequestAlertException("A new configSite cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ConfigSite result = configSiteService.save(configSite);
        return ResponseEntity.created(new URI("/api/config-sites/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /config-sites : Updates an existing configSite.
     *
     * @param configSite the configSite to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated configSite,
     * or with status 400 (Bad Request) if the configSite is not valid,
     * or with status 500 (Internal Server Error) if the configSite couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/config-sites")
    @Timed
    public ResponseEntity<ConfigSite> updateConfigSite(@RequestBody ConfigSite configSite) throws URISyntaxException {
        log.debug("REST request to update ConfigSite : {}", configSite);
        if (configSite.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ConfigSite result = configSiteService.save(configSite);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, configSite.getId().toString()))
            .body(result);
    }

    /**
     * GET  /config-sites : get all the configSites.
     *
     * @param pageable the pagination information
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many)
     * @return the ResponseEntity with status 200 (OK) and the list of configSites in body
     */
    @GetMapping("/config-sites")
    @Timed
    public ResponseEntity<List<ConfigSite>> getAllConfigSites(Pageable pageable, @RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get a page of ConfigSites");
        Page<ConfigSite> page;
        if (eagerload) {
            page = configSiteService.findAllWithEagerRelationships(pageable);
        } else {
            page = configSiteService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, String.format("/api/config-sites?eagerload=%b", eagerload));
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /config-sites/:id : get the "id" configSite.
     *
     * @param id the id of the configSite to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the configSite, or with status 404 (Not Found)
     */
    @GetMapping("/config-sites/{id}")
    @Timed
    public ResponseEntity<ConfigSite> getConfigSite(@PathVariable String id) {
        log.debug("REST request to get ConfigSite : {}", id);
        Optional<ConfigSite> configSite = configSiteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(configSite);
    }

    /**
     * DELETE  /config-sites/:id : delete the "id" configSite.
     *
     * @param id the id of the configSite to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/config-sites/{id}")
    @Timed
    public ResponseEntity<Void> deleteConfigSite(@PathVariable String id) {
        log.debug("REST request to delete ConfigSite : {}", id);
        configSiteService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }

    /**
     * SEARCH  /_search/config-sites?query=:query : search for the configSite corresponding
     * to the query.
     *
     * @param query the query of the configSite search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/config-sites")
    @Timed
    public ResponseEntity<List<ConfigSite>> searchConfigSites(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ConfigSites for query {}", query);
        Page<ConfigSite> page = configSiteService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/config-sites");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
