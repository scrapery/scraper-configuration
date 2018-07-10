package io.github.scrapery.setting.web.rest;

import io.github.scrapery.setting.ScraperSettingApp;

import io.github.scrapery.setting.domain.Channel;
import io.github.scrapery.setting.repository.ChannelRepository;
import io.github.scrapery.setting.repository.search.ChannelSearchRepository;
import io.github.scrapery.setting.service.ChannelService;
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
import io.github.scrapery.setting.domain.enumeration.PostType;
import io.github.scrapery.setting.domain.enumeration.FetchEngine;
import io.github.scrapery.setting.domain.enumeration.PostType;
import io.github.scrapery.setting.domain.enumeration.FetchEngine;
import io.github.scrapery.setting.domain.enumeration.FetchEngine;
import io.github.scrapery.setting.domain.enumeration.FetchEngine;
import io.github.scrapery.setting.domain.enumeration.FetchEngine;
import io.github.scrapery.setting.domain.enumeration.DocType;
import io.github.scrapery.setting.domain.enumeration.DocType;
import io.github.scrapery.setting.domain.enumeration.DocType;
import io.github.scrapery.setting.domain.enumeration.DocType;
import io.github.scrapery.setting.domain.enumeration.ChannelType;
import io.github.scrapery.setting.domain.enumeration.FetchEngine;
import io.github.scrapery.setting.domain.enumeration.FetchEngine;
import io.github.scrapery.setting.domain.enumeration.FetchEngine;
import io.github.scrapery.setting.domain.enumeration.FetchEngine;
import io.github.scrapery.setting.domain.enumeration.DocType;
import io.github.scrapery.setting.domain.enumeration.DocType;
import io.github.scrapery.setting.domain.enumeration.DocType;
import io.github.scrapery.setting.domain.enumeration.DocType;
import io.github.scrapery.setting.domain.enumeration.TargetChannel;
import io.github.scrapery.setting.domain.enumeration.TargetChannel;
/**
 * Test class for the ChannelResource REST controller.
 *
 * @see ChannelResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ScraperSettingApp.class)
public class ChannelResourceIntTest {

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final DocType DEFAULT_CONTENT_TYPE = DocType.HTML;
    private static final DocType UPDATED_CONTENT_TYPE = DocType.XML;

    private static final String DEFAULT_SCHEDULE = "AAAAAAAAAA";
    private static final String UPDATED_SCHEDULE = "BBBBBBBBBB";

    private static final String DEFAULT_SCHEDULE_TIME_ZONE = "AAAAAAAAAA";
    private static final String UPDATED_SCHEDULE_TIME_ZONE = "BBBBBBBBBB";

    private static final Integer DEFAULT_TOTAL_LEVEL = 1;
    private static final Integer UPDATED_TOTAL_LEVEL = 2;

    private static final Integer DEFAULT_ARCHIVE_LEVEL = 1;
    private static final Integer UPDATED_ARCHIVE_LEVEL = 2;

    private static final Boolean DEFAULT_UNLIMITED_LEVEL = false;
    private static final Boolean UPDATED_UNLIMITED_LEVEL = true;

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

    private static final String DEFAULT_TOPICS = "AAAAAAAAAA";
    private static final String UPDATED_TOPICS = "BBBBBBBBBB";

    private static final String DEFAULT_TOPIC_SLUGS = "AAAAAAAAAA";
    private static final String UPDATED_TOPIC_SLUGS = "BBBBBBBBBB";

    private static final PostType DEFAULT_POST_TYPE = PostType.ARTICLE;
    private static final PostType UPDATED_POST_TYPE = PostType.VIDEO;

    private static final Integer DEFAULT_RANKING_COUNTRY = 1;
    private static final Integer UPDATED_RANKING_COUNTRY = 2;

    private static final Integer DEFAULT_CHANNEL_TOTAL_LEVEL = 1;
    private static final Integer UPDATED_CHANNEL_TOTAL_LEVEL = 2;

    private static final Integer DEFAULT_CHANNEL_ARCHIVE_LEVEL = 1;
    private static final Integer UPDATED_CHANNEL_ARCHIVE_LEVEL = 2;

    private static final FetchEngine DEFAULT_CHANNEL_FETCH_ENGINE = FetchEngine.SELENIUM;
    private static final FetchEngine UPDATED_CHANNEL_FETCH_ENGINE = FetchEngine.HTTP;

    private static final Integer DEFAULT_CHANNEL_RANKING = 1;
    private static final Integer UPDATED_CHANNEL_RANKING = 2;

    private static final String DEFAULT_CHANNEL_TARGET_QUEUE = "AAAAAAAAAA";
    private static final String UPDATED_CHANNEL_TARGET_QUEUE = "BBBBBBBBBB";

    private static final PostType DEFAULT_CHANNEL_TARGET_POST_TYPE = PostType.ARTICLE;
    private static final PostType UPDATED_CHANNEL_TARGET_POST_TYPE = PostType.VIDEO;

    private static final FetchEngine DEFAULT_CHANNEL_LEVEL_ONE_FETCH_ENGINE = FetchEngine.SELENIUM;
    private static final FetchEngine UPDATED_CHANNEL_LEVEL_ONE_FETCH_ENGINE = FetchEngine.HTTP;

    private static final FetchEngine DEFAULT_CHANNEL_LEVEL_TWO_FETCH_ENGINE = FetchEngine.SELENIUM;
    private static final FetchEngine UPDATED_CHANNEL_LEVEL_TWO_FETCH_ENGINE = FetchEngine.HTTP;

    private static final FetchEngine DEFAULT_CHANNEL_LEVEL_THREE_FETCH_ENGINE = FetchEngine.SELENIUM;
    private static final FetchEngine UPDATED_CHANNEL_LEVEL_THREE_FETCH_ENGINE = FetchEngine.HTTP;

    private static final FetchEngine DEFAULT_CHANNEL_LEVEL_FOUR_FETCH_ENGINE = FetchEngine.SELENIUM;
    private static final FetchEngine UPDATED_CHANNEL_LEVEL_FOUR_FETCH_ENGINE = FetchEngine.HTTP;

    private static final DocType DEFAULT_CHANNEL_LEVEL_ONE_CONTENT_TYPE = DocType.HTML;
    private static final DocType UPDATED_CHANNEL_LEVEL_ONE_CONTENT_TYPE = DocType.XML;

    private static final DocType DEFAULT_CHANNEL_LEVEL_TWO_CONTENT_TYPE = DocType.HTML;
    private static final DocType UPDATED_CHANNEL_LEVEL_TWO_CONTENT_TYPE = DocType.XML;

    private static final DocType DEFAULT_CHANNEL_LEVEL_THREE_CONTENT_TYPE = DocType.HTML;
    private static final DocType UPDATED_CHANNEL_LEVEL_THREE_CONTENT_TYPE = DocType.XML;

    private static final DocType DEFAULT_CHANNEL_LEVEL_FOUR_CONTENT_TYPE = DocType.HTML;
    private static final DocType UPDATED_CHANNEL_LEVEL_FOUR_CONTENT_TYPE = DocType.XML;

    private static final Boolean DEFAULT_CHANNEL_ALLOW_EXTERNAL_URL = false;
    private static final Boolean UPDATED_CHANNEL_ALLOW_EXTERNAL_URL = true;

    private static final String DEFAULT_CHANNEL_LOGO = "AAAAAAAAAA";
    private static final String UPDATED_CHANNEL_LOGO = "BBBBBBBBBB";

    private static final String DEFAULT_CHANNEL_SITE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CHANNEL_SITE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LOGO = "AAAAAAAAAA";
    private static final String UPDATED_LOGO = "BBBBBBBBBB";

    private static final String DEFAULT_SITE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SITE_NAME = "BBBBBBBBBB";

    private static final ChannelType DEFAULT_CHANNEL_TYPE = ChannelType.CHANNEL_FOR_GETTING_CHANNEL;
    private static final ChannelType UPDATED_CHANNEL_TYPE = ChannelType.CHANNEL_FOR_GETTING_FEED;

    private static final FetchEngine DEFAULT_LEVEL_ONE_FETCH_ENGINE = FetchEngine.SELENIUM;
    private static final FetchEngine UPDATED_LEVEL_ONE_FETCH_ENGINE = FetchEngine.HTTP;

    private static final FetchEngine DEFAULT_LEVEL_TWO_FETCH_ENGINE = FetchEngine.SELENIUM;
    private static final FetchEngine UPDATED_LEVEL_TWO_FETCH_ENGINE = FetchEngine.HTTP;

    private static final FetchEngine DEFAULT_LEVEL_THREE_FETCH_ENGINE = FetchEngine.SELENIUM;
    private static final FetchEngine UPDATED_LEVEL_THREE_FETCH_ENGINE = FetchEngine.HTTP;

    private static final FetchEngine DEFAULT_LEVEL_FOUR_FETCH_ENGINE = FetchEngine.SELENIUM;
    private static final FetchEngine UPDATED_LEVEL_FOUR_FETCH_ENGINE = FetchEngine.HTTP;

    private static final DocType DEFAULT_LEVEL_ONE_CONTENT_TYPE = DocType.HTML;
    private static final DocType UPDATED_LEVEL_ONE_CONTENT_TYPE = DocType.XML;

    private static final DocType DEFAULT_LEVEL_TWO_CONTENT_TYPE = DocType.HTML;
    private static final DocType UPDATED_LEVEL_TWO_CONTENT_TYPE = DocType.XML;

    private static final DocType DEFAULT_LEVEL_THREE_CONTENT_TYPE = DocType.HTML;
    private static final DocType UPDATED_LEVEL_THREE_CONTENT_TYPE = DocType.XML;

    private static final DocType DEFAULT_LEVEL_FOUR_CONTENT_TYPE = DocType.HTML;
    private static final DocType UPDATED_LEVEL_FOUR_CONTENT_TYPE = DocType.XML;

    private static final Boolean DEFAULT_ALLOW_EXTERNAL_URL = false;
    private static final Boolean UPDATED_ALLOW_EXTERNAL_URL = true;

    private static final TargetChannel DEFAULT_TARGET_CHANNEL = TargetChannel.PUBLISHER;
    private static final TargetChannel UPDATED_TARGET_CHANNEL = TargetChannel.CATEGORY;

    private static final TargetChannel DEFAULT_TARGET = TargetChannel.PUBLISHER;
    private static final TargetChannel UPDATED_TARGET = TargetChannel.CATEGORY;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SITE_DOMAIN = "AAAAAAAAAA";
    private static final String UPDATED_SITE_DOMAIN = "BBBBBBBBBB";

    private static final String DEFAULT_SITE_URL = "AAAAAAAAAA";
    private static final String UPDATED_SITE_URL = "BBBBBBBBBB";

    @Autowired
    private ChannelRepository channelRepository;
    @Mock
    private ChannelRepository channelRepositoryMock;
    
    @Mock
    private ChannelService channelServiceMock;

    @Autowired
    private ChannelService channelService;

    /**
     * This repository is mocked in the io.github.scrapery.setting.repository.search test package.
     *
     * @see io.github.scrapery.setting.repository.search.ChannelSearchRepositoryMockConfiguration
     */
    @Autowired
    private ChannelSearchRepository mockChannelSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restChannelMockMvc;

    private Channel channel;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ChannelResource channelResource = new ChannelResource(channelService);
        this.restChannelMockMvc = MockMvcBuilders.standaloneSetup(channelResource)
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
    public static Channel createEntity() {
        Channel channel = new Channel()
            .url(DEFAULT_URL)
            .contentType(DEFAULT_CONTENT_TYPE)
            .schedule(DEFAULT_SCHEDULE)
            .scheduleTimeZone(DEFAULT_SCHEDULE_TIME_ZONE)
            .totalLevel(DEFAULT_TOTAL_LEVEL)
            .archiveLevel(DEFAULT_ARCHIVE_LEVEL)
            .unlimitedLevel(DEFAULT_UNLIMITED_LEVEL)
            .fetchEngine(DEFAULT_FETCH_ENGINE)
            .category(DEFAULT_CATEGORY)
            .tag(DEFAULT_TAG)
            .categorySlug(DEFAULT_CATEGORY_SLUG)
            .tagSlug(DEFAULT_TAG_SLUG)
            .countryCode(DEFAULT_COUNTRY_CODE)
            .languageCode(DEFAULT_LANGUAGE_CODE)
            .targetQueueChannel(DEFAULT_TARGET_QUEUE_CHANNEL)
            .topics(DEFAULT_TOPICS)
            .topicSlugs(DEFAULT_TOPIC_SLUGS)
            .postType(DEFAULT_POST_TYPE)
            .rankingCountry(DEFAULT_RANKING_COUNTRY)
            .channelTotalLevel(DEFAULT_CHANNEL_TOTAL_LEVEL)
            .channelArchiveLevel(DEFAULT_CHANNEL_ARCHIVE_LEVEL)
            .channelFetchEngine(DEFAULT_CHANNEL_FETCH_ENGINE)
            .channelRanking(DEFAULT_CHANNEL_RANKING)
            .channelTargetQueue(DEFAULT_CHANNEL_TARGET_QUEUE)
            .channelTargetPostType(DEFAULT_CHANNEL_TARGET_POST_TYPE)
            .channelLevelOneFetchEngine(DEFAULT_CHANNEL_LEVEL_ONE_FETCH_ENGINE)
            .channelLevelTwoFetchEngine(DEFAULT_CHANNEL_LEVEL_TWO_FETCH_ENGINE)
            .channelLevelThreeFetchEngine(DEFAULT_CHANNEL_LEVEL_THREE_FETCH_ENGINE)
            .channelLevelFourFetchEngine(DEFAULT_CHANNEL_LEVEL_FOUR_FETCH_ENGINE)
            .channelLevelOneContentType(DEFAULT_CHANNEL_LEVEL_ONE_CONTENT_TYPE)
            .channelLevelTwoContentType(DEFAULT_CHANNEL_LEVEL_TWO_CONTENT_TYPE)
            .channelLevelThreeContentType(DEFAULT_CHANNEL_LEVEL_THREE_CONTENT_TYPE)
            .channelLevelFourContentType(DEFAULT_CHANNEL_LEVEL_FOUR_CONTENT_TYPE)
            .channelAllowExternalUrl(DEFAULT_CHANNEL_ALLOW_EXTERNAL_URL)
            .channelLogo(DEFAULT_CHANNEL_LOGO)
            .channelSiteName(DEFAULT_CHANNEL_SITE_NAME)
            .logo(DEFAULT_LOGO)
            .siteName(DEFAULT_SITE_NAME)
            .channelType(DEFAULT_CHANNEL_TYPE)
            .levelOneFetchEngine(DEFAULT_LEVEL_ONE_FETCH_ENGINE)
            .levelTwoFetchEngine(DEFAULT_LEVEL_TWO_FETCH_ENGINE)
            .levelThreeFetchEngine(DEFAULT_LEVEL_THREE_FETCH_ENGINE)
            .levelFourFetchEngine(DEFAULT_LEVEL_FOUR_FETCH_ENGINE)
            .levelOneContentType(DEFAULT_LEVEL_ONE_CONTENT_TYPE)
            .levelTwoContentType(DEFAULT_LEVEL_TWO_CONTENT_TYPE)
            .levelThreeContentType(DEFAULT_LEVEL_THREE_CONTENT_TYPE)
            .levelFourContentType(DEFAULT_LEVEL_FOUR_CONTENT_TYPE)
            .allowExternalUrl(DEFAULT_ALLOW_EXTERNAL_URL)
            .targetChannel(DEFAULT_TARGET_CHANNEL)
            .target(DEFAULT_TARGET)
            .name(DEFAULT_NAME)
            .siteDomain(DEFAULT_SITE_DOMAIN)
            .siteUrl(DEFAULT_SITE_URL);
        return channel;
    }

    @Before
    public void initTest() {
        channelRepository.deleteAll();
        channel = createEntity();
    }

    @Test
    public void createChannel() throws Exception {
        int databaseSizeBeforeCreate = channelRepository.findAll().size();

        // Create the Channel
        restChannelMockMvc.perform(post("/api/channels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(channel)))
            .andExpect(status().isCreated());

        // Validate the Channel in the database
        List<Channel> channelList = channelRepository.findAll();
        assertThat(channelList).hasSize(databaseSizeBeforeCreate + 1);
        Channel testChannel = channelList.get(channelList.size() - 1);
        assertThat(testChannel.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testChannel.getContentType()).isEqualTo(DEFAULT_CONTENT_TYPE);
        assertThat(testChannel.getSchedule()).isEqualTo(DEFAULT_SCHEDULE);
        assertThat(testChannel.getScheduleTimeZone()).isEqualTo(DEFAULT_SCHEDULE_TIME_ZONE);
        assertThat(testChannel.getTotalLevel()).isEqualTo(DEFAULT_TOTAL_LEVEL);
        assertThat(testChannel.getArchiveLevel()).isEqualTo(DEFAULT_ARCHIVE_LEVEL);
        assertThat(testChannel.isUnlimitedLevel()).isEqualTo(DEFAULT_UNLIMITED_LEVEL);
        assertThat(testChannel.getFetchEngine()).isEqualTo(DEFAULT_FETCH_ENGINE);
        assertThat(testChannel.getCategory()).isEqualTo(DEFAULT_CATEGORY);
        assertThat(testChannel.getTag()).isEqualTo(DEFAULT_TAG);
        assertThat(testChannel.getCategorySlug()).isEqualTo(DEFAULT_CATEGORY_SLUG);
        assertThat(testChannel.getTagSlug()).isEqualTo(DEFAULT_TAG_SLUG);
        assertThat(testChannel.getCountryCode()).isEqualTo(DEFAULT_COUNTRY_CODE);
        assertThat(testChannel.getLanguageCode()).isEqualTo(DEFAULT_LANGUAGE_CODE);
        assertThat(testChannel.getTargetQueueChannel()).isEqualTo(DEFAULT_TARGET_QUEUE_CHANNEL);
        assertThat(testChannel.getTopics()).isEqualTo(DEFAULT_TOPICS);
        assertThat(testChannel.getTopicSlugs()).isEqualTo(DEFAULT_TOPIC_SLUGS);
        assertThat(testChannel.getPostType()).isEqualTo(DEFAULT_POST_TYPE);
        assertThat(testChannel.getRankingCountry()).isEqualTo(DEFAULT_RANKING_COUNTRY);
        assertThat(testChannel.getChannelTotalLevel()).isEqualTo(DEFAULT_CHANNEL_TOTAL_LEVEL);
        assertThat(testChannel.getChannelArchiveLevel()).isEqualTo(DEFAULT_CHANNEL_ARCHIVE_LEVEL);
        assertThat(testChannel.getChannelFetchEngine()).isEqualTo(DEFAULT_CHANNEL_FETCH_ENGINE);
        assertThat(testChannel.getChannelRanking()).isEqualTo(DEFAULT_CHANNEL_RANKING);
        assertThat(testChannel.getChannelTargetQueue()).isEqualTo(DEFAULT_CHANNEL_TARGET_QUEUE);
        assertThat(testChannel.getChannelTargetPostType()).isEqualTo(DEFAULT_CHANNEL_TARGET_POST_TYPE);
        assertThat(testChannel.getChannelLevelOneFetchEngine()).isEqualTo(DEFAULT_CHANNEL_LEVEL_ONE_FETCH_ENGINE);
        assertThat(testChannel.getChannelLevelTwoFetchEngine()).isEqualTo(DEFAULT_CHANNEL_LEVEL_TWO_FETCH_ENGINE);
        assertThat(testChannel.getChannelLevelThreeFetchEngine()).isEqualTo(DEFAULT_CHANNEL_LEVEL_THREE_FETCH_ENGINE);
        assertThat(testChannel.getChannelLevelFourFetchEngine()).isEqualTo(DEFAULT_CHANNEL_LEVEL_FOUR_FETCH_ENGINE);
        assertThat(testChannel.getChannelLevelOneContentType()).isEqualTo(DEFAULT_CHANNEL_LEVEL_ONE_CONTENT_TYPE);
        assertThat(testChannel.getChannelLevelTwoContentType()).isEqualTo(DEFAULT_CHANNEL_LEVEL_TWO_CONTENT_TYPE);
        assertThat(testChannel.getChannelLevelThreeContentType()).isEqualTo(DEFAULT_CHANNEL_LEVEL_THREE_CONTENT_TYPE);
        assertThat(testChannel.getChannelLevelFourContentType()).isEqualTo(DEFAULT_CHANNEL_LEVEL_FOUR_CONTENT_TYPE);
        assertThat(testChannel.isChannelAllowExternalUrl()).isEqualTo(DEFAULT_CHANNEL_ALLOW_EXTERNAL_URL);
        assertThat(testChannel.getChannelLogo()).isEqualTo(DEFAULT_CHANNEL_LOGO);
        assertThat(testChannel.getChannelSiteName()).isEqualTo(DEFAULT_CHANNEL_SITE_NAME);
        assertThat(testChannel.getLogo()).isEqualTo(DEFAULT_LOGO);
        assertThat(testChannel.getSiteName()).isEqualTo(DEFAULT_SITE_NAME);
        assertThat(testChannel.getChannelType()).isEqualTo(DEFAULT_CHANNEL_TYPE);
        assertThat(testChannel.getLevelOneFetchEngine()).isEqualTo(DEFAULT_LEVEL_ONE_FETCH_ENGINE);
        assertThat(testChannel.getLevelTwoFetchEngine()).isEqualTo(DEFAULT_LEVEL_TWO_FETCH_ENGINE);
        assertThat(testChannel.getLevelThreeFetchEngine()).isEqualTo(DEFAULT_LEVEL_THREE_FETCH_ENGINE);
        assertThat(testChannel.getLevelFourFetchEngine()).isEqualTo(DEFAULT_LEVEL_FOUR_FETCH_ENGINE);
        assertThat(testChannel.getLevelOneContentType()).isEqualTo(DEFAULT_LEVEL_ONE_CONTENT_TYPE);
        assertThat(testChannel.getLevelTwoContentType()).isEqualTo(DEFAULT_LEVEL_TWO_CONTENT_TYPE);
        assertThat(testChannel.getLevelThreeContentType()).isEqualTo(DEFAULT_LEVEL_THREE_CONTENT_TYPE);
        assertThat(testChannel.getLevelFourContentType()).isEqualTo(DEFAULT_LEVEL_FOUR_CONTENT_TYPE);
        assertThat(testChannel.isAllowExternalUrl()).isEqualTo(DEFAULT_ALLOW_EXTERNAL_URL);
        assertThat(testChannel.getTargetChannel()).isEqualTo(DEFAULT_TARGET_CHANNEL);
        assertThat(testChannel.getTarget()).isEqualTo(DEFAULT_TARGET);
        assertThat(testChannel.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testChannel.getSiteDomain()).isEqualTo(DEFAULT_SITE_DOMAIN);
        assertThat(testChannel.getSiteUrl()).isEqualTo(DEFAULT_SITE_URL);

        // Validate the Channel in Elasticsearch
        verify(mockChannelSearchRepository, times(1)).save(testChannel);
    }

    @Test
    public void createChannelWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = channelRepository.findAll().size();

        // Create the Channel with an existing ID
        channel.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restChannelMockMvc.perform(post("/api/channels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(channel)))
            .andExpect(status().isBadRequest());

        // Validate the Channel in the database
        List<Channel> channelList = channelRepository.findAll();
        assertThat(channelList).hasSize(databaseSizeBeforeCreate);

        // Validate the Channel in Elasticsearch
        verify(mockChannelSearchRepository, times(0)).save(channel);
    }

    @Test
    public void getAllChannels() throws Exception {
        // Initialize the database
        channelRepository.save(channel);

        // Get all the channelList
        restChannelMockMvc.perform(get("/api/channels?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(channel.getId())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
            .andExpect(jsonPath("$.[*].contentType").value(hasItem(DEFAULT_CONTENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].schedule").value(hasItem(DEFAULT_SCHEDULE.toString())))
            .andExpect(jsonPath("$.[*].scheduleTimeZone").value(hasItem(DEFAULT_SCHEDULE_TIME_ZONE.toString())))
            .andExpect(jsonPath("$.[*].totalLevel").value(hasItem(DEFAULT_TOTAL_LEVEL)))
            .andExpect(jsonPath("$.[*].archiveLevel").value(hasItem(DEFAULT_ARCHIVE_LEVEL)))
            .andExpect(jsonPath("$.[*].unlimitedLevel").value(hasItem(DEFAULT_UNLIMITED_LEVEL.booleanValue())))
            .andExpect(jsonPath("$.[*].fetchEngine").value(hasItem(DEFAULT_FETCH_ENGINE.toString())))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].tag").value(hasItem(DEFAULT_TAG.toString())))
            .andExpect(jsonPath("$.[*].categorySlug").value(hasItem(DEFAULT_CATEGORY_SLUG.toString())))
            .andExpect(jsonPath("$.[*].tagSlug").value(hasItem(DEFAULT_TAG_SLUG.toString())))
            .andExpect(jsonPath("$.[*].countryCode").value(hasItem(DEFAULT_COUNTRY_CODE.toString())))
            .andExpect(jsonPath("$.[*].languageCode").value(hasItem(DEFAULT_LANGUAGE_CODE.toString())))
            .andExpect(jsonPath("$.[*].targetQueueChannel").value(hasItem(DEFAULT_TARGET_QUEUE_CHANNEL.toString())))
            .andExpect(jsonPath("$.[*].topics").value(hasItem(DEFAULT_TOPICS.toString())))
            .andExpect(jsonPath("$.[*].topicSlugs").value(hasItem(DEFAULT_TOPIC_SLUGS.toString())))
            .andExpect(jsonPath("$.[*].postType").value(hasItem(DEFAULT_POST_TYPE.toString())))
            .andExpect(jsonPath("$.[*].rankingCountry").value(hasItem(DEFAULT_RANKING_COUNTRY)))
            .andExpect(jsonPath("$.[*].channelTotalLevel").value(hasItem(DEFAULT_CHANNEL_TOTAL_LEVEL)))
            .andExpect(jsonPath("$.[*].channelArchiveLevel").value(hasItem(DEFAULT_CHANNEL_ARCHIVE_LEVEL)))
            .andExpect(jsonPath("$.[*].channelFetchEngine").value(hasItem(DEFAULT_CHANNEL_FETCH_ENGINE.toString())))
            .andExpect(jsonPath("$.[*].channelRanking").value(hasItem(DEFAULT_CHANNEL_RANKING)))
            .andExpect(jsonPath("$.[*].channelTargetQueue").value(hasItem(DEFAULT_CHANNEL_TARGET_QUEUE.toString())))
            .andExpect(jsonPath("$.[*].channelTargetPostType").value(hasItem(DEFAULT_CHANNEL_TARGET_POST_TYPE.toString())))
            .andExpect(jsonPath("$.[*].channelLevelOneFetchEngine").value(hasItem(DEFAULT_CHANNEL_LEVEL_ONE_FETCH_ENGINE.toString())))
            .andExpect(jsonPath("$.[*].channelLevelTwoFetchEngine").value(hasItem(DEFAULT_CHANNEL_LEVEL_TWO_FETCH_ENGINE.toString())))
            .andExpect(jsonPath("$.[*].channelLevelThreeFetchEngine").value(hasItem(DEFAULT_CHANNEL_LEVEL_THREE_FETCH_ENGINE.toString())))
            .andExpect(jsonPath("$.[*].channelLevelFourFetchEngine").value(hasItem(DEFAULT_CHANNEL_LEVEL_FOUR_FETCH_ENGINE.toString())))
            .andExpect(jsonPath("$.[*].channelLevelOneContentType").value(hasItem(DEFAULT_CHANNEL_LEVEL_ONE_CONTENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].channelLevelTwoContentType").value(hasItem(DEFAULT_CHANNEL_LEVEL_TWO_CONTENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].channelLevelThreeContentType").value(hasItem(DEFAULT_CHANNEL_LEVEL_THREE_CONTENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].channelLevelFourContentType").value(hasItem(DEFAULT_CHANNEL_LEVEL_FOUR_CONTENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].channelAllowExternalUrl").value(hasItem(DEFAULT_CHANNEL_ALLOW_EXTERNAL_URL.booleanValue())))
            .andExpect(jsonPath("$.[*].channelLogo").value(hasItem(DEFAULT_CHANNEL_LOGO.toString())))
            .andExpect(jsonPath("$.[*].channelSiteName").value(hasItem(DEFAULT_CHANNEL_SITE_NAME.toString())))
            .andExpect(jsonPath("$.[*].logo").value(hasItem(DEFAULT_LOGO.toString())))
            .andExpect(jsonPath("$.[*].siteName").value(hasItem(DEFAULT_SITE_NAME.toString())))
            .andExpect(jsonPath("$.[*].channelType").value(hasItem(DEFAULT_CHANNEL_TYPE.toString())))
            .andExpect(jsonPath("$.[*].levelOneFetchEngine").value(hasItem(DEFAULT_LEVEL_ONE_FETCH_ENGINE.toString())))
            .andExpect(jsonPath("$.[*].levelTwoFetchEngine").value(hasItem(DEFAULT_LEVEL_TWO_FETCH_ENGINE.toString())))
            .andExpect(jsonPath("$.[*].levelThreeFetchEngine").value(hasItem(DEFAULT_LEVEL_THREE_FETCH_ENGINE.toString())))
            .andExpect(jsonPath("$.[*].levelFourFetchEngine").value(hasItem(DEFAULT_LEVEL_FOUR_FETCH_ENGINE.toString())))
            .andExpect(jsonPath("$.[*].levelOneContentType").value(hasItem(DEFAULT_LEVEL_ONE_CONTENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].levelTwoContentType").value(hasItem(DEFAULT_LEVEL_TWO_CONTENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].levelThreeContentType").value(hasItem(DEFAULT_LEVEL_THREE_CONTENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].levelFourContentType").value(hasItem(DEFAULT_LEVEL_FOUR_CONTENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].allowExternalUrl").value(hasItem(DEFAULT_ALLOW_EXTERNAL_URL.booleanValue())))
            .andExpect(jsonPath("$.[*].targetChannel").value(hasItem(DEFAULT_TARGET_CHANNEL.toString())))
            .andExpect(jsonPath("$.[*].target").value(hasItem(DEFAULT_TARGET.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].siteDomain").value(hasItem(DEFAULT_SITE_DOMAIN.toString())))
            .andExpect(jsonPath("$.[*].siteUrl").value(hasItem(DEFAULT_SITE_URL.toString())));
    }
    
    public void getAllChannelsWithEagerRelationshipsIsEnabled() throws Exception {
        ChannelResource channelResource = new ChannelResource(channelServiceMock);
        when(channelServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restChannelMockMvc = MockMvcBuilders.standaloneSetup(channelResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restChannelMockMvc.perform(get("/api/channels?eagerload=true"))
        .andExpect(status().isOk());

        verify(channelServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    public void getAllChannelsWithEagerRelationshipsIsNotEnabled() throws Exception {
        ChannelResource channelResource = new ChannelResource(channelServiceMock);
            when(channelServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restChannelMockMvc = MockMvcBuilders.standaloneSetup(channelResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restChannelMockMvc.perform(get("/api/channels?eagerload=true"))
        .andExpect(status().isOk());

            verify(channelServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    public void getChannel() throws Exception {
        // Initialize the database
        channelRepository.save(channel);

        // Get the channel
        restChannelMockMvc.perform(get("/api/channels/{id}", channel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(channel.getId()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()))
            .andExpect(jsonPath("$.contentType").value(DEFAULT_CONTENT_TYPE.toString()))
            .andExpect(jsonPath("$.schedule").value(DEFAULT_SCHEDULE.toString()))
            .andExpect(jsonPath("$.scheduleTimeZone").value(DEFAULT_SCHEDULE_TIME_ZONE.toString()))
            .andExpect(jsonPath("$.totalLevel").value(DEFAULT_TOTAL_LEVEL))
            .andExpect(jsonPath("$.archiveLevel").value(DEFAULT_ARCHIVE_LEVEL))
            .andExpect(jsonPath("$.unlimitedLevel").value(DEFAULT_UNLIMITED_LEVEL.booleanValue()))
            .andExpect(jsonPath("$.fetchEngine").value(DEFAULT_FETCH_ENGINE.toString()))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY.toString()))
            .andExpect(jsonPath("$.tag").value(DEFAULT_TAG.toString()))
            .andExpect(jsonPath("$.categorySlug").value(DEFAULT_CATEGORY_SLUG.toString()))
            .andExpect(jsonPath("$.tagSlug").value(DEFAULT_TAG_SLUG.toString()))
            .andExpect(jsonPath("$.countryCode").value(DEFAULT_COUNTRY_CODE.toString()))
            .andExpect(jsonPath("$.languageCode").value(DEFAULT_LANGUAGE_CODE.toString()))
            .andExpect(jsonPath("$.targetQueueChannel").value(DEFAULT_TARGET_QUEUE_CHANNEL.toString()))
            .andExpect(jsonPath("$.topics").value(DEFAULT_TOPICS.toString()))
            .andExpect(jsonPath("$.topicSlugs").value(DEFAULT_TOPIC_SLUGS.toString()))
            .andExpect(jsonPath("$.postType").value(DEFAULT_POST_TYPE.toString()))
            .andExpect(jsonPath("$.rankingCountry").value(DEFAULT_RANKING_COUNTRY))
            .andExpect(jsonPath("$.channelTotalLevel").value(DEFAULT_CHANNEL_TOTAL_LEVEL))
            .andExpect(jsonPath("$.channelArchiveLevel").value(DEFAULT_CHANNEL_ARCHIVE_LEVEL))
            .andExpect(jsonPath("$.channelFetchEngine").value(DEFAULT_CHANNEL_FETCH_ENGINE.toString()))
            .andExpect(jsonPath("$.channelRanking").value(DEFAULT_CHANNEL_RANKING))
            .andExpect(jsonPath("$.channelTargetQueue").value(DEFAULT_CHANNEL_TARGET_QUEUE.toString()))
            .andExpect(jsonPath("$.channelTargetPostType").value(DEFAULT_CHANNEL_TARGET_POST_TYPE.toString()))
            .andExpect(jsonPath("$.channelLevelOneFetchEngine").value(DEFAULT_CHANNEL_LEVEL_ONE_FETCH_ENGINE.toString()))
            .andExpect(jsonPath("$.channelLevelTwoFetchEngine").value(DEFAULT_CHANNEL_LEVEL_TWO_FETCH_ENGINE.toString()))
            .andExpect(jsonPath("$.channelLevelThreeFetchEngine").value(DEFAULT_CHANNEL_LEVEL_THREE_FETCH_ENGINE.toString()))
            .andExpect(jsonPath("$.channelLevelFourFetchEngine").value(DEFAULT_CHANNEL_LEVEL_FOUR_FETCH_ENGINE.toString()))
            .andExpect(jsonPath("$.channelLevelOneContentType").value(DEFAULT_CHANNEL_LEVEL_ONE_CONTENT_TYPE.toString()))
            .andExpect(jsonPath("$.channelLevelTwoContentType").value(DEFAULT_CHANNEL_LEVEL_TWO_CONTENT_TYPE.toString()))
            .andExpect(jsonPath("$.channelLevelThreeContentType").value(DEFAULT_CHANNEL_LEVEL_THREE_CONTENT_TYPE.toString()))
            .andExpect(jsonPath("$.channelLevelFourContentType").value(DEFAULT_CHANNEL_LEVEL_FOUR_CONTENT_TYPE.toString()))
            .andExpect(jsonPath("$.channelAllowExternalUrl").value(DEFAULT_CHANNEL_ALLOW_EXTERNAL_URL.booleanValue()))
            .andExpect(jsonPath("$.channelLogo").value(DEFAULT_CHANNEL_LOGO.toString()))
            .andExpect(jsonPath("$.channelSiteName").value(DEFAULT_CHANNEL_SITE_NAME.toString()))
            .andExpect(jsonPath("$.logo").value(DEFAULT_LOGO.toString()))
            .andExpect(jsonPath("$.siteName").value(DEFAULT_SITE_NAME.toString()))
            .andExpect(jsonPath("$.channelType").value(DEFAULT_CHANNEL_TYPE.toString()))
            .andExpect(jsonPath("$.levelOneFetchEngine").value(DEFAULT_LEVEL_ONE_FETCH_ENGINE.toString()))
            .andExpect(jsonPath("$.levelTwoFetchEngine").value(DEFAULT_LEVEL_TWO_FETCH_ENGINE.toString()))
            .andExpect(jsonPath("$.levelThreeFetchEngine").value(DEFAULT_LEVEL_THREE_FETCH_ENGINE.toString()))
            .andExpect(jsonPath("$.levelFourFetchEngine").value(DEFAULT_LEVEL_FOUR_FETCH_ENGINE.toString()))
            .andExpect(jsonPath("$.levelOneContentType").value(DEFAULT_LEVEL_ONE_CONTENT_TYPE.toString()))
            .andExpect(jsonPath("$.levelTwoContentType").value(DEFAULT_LEVEL_TWO_CONTENT_TYPE.toString()))
            .andExpect(jsonPath("$.levelThreeContentType").value(DEFAULT_LEVEL_THREE_CONTENT_TYPE.toString()))
            .andExpect(jsonPath("$.levelFourContentType").value(DEFAULT_LEVEL_FOUR_CONTENT_TYPE.toString()))
            .andExpect(jsonPath("$.allowExternalUrl").value(DEFAULT_ALLOW_EXTERNAL_URL.booleanValue()))
            .andExpect(jsonPath("$.targetChannel").value(DEFAULT_TARGET_CHANNEL.toString()))
            .andExpect(jsonPath("$.target").value(DEFAULT_TARGET.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.siteDomain").value(DEFAULT_SITE_DOMAIN.toString()))
            .andExpect(jsonPath("$.siteUrl").value(DEFAULT_SITE_URL.toString()));
    }
    @Test
    public void getNonExistingChannel() throws Exception {
        // Get the channel
        restChannelMockMvc.perform(get("/api/channels/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateChannel() throws Exception {
        // Initialize the database
        channelService.save(channel);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockChannelSearchRepository);

        int databaseSizeBeforeUpdate = channelRepository.findAll().size();

        // Update the channel
        Channel updatedChannel = channelRepository.findById(channel.getId()).get();
        updatedChannel
            .url(UPDATED_URL)
            .contentType(UPDATED_CONTENT_TYPE)
            .schedule(UPDATED_SCHEDULE)
            .scheduleTimeZone(UPDATED_SCHEDULE_TIME_ZONE)
            .totalLevel(UPDATED_TOTAL_LEVEL)
            .archiveLevel(UPDATED_ARCHIVE_LEVEL)
            .unlimitedLevel(UPDATED_UNLIMITED_LEVEL)
            .fetchEngine(UPDATED_FETCH_ENGINE)
            .category(UPDATED_CATEGORY)
            .tag(UPDATED_TAG)
            .categorySlug(UPDATED_CATEGORY_SLUG)
            .tagSlug(UPDATED_TAG_SLUG)
            .countryCode(UPDATED_COUNTRY_CODE)
            .languageCode(UPDATED_LANGUAGE_CODE)
            .targetQueueChannel(UPDATED_TARGET_QUEUE_CHANNEL)
            .topics(UPDATED_TOPICS)
            .topicSlugs(UPDATED_TOPIC_SLUGS)
            .postType(UPDATED_POST_TYPE)
            .rankingCountry(UPDATED_RANKING_COUNTRY)
            .channelTotalLevel(UPDATED_CHANNEL_TOTAL_LEVEL)
            .channelArchiveLevel(UPDATED_CHANNEL_ARCHIVE_LEVEL)
            .channelFetchEngine(UPDATED_CHANNEL_FETCH_ENGINE)
            .channelRanking(UPDATED_CHANNEL_RANKING)
            .channelTargetQueue(UPDATED_CHANNEL_TARGET_QUEUE)
            .channelTargetPostType(UPDATED_CHANNEL_TARGET_POST_TYPE)
            .channelLevelOneFetchEngine(UPDATED_CHANNEL_LEVEL_ONE_FETCH_ENGINE)
            .channelLevelTwoFetchEngine(UPDATED_CHANNEL_LEVEL_TWO_FETCH_ENGINE)
            .channelLevelThreeFetchEngine(UPDATED_CHANNEL_LEVEL_THREE_FETCH_ENGINE)
            .channelLevelFourFetchEngine(UPDATED_CHANNEL_LEVEL_FOUR_FETCH_ENGINE)
            .channelLevelOneContentType(UPDATED_CHANNEL_LEVEL_ONE_CONTENT_TYPE)
            .channelLevelTwoContentType(UPDATED_CHANNEL_LEVEL_TWO_CONTENT_TYPE)
            .channelLevelThreeContentType(UPDATED_CHANNEL_LEVEL_THREE_CONTENT_TYPE)
            .channelLevelFourContentType(UPDATED_CHANNEL_LEVEL_FOUR_CONTENT_TYPE)
            .channelAllowExternalUrl(UPDATED_CHANNEL_ALLOW_EXTERNAL_URL)
            .channelLogo(UPDATED_CHANNEL_LOGO)
            .channelSiteName(UPDATED_CHANNEL_SITE_NAME)
            .logo(UPDATED_LOGO)
            .siteName(UPDATED_SITE_NAME)
            .channelType(UPDATED_CHANNEL_TYPE)
            .levelOneFetchEngine(UPDATED_LEVEL_ONE_FETCH_ENGINE)
            .levelTwoFetchEngine(UPDATED_LEVEL_TWO_FETCH_ENGINE)
            .levelThreeFetchEngine(UPDATED_LEVEL_THREE_FETCH_ENGINE)
            .levelFourFetchEngine(UPDATED_LEVEL_FOUR_FETCH_ENGINE)
            .levelOneContentType(UPDATED_LEVEL_ONE_CONTENT_TYPE)
            .levelTwoContentType(UPDATED_LEVEL_TWO_CONTENT_TYPE)
            .levelThreeContentType(UPDATED_LEVEL_THREE_CONTENT_TYPE)
            .levelFourContentType(UPDATED_LEVEL_FOUR_CONTENT_TYPE)
            .allowExternalUrl(UPDATED_ALLOW_EXTERNAL_URL)
            .targetChannel(UPDATED_TARGET_CHANNEL)
            .target(UPDATED_TARGET)
            .name(UPDATED_NAME)
            .siteDomain(UPDATED_SITE_DOMAIN)
            .siteUrl(UPDATED_SITE_URL);

        restChannelMockMvc.perform(put("/api/channels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedChannel)))
            .andExpect(status().isOk());

        // Validate the Channel in the database
        List<Channel> channelList = channelRepository.findAll();
        assertThat(channelList).hasSize(databaseSizeBeforeUpdate);
        Channel testChannel = channelList.get(channelList.size() - 1);
        assertThat(testChannel.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testChannel.getContentType()).isEqualTo(UPDATED_CONTENT_TYPE);
        assertThat(testChannel.getSchedule()).isEqualTo(UPDATED_SCHEDULE);
        assertThat(testChannel.getScheduleTimeZone()).isEqualTo(UPDATED_SCHEDULE_TIME_ZONE);
        assertThat(testChannel.getTotalLevel()).isEqualTo(UPDATED_TOTAL_LEVEL);
        assertThat(testChannel.getArchiveLevel()).isEqualTo(UPDATED_ARCHIVE_LEVEL);
        assertThat(testChannel.isUnlimitedLevel()).isEqualTo(UPDATED_UNLIMITED_LEVEL);
        assertThat(testChannel.getFetchEngine()).isEqualTo(UPDATED_FETCH_ENGINE);
        assertThat(testChannel.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testChannel.getTag()).isEqualTo(UPDATED_TAG);
        assertThat(testChannel.getCategorySlug()).isEqualTo(UPDATED_CATEGORY_SLUG);
        assertThat(testChannel.getTagSlug()).isEqualTo(UPDATED_TAG_SLUG);
        assertThat(testChannel.getCountryCode()).isEqualTo(UPDATED_COUNTRY_CODE);
        assertThat(testChannel.getLanguageCode()).isEqualTo(UPDATED_LANGUAGE_CODE);
        assertThat(testChannel.getTargetQueueChannel()).isEqualTo(UPDATED_TARGET_QUEUE_CHANNEL);
        assertThat(testChannel.getTopics()).isEqualTo(UPDATED_TOPICS);
        assertThat(testChannel.getTopicSlugs()).isEqualTo(UPDATED_TOPIC_SLUGS);
        assertThat(testChannel.getPostType()).isEqualTo(UPDATED_POST_TYPE);
        assertThat(testChannel.getRankingCountry()).isEqualTo(UPDATED_RANKING_COUNTRY);
        assertThat(testChannel.getChannelTotalLevel()).isEqualTo(UPDATED_CHANNEL_TOTAL_LEVEL);
        assertThat(testChannel.getChannelArchiveLevel()).isEqualTo(UPDATED_CHANNEL_ARCHIVE_LEVEL);
        assertThat(testChannel.getChannelFetchEngine()).isEqualTo(UPDATED_CHANNEL_FETCH_ENGINE);
        assertThat(testChannel.getChannelRanking()).isEqualTo(UPDATED_CHANNEL_RANKING);
        assertThat(testChannel.getChannelTargetQueue()).isEqualTo(UPDATED_CHANNEL_TARGET_QUEUE);
        assertThat(testChannel.getChannelTargetPostType()).isEqualTo(UPDATED_CHANNEL_TARGET_POST_TYPE);
        assertThat(testChannel.getChannelLevelOneFetchEngine()).isEqualTo(UPDATED_CHANNEL_LEVEL_ONE_FETCH_ENGINE);
        assertThat(testChannel.getChannelLevelTwoFetchEngine()).isEqualTo(UPDATED_CHANNEL_LEVEL_TWO_FETCH_ENGINE);
        assertThat(testChannel.getChannelLevelThreeFetchEngine()).isEqualTo(UPDATED_CHANNEL_LEVEL_THREE_FETCH_ENGINE);
        assertThat(testChannel.getChannelLevelFourFetchEngine()).isEqualTo(UPDATED_CHANNEL_LEVEL_FOUR_FETCH_ENGINE);
        assertThat(testChannel.getChannelLevelOneContentType()).isEqualTo(UPDATED_CHANNEL_LEVEL_ONE_CONTENT_TYPE);
        assertThat(testChannel.getChannelLevelTwoContentType()).isEqualTo(UPDATED_CHANNEL_LEVEL_TWO_CONTENT_TYPE);
        assertThat(testChannel.getChannelLevelThreeContentType()).isEqualTo(UPDATED_CHANNEL_LEVEL_THREE_CONTENT_TYPE);
        assertThat(testChannel.getChannelLevelFourContentType()).isEqualTo(UPDATED_CHANNEL_LEVEL_FOUR_CONTENT_TYPE);
        assertThat(testChannel.isChannelAllowExternalUrl()).isEqualTo(UPDATED_CHANNEL_ALLOW_EXTERNAL_URL);
        assertThat(testChannel.getChannelLogo()).isEqualTo(UPDATED_CHANNEL_LOGO);
        assertThat(testChannel.getChannelSiteName()).isEqualTo(UPDATED_CHANNEL_SITE_NAME);
        assertThat(testChannel.getLogo()).isEqualTo(UPDATED_LOGO);
        assertThat(testChannel.getSiteName()).isEqualTo(UPDATED_SITE_NAME);
        assertThat(testChannel.getChannelType()).isEqualTo(UPDATED_CHANNEL_TYPE);
        assertThat(testChannel.getLevelOneFetchEngine()).isEqualTo(UPDATED_LEVEL_ONE_FETCH_ENGINE);
        assertThat(testChannel.getLevelTwoFetchEngine()).isEqualTo(UPDATED_LEVEL_TWO_FETCH_ENGINE);
        assertThat(testChannel.getLevelThreeFetchEngine()).isEqualTo(UPDATED_LEVEL_THREE_FETCH_ENGINE);
        assertThat(testChannel.getLevelFourFetchEngine()).isEqualTo(UPDATED_LEVEL_FOUR_FETCH_ENGINE);
        assertThat(testChannel.getLevelOneContentType()).isEqualTo(UPDATED_LEVEL_ONE_CONTENT_TYPE);
        assertThat(testChannel.getLevelTwoContentType()).isEqualTo(UPDATED_LEVEL_TWO_CONTENT_TYPE);
        assertThat(testChannel.getLevelThreeContentType()).isEqualTo(UPDATED_LEVEL_THREE_CONTENT_TYPE);
        assertThat(testChannel.getLevelFourContentType()).isEqualTo(UPDATED_LEVEL_FOUR_CONTENT_TYPE);
        assertThat(testChannel.isAllowExternalUrl()).isEqualTo(UPDATED_ALLOW_EXTERNAL_URL);
        assertThat(testChannel.getTargetChannel()).isEqualTo(UPDATED_TARGET_CHANNEL);
        assertThat(testChannel.getTarget()).isEqualTo(UPDATED_TARGET);
        assertThat(testChannel.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testChannel.getSiteDomain()).isEqualTo(UPDATED_SITE_DOMAIN);
        assertThat(testChannel.getSiteUrl()).isEqualTo(UPDATED_SITE_URL);

        // Validate the Channel in Elasticsearch
        verify(mockChannelSearchRepository, times(1)).save(testChannel);
    }

    @Test
    public void updateNonExistingChannel() throws Exception {
        int databaseSizeBeforeUpdate = channelRepository.findAll().size();

        // Create the Channel

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restChannelMockMvc.perform(put("/api/channels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(channel)))
            .andExpect(status().isBadRequest());

        // Validate the Channel in the database
        List<Channel> channelList = channelRepository.findAll();
        assertThat(channelList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Channel in Elasticsearch
        verify(mockChannelSearchRepository, times(0)).save(channel);
    }

    @Test
    public void deleteChannel() throws Exception {
        // Initialize the database
        channelService.save(channel);

        int databaseSizeBeforeDelete = channelRepository.findAll().size();

        // Get the channel
        restChannelMockMvc.perform(delete("/api/channels/{id}", channel.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Channel> channelList = channelRepository.findAll();
        assertThat(channelList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Channel in Elasticsearch
        verify(mockChannelSearchRepository, times(1)).deleteById(channel.getId());
    }

    @Test
    public void searchChannel() throws Exception {
        // Initialize the database
        channelService.save(channel);
        when(mockChannelSearchRepository.search(queryStringQuery("id:" + channel.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(channel), PageRequest.of(0, 1), 1));
        // Search the channel
        restChannelMockMvc.perform(get("/api/_search/channels?query=id:" + channel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(channel.getId())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
            .andExpect(jsonPath("$.[*].contentType").value(hasItem(DEFAULT_CONTENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].schedule").value(hasItem(DEFAULT_SCHEDULE.toString())))
            .andExpect(jsonPath("$.[*].scheduleTimeZone").value(hasItem(DEFAULT_SCHEDULE_TIME_ZONE.toString())))
            .andExpect(jsonPath("$.[*].totalLevel").value(hasItem(DEFAULT_TOTAL_LEVEL)))
            .andExpect(jsonPath("$.[*].archiveLevel").value(hasItem(DEFAULT_ARCHIVE_LEVEL)))
            .andExpect(jsonPath("$.[*].unlimitedLevel").value(hasItem(DEFAULT_UNLIMITED_LEVEL.booleanValue())))
            .andExpect(jsonPath("$.[*].fetchEngine").value(hasItem(DEFAULT_FETCH_ENGINE.toString())))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].tag").value(hasItem(DEFAULT_TAG.toString())))
            .andExpect(jsonPath("$.[*].categorySlug").value(hasItem(DEFAULT_CATEGORY_SLUG.toString())))
            .andExpect(jsonPath("$.[*].tagSlug").value(hasItem(DEFAULT_TAG_SLUG.toString())))
            .andExpect(jsonPath("$.[*].countryCode").value(hasItem(DEFAULT_COUNTRY_CODE.toString())))
            .andExpect(jsonPath("$.[*].languageCode").value(hasItem(DEFAULT_LANGUAGE_CODE.toString())))
            .andExpect(jsonPath("$.[*].targetQueueChannel").value(hasItem(DEFAULT_TARGET_QUEUE_CHANNEL.toString())))
            .andExpect(jsonPath("$.[*].topics").value(hasItem(DEFAULT_TOPICS.toString())))
            .andExpect(jsonPath("$.[*].topicSlugs").value(hasItem(DEFAULT_TOPIC_SLUGS.toString())))
            .andExpect(jsonPath("$.[*].postType").value(hasItem(DEFAULT_POST_TYPE.toString())))
            .andExpect(jsonPath("$.[*].rankingCountry").value(hasItem(DEFAULT_RANKING_COUNTRY)))
            .andExpect(jsonPath("$.[*].channelTotalLevel").value(hasItem(DEFAULT_CHANNEL_TOTAL_LEVEL)))
            .andExpect(jsonPath("$.[*].channelArchiveLevel").value(hasItem(DEFAULT_CHANNEL_ARCHIVE_LEVEL)))
            .andExpect(jsonPath("$.[*].channelFetchEngine").value(hasItem(DEFAULT_CHANNEL_FETCH_ENGINE.toString())))
            .andExpect(jsonPath("$.[*].channelRanking").value(hasItem(DEFAULT_CHANNEL_RANKING)))
            .andExpect(jsonPath("$.[*].channelTargetQueue").value(hasItem(DEFAULT_CHANNEL_TARGET_QUEUE.toString())))
            .andExpect(jsonPath("$.[*].channelTargetPostType").value(hasItem(DEFAULT_CHANNEL_TARGET_POST_TYPE.toString())))
            .andExpect(jsonPath("$.[*].channelLevelOneFetchEngine").value(hasItem(DEFAULT_CHANNEL_LEVEL_ONE_FETCH_ENGINE.toString())))
            .andExpect(jsonPath("$.[*].channelLevelTwoFetchEngine").value(hasItem(DEFAULT_CHANNEL_LEVEL_TWO_FETCH_ENGINE.toString())))
            .andExpect(jsonPath("$.[*].channelLevelThreeFetchEngine").value(hasItem(DEFAULT_CHANNEL_LEVEL_THREE_FETCH_ENGINE.toString())))
            .andExpect(jsonPath("$.[*].channelLevelFourFetchEngine").value(hasItem(DEFAULT_CHANNEL_LEVEL_FOUR_FETCH_ENGINE.toString())))
            .andExpect(jsonPath("$.[*].channelLevelOneContentType").value(hasItem(DEFAULT_CHANNEL_LEVEL_ONE_CONTENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].channelLevelTwoContentType").value(hasItem(DEFAULT_CHANNEL_LEVEL_TWO_CONTENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].channelLevelThreeContentType").value(hasItem(DEFAULT_CHANNEL_LEVEL_THREE_CONTENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].channelLevelFourContentType").value(hasItem(DEFAULT_CHANNEL_LEVEL_FOUR_CONTENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].channelAllowExternalUrl").value(hasItem(DEFAULT_CHANNEL_ALLOW_EXTERNAL_URL.booleanValue())))
            .andExpect(jsonPath("$.[*].channelLogo").value(hasItem(DEFAULT_CHANNEL_LOGO.toString())))
            .andExpect(jsonPath("$.[*].channelSiteName").value(hasItem(DEFAULT_CHANNEL_SITE_NAME.toString())))
            .andExpect(jsonPath("$.[*].logo").value(hasItem(DEFAULT_LOGO.toString())))
            .andExpect(jsonPath("$.[*].siteName").value(hasItem(DEFAULT_SITE_NAME.toString())))
            .andExpect(jsonPath("$.[*].channelType").value(hasItem(DEFAULT_CHANNEL_TYPE.toString())))
            .andExpect(jsonPath("$.[*].levelOneFetchEngine").value(hasItem(DEFAULT_LEVEL_ONE_FETCH_ENGINE.toString())))
            .andExpect(jsonPath("$.[*].levelTwoFetchEngine").value(hasItem(DEFAULT_LEVEL_TWO_FETCH_ENGINE.toString())))
            .andExpect(jsonPath("$.[*].levelThreeFetchEngine").value(hasItem(DEFAULT_LEVEL_THREE_FETCH_ENGINE.toString())))
            .andExpect(jsonPath("$.[*].levelFourFetchEngine").value(hasItem(DEFAULT_LEVEL_FOUR_FETCH_ENGINE.toString())))
            .andExpect(jsonPath("$.[*].levelOneContentType").value(hasItem(DEFAULT_LEVEL_ONE_CONTENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].levelTwoContentType").value(hasItem(DEFAULT_LEVEL_TWO_CONTENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].levelThreeContentType").value(hasItem(DEFAULT_LEVEL_THREE_CONTENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].levelFourContentType").value(hasItem(DEFAULT_LEVEL_FOUR_CONTENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].allowExternalUrl").value(hasItem(DEFAULT_ALLOW_EXTERNAL_URL.booleanValue())))
            .andExpect(jsonPath("$.[*].targetChannel").value(hasItem(DEFAULT_TARGET_CHANNEL.toString())))
            .andExpect(jsonPath("$.[*].target").value(hasItem(DEFAULT_TARGET.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].siteDomain").value(hasItem(DEFAULT_SITE_DOMAIN.toString())))
            .andExpect(jsonPath("$.[*].siteUrl").value(hasItem(DEFAULT_SITE_URL.toString())));
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Channel.class);
        Channel channel1 = new Channel();
        channel1.setId("id1");
        Channel channel2 = new Channel();
        channel2.setId(channel1.getId());
        assertThat(channel1).isEqualTo(channel2);
        channel2.setId("id2");
        assertThat(channel1).isNotEqualTo(channel2);
        channel1.setId(null);
        assertThat(channel1).isNotEqualTo(channel2);
    }
}
