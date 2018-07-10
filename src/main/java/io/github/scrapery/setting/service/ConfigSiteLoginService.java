package io.github.scrapery.setting.service;

import io.github.scrapery.setting.domain.ConfigSiteLogin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing ConfigSiteLogin.
 */
public interface ConfigSiteLoginService {

    /**
     * Save a configSiteLogin.
     *
     * @param configSiteLogin the entity to save
     * @return the persisted entity
     */
    ConfigSiteLogin save(ConfigSiteLogin configSiteLogin);

    /**
     * Get all the configSiteLogins.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ConfigSiteLogin> findAll(Pageable pageable);


    /**
     * Get the "id" configSiteLogin.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<ConfigSiteLogin> findOne(String id);

    /**
     * Delete the "id" configSiteLogin.
     *
     * @param id the id of the entity
     */
    void delete(String id);

    /**
     * Search for the configSiteLogin corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ConfigSiteLogin> search(String query, Pageable pageable);
}
