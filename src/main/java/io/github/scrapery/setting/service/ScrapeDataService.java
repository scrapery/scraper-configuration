package io.github.scrapery.setting.service;

import io.github.scrapery.setting.domain.ScrapeData;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing ScrapeData.
 */
public interface ScrapeDataService {

    /**
     * Save a scrapeData.
     *
     * @param scrapeData the entity to save
     * @return the persisted entity
     */
    ScrapeData save(ScrapeData scrapeData);

    /**
     * Get all the scrapeData.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ScrapeData> findAll(Pageable pageable);


    /**
     * Get the "id" scrapeData.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<ScrapeData> findOne(String id);

    /**
     * Delete the "id" scrapeData.
     *
     * @param id the id of the entity
     */
    void delete(String id);

    /**
     * Search for the scrapeData corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ScrapeData> search(String query, Pageable pageable);
}
