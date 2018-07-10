package io.github.scrapery.setting.service;

import io.github.scrapery.setting.domain.TryParser;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing TryParser.
 */
public interface TryParserService {

    /**
     * Save a tryParser.
     *
     * @param tryParser the entity to save
     * @return the persisted entity
     */
    TryParser save(TryParser tryParser);

    /**
     * Get all the tryParsers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<TryParser> findAll(Pageable pageable);


    /**
     * Get the "id" tryParser.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<TryParser> findOne(String id);

    /**
     * Delete the "id" tryParser.
     *
     * @param id the id of the entity
     */
    void delete(String id);

    /**
     * Search for the tryParser corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<TryParser> search(String query, Pageable pageable);
}
