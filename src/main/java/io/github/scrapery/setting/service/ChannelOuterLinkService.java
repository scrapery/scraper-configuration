package io.github.scrapery.setting.service;

import io.github.scrapery.setting.domain.ChannelOuterLink;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing ChannelOuterLink.
 */
public interface ChannelOuterLinkService {

    /**
     * Save a channelOuterLink.
     *
     * @param channelOuterLink the entity to save
     * @return the persisted entity
     */
    ChannelOuterLink save(ChannelOuterLink channelOuterLink);

    /**
     * Get all the channelOuterLinks.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ChannelOuterLink> findAll(Pageable pageable);


    /**
     * Get the "id" channelOuterLink.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<ChannelOuterLink> findOne(String id);

    /**
     * Delete the "id" channelOuterLink.
     *
     * @param id the id of the entity
     */
    void delete(String id);

    /**
     * Search for the channelOuterLink corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ChannelOuterLink> search(String query, Pageable pageable);
}
