package io.github.scrapery.setting.service.impl;

import io.github.scrapery.setting.service.ConfigSiteLoginService;
import io.github.scrapery.setting.domain.ConfigSiteLogin;
import io.github.scrapery.setting.repository.ConfigSiteLoginRepository;
import io.github.scrapery.setting.repository.search.ConfigSiteLoginSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing ConfigSiteLogin.
 */
@Service
public class ConfigSiteLoginServiceImpl implements ConfigSiteLoginService {

    private final Logger log = LoggerFactory.getLogger(ConfigSiteLoginServiceImpl.class);

    private final ConfigSiteLoginRepository configSiteLoginRepository;

    private final ConfigSiteLoginSearchRepository configSiteLoginSearchRepository;

    public ConfigSiteLoginServiceImpl(ConfigSiteLoginRepository configSiteLoginRepository, ConfigSiteLoginSearchRepository configSiteLoginSearchRepository) {
        this.configSiteLoginRepository = configSiteLoginRepository;
        this.configSiteLoginSearchRepository = configSiteLoginSearchRepository;
    }

    /**
     * Save a configSiteLogin.
     *
     * @param configSiteLogin the entity to save
     * @return the persisted entity
     */
    @Override
    public ConfigSiteLogin save(ConfigSiteLogin configSiteLogin) {
        log.debug("Request to save ConfigSiteLogin : {}", configSiteLogin);        ConfigSiteLogin result = configSiteLoginRepository.save(configSiteLogin);
        configSiteLoginSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the configSiteLogins.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<ConfigSiteLogin> findAll(Pageable pageable) {
        log.debug("Request to get all ConfigSiteLogins");
        return configSiteLoginRepository.findAll(pageable);
    }


    /**
     * Get one configSiteLogin by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    public Optional<ConfigSiteLogin> findOne(String id) {
        log.debug("Request to get ConfigSiteLogin : {}", id);
        return configSiteLoginRepository.findById(id);
    }

    /**
     * Delete the configSiteLogin by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete ConfigSiteLogin : {}", id);
        configSiteLoginRepository.deleteById(id);
        configSiteLoginSearchRepository.deleteById(id);
    }

    /**
     * Search for the configSiteLogin corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<ConfigSiteLogin> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ConfigSiteLogins for query {}", query);
        return configSiteLoginSearchRepository.search(queryStringQuery(query), pageable);    }
}
