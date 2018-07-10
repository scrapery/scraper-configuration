package io.github.scrapery.setting.service.impl;

import io.github.scrapery.setting.service.ChannelOuterLinkService;
import io.github.scrapery.setting.domain.ChannelOuterLink;
import io.github.scrapery.setting.repository.ChannelOuterLinkRepository;
import io.github.scrapery.setting.repository.search.ChannelOuterLinkSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing ChannelOuterLink.
 */
@Service
public class ChannelOuterLinkServiceImpl implements ChannelOuterLinkService {

    private final Logger log = LoggerFactory.getLogger(ChannelOuterLinkServiceImpl.class);

    private final ChannelOuterLinkRepository channelOuterLinkRepository;

    private final ChannelOuterLinkSearchRepository channelOuterLinkSearchRepository;

    public ChannelOuterLinkServiceImpl(ChannelOuterLinkRepository channelOuterLinkRepository, ChannelOuterLinkSearchRepository channelOuterLinkSearchRepository) {
        this.channelOuterLinkRepository = channelOuterLinkRepository;
        this.channelOuterLinkSearchRepository = channelOuterLinkSearchRepository;
    }

    /**
     * Save a channelOuterLink.
     *
     * @param channelOuterLink the entity to save
     * @return the persisted entity
     */
    @Override
    public ChannelOuterLink save(ChannelOuterLink channelOuterLink) {
        log.debug("Request to save ChannelOuterLink : {}", channelOuterLink);        ChannelOuterLink result = channelOuterLinkRepository.save(channelOuterLink);
        channelOuterLinkSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the channelOuterLinks.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<ChannelOuterLink> findAll(Pageable pageable) {
        log.debug("Request to get all ChannelOuterLinks");
        return channelOuterLinkRepository.findAll(pageable);
    }


    /**
     * Get one channelOuterLink by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    public Optional<ChannelOuterLink> findOne(String id) {
        log.debug("Request to get ChannelOuterLink : {}", id);
        return channelOuterLinkRepository.findById(id);
    }

    /**
     * Delete the channelOuterLink by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete ChannelOuterLink : {}", id);
        channelOuterLinkRepository.deleteById(id);
        channelOuterLinkSearchRepository.deleteById(id);
    }

    /**
     * Search for the channelOuterLink corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<ChannelOuterLink> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ChannelOuterLinks for query {}", query);
        return channelOuterLinkSearchRepository.search(queryStringQuery(query), pageable);    }
}
