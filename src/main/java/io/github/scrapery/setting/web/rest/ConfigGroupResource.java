package io.github.scrapery.setting.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.scrapery.setting.domain.ConfigGroup;
import io.github.scrapery.setting.service.ConfigGroupService;
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
 * REST controller for managing ConfigGroup.
 */
@RestController
@RequestMapping("/api")
public class ConfigGroupResource {

    private final Logger log = LoggerFactory.getLogger(ConfigGroupResource.class);

    private static final String ENTITY_NAME = "configGroup";

    private final ConfigGroupService configGroupService;

    public ConfigGroupResource(ConfigGroupService configGroupService) {
        this.configGroupService = configGroupService;
    }

    /**
     * POST  /config-groups : Create a new configGroup.
     *
     * @param configGroup the configGroup to create
     * @return the ResponseEntity with status 201 (Created) and with body the new configGroup, or with status 400 (Bad Request) if the configGroup has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/config-groups")
    @Timed
    public ResponseEntity<ConfigGroup> createConfigGroup(@RequestBody ConfigGroup configGroup) throws URISyntaxException {
        log.debug("REST request to save ConfigGroup : {}", configGroup);
        if (configGroup.getId() != null) {
            throw new BadRequestAlertException("A new configGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ConfigGroup result = configGroupService.save(configGroup);
        return ResponseEntity.created(new URI("/api/config-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /config-groups : Updates an existing configGroup.
     *
     * @param configGroup the configGroup to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated configGroup,
     * or with status 400 (Bad Request) if the configGroup is not valid,
     * or with status 500 (Internal Server Error) if the configGroup couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/config-groups")
    @Timed
    public ResponseEntity<ConfigGroup> updateConfigGroup(@RequestBody ConfigGroup configGroup) throws URISyntaxException {
        log.debug("REST request to update ConfigGroup : {}", configGroup);
        if (configGroup.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ConfigGroup result = configGroupService.save(configGroup);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, configGroup.getId().toString()))
            .body(result);
    }

    /**
     * GET  /config-groups : get all the configGroups.
     *
     * @param pageable the pagination information
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many)
     * @return the ResponseEntity with status 200 (OK) and the list of configGroups in body
     */
    @GetMapping("/config-groups")
    @Timed
    public ResponseEntity<List<ConfigGroup>> getAllConfigGroups(Pageable pageable, @RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get a page of ConfigGroups");
        Page<ConfigGroup> page;
        if (eagerload) {
            page = configGroupService.findAllWithEagerRelationships(pageable);
        } else {
            page = configGroupService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, String.format("/api/config-groups?eagerload=%b", eagerload));
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /config-groups/:id : get the "id" configGroup.
     *
     * @param id the id of the configGroup to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the configGroup, or with status 404 (Not Found)
     */
    @GetMapping("/config-groups/{id}")
    @Timed
    public ResponseEntity<ConfigGroup> getConfigGroup(@PathVariable String id) {
        log.debug("REST request to get ConfigGroup : {}", id);
        Optional<ConfigGroup> configGroup = configGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(configGroup);
    }

    /**
     * DELETE  /config-groups/:id : delete the "id" configGroup.
     *
     * @param id the id of the configGroup to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/config-groups/{id}")
    @Timed
    public ResponseEntity<Void> deleteConfigGroup(@PathVariable String id) {
        log.debug("REST request to delete ConfigGroup : {}", id);
        configGroupService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }

    /**
     * SEARCH  /_search/config-groups?query=:query : search for the configGroup corresponding
     * to the query.
     *
     * @param query the query of the configGroup search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/config-groups")
    @Timed
    public ResponseEntity<List<ConfigGroup>> searchConfigGroups(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ConfigGroups for query {}", query);
        Page<ConfigGroup> page = configGroupService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/config-groups");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
