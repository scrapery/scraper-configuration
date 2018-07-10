package io.github.scrapery.setting.service;

import io.github.scrapery.setting.domain.Scrape;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Scrape.
 */
public interface ScrapeService {

    /**
     * Save a scrape.
     *
     * @param scrape the entity to save
     * @return the persisted entity
     */
    Scrape save(Scrape scrape);

    /**
     * Get all the scrapes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Scrape> findAll(Pageable pageable);

    /**
     * Get all the Scrape with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    Page<Scrape> findAllWithEagerRelationships(Pageable pageable);
    
    /**
     * Get the "id" scrape.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Scrape> findOne(String id);

    /**
     * Delete the "id" scrape.
     *
     * @param id the id of the entity
     */
    void delete(String id);

    /**
     * Search for the scrape corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Scrape> search(String query, Pageable pageable);
}
