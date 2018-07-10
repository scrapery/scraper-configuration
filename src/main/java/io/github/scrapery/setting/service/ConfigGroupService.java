package io.github.scrapery.setting.service;

import io.github.scrapery.setting.domain.ConfigGroup;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing ConfigGroup.
 */
public interface ConfigGroupService {

    /**
     * Save a configGroup.
     *
     * @param configGroup the entity to save
     * @return the persisted entity
     */
    ConfigGroup save(ConfigGroup configGroup);

    /**
     * Get all the configGroups.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ConfigGroup> findAll(Pageable pageable);

    /**
     * Get all the ConfigGroup with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    Page<ConfigGroup> findAllWithEagerRelationships(Pageable pageable);
    
    /**
     * Get the "id" configGroup.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<ConfigGroup> findOne(String id);

    /**
     * Delete the "id" configGroup.
     *
     * @param id the id of the entity
     */
    void delete(String id);

    /**
     * Search for the configGroup corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ConfigGroup> search(String query, Pageable pageable);
}
