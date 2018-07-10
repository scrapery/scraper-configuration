package io.github.scrapery.setting.service;

import io.github.scrapery.setting.domain.ScrapeChannel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing ScrapeChannel.
 */
public interface ScrapeChannelService {

    /**
     * Save a scrapeChannel.
     *
     * @param scrapeChannel the entity to save
     * @return the persisted entity
     */
    ScrapeChannel save(ScrapeChannel scrapeChannel);

    /**
     * Get all the scrapeChannels.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ScrapeChannel> findAll(Pageable pageable);

    /**
     * Get all the ScrapeChannel with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    Page<ScrapeChannel> findAllWithEagerRelationships(Pageable pageable);
    
    /**
     * Get the "id" scrapeChannel.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<ScrapeChannel> findOne(String id);

    /**
     * Delete the "id" scrapeChannel.
     *
     * @param id the id of the entity
     */
    void delete(String id);

    /**
     * Search for the scrapeChannel corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ScrapeChannel> search(String query, Pageable pageable);
}
