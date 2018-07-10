package io.github.scrapery.setting.web.rest;

import io.github.scrapery.setting.ScraperSettingApp;

import io.github.scrapery.setting.domain.ChannelOuterLink;
import io.github.scrapery.setting.repository.ChannelOuterLinkRepository;
import io.github.scrapery.setting.repository.search.ChannelOuterLinkSearchRepository;
import io.github.scrapery.setting.service.ChannelOuterLinkService;
import io.github.scrapery.setting.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;


import static io.github.scrapery.setting.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ChannelOuterLinkResource REST controller.
 *
 * @see ChannelOuterLinkResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ScraperSettingApp.class)
public class ChannelOuterLinkResourceIntTest {

    private static final String DEFAULT_CONFIG_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CONFIG_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SELECTOR_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SELECTOR_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SELECTOR_ATTR = "AAAAAAAAAA";
    private static final String UPDATED_SELECTOR_ATTR = "BBBBBBBBBB";

    private static final String DEFAULT_HOST = "AAAAAAAAAA";
    private static final String UPDATED_HOST = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    @Autowired
    private ChannelOuterLinkRepository channelOuterLinkRepository;

    

    @Autowired
    private ChannelOuterLinkService channelOuterLinkService;

    /**
     * This repository is mocked in the io.github.scrapery.setting.repository.search test package.
     *
     * @see io.github.scrapery.setting.repository.search.ChannelOuterLinkSearchRepositoryMockConfiguration
     */
    @Autowired
    private ChannelOuterLinkSearchRepository mockChannelOuterLinkSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restChannelOuterLinkMockMvc;

    private ChannelOuterLink channelOuterLink;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ChannelOuterLinkResource channelOuterLinkResource = new ChannelOuterLinkResource(channelOuterLinkService);
        this.restChannelOuterLinkMockMvc = MockMvcBuilders.standaloneSetup(channelOuterLinkResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChannelOuterLink createEntity() {
        ChannelOuterLink channelOuterLink = new ChannelOuterLink()
            .configName(DEFAULT_CONFIG_NAME)
            .selectorName(DEFAULT_SELECTOR_NAME)
            .selectorAttr(DEFAULT_SELECTOR_ATTR)
            .host(DEFAULT_HOST)
            .url(DEFAULT_URL);
        return channelOuterLink;
    }

    @Before
    public void initTest() {
        channelOuterLinkRepository.deleteAll();
        channelOuterLink = createEntity();
    }

    @Test
    public void createChannelOuterLink() throws Exception {
        int databaseSizeBeforeCreate = channelOuterLinkRepository.findAll().size();

        // Create the ChannelOuterLink
        restChannelOuterLinkMockMvc.perform(post("/api/channel-outer-links")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(channelOuterLink)))
            .andExpect(status().isCreated());

        // Validate the ChannelOuterLink in the database
        List<ChannelOuterLink> channelOuterLinkList = channelOuterLinkRepository.findAll();
        assertThat(channelOuterLinkList).hasSize(databaseSizeBeforeCreate + 1);
        ChannelOuterLink testChannelOuterLink = channelOuterLinkList.get(channelOuterLinkList.size() - 1);
        assertThat(testChannelOuterLink.getConfigName()).isEqualTo(DEFAULT_CONFIG_NAME);
        assertThat(testChannelOuterLink.getSelectorName()).isEqualTo(DEFAULT_SELECTOR_NAME);
        assertThat(testChannelOuterLink.getSelectorAttr()).isEqualTo(DEFAULT_SELECTOR_ATTR);
        assertThat(testChannelOuterLink.getHost()).isEqualTo(DEFAULT_HOST);
        assertThat(testChannelOuterLink.getUrl()).isEqualTo(DEFAULT_URL);

        // Validate the ChannelOuterLink in Elasticsearch
        verify(mockChannelOuterLinkSearchRepository, times(1)).save(testChannelOuterLink);
    }

    @Test
    public void createChannelOuterLinkWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = channelOuterLinkRepository.findAll().size();

        // Create the ChannelOuterLink with an existing ID
        channelOuterLink.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restChannelOuterLinkMockMvc.perform(post("/api/channel-outer-links")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(channelOuterLink)))
            .andExpect(status().isBadRequest());

        // Validate the ChannelOuterLink in the database
        List<ChannelOuterLink> channelOuterLinkList = channelOuterLinkRepository.findAll();
        assertThat(channelOuterLinkList).hasSize(databaseSizeBeforeCreate);

        // Validate the ChannelOuterLink in Elasticsearch
        verify(mockChannelOuterLinkSearchRepository, times(0)).save(channelOuterLink);
    }

    @Test
    public void getAllChannelOuterLinks() throws Exception {
        // Initialize the database
        channelOuterLinkRepository.save(channelOuterLink);

        // Get all the channelOuterLinkList
        restChannelOuterLinkMockMvc.perform(get("/api/channel-outer-links?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(channelOuterLink.getId())))
            .andExpect(jsonPath("$.[*].configName").value(hasItem(DEFAULT_CONFIG_NAME.toString())))
            .andExpect(jsonPath("$.[*].selectorName").value(hasItem(DEFAULT_SELECTOR_NAME.toString())))
            .andExpect(jsonPath("$.[*].selectorAttr").value(hasItem(DEFAULT_SELECTOR_ATTR.toString())))
            .andExpect(jsonPath("$.[*].host").value(hasItem(DEFAULT_HOST.toString())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())));
    }
    

    @Test
    public void getChannelOuterLink() throws Exception {
        // Initialize the database
        channelOuterLinkRepository.save(channelOuterLink);

        // Get the channelOuterLink
        restChannelOuterLinkMockMvc.perform(get("/api/channel-outer-links/{id}", channelOuterLink.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(channelOuterLink.getId()))
            .andExpect(jsonPath("$.configName").value(DEFAULT_CONFIG_NAME.toString()))
            .andExpect(jsonPath("$.selectorName").value(DEFAULT_SELECTOR_NAME.toString()))
            .andExpect(jsonPath("$.selectorAttr").value(DEFAULT_SELECTOR_ATTR.toString()))
            .andExpect(jsonPath("$.host").value(DEFAULT_HOST.toString()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()));
    }
    @Test
    public void getNonExistingChannelOuterLink() throws Exception {
        // Get the channelOuterLink
        restChannelOuterLinkMockMvc.perform(get("/api/channel-outer-links/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateChannelOuterLink() throws Exception {
        // Initialize the database
        channelOuterLinkService.save(channelOuterLink);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockChannelOuterLinkSearchRepository);

        int databaseSizeBeforeUpdate = channelOuterLinkRepository.findAll().size();

        // Update the channelOuterLink
        ChannelOuterLink updatedChannelOuterLink = channelOuterLinkRepository.findById(channelOuterLink.getId()).get();
        updatedChannelOuterLink
            .configName(UPDATED_CONFIG_NAME)
            .selectorName(UPDATED_SELECTOR_NAME)
            .selectorAttr(UPDATED_SELECTOR_ATTR)
            .host(UPDATED_HOST)
            .url(UPDATED_URL);

        restChannelOuterLinkMockMvc.perform(put("/api/channel-outer-links")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedChannelOuterLink)))
            .andExpect(status().isOk());

        // Validate the ChannelOuterLink in the database
        List<ChannelOuterLink> channelOuterLinkList = channelOuterLinkRepository.findAll();
        assertThat(channelOuterLinkList).hasSize(databaseSizeBeforeUpdate);
        ChannelOuterLink testChannelOuterLink = channelOuterLinkList.get(channelOuterLinkList.size() - 1);
        assertThat(testChannelOuterLink.getConfigName()).isEqualTo(UPDATED_CONFIG_NAME);
        assertThat(testChannelOuterLink.getSelectorName()).isEqualTo(UPDATED_SELECTOR_NAME);
        assertThat(testChannelOuterLink.getSelectorAttr()).isEqualTo(UPDATED_SELECTOR_ATTR);
        assertThat(testChannelOuterLink.getHost()).isEqualTo(UPDATED_HOST);
        assertThat(testChannelOuterLink.getUrl()).isEqualTo(UPDATED_URL);

        // Validate the ChannelOuterLink in Elasticsearch
        verify(mockChannelOuterLinkSearchRepository, times(1)).save(testChannelOuterLink);
    }

    @Test
    public void updateNonExistingChannelOuterLink() throws Exception {
        int databaseSizeBeforeUpdate = channelOuterLinkRepository.findAll().size();

        // Create the ChannelOuterLink

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restChannelOuterLinkMockMvc.perform(put("/api/channel-outer-links")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(channelOuterLink)))
            .andExpect(status().isBadRequest());

        // Validate the ChannelOuterLink in the database
        List<ChannelOuterLink> channelOuterLinkList = channelOuterLinkRepository.findAll();
        assertThat(channelOuterLinkList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ChannelOuterLink in Elasticsearch
        verify(mockChannelOuterLinkSearchRepository, times(0)).save(channelOuterLink);
    }

    @Test
    public void deleteChannelOuterLink() throws Exception {
        // Initialize the database
        channelOuterLinkService.save(channelOuterLink);

        int databaseSizeBeforeDelete = channelOuterLinkRepository.findAll().size();

        // Get the channelOuterLink
        restChannelOuterLinkMockMvc.perform(delete("/api/channel-outer-links/{id}", channelOuterLink.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ChannelOuterLink> channelOuterLinkList = channelOuterLinkRepository.findAll();
        assertThat(channelOuterLinkList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ChannelOuterLink in Elasticsearch
        verify(mockChannelOuterLinkSearchRepository, times(1)).deleteById(channelOuterLink.getId());
    }

    @Test
    public void searchChannelOuterLink() throws Exception {
        // Initialize the database
        channelOuterLinkService.save(channelOuterLink);
        when(mockChannelOuterLinkSearchRepository.search(queryStringQuery("id:" + channelOuterLink.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(channelOuterLink), PageRequest.of(0, 1), 1));
        // Search the channelOuterLink
        restChannelOuterLinkMockMvc.perform(get("/api/_search/channel-outer-links?query=id:" + channelOuterLink.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(channelOuterLink.getId())))
            .andExpect(jsonPath("$.[*].configName").value(hasItem(DEFAULT_CONFIG_NAME.toString())))
            .andExpect(jsonPath("$.[*].selectorName").value(hasItem(DEFAULT_SELECTOR_NAME.toString())))
            .andExpect(jsonPath("$.[*].selectorAttr").value(hasItem(DEFAULT_SELECTOR_ATTR.toString())))
            .andExpect(jsonPath("$.[*].host").value(hasItem(DEFAULT_HOST.toString())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())));
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChannelOuterLink.class);
        ChannelOuterLink channelOuterLink1 = new ChannelOuterLink();
        channelOuterLink1.setId("id1");
        ChannelOuterLink channelOuterLink2 = new ChannelOuterLink();
        channelOuterLink2.setId(channelOuterLink1.getId());
        assertThat(channelOuterLink1).isEqualTo(channelOuterLink2);
        channelOuterLink2.setId("id2");
        assertThat(channelOuterLink1).isNotEqualTo(channelOuterLink2);
        channelOuterLink1.setId(null);
        assertThat(channelOuterLink1).isNotEqualTo(channelOuterLink2);
    }
}
