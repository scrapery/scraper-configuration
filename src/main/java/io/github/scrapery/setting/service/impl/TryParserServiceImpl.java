package io.github.scrapery.setting.service.impl;

import io.github.scrapery.setting.service.TryParserService;
import io.github.scrapery.setting.domain.TryParser;
import io.github.scrapery.setting.repository.TryParserRepository;
import io.github.scrapery.setting.repository.search.TryParserSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing TryParser.
 */
@Service
public class TryParserServiceImpl implements TryParserService {

    private final Logger log = LoggerFactory.getLogger(TryParserServiceImpl.class);

    private final TryParserRepository tryParserRepository;

    private final TryParserSearchRepository tryParserSearchRepository;

    public TryParserServiceImpl(TryParserRepository tryParserRepository, TryParserSearchRepository tryParserSearchRepository) {
        this.tryParserRepository = tryParserRepository;
        this.tryParserSearchRepository = tryParserSearchRepository;
    }

    /**
     * Save a tryParser.
     *
     * @param tryParser the entity to save
     * @return the persisted entity
     */
    @Override
    public TryParser save(TryParser tryParser) {
        log.debug("Request to save TryParser : {}", tryParser);        TryParser result = tryParserRepository.save(tryParser);
        tryParserSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the tryParsers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<TryParser> findAll(Pageable pageable) {
        log.debug("Request to get all TryParsers");
        return tryParserRepository.findAll(pageable);
    }


    /**
     * Get one tryParser by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    public Optional<TryParser> findOne(String id) {
        log.debug("Request to get TryParser : {}", id);
        return tryParserRepository.findById(id);
    }

    /**
     * Delete the tryParser by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete TryParser : {}", id);
        tryParserRepository.deleteById(id);
        tryParserSearchRepository.deleteById(id);
    }

    /**
     * Search for the tryParser corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<TryParser> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TryParsers for query {}", query);
        return tryParserSearchRepository.search(queryStringQuery(query), pageable);    }
}
