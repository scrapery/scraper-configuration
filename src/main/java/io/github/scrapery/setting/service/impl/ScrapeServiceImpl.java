package io.github.scrapery.setting.service.impl;

import io.github.scrapery.setting.service.ScrapeService;
import io.github.scrapery.setting.domain.Scrape;
import io.github.scrapery.setting.repository.ScrapeRepository;
import io.github.scrapery.setting.repository.search.ScrapeSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Scrape.
 */
@Service
public class ScrapeServiceImpl implements ScrapeService {

    private final Logger log = LoggerFactory.getLogger(ScrapeServiceImpl.class);

    private final ScrapeRepository scrapeRepository;

    private final ScrapeSearchRepository scrapeSearchRepository;

    public ScrapeServiceImpl(ScrapeRepository scrapeRepository, ScrapeSearchRepository scrapeSearchRepository) {
        this.scrapeRepository = scrapeRepository;
        this.scrapeSearchRepository = scrapeSearchRepository;
    }

    /**
     * Save a scrape.
     *
     * @param scrape the entity to save
     * @return the persisted entity
     */
    @Override
    public Scrape save(Scrape scrape) {
        log.debug("Request to save Scrape : {}", scrape);        Scrape result = scrapeRepository.save(scrape);
        scrapeSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the scrapes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<Scrape> findAll(Pageable pageable) {
        log.debug("Request to get all Scrapes");
        return scrapeRepository.findAll(pageable);
    }

    /**
     * Get all the Scrape with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<Scrape> findAllWithEagerRelationships(Pageable pageable) {
        return scrapeRepository.findAll(pageable);
    }


    /**
     * Get one scrape by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    public Optional<Scrape> findOne(String id) {
        log.debug("Request to get Scrape : {}", id);
        return scrapeRepository.findById(id);
    }

    /**
     * Delete the scrape by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete Scrape : {}", id);
        scrapeRepository.deleteById(id);
        scrapeSearchRepository.deleteById(id);
    }

    /**
     * Search for the scrape corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<Scrape> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Scrapes for query {}", query);
        return scrapeSearchRepository.search(queryStringQuery(query), pageable);    }
}
