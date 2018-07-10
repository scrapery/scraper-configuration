package io.github.scrapery.setting.service;

import io.github.scrapery.setting.domain.ConfigSite;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing ConfigSite.
 */
public interface ConfigSiteService {

    /**
     * Save a configSite.
     *
     * @param configSite the entity to save
     * @return the persisted entity
     */
    ConfigSite save(ConfigSite configSite);

    /**
     * Get all the configSites.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ConfigSite> findAll(Pageable pageable);

    /**
     * Get all the ConfigSite with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    Page<ConfigSite> findAllWithEagerRelationships(Pageable pageable);
    
    /**
     * Get the "id" configSite.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<ConfigSite> findOne(String id);

    /**
     * Delete the "id" configSite.
     *
     * @param id the id of the entity
     */
    void delete(String id);

    /**
     * Search for the configSite corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ConfigSite> search(String query, Pageable pageable);
}
