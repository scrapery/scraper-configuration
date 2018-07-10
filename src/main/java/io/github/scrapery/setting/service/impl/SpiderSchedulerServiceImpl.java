package io.github.scrapery.setting.service.impl;

import io.github.scrapery.setting.service.SpiderSchedulerService;
import io.github.scrapery.setting.domain.SpiderScheduler;
import io.github.scrapery.setting.repository.SpiderSchedulerRepository;
import io.github.scrapery.setting.repository.search.SpiderSchedulerSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing SpiderScheduler.
 */
@Service
public class SpiderSchedulerServiceImpl implements SpiderSchedulerService {

    private final Logger log = LoggerFactory.getLogger(SpiderSchedulerServiceImpl.class);

    private final SpiderSchedulerRepository spiderSchedulerRepository;

    private final SpiderSchedulerSearchRepository spiderSchedulerSearchRepository;

    public SpiderSchedulerServiceImpl(SpiderSchedulerRepository spiderSchedulerRepository, SpiderSchedulerSearchRepository spiderSchedulerSearchRepository) {
        this.spiderSchedulerRepository = spiderSchedulerRepository;
        this.spiderSchedulerSearchRepository = spiderSchedulerSearchRepository;
    }

    /**
     * Save a spiderScheduler.
     *
     * @param spiderScheduler the entity to save
     * @return the persisted entity
     */
    @Override
    public SpiderScheduler save(SpiderScheduler spiderScheduler) {
        log.debug("Request to save SpiderScheduler : {}", spiderScheduler);        SpiderScheduler result = spiderSchedulerRepository.save(spiderScheduler);
        spiderSchedulerSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the spiderSchedulers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<SpiderScheduler> findAll(Pageable pageable) {
        log.debug("Request to get all SpiderSchedulers");
        return spiderSchedulerRepository.findAll(pageable);
    }


    /**
     * Get one spiderScheduler by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    public Optional<SpiderScheduler> findOne(String id) {
        log.debug("Request to get SpiderScheduler : {}", id);
        return spiderSchedulerRepository.findById(id);
    }

    /**
     * Delete the spiderScheduler by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete SpiderScheduler : {}", id);
        spiderSchedulerRepository.deleteById(id);
        spiderSchedulerSearchRepository.deleteById(id);
    }

    /**
     * Search for the spiderScheduler corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<SpiderScheduler> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SpiderSchedulers for query {}", query);
        return spiderSchedulerSearchRepository.search(queryStringQuery(query), pageable);    }
}
