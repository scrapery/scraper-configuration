package io.github.scrapery.setting.service.impl;

import io.github.scrapery.setting.service.DemoService;
import io.github.scrapery.setting.domain.Demo;
import io.github.scrapery.setting.repository.DemoRepository;
import io.github.scrapery.setting.repository.search.DemoSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Demo.
 */
@Service
public class DemoServiceImpl implements DemoService {

    private final Logger log = LoggerFactory.getLogger(DemoServiceImpl.class);

    private final DemoRepository demoRepository;

    private final DemoSearchRepository demoSearchRepository;

    public DemoServiceImpl(DemoRepository demoRepository, DemoSearchRepository demoSearchRepository) {
        this.demoRepository = demoRepository;
        this.demoSearchRepository = demoSearchRepository;
    }

    /**
     * Save a demo.
     *
     * @param demo the entity to save
     * @return the persisted entity
     */
    @Override
    public Demo save(Demo demo) {
        log.debug("Request to save Demo : {}", demo);        Demo result = demoRepository.save(demo);
        demoSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the demos.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<Demo> findAll(Pageable pageable) {
        log.debug("Request to get all Demos");
        return demoRepository.findAll(pageable);
    }


    /**
     * Get one demo by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    public Optional<Demo> findOne(String id) {
        log.debug("Request to get Demo : {}", id);
        return demoRepository.findById(id);
    }

    /**
     * Delete the demo by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete Demo : {}", id);
        demoRepository.deleteById(id);
        demoSearchRepository.deleteById(id);
    }

    /**
     * Search for the demo corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<Demo> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Demos for query {}", query);
        return demoSearchRepository.search(queryStringQuery(query), pageable);    }
}
