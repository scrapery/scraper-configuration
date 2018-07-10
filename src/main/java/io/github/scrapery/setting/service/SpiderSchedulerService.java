package io.github.scrapery.setting.service;

import io.github.scrapery.setting.domain.SpiderScheduler;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing SpiderScheduler.
 */
public interface SpiderSchedulerService {

    /**
     * Save a spiderScheduler.
     *
     * @param spiderScheduler the entity to save
     * @return the persisted entity
     */
    SpiderScheduler save(SpiderScheduler spiderScheduler);

    /**
     * Get all the spiderSchedulers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<SpiderScheduler> findAll(Pageable pageable);


    /**
     * Get the "id" spiderScheduler.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<SpiderScheduler> findOne(String id);

    /**
     * Delete the "id" spiderScheduler.
     *
     * @param id the id of the entity
     */
    void delete(String id);

    /**
     * Search for the spiderScheduler corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<SpiderScheduler> search(String query, Pageable pageable);
}
