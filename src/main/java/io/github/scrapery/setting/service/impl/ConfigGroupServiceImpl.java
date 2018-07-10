package io.github.scrapery.setting.service.impl;

import io.github.scrapery.setting.service.ConfigGroupService;
import io.github.scrapery.setting.domain.ConfigGroup;
import io.github.scrapery.setting.repository.ConfigGroupRepository;
import io.github.scrapery.setting.repository.search.ConfigGroupSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing ConfigGroup.
 */
@Service
public class ConfigGroupServiceImpl implements ConfigGroupService {

    private final Logger log = LoggerFactory.getLogger(ConfigGroupServiceImpl.class);

    private final ConfigGroupRepository configGroupRepository;

    private final ConfigGroupSearchRepository configGroupSearchRepository;

    public ConfigGroupServiceImpl(ConfigGroupRepository configGroupRepository, ConfigGroupSearchRepository configGroupSearchRepository) {
        this.configGroupRepository = configGroupRepository;
        this.configGroupSearchRepository = configGroupSearchRepository;
    }

    /**
     * Save a configGroup.
     *
     * @param configGroup the entity to save
     * @return the persisted entity
     */
    @Override
    public ConfigGroup save(ConfigGroup configGroup) {
        log.debug("Request to save ConfigGroup : {}", configGroup);        ConfigGroup result = configGroupRepository.save(configGroup);
        configGroupSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the configGroups.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<ConfigGroup> findAll(Pageable pageable) {
        log.debug("Request to get all ConfigGroups");
        return configGroupRepository.findAll(pageable);
    }

    /**
     * Get all the ConfigGroup with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<ConfigGroup> findAllWithEagerRelationships(Pageable pageable) {
        return configGroupRepository.findAll(pageable);
    }


    /**
     * Get one configGroup by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    public Optional<ConfigGroup> findOne(String id) {
        log.debug("Request to get ConfigGroup : {}", id);
        return configGroupRepository.findById(id);
    }

    /**
     * Delete the configGroup by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete ConfigGroup : {}", id);
        configGroupRepository.deleteById(id);
        configGroupSearchRepository.deleteById(id);
    }

    /**
     * Search for the configGroup corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<ConfigGroup> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ConfigGroups for query {}", query);
        return configGroupSearchRepository.search(queryStringQuery(query), pageable);    }
}
