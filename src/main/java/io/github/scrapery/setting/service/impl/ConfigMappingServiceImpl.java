package io.github.scrapery.setting.service.impl;

import io.github.scrapery.setting.service.ConfigMappingService;
import io.github.scrapery.setting.domain.ConfigMapping;
import io.github.scrapery.setting.repository.ConfigMappingRepository;
import io.github.scrapery.setting.repository.search.ConfigMappingSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing ConfigMapping.
 */
@Service
public class ConfigMappingServiceImpl implements ConfigMappingService {

    private final Logger log = LoggerFactory.getLogger(ConfigMappingServiceImpl.class);

    private final ConfigMappingRepository configMappingRepository;

    private final ConfigMappingSearchRepository configMappingSearchRepository;

    public ConfigMappingServiceImpl(ConfigMappingRepository configMappingRepository, ConfigMappingSearchRepository configMappingSearchRepository) {
        this.configMappingRepository = configMappingRepository;
        this.configMappingSearchRepository = configMappingSearchRepository;
    }

    /**
     * Save a configMapping.
     *
     * @param configMapping the entity to save
     * @return the persisted entity
     */
    @Override
    public ConfigMapping save(ConfigMapping configMapping) {
        log.debug("Request to save ConfigMapping : {}", configMapping);        ConfigMapping result = configMappingRepository.save(configMapping);
        configMappingSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the configMappings.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<ConfigMapping> findAll(Pageable pageable) {
        log.debug("Request to get all ConfigMappings");
        return configMappingRepository.findAll(pageable);
    }


    /**
     * Get one configMapping by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    public Optional<ConfigMapping> findOne(String id) {
        log.debug("Request to get ConfigMapping : {}", id);
        return configMappingRepository.findById(id);
    }

    /**
     * Delete the configMapping by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete ConfigMapping : {}", id);
        configMappingRepository.deleteById(id);
        configMappingSearchRepository.deleteById(id);
    }

    /**
     * Search for the configMapping corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<ConfigMapping> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ConfigMappings for query {}", query);
        return configMappingSearchRepository.search(queryStringQuery(query), pageable);    }
}
