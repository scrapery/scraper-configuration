package io.github.scrapery.setting.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.scrapery.setting.domain.Channel;
import io.github.scrapery.setting.service.ChannelService;
import io.github.scrapery.setting.web.rest.errors.BadRequestAlertException;
import io.github.scrapery.setting.web.rest.util.HeaderUtil;
import io.github.scrapery.setting.web.rest.util.PaginationUtil;
import io.github.simlife.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Channel.
 */
@RestController
@RequestMapping("/api")
public class ChannelResource {

    private final Logger log = LoggerFactory.getLogger(ChannelResource.class);

    private static final String ENTITY_NAME = "channel";

    private final ChannelService channelService;

    public ChannelResource(ChannelService channelService) {
        this.channelService = channelService;
    }

    /**
     * POST  /channels : Create a new channel.
     *
     * @param channel the channel to create
     * @return the ResponseEntity with status 201 (Created) and with body the new channel, or with status 400 (Bad Request) if the channel has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/channels")
    @Timed
    public ResponseEntity<Channel> createChannel(@RequestBody Channel channel) throws URISyntaxException {
        log.debug("REST request to save Channel : {}", channel);
        if (channel.getId() != null) {
            throw new BadRequestAlertException("A new channel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Channel result = channelService.save(channel);
        return ResponseEntity.created(new URI("/api/channels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /channels : Updates an existing channel.
     *
     * @param channel the channel to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated channel,
     * or with status 400 (Bad Request) if the channel is not valid,
     * or with status 500 (Internal Server Error) if the channel couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/channels")
    @Timed
    public ResponseEntity<Channel> updateChannel(@RequestBody Channel channel) throws URISyntaxException {
        log.debug("REST request to update Channel : {}", channel);
        if (channel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Channel result = channelService.save(channel);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, channel.getId().toString()))
            .body(result);
    }

    /**
     * GET  /channels : get all the channels.
     *
     * @param pageable the pagination information
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many)
     * @return the ResponseEntity with status 200 (OK) and the list of channels in body
     */
    @GetMapping("/channels")
    @Timed
    public ResponseEntity<List<Channel>> getAllChannels(Pageable pageable, @RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get a page of Channels");
        Page<Channel> page;
        if (eagerload) {
            page = channelService.findAllWithEagerRelationships(pageable);
        } else {
            page = channelService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, String.format("/api/channels?eagerload=%b", eagerload));
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /channels/:id : get the "id" channel.
     *
     * @param id the id of the channel to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the channel, or with status 404 (Not Found)
     */
    @GetMapping("/channels/{id}")
    @Timed
    public ResponseEntity<Channel> getChannel(@PathVariable String id) {
        log.debug("REST request to get Channel : {}", id);
        Optional<Channel> channel = channelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(channel);
    }

    /**
     * DELETE  /channels/:id : delete the "id" channel.
     *
     * @param id the id of the channel to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/channels/{id}")
    @Timed
    public ResponseEntity<Void> deleteChannel(@PathVariable String id) {
        log.debug("REST request to delete Channel : {}", id);
        channelService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }

    /**
     * SEARCH  /_search/channels?query=:query : search for the channel corresponding
     * to the query.
     *
     * @param query the query of the channel search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/channels")
    @Timed
    public ResponseEntity<List<Channel>> searchChannels(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Channels for query {}", query);
        Page<Channel> page = channelService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/channels");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
