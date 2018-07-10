package io.github.scrapery.setting.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.scrapery.setting.domain.ConfigSiteLogin;
import io.github.scrapery.setting.service.ConfigSiteLoginService;
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
 * REST controller for managing ConfigSiteLogin.
 */
@RestController
@RequestMapping("/api")
public class ConfigSiteLoginResource {

    private final Logger log = LoggerFactory.getLogger(ConfigSiteLoginResource.class);

    private static final String ENTITY_NAME = "configSiteLogin";

    private final ConfigSiteLoginService configSiteLoginService;

    public ConfigSiteLoginResource(ConfigSiteLoginService configSiteLoginService) {
        this.configSiteLoginService = configSiteLoginService;
    }

    /**
     * POST  /config-site-logins : Create a new configSiteLogin.
     *
     * @param configSiteLogin the configSiteLogin to create
     * @return the ResponseEntity with status 201 (Created) and with body the new configSiteLogin, or with status 400 (Bad Request) if the configSiteLogin has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/config-site-logins")
    @Timed
    public ResponseEntity<ConfigSiteLogin> createConfigSiteLogin(@RequestBody ConfigSiteLogin configSiteLogin) throws URISyntaxException {
        log.debug("REST request to save ConfigSiteLogin : {}", configSiteLogin);
        if (configSiteLogin.getId() != null) {
            throw new BadRequestAlertException("A new configSiteLogin cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ConfigSiteLogin result = configSiteLoginService.save(configSiteLogin);
        return ResponseEntity.created(new URI("/api/config-site-logins/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /config-site-logins : Updates an existing configSiteLogin.
     *
     * @param configSiteLogin the configSiteLogin to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated configSiteLogin,
     * or with status 400 (Bad Request) if the configSiteLogin is not valid,
     * or with status 500 (Internal Server Error) if the configSiteLogin couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/config-site-logins")
    @Timed
    public ResponseEntity<ConfigSiteLogin> updateConfigSiteLogin(@RequestBody ConfigSiteLogin configSiteLogin) throws URISyntaxException {
        log.debug("REST request to update ConfigSiteLogin : {}", configSiteLogin);
        if (configSiteLogin.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ConfigSiteLogin result = configSiteLoginService.save(configSiteLogin);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, configSiteLogin.getId().toString()))
            .body(result);
    }

    /**
     * GET  /config-site-logins : get all the configSiteLogins.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of configSiteLogins in body
     */
    @GetMapping("/config-site-logins")
    @Timed
    public ResponseEntity<List<ConfigSiteLogin>> getAllConfigSiteLogins(Pageable pageable) {
        log.debug("REST request to get a page of ConfigSiteLogins");
        Page<ConfigSiteLogin> page = configSiteLoginService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/config-site-logins");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /config-site-logins/:id : get the "id" configSiteLogin.
     *
     * @param id the id of the configSiteLogin to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the configSiteLogin, or with status 404 (Not Found)
     */
    @GetMapping("/config-site-logins/{id}")
    @Timed
    public ResponseEntity<ConfigSiteLogin> getConfigSiteLogin(@PathVariable String id) {
        log.debug("REST request to get ConfigSiteLogin : {}", id);
        Optional<ConfigSiteLogin> configSiteLogin = configSiteLoginService.findOne(id);
        return ResponseUtil.wrapOrNotFound(configSiteLogin);
    }

    /**
     * DELETE  /config-site-logins/:id : delete the "id" configSiteLogin.
     *
     * @param id the id of the configSiteLogin to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/config-site-logins/{id}")
    @Timed
    public ResponseEntity<Void> deleteConfigSiteLogin(@PathVariable String id) {
        log.debug("REST request to delete ConfigSiteLogin : {}", id);
        configSiteLoginService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }

    /**
     * SEARCH  /_search/config-site-logins?query=:query : search for the configSiteLogin corresponding
     * to the query.
     *
     * @param query the query of the configSiteLogin search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/config-site-logins")
    @Timed
    public ResponseEntity<List<ConfigSiteLogin>> searchConfigSiteLogins(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ConfigSiteLogins for query {}", query);
        Page<ConfigSiteLogin> page = configSiteLoginService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/config-site-logins");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
