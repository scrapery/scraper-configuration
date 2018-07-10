package io.github.scrapery.setting.service.impl;

import io.github.scrapery.setting.service.ConfigSiteService;
import io.github.scrapery.setting.domain.ConfigSite;
import io.github.scrapery.setting.repository.ConfigSiteRepository;
import io.github.scrapery.setting.repository.search.ConfigSiteSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing ConfigSite.
 */
@Service
public class ConfigSiteServiceImpl implements ConfigSiteService {

    private final Logger log = LoggerFactory.getLogger(ConfigSiteServiceImpl.class);

    private final ConfigSiteRepository configSiteRepository;

    private final ConfigSiteSearchRepository configSiteSearchRepository;

    public ConfigSiteServiceImpl(ConfigSiteRepository configSiteRepository, ConfigSiteSearchRepository configSiteSearchRepository) {
        this.configSiteRepository = configSiteRepository;
        this.configSiteSearchRepository = configSiteSearchRepository;
    }

    /**
     * Save a configSite.
     *
     * @param configSite the entity to save
     * @return the persisted entity
     */
    @Override
    public ConfigSite save(ConfigSite configSite) {
        log.debug("Request to save ConfigSite : {}", configSite);        ConfigSite result = configSiteRepository.save(configSite);
        configSiteSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the configSites.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<ConfigSite> findAll(Pageable pageable) {
        log.debug("Request to get all ConfigSites");
        return configSiteRepository.findAll(pageable);
    }

    /**
     * Get all the ConfigSite with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<ConfigSite> findAllWithEagerRelationships(Pageable pageable) {
        return configSiteRepository.findAll(pageable);
    }


    /**
     * Get one configSite by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    public Optional<ConfigSite> findOne(String id) {
        log.debug("Request to get ConfigSite : {}", id);
        return configSiteRepository.findById(id);
    }

    /**
     * Delete the configSite by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete ConfigSite : {}", id);
        configSiteRepository.deleteById(id);
        configSiteSearchRepository.deleteById(id);
    }

    /**
     * Search for the configSite corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<ConfigSite> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ConfigSites for query {}", query);
        return configSiteSearchRepository.search(queryStringQuery(query), pageable);    }
}
