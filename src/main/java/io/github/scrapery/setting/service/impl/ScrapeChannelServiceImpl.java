package io.github.scrapery.setting.service.impl;

import io.github.scrapery.setting.service.ScrapeChannelService;
import io.github.scrapery.setting.domain.ScrapeChannel;
import io.github.scrapery.setting.repository.ScrapeChannelRepository;
import io.github.scrapery.setting.repository.search.ScrapeChannelSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing ScrapeChannel.
 */
@Service
public class ScrapeChannelServiceImpl implements ScrapeChannelService {

    private final Logger log = LoggerFactory.getLogger(ScrapeChannelServiceImpl.class);

    private final ScrapeChannelRepository scrapeChannelRepository;

    private final ScrapeChannelSearchRepository scrapeChannelSearchRepository;

    public ScrapeChannelServiceImpl(ScrapeChannelRepository scrapeChannelRepository, ScrapeChannelSearchRepository scrapeChannelSearchRepository) {
        this.scrapeChannelRepository = scrapeChannelRepository;
        this.scrapeChannelSearchRepository = scrapeChannelSearchRepository;
    }

    /**
     * Save a scrapeChannel.
     *
     * @param scrapeChannel the entity to save
     * @return the persisted entity
     */
    @Override
    public ScrapeChannel save(ScrapeChannel scrapeChannel) {
        log.debug("Request to save ScrapeChannel : {}", scrapeChannel);        ScrapeChannel result = scrapeChannelRepository.save(scrapeChannel);
        scrapeChannelSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the scrapeChannels.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<ScrapeChannel> findAll(Pageable pageable) {
        log.debug("Request to get all ScrapeChannels");
        return scrapeChannelRepository.findAll(pageable);
    }

    /**
     * Get all the ScrapeChannel with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<ScrapeChannel> findAllWithEagerRelationships(Pageable pageable) {
        return scrapeChannelRepository.findAll(pageable);
    }


    /**
     * Get one scrapeChannel by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    public Optional<ScrapeChannel> findOne(String id) {
        log.debug("Request to get ScrapeChannel : {}", id);
        return scrapeChannelRepository.findById(id);
    }

    /**
     * Delete the scrapeChannel by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete ScrapeChannel : {}", id);
        scrapeChannelRepository.deleteById(id);
        scrapeChannelSearchRepository.deleteById(id);
    }

    /**
     * Search for the scrapeChannel corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<ScrapeChannel> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ScrapeChannels for query {}", query);
        return scrapeChannelSearchRepository.search(queryStringQuery(query), pageable);    }
}
