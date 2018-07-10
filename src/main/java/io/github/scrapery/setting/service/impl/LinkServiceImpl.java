package io.github.scrapery.setting.service.impl;

import io.github.scrapery.setting.service.LinkService;
import io.github.scrapery.setting.domain.Link;
import io.github.scrapery.setting.repository.LinkRepository;
import io.github.scrapery.setting.repository.search.LinkSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Link.
 */
@Service
public class LinkServiceImpl implements LinkService {

    private final Logger log = LoggerFactory.getLogger(LinkServiceImpl.class);

    private final LinkRepository linkRepository;

    private final LinkSearchRepository linkSearchRepository;

    public LinkServiceImpl(LinkRepository linkRepository, LinkSearchRepository linkSearchRepository) {
        this.linkRepository = linkRepository;
        this.linkSearchRepository = linkSearchRepository;
    }

    /**
     * Save a link.
     *
     * @param link the entity to save
     * @return the persisted entity
     */
    @Override
    public Link save(Link link) {
        log.debug("Request to save Link : {}", link);        Link result = linkRepository.save(link);
        linkSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the links.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<Link> findAll(Pageable pageable) {
        log.debug("Request to get all Links");
        return linkRepository.findAll(pageable);
    }


    /**
     * Get one link by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    public Optional<Link> findOne(String id) {
        log.debug("Request to get Link : {}", id);
        return linkRepository.findById(id);
    }

    /**
     * Delete the link by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete Link : {}", id);
        linkRepository.deleteById(id);
        linkSearchRepository.deleteById(id);
    }

    /**
     * Search for the link corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<Link> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Links for query {}", query);
        return linkSearchRepository.search(queryStringQuery(query), pageable);    }
}
