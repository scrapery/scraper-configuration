package io.github.scrapery.setting.service;

import io.github.scrapery.setting.domain.Link;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Link.
 */
public interface LinkService {

    /**
     * Save a link.
     *
     * @param link the entity to save
     * @return the persisted entity
     */
    Link save(Link link);

    /**
     * Get all the links.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Link> findAll(Pageable pageable);


    /**
     * Get the "id" link.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Link> findOne(String id);

    /**
     * Delete the "id" link.
     *
     * @param id the id of the entity
     */
    void delete(String id);

    /**
     * Search for the link corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Link> search(String query, Pageable pageable);
}
