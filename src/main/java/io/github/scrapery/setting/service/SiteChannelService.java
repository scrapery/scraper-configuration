package io.github.scrapery.setting.service;

import io.github.scrapery.setting.domain.SiteChannel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing SiteChannel.
 */
public interface SiteChannelService {

    /**
     * Save a siteChannel.
     *
     * @param siteChannel the entity to save
     * @return the persisted entity
     */
    SiteChannel save(SiteChannel siteChannel);

    /**
     * Get all the siteChannels.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<SiteChannel> findAll(Pageable pageable);

    /**
     * Get all the SiteChannel with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    Page<SiteChannel> findAllWithEagerRelationships(Pageable pageable);
    
    /**
     * Get the "id" siteChannel.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<SiteChannel> findOne(String id);

    /**
     * Delete the "id" siteChannel.
     *
     * @param id the id of the entity
     */
    void delete(String id);

    /**
     * Search for the siteChannel corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<SiteChannel> search(String query, Pageable pageable);
}
