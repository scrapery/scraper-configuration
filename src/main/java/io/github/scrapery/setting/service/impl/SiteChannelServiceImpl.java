package io.github.scrapery.setting.service.impl;

import io.github.scrapery.setting.service.SiteChannelService;
import io.github.scrapery.setting.domain.SiteChannel;
import io.github.scrapery.setting.repository.SiteChannelRepository;
import io.github.scrapery.setting.repository.search.SiteChannelSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing SiteChannel.
 */
@Service
public class SiteChannelServiceImpl implements SiteChannelService {

    private final Logger log = LoggerFactory.getLogger(SiteChannelServiceImpl.class);

    private final SiteChannelRepository siteChannelRepository;

    private final SiteChannelSearchRepository siteChannelSearchRepository;

    public SiteChannelServiceImpl(SiteChannelRepository siteChannelRepository, SiteChannelSearchRepository siteChannelSearchRepository) {
        this.siteChannelRepository = siteChannelRepository;
        this.siteChannelSearchRepository = siteChannelSearchRepository;
    }

    /**
     * Save a siteChannel.
     *
     * @param siteChannel the entity to save
     * @return the persisted entity
     */
    @Override
    public SiteChannel save(SiteChannel siteChannel) {
        log.debug("Request to save SiteChannel : {}", siteChannel);        SiteChannel result = siteChannelRepository.save(siteChannel);
        siteChannelSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the siteChannels.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<SiteChannel> findAll(Pageable pageable) {
        log.debug("Request to get all SiteChannels");
        return siteChannelRepository.findAll(pageable);
    }

    /**
     * Get all the SiteChannel with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<SiteChannel> findAllWithEagerRelationships(Pageable pageable) {
        return siteChannelRepository.findAll(pageable);
    }


    /**
     * Get one siteChannel by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    public Optional<SiteChannel> findOne(String id) {
        log.debug("Request to get SiteChannel : {}", id);
        return siteChannelRepository.findById(id);
    }

    /**
     * Delete the siteChannel by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete SiteChannel : {}", id);
        siteChannelRepository.deleteById(id);
        siteChannelSearchRepository.deleteById(id);
    }

    /**
     * Search for the siteChannel corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<SiteChannel> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SiteChannels for query {}", query);
        return siteChannelSearchRepository.search(queryStringQuery(query), pageable);    }
}
