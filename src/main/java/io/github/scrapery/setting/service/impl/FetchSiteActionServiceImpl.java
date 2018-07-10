package io.github.scrapery.setting.service.impl;

import io.github.scrapery.setting.service.FetchSiteActionService;
import io.github.scrapery.setting.domain.FetchSiteAction;
import io.github.scrapery.setting.repository.FetchSiteActionRepository;
import io.github.scrapery.setting.repository.search.FetchSiteActionSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing FetchSiteAction.
 */
@Service
public class FetchSiteActionServiceImpl implements FetchSiteActionService {

    private final Logger log = LoggerFactory.getLogger(FetchSiteActionServiceImpl.class);

    private final FetchSiteActionRepository fetchSiteActionRepository;

    private final FetchSiteActionSearchRepository fetchSiteActionSearchRepository;

    public FetchSiteActionServiceImpl(FetchSiteActionRepository fetchSiteActionRepository, FetchSiteActionSearchRepository fetchSiteActionSearchRepository) {
        this.fetchSiteActionRepository = fetchSiteActionRepository;
        this.fetchSiteActionSearchRepository = fetchSiteActionSearchRepository;
    }

    /**
     * Save a fetchSiteAction.
     *
     * @param fetchSiteAction the entity to save
     * @return the persisted entity
     */
    @Override
    public FetchSiteAction save(FetchSiteAction fetchSiteAction) {
        log.debug("Request to save FetchSiteAction : {}", fetchSiteAction);        FetchSiteAction result = fetchSiteActionRepository.save(fetchSiteAction);
        fetchSiteActionSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the fetchSiteActions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<FetchSiteAction> findAll(Pageable pageable) {
        log.debug("Request to get all FetchSiteActions");
        return fetchSiteActionRepository.findAll(pageable);
    }


    /**
     * Get one fetchSiteAction by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    public Optional<FetchSiteAction> findOne(String id) {
        log.debug("Request to get FetchSiteAction : {}", id);
        return fetchSiteActionRepository.findById(id);
    }

    /**
     * Delete the fetchSiteAction by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete FetchSiteAction : {}", id);
        fetchSiteActionRepository.deleteById(id);
        fetchSiteActionSearchRepository.deleteById(id);
    }

    /**
     * Search for the fetchSiteAction corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<FetchSiteAction> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of FetchSiteActions for query {}", query);
        return fetchSiteActionSearchRepository.search(queryStringQuery(query), pageable);    }
}
