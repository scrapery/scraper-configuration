package io.github.scrapery.setting.service.impl;

import io.github.scrapery.setting.service.ScrapeDataService;
import io.github.scrapery.setting.domain.ScrapeData;
import io.github.scrapery.setting.repository.ScrapeDataRepository;
import io.github.scrapery.setting.repository.search.ScrapeDataSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing ScrapeData.
 */
@Service
public class ScrapeDataServiceImpl implements ScrapeDataService {

    private final Logger log = LoggerFactory.getLogger(ScrapeDataServiceImpl.class);

    private final ScrapeDataRepository scrapeDataRepository;

    private final ScrapeDataSearchRepository scrapeDataSearchRepository;

    public ScrapeDataServiceImpl(ScrapeDataRepository scrapeDataRepository, ScrapeDataSearchRepository scrapeDataSearchRepository) {
        this.scrapeDataRepository = scrapeDataRepository;
        this.scrapeDataSearchRepository = scrapeDataSearchRepository;
    }

    /**
     * Save a scrapeData.
     *
     * @param scrapeData the entity to save
     * @return the persisted entity
     */
    @Override
    public ScrapeData save(ScrapeData scrapeData) {
        log.debug("Request to save ScrapeData : {}", scrapeData);        ScrapeData result = scrapeDataRepository.save(scrapeData);
        scrapeDataSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the scrapeData.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<ScrapeData> findAll(Pageable pageable) {
        log.debug("Request to get all ScrapeData");
        return scrapeDataRepository.findAll(pageable);
    }


    /**
     * Get one scrapeData by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    public Optional<ScrapeData> findOne(String id) {
        log.debug("Request to get ScrapeData : {}", id);
        return scrapeDataRepository.findById(id);
    }

    /**
     * Delete the scrapeData by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete ScrapeData : {}", id);
        scrapeDataRepository.deleteById(id);
        scrapeDataSearchRepository.deleteById(id);
    }

    /**
     * Search for the scrapeData corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<ScrapeData> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ScrapeData for query {}", query);
        return scrapeDataSearchRepository.search(queryStringQuery(query), pageable);    }
}
