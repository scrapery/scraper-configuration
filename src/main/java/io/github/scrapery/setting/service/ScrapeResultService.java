package io.github.scrapery.setting.service;

import io.github.scrapery.setting.domain.ScrapeResult;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing ScrapeResult.
 */
public interface ScrapeResultService {

    /**
     * Save a scrapeResult.
     *
     * @param scrapeResult the entity to save
     * @return the persisted entity
     */
    ScrapeResult save(ScrapeResult scrapeResult);

    /**
     * Get all the scrapeResults.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ScrapeResult> findAll(Pageable pageable);


    /**
     * Get the "id" scrapeResult.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<ScrapeResult> findOne(String id);

    /**
     * Delete the "id" scrapeResult.
     *
     * @param id the id of the entity
     */
    void delete(String id);

    /**
     * Search for the scrapeResult corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ScrapeResult> search(String query, Pageable pageable);
}
