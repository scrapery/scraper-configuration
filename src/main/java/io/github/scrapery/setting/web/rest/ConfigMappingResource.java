package io.github.scrapery.setting.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.scrapery.setting.domain.ConfigMapping;
import io.github.scrapery.setting.service.ConfigMappingService;
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
 * REST controller for managing ConfigMapping.
 */
@RestController
@RequestMapping("/api")
public class ConfigMappingResource {

    private final Logger log = LoggerFactory.getLogger(ConfigMappingResource.class);

    private static final String ENTITY_NAME = "configMapping";

    private final ConfigMappingService configMappingService;

    public ConfigMappingResource(ConfigMappingService configMappingService) {
        this.configMappingService = configMappingService;
    }

    /**
     * POST  /config-mappings : Create a new configMapping.
     *
     * @param configMapping the configMapping to create
     * @return the ResponseEntity with status 201 (Created) and with body the new configMapping, or with status 400 (Bad Request) if the configMapping has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/config-mappings")
    @Timed
    public ResponseEntity<ConfigMapping> createConfigMapping(@RequestBody ConfigMapping configMapping) throws URISyntaxException {
        log.debug("REST request to save ConfigMapping : {}", configMapping);
        if (configMapping.getId() != null) {
            throw new BadRequestAlertException("A new configMapping cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ConfigMapping result = configMappingService.save(configMapping);
        return ResponseEntity.created(new URI("/api/config-mappings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /config-mappings : Updates an existing configMapping.
     *
     * @param configMapping the configMapping to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated configMapping,
     * or with status 400 (Bad Request) if the configMapping is not valid,
     * or with status 500 (Internal Server Error) if the configMapping couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/config-mappings")
    @Timed
    public ResponseEntity<ConfigMapping> updateConfigMapping(@RequestBody ConfigMapping configMapping) throws URISyntaxException {
        log.debug("REST request to update ConfigMapping : {}", configMapping);
        if (configMapping.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ConfigMapping result = configMappingService.save(configMapping);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, configMapping.getId().toString()))
            .body(result);
    }

    /**
     * GET  /config-mappings : get all the configMappings.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of configMappings in body
     */
    @GetMapping("/config-mappings")
    @Timed
    public ResponseEntity<List<ConfigMapping>> getAllConfigMappings(Pageable pageable) {
        log.debug("REST request to get a page of ConfigMappings");
        Page<ConfigMapping> page = configMappingService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/config-mappings");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /config-mappings/:id : get the "id" configMapping.
     *
     * @param id the id of the configMapping to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the configMapping, or with status 404 (Not Found)
     */
    @GetMapping("/config-mappings/{id}")
    @Timed
    public ResponseEntity<ConfigMapping> getConfigMapping(@PathVariable String id) {
        log.debug("REST request to get ConfigMapping : {}", id);
        Optional<ConfigMapping> configMapping = configMappingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(configMapping);
    }

    /**
     * DELETE  /config-mappings/:id : delete the "id" configMapping.
     *
     * @param id the id of the configMapping to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/config-mappings/{id}")
    @Timed
    public ResponseEntity<Void> deleteConfigMapping(@PathVariable String id) {
        log.debug("REST request to delete ConfigMapping : {}", id);
        configMappingService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }

    /**
     * SEARCH  /_search/config-mappings?query=:query : search for the configMapping corresponding
     * to the query.
     *
     * @param query the query of the configMapping search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/config-mappings")
    @Timed
    public ResponseEntity<List<ConfigMapping>> searchConfigMappings(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ConfigMappings for query {}", query);
        Page<ConfigMapping> page = configMappingService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/config-mappings");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
