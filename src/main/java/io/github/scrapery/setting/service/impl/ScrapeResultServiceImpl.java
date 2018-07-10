package io.github.scrapery.setting.service.impl;

import io.github.scrapery.setting.service.ScrapeResultService;
import io.github.scrapery.setting.domain.ScrapeResult;
import io.github.scrapery.setting.repository.ScrapeResultRepository;
import io.github.scrapery.setting.repository.search.ScrapeResultSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing ScrapeResult.
 */
@Service
public class ScrapeResultServiceImpl implements ScrapeResultService {

    private final Logger log = LoggerFactory.getLogger(ScrapeResultServiceImpl.class);

    private final ScrapeResultRepository scrapeResultRepository;

    private final ScrapeResultSearchRepository scrapeResultSearchRepository;

    public ScrapeResultServiceImpl(ScrapeResultRepository scrapeResultRepository, ScrapeResultSearchRepository scrapeResultSearchRepository) {
        this.scrapeResultRepository = scrapeResultRepository;
        this.scrapeResultSearchRepository = scrapeResultSearchRepository;
    }

    /**
     * Save a scrapeResult.
     *
     * @param scrapeResult the entity to save
     * @return the persisted entity
     */
    @Override
    public ScrapeResult save(ScrapeResult scrapeResult) {
        log.debug("Request to save ScrapeResult : {}", scrapeResult);        ScrapeResult result = scrapeResultRepository.save(scrapeResult);
        scrapeResultSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the scrapeResults.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<ScrapeResult> findAll(Pageable pageable) {
        log.debug("Request to get all ScrapeResults");
        return scrapeResultRepository.findAll(pageable);
    }


    /**
     * Get one scrapeResult by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    public Optional<ScrapeResult> findOne(String id) {
        log.debug("Request to get ScrapeResult : {}", id);
        return scrapeResultRepository.findById(id);
    }

    /**
     * Delete the scrapeResult by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete ScrapeResult : {}", id);
        scrapeResultRepository.deleteById(id);
        scrapeResultSearchRepository.deleteById(id);
    }

    /**
     * Search for the scrapeResult corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<ScrapeResult> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ScrapeResults for query {}", query);
        return scrapeResultSearchRepository.search(queryStringQuery(query), pageable);    }
}
