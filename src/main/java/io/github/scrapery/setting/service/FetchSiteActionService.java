package io.github.scrapery.setting.service;

import io.github.scrapery.setting.domain.FetchSiteAction;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing FetchSiteAction.
 */
public interface FetchSiteActionService {

    /**
     * Save a fetchSiteAction.
     *
     * @param fetchSiteAction the entity to save
     * @return the persisted entity
     */
    FetchSiteAction save(FetchSiteAction fetchSiteAction);

    /**
     * Get all the fetchSiteActions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<FetchSiteAction> findAll(Pageable pageable);


    /**
     * Get the "id" fetchSiteAction.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<FetchSiteAction> findOne(String id);

    /**
     * Delete the "id" fetchSiteAction.
     *
     * @param id the id of the entity
     */
    void delete(String id);

    /**
     * Search for the fetchSiteAction corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<FetchSiteAction> search(String query, Pageable pageable);
}
