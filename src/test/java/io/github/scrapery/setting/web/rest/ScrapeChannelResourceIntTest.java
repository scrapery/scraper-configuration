package io.github.scrapery.setting.web.rest;

import io.github.scrapery.setting.ScraperSettingApp;

import io.github.scrapery.setting.domain.ScrapeChannel;
import io.github.scrapery.setting.repository.ScrapeChannelRepository;
import io.github.scrapery.setting.repository.search.ScrapeChannelSearchRepository;
import io.github.scrapery.setting.service.ScrapeChannelService;
import io.github.scrapery.setting.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import static io.github.scrapery.setting.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.scrapery.setting.domain.enumeration.DocType;
import io.github.scrapery.setting.domain.enumeration.FetchEngine;
import io.github.scrapery.setting.domain.enumeration.FetchEngine;
/**
 * Test class for the ScrapeChannelResource REST controller.
 *
 * @see ScrapeChannelResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ScraperSettingApp.class)
public class ScrapeChannelResourceIntTest {

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final DocType DEFAULT_CONTENT_TYPE = DocType.HTML;
    private static final DocType UPDATED_CONTENT_TYPE = DocType.XML;

    private static final Integer DEFAULT_TOTAL_LEVEL = 1;
    private static final Integer UPDATED_TOTAL_LEVEL = 2;

    private static final Integer DEFAULT_ARCHIVE_LEVEL = 1;
    private static final Integer UPDATED_ARCHIVE_LEVEL = 2;

    private static final FetchEngine DEFAULT_FETCH_ENGINE = FetchEngine.SELENIUM;
    private static final FetchEngine UPDATED_FETCH_ENGINE = FetchEngine.HTTP;

    private static final String DEFAULT_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY = "BBBBBBBBBB";

    private static final String DEFAULT_TAG = "AAAAAAAAAA";
    private static final String UPDATED_TAG = "BBBBBBBBBB";

    private static final String DEFAULT_CATEGORY_SLUG = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY_SLUG = "BBBBBBBBBB";

    private static final String DEFAULT_TAG_SLUG = "AAAAAAAAAA";
    private static final String UPDATED_TAG_SLUG = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY_CODE = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_LANGUAGE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_LANGUAGE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_TARGET_QUEUE_CHANNEL = "AAAAAAAAAA";
    private static final String UPDATED_TARGET_QUEUE_CHANNEL = "BBBBBBBBBB";

    private static final Integer DEFAULT_CHANNEL_TOTAL_LEVEL = 1;
    private static final Integer UPDATED_CHANNEL_TOTAL_LEVEL = 2;

    private static final Integer DEFAULT_CHANNEL_ARCHIVE_LEVEL = 1;
    private static final Integer UPDATED_CHANNEL_ARCHIVE_LEVEL = 2;

    private static final FetchEngine DEFAULT_CHANNEL_FETCH_ENGINE = FetchEngine.SELENIUM;
    private static final FetchEngine UPDATED_CHANNEL_FETCH_ENGINE = FetchEngine.HTTP;

    @Autowired
    private ScrapeChannelRepository scrapeChannelRepository;
    @Mock
    private ScrapeChannelRepository scrapeChannelRepositoryMock;
    
    @Mock
    private ScrapeChannelService scrapeChannelServiceMock;

    @Autowired
    private ScrapeChannelService scrapeChannelService;

    /**
     * This repository is mocked in the io.github.scrapery.setting.repository.search test package.
     *
     * @see io.github.scrapery.setting.repository.search.ScrapeChannelSearchRepositoryMockConfiguration
     */
    @Autowired
    private ScrapeChannelSearchRepository mockScrapeChannelSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restScrapeChannelMockMvc;

    private ScrapeChannel scrapeChannel;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ScrapeChannelResource scrapeChannelResource = new ScrapeChannelResource(scrapeChannelService);
        this.restScrapeChannelMockMvc = MockMvcBuilders.standaloneSetup(scrapeChannelResource)
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
    public static ScrapeChannel createEntity() {
        ScrapeChannel scrapeChannel = new ScrapeChannel()
            .url(DEFAULT_URL)
            .contentType(DEFAULT_CONTENT_TYPE)
            .totalLevel(DEFAULT_TOTAL_LEVEL)
            .archiveLevel(DEFAULT_ARCHIVE_LEVEL)
            .fetchEngine(DEFAULT_FETCH_ENGINE)
            .category(DEFAULT_CATEGORY)
            .tag(DEFAULT_TAG)
            .categorySlug(DEFAULT_CATEGORY_SLUG)
            .tagSlug(DEFAULT_TAG_SLUG)
            .countryCode(DEFAULT_COUNTRY_CODE)
            .languageCode(DEFAULT_LANGUAGE_CODE)
            .targetQueueChannel(DEFAULT_TARGET_QUEUE_CHANNEL)
            .channelTotalLevel(DEFAULT_CHANNEL_TOTAL_LEVEL)
            .channelArchiveLevel(DEFAULT_CHANNEL_ARCHIVE_LEVEL)
            .channelFetchEngine(DEFAULT_CHANNEL_FETCH_ENGINE);
        return scrapeChannel;
    }

    @Before
    public void initTest() {
        scrapeChannelRepository.deleteAll();
        scrapeChannel = createEntity();
    }

    @Test
    public void createScrapeChannel() throws Exception {
        int databaseSizeBeforeCreate = scrapeChannelRepository.findAll().size();

        // Create the ScrapeChannel
        restScrapeChannelMockMvc.perform(post("/api/scrape-channels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(scrapeChannel)))
            .andExpect(status().isCreated());

        // Validate the ScrapeChannel in the database
        List<ScrapeChannel> scrapeChannelList = scrapeChannelRepository.findAll();
        assertThat(scrapeChannelList).hasSize(databaseSizeBeforeCreate + 1);
        ScrapeChannel testScrapeChannel = scrapeChannelList.get(scrapeChannelList.size() - 1);
        assertThat(testScrapeChannel.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testScrapeChannel.getContentType()).isEqualTo(DEFAULT_CONTENT_TYPE);
        assertThat(testScrapeChannel.getTotalLevel()).isEqualTo(DEFAULT_TOTAL_LEVEL);
        assertThat(testScrapeChannel.getArchiveLevel()).isEqualTo(DEFAULT_ARCHIVE_LEVEL);
        assertThat(testScrapeChannel.getFetchEngine()).isEqualTo(DEFAULT_FETCH_ENGINE);
        assertThat(testScrapeChannel.getCategory()).isEqualTo(DEFAULT_CATEGORY);
        assertThat(testScrapeChannel.getTag()).isEqualTo(DEFAULT_TAG);
        assertThat(testScrapeChannel.getCategorySlug()).isEqualTo(DEFAULT_CATEGORY_SLUG);
        assertThat(testScrapeChannel.getTagSlug()).isEqualTo(DEFAULT_TAG_SLUG);
        assertThat(testScrapeChannel.getCountryCode()).isEqualTo(DEFAULT_COUNTRY_CODE);
        assertThat(testScrapeChannel.getLanguageCode()).isEqualTo(DEFAULT_LANGUAGE_CODE);
        assertThat(testScrapeChannel.getTargetQueueChannel()).isEqualTo(DEFAULT_TARGET_QUEUE_CHANNEL);
        assertThat(testScrapeChannel.getChannelTotalLevel()).isEqualTo(DEFAULT_CHANNEL_TOTAL_LEVEL);
        assertThat(testScrapeChannel.getChannelArchiveLevel()).isEqualTo(DEFAULT_CHANNEL_ARCHIVE_LEVEL);
        assertThat(testScrapeChannel.getChannelFetchEngine()).isEqualTo(DEFAULT_CHANNEL_FETCH_ENGINE);

        // Validate the ScrapeChannel in Elasticsearch
        verify(mockScrapeChannelSearchRepository, times(1)).save(testScrapeChannel);
    }

    @Test
    public void createScrapeChannelWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = scrapeChannelRepository.findAll().size();

        // Create the ScrapeChannel with an existing ID
        scrapeChannel.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restScrapeChannelMockMvc.perform(post("/api/scrape-channels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(scrapeChannel)))
            .andExpect(status().isBadRequest());

        // Validate the ScrapeChannel in the database
        List<ScrapeChannel> scrapeChannelList = scrapeChannelRepository.findAll();
        assertThat(scrapeChannelList).hasSize(databaseSizeBeforeCreate);

        // Validate the ScrapeChannel in Elasticsearch
        verify(mockScrapeChannelSearchRepository, times(0)).save(scrapeChannel);
    }

    @Test
    public void getAllScrapeChannels() throws Exception {
        // Initialize the database
        scrapeChannelRepository.save(scrapeChannel);

        // Get all the scrapeChannelList
        restScrapeChannelMockMvc.perform(get("/api/scrape-channels?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(scrapeChannel.getId())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
            .andExpect(jsonPath("$.[*].contentType").value(hasItem(DEFAULT_CONTENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].totalLevel").value(hasItem(DEFAULT_TOTAL_LEVEL)))
            .andExpect(jsonPath("$.[*].archiveLevel").value(hasItem(DEFAULT_ARCHIVE_LEVEL)))
            .andExpect(jsonPath("$.[*].fetchEngine").value(hasItem(DEFAULT_FETCH_ENGINE.toString())))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].tag").value(hasItem(DEFAULT_TAG.toString())))
            .andExpect(jsonPath("$.[*].categorySlug").value(hasItem(DEFAULT_CATEGORY_SLUG.toString())))
            .andExpect(jsonPath("$.[*].tagSlug").value(hasItem(DEFAULT_TAG_SLUG.toString())))
            .andExpect(jsonPath("$.[*].countryCode").value(hasItem(DEFAULT_COUNTRY_CODE.toString())))
            .andExpect(jsonPath("$.[*].languageCode").value(hasItem(DEFAULT_LANGUAGE_CODE.toString())))
            .andExpect(jsonPath("$.[*].targetQueueChannel").value(hasItem(DEFAULT_TARGET_QUEUE_CHANNEL.toString())))
            .andExpect(jsonPath("$.[*].channelTotalLevel").value(hasItem(DEFAULT_CHANNEL_TOTAL_LEVEL)))
            .andExpect(jsonPath("$.[*].channelArchiveLevel").value(hasItem(DEFAULT_CHANNEL_ARCHIVE_LEVEL)))
            .andExpect(jsonPath("$.[*].channelFetchEngine").value(hasItem(DEFAULT_CHANNEL_FETCH_ENGINE.toString())));
    }
    
    public void getAllScrapeChannelsWithEagerRelationshipsIsEnabled() throws Exception {
        ScrapeChannelResource scrapeChannelResource = new ScrapeChannelResource(scrapeChannelServiceMock);
        when(scrapeChannelServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restScrapeChannelMockMvc = MockMvcBuilders.standaloneSetup(scrapeChannelResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restScrapeChannelMockMvc.perform(get("/api/scrape-channels?eagerload=true"))
        .andExpect(status().isOk());

        verify(scrapeChannelServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    public void getAllScrapeChannelsWithEagerRelationshipsIsNotEnabled() throws Exception {
        ScrapeChannelResource scrapeChannelResource = new ScrapeChannelResource(scrapeChannelServiceMock);
            when(scrapeChannelServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restScrapeChannelMockMvc = MockMvcBuilders.standaloneSetup(scrapeChannelResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restScrapeChannelMockMvc.perform(get("/api/scrape-channels?eagerload=true"))
        .andExpect(status().isOk());

            verify(scrapeChannelServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    public void getScrapeChannel() throws Exception {
        // Initialize the database
        scrapeChannelRepository.save(scrapeChannel);

        // Get the scrapeChannel
        restScrapeChannelMockMvc.perform(get("/api/scrape-channels/{id}", scrapeChannel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(scrapeChannel.getId()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()))
            .andExpect(jsonPath("$.contentType").value(DEFAULT_CONTENT_TYPE.toString()))
            .andExpect(jsonPath("$.totalLevel").value(DEFAULT_TOTAL_LEVEL))
            .andExpect(jsonPath("$.archiveLevel").value(DEFAULT_ARCHIVE_LEVEL))
            .andExpect(jsonPath("$.fetchEngine").value(DEFAULT_FETCH_ENGINE.toString()))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY.toString()))
            .andExpect(jsonPath("$.tag").value(DEFAULT_TAG.toString()))
            .andExpect(jsonPath("$.categorySlug").value(DEFAULT_CATEGORY_SLUG.toString()))
            .andExpect(jsonPath("$.tagSlug").value(DEFAULT_TAG_SLUG.toString()))
            .andExpect(jsonPath("$.countryCode").value(DEFAULT_COUNTRY_CODE.toString()))
            .andExpect(jsonPath("$.languageCode").value(DEFAULT_LANGUAGE_CODE.toString()))
            .andExpect(jsonPath("$.targetQueueChannel").value(DEFAULT_TARGET_QUEUE_CHANNEL.toString()))
            .andExpect(jsonPath("$.channelTotalLevel").value(DEFAULT_CHANNEL_TOTAL_LEVEL))
            .andExpect(jsonPath("$.channelArchiveLevel").value(DEFAULT_CHANNEL_ARCHIVE_LEVEL))
            .andExpect(jsonPath("$.channelFetchEngine").value(DEFAULT_CHANNEL_FETCH_ENGINE.toString()));
    }
    @Test
    public void getNonExistingScrapeChannel() throws Exception {
        // Get the scrapeChannel
        restScrapeChannelMockMvc.perform(get("/api/scrape-channels/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateScrapeChannel() throws Exception {
        // Initialize the database
        scrapeChannelService.save(scrapeChannel);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockScrapeChannelSearchRepository);

        int databaseSizeBeforeUpdate = scrapeChannelRepository.findAll().size();

        // Update the scrapeChannel
        ScrapeChannel updatedScrapeChannel = scrapeChannelRepository.findById(scrapeChannel.getId()).get();
        updatedScrapeChannel
            .url(UPDATED_URL)
            .contentType(UPDATED_CONTENT_TYPE)
            .totalLevel(UPDATED_TOTAL_LEVEL)
            .archiveLevel(UPDATED_ARCHIVE_LEVEL)
            .fetchEngine(UPDATED_FETCH_ENGINE)
            .category(UPDATED_CATEGORY)
            .tag(UPDATED_TAG)
            .categorySlug(UPDATED_CATEGORY_SLUG)
            .tagSlug(UPDATED_TAG_SLUG)
            .countryCode(UPDATED_COUNTRY_CODE)
            .languageCode(UPDATED_LANGUAGE_CODE)
            .targetQueueChannel(UPDATED_TARGET_QUEUE_CHANNEL)
            .channelTotalLevel(UPDATED_CHANNEL_TOTAL_LEVEL)
            .channelArchiveLevel(UPDATED_CHANNEL_ARCHIVE_LEVEL)
            .channelFetchEngine(UPDATED_CHANNEL_FETCH_ENGINE);

        restScrapeChannelMockMvc.perform(put("/api/scrape-channels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedScrapeChannel)))
            .andExpect(status().isOk());

        // Validate the ScrapeChannel in the database
        List<ScrapeChannel> scrapeChannelList = scrapeChannelRepository.findAll();
        assertThat(scrapeChannelList).hasSize(databaseSizeBeforeUpdate);
        ScrapeChannel testScrapeChannel = scrapeChannelList.get(scrapeChannelList.size() - 1);
        assertThat(testScrapeChannel.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testScrapeChannel.getContentType()).isEqualTo(UPDATED_CONTENT_TYPE);
        assertThat(testScrapeChannel.getTotalLevel()).isEqualTo(UPDATED_TOTAL_LEVEL);
        assertThat(testScrapeChannel.getArchiveLevel()).isEqualTo(UPDATED_ARCHIVE_LEVEL);
        assertThat(testScrapeChannel.getFetchEngine()).isEqualTo(UPDATED_FETCH_ENGINE);
        assertThat(testScrapeChannel.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testScrapeChannel.getTag()).isEqualTo(UPDATED_TAG);
        assertThat(testScrapeChannel.getCategorySlug()).isEqualTo(UPDATED_CATEGORY_SLUG);
        assertThat(testScrapeChannel.getTagSlug()).isEqualTo(UPDATED_TAG_SLUG);
        assertThat(testScrapeChannel.getCountryCode()).isEqualTo(UPDATED_COUNTRY_CODE);
        assertThat(testScrapeChannel.getLanguageCode()).isEqualTo(UPDATED_LANGUAGE_CODE);
        assertThat(testScrapeChannel.getTargetQueueChannel()).isEqualTo(UPDATED_TARGET_QUEUE_CHANNEL);
        assertThat(testScrapeChannel.getChannelTotalLevel()).isEqualTo(UPDATED_CHANNEL_TOTAL_LEVEL);
        assertThat(testScrapeChannel.getChannelArchiveLevel()).isEqualTo(UPDATED_CHANNEL_ARCHIVE_LEVEL);
        assertThat(testScrapeChannel.getChannelFetchEngine()).isEqualTo(UPDATED_CHANNEL_FETCH_ENGINE);

        // Validate the ScrapeChannel in Elasticsearch
        verify(mockScrapeChannelSearchRepository, times(1)).save(testScrapeChannel);
    }

    @Test
    public void updateNonExistingScrapeChannel() throws Exception {
        int databaseSizeBeforeUpdate = scrapeChannelRepository.findAll().size();

        // Create the ScrapeChannel

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restScrapeChannelMockMvc.perform(put("/api/scrape-channels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(scrapeChannel)))
            .andExpect(status().isBadRequest());

        // Validate the ScrapeChannel in the database
        List<ScrapeChannel> scrapeChannelList = scrapeChannelRepository.findAll();
        assertThat(scrapeChannelList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ScrapeChannel in Elasticsearch
        verify(mockScrapeChannelSearchRepository, times(0)).save(scrapeChannel);
    }

    @Test
    public void deleteScrapeChannel() throws Exception {
        // Initialize the database
        scrapeChannelService.save(scrapeChannel);

        int databaseSizeBeforeDelete = scrapeChannelRepository.findAll().size();

        // Get the scrapeChannel
        restScrapeChannelMockMvc.perform(delete("/api/scrape-channels/{id}", scrapeChannel.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ScrapeChannel> scrapeChannelList = scrapeChannelRepository.findAll();
        assertThat(scrapeChannelList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ScrapeChannel in Elasticsearch
        verify(mockScrapeChannelSearchRepository, times(1)).deleteById(scrapeChannel.getId());
    }

    @Test
    public void searchScrapeChannel() throws Exception {
        // Initialize the database
        scrapeChannelService.save(scrapeChannel);
        when(mockScrapeChannelSearchRepository.search(queryStringQuery("id:" + scrapeChannel.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(scrapeChannel), PageRequest.of(0, 1), 1));
        // Search the scrapeChannel
        restScrapeChannelMockMvc.perform(get("/api/_search/scrape-channels?query=id:" + scrapeChannel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(scrapeChannel.getId())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
            .andExpect(jsonPath("$.[*].contentType").value(hasItem(DEFAULT_CONTENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].totalLevel").value(hasItem(DEFAULT_TOTAL_LEVEL)))
            .andExpect(jsonPath("$.[*].archiveLevel").value(hasItem(DEFAULT_ARCHIVE_LEVEL)))
            .andExpect(jsonPath("$.[*].fetchEngine").value(hasItem(DEFAULT_FETCH_ENGINE.toString())))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].tag").value(hasItem(DEFAULT_TAG.toString())))
            .andExpect(jsonPath("$.[*].categorySlug").value(hasItem(DEFAULT_CATEGORY_SLUG.toString())))
            .andExpect(jsonPath("$.[*].tagSlug").value(hasItem(DEFAULT_TAG_SLUG.toString())))
            .andExpect(jsonPath("$.[*].countryCode").value(hasItem(DEFAULT_COUNTRY_CODE.toString())))
            .andExpect(jsonPath("$.[*].languageCode").value(hasItem(DEFAULT_LANGUAGE_CODE.toString())))
            .andExpect(jsonPath("$.[*].targetQueueChannel").value(hasItem(DEFAULT_TARGET_QUEUE_CHANNEL.toString())))
            .andExpect(jsonPath("$.[*].channelTotalLevel").value(hasItem(DEFAULT_CHANNEL_TOTAL_LEVEL)))
            .andExpect(jsonPath("$.[*].channelArchiveLevel").value(hasItem(DEFAULT_CHANNEL_ARCHIVE_LEVEL)))
            .andExpect(jsonPath("$.[*].channelFetchEngine").value(hasItem(DEFAULT_CHANNEL_FETCH_ENGINE.toString())));
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ScrapeChannel.class);
        ScrapeChannel scrapeChannel1 = new ScrapeChannel();
        scrapeChannel1.setId("id1");
        ScrapeChannel scrapeChannel2 = new ScrapeChannel();
        scrapeChannel2.setId(scrapeChannel1.getId());
        assertThat(scrapeChannel1).isEqualTo(scrapeChannel2);
        scrapeChannel2.setId("id2");
        assertThat(scrapeChannel1).isNotEqualTo(scrapeChannel2);
        scrapeChannel1.setId(null);
        assertThat(scrapeChannel1).isNotEqualTo(scrapeChannel2);
    }
}
