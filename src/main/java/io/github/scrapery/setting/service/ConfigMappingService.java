package io.github.scrapery.setting.service;

import io.github.scrapery.setting.domain.ConfigMapping;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing ConfigMapping.
 */
public interface ConfigMappingService {

    /**
     * Save a configMapping.
     *
     * @param configMapping the entity to save
     * @return the persisted entity
     */
    ConfigMapping save(ConfigMapping configMapping);

    /**
     * Get all the configMappings.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ConfigMapping> findAll(Pageable pageable);


    /**
     * Get the "id" configMapping.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<ConfigMapping> findOne(String id);

    /**
     * Delete the "id" configMapping.
     *
     * @param id the id of the entity
     */
    void delete(String id);

    /**
     * Search for the configMapping corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ConfigMapping> search(String query, Pageable pageable);
}
