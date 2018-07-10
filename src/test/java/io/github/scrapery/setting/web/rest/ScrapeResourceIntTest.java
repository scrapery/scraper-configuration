package io.github.scrapery.setting.web.rest;

import io.github.scrapery.setting.ScraperSettingApp;

import io.github.scrapery.setting.domain.Scrape;
import io.github.scrapery.setting.repository.ScrapeRepository;
import io.github.scrapery.setting.repository.search.ScrapeSearchRepository;
import io.github.scrapery.setting.service.ScrapeService;
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

import io.github.scrapery.setting.domain.enumeration.FetchEngine;
import io.github.scrapery.setting.domain.enumeration.DocType;
/**
 * Test class for the ScrapeResource REST controller.
 *
 * @see ScrapeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ScraperSettingApp.class)
public class ScrapeResourceIntTest {

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final Integer DEFAULT_TOTAL_LEVEL = 1;
    private static final Integer UPDATED_TOTAL_LEVEL = 2;

    private static final Integer DEFAULT_ARCHIVE_LEVEL = 1;
    private static final Integer UPDATED_ARCHIVE_LEVEL = 2;

    private static final Integer DEFAULT_CURRENT_LEVEL = 1;
    private static final Integer UPDATED_CURRENT_LEVEL = 2;

    private static final Boolean DEFAULT_UNLIMITED_LEVEL = false;
    private static final Boolean UPDATED_UNLIMITED_LEVEL = true;

    private static final FetchEngine DEFAULT_FETCH_ENGINE = FetchEngine.SELENIUM;
    private static final FetchEngine UPDATED_FETCH_ENGINE = FetchEngine.HTTP;

    private static final Long DEFAULT_CHANNEL_ID = 1L;
    private static final Long UPDATED_CHANNEL_ID = 2L;

    private static final String DEFAULT_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY = "BBBBBBBBBB";

    private static final String DEFAULT_TAG = "AAAAAAAAAA";
    private static final String UPDATED_TAG = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY_CODE = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_LANGUAGE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_LANGUAGE_CODE = "BBBBBBBBBB";

    private static final DocType DEFAULT_DOC_TYPE = DocType.HTML;
    private static final DocType UPDATED_DOC_TYPE = DocType.XML;

    @Autowired
    private ScrapeRepository scrapeRepository;
    @Mock
    private ScrapeRepository scrapeRepositoryMock;
    
    @Mock
    private ScrapeService scrapeServiceMock;

    @Autowired
    private ScrapeService scrapeService;

    /**
     * This repository is mocked in the io.github.scrapery.setting.repository.search test package.
     *
     * @see io.github.scrapery.setting.repository.search.ScrapeSearchRepositoryMockConfiguration
     */
    @Autowired
    private ScrapeSearchRepository mockScrapeSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restScrapeMockMvc;

    private Scrape scrape;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ScrapeResource scrapeResource = new ScrapeResource(scrapeService);
        this.restScrapeMockMvc = MockMvcBuilders.standaloneSetup(scrapeResource)
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
    public static Scrape createEntity() {
        Scrape scrape = new Scrape()
            .url(DEFAULT_URL)
            .totalLevel(DEFAULT_TOTAL_LEVEL)
            .archiveLevel(DEFAULT_ARCHIVE_LEVEL)
            .currentLevel(DEFAULT_CURRENT_LEVEL)
            .unlimitedLevel(DEFAULT_UNLIMITED_LEVEL)
            .fetchEngine(DEFAULT_FETCH_ENGINE)
            .channelId(DEFAULT_CHANNEL_ID)
            .category(DEFAULT_CATEGORY)
            .tag(DEFAULT_TAG)
            .countryCode(DEFAULT_COUNTRY_CODE)
            .languageCode(DEFAULT_LANGUAGE_CODE)
            .docType(DEFAULT_DOC_TYPE);
        return scrape;
    }

    @Before
    public void initTest() {
        scrapeRepository.deleteAll();
        scrape = createEntity();
    }

    @Test
    public void createScrape() throws Exception {
        int databaseSizeBeforeCreate = scrapeRepository.findAll().size();

        // Create the Scrape
        restScrapeMockMvc.perform(post("/api/scrapes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(scrape)))
            .andExpect(status().isCreated());

        // Validate the Scrape in the database
        List<Scrape> scrapeList = scrapeRepository.findAll();
        assertThat(scrapeList).hasSize(databaseSizeBeforeCreate + 1);
        Scrape testScrape = scrapeList.get(scrapeList.size() - 1);
        assertThat(testScrape.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testScrape.getTotalLevel()).isEqualTo(DEFAULT_TOTAL_LEVEL);
        assertThat(testScrape.getArchiveLevel()).isEqualTo(DEFAULT_ARCHIVE_LEVEL);
        assertThat(testScrape.getCurrentLevel()).isEqualTo(DEFAULT_CURRENT_LEVEL);
        assertThat(testScrape.isUnlimitedLevel()).isEqualTo(DEFAULT_UNLIMITED_LEVEL);
        assertThat(testScrape.getFetchEngine()).isEqualTo(DEFAULT_FETCH_ENGINE);
        assertThat(testScrape.getChannelId()).isEqualTo(DEFAULT_CHANNEL_ID);
        assertThat(testScrape.getCategory()).isEqualTo(DEFAULT_CATEGORY);
        assertThat(testScrape.getTag()).isEqualTo(DEFAULT_TAG);
        assertThat(testScrape.getCountryCode()).isEqualTo(DEFAULT_COUNTRY_CODE);
        assertThat(testScrape.getLanguageCode()).isEqualTo(DEFAULT_LANGUAGE_CODE);
        assertThat(testScrape.getDocType()).isEqualTo(DEFAULT_DOC_TYPE);

        // Validate the Scrape in Elasticsearch
        verify(mockScrapeSearchRepository, times(1)).save(testScrape);
    }

    @Test
    public void createScrapeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = scrapeRepository.findAll().size();

        // Create the Scrape with an existing ID
        scrape.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restScrapeMockMvc.perform(post("/api/scrapes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(scrape)))
            .andExpect(status().isBadRequest());

        // Validate the Scrape in the database
        List<Scrape> scrapeList = scrapeRepository.findAll();
        assertThat(scrapeList).hasSize(databaseSizeBeforeCreate);

        // Validate the Scrape in Elasticsearch
        verify(mockScrapeSearchRepository, times(0)).save(scrape);
    }

    @Test
    public void getAllScrapes() throws Exception {
        // Initialize the database
        scrapeRepository.save(scrape);

        // Get all the scrapeList
        restScrapeMockMvc.perform(get("/api/scrapes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(scrape.getId())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
            .andExpect(jsonPath("$.[*].totalLevel").value(hasItem(DEFAULT_TOTAL_LEVEL)))
            .andExpect(jsonPath("$.[*].archiveLevel").value(hasItem(DEFAULT_ARCHIVE_LEVEL)))
            .andExpect(jsonPath("$.[*].currentLevel").value(hasItem(DEFAULT_CURRENT_LEVEL)))
            .andExpect(jsonPath("$.[*].unlimitedLevel").value(hasItem(DEFAULT_UNLIMITED_LEVEL.booleanValue())))
            .andExpect(jsonPath("$.[*].fetchEngine").value(hasItem(DEFAULT_FETCH_ENGINE.toString())))
            .andExpect(jsonPath("$.[*].channelId").value(hasItem(DEFAULT_CHANNEL_ID.intValue())))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].tag").value(hasItem(DEFAULT_TAG.toString())))
            .andExpect(jsonPath("$.[*].countryCode").value(hasItem(DEFAULT_COUNTRY_CODE.toString())))
            .andExpect(jsonPath("$.[*].languageCode").value(hasItem(DEFAULT_LANGUAGE_CODE.toString())))
            .andExpect(jsonPath("$.[*].docType").value(hasItem(DEFAULT_DOC_TYPE.toString())));
    }
    
    public void getAllScrapesWithEagerRelationshipsIsEnabled() throws Exception {
        ScrapeResource scrapeResource = new ScrapeResource(scrapeServiceMock);
        when(scrapeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restScrapeMockMvc = MockMvcBuilders.standaloneSetup(scrapeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restScrapeMockMvc.perform(get("/api/scrapes?eagerload=true"))
        .andExpect(status().isOk());

        verify(scrapeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    public void getAllScrapesWithEagerRelationshipsIsNotEnabled() throws Exception {
        ScrapeResource scrapeResource = new ScrapeResource(scrapeServiceMock);
            when(scrapeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restScrapeMockMvc = MockMvcBuilders.standaloneSetup(scrapeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restScrapeMockMvc.perform(get("/api/scrapes?eagerload=true"))
        .andExpect(status().isOk());

            verify(scrapeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    public void getScrape() throws Exception {
        // Initialize the database
        scrapeRepository.save(scrape);

        // Get the scrape
        restScrapeMockMvc.perform(get("/api/scrapes/{id}", scrape.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(scrape.getId()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()))
            .andExpect(jsonPath("$.totalLevel").value(DEFAULT_TOTAL_LEVEL))
            .andExpect(jsonPath("$.archiveLevel").value(DEFAULT_ARCHIVE_LEVEL))
            .andExpect(jsonPath("$.currentLevel").value(DEFAULT_CURRENT_LEVEL))
            .andExpect(jsonPath("$.unlimitedLevel").value(DEFAULT_UNLIMITED_LEVEL.booleanValue()))
            .andExpect(jsonPath("$.fetchEngine").value(DEFAULT_FETCH_ENGINE.toString()))
            .andExpect(jsonPath("$.channelId").value(DEFAULT_CHANNEL_ID.intValue()))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY.toString()))
            .andExpect(jsonPath("$.tag").value(DEFAULT_TAG.toString()))
            .andExpect(jsonPath("$.countryCode").value(DEFAULT_COUNTRY_CODE.toString()))
            .andExpect(jsonPath("$.languageCode").value(DEFAULT_LANGUAGE_CODE.toString()))
            .andExpect(jsonPath("$.docType").value(DEFAULT_DOC_TYPE.toString()));
    }
    @Test
    public void getNonExistingScrape() throws Exception {
        // Get the scrape
        restScrapeMockMvc.perform(get("/api/scrapes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateScrape() throws Exception {
        // Initialize the database
        scrapeService.save(scrape);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockScrapeSearchRepository);

        int databaseSizeBeforeUpdate = scrapeRepository.findAll().size();

        // Update the scrape
        Scrape updatedScrape = scrapeRepository.findById(scrape.getId()).get();
        updatedScrape
            .url(UPDATED_URL)
            .totalLevel(UPDATED_TOTAL_LEVEL)
            .archiveLevel(UPDATED_ARCHIVE_LEVEL)
            .currentLevel(UPDATED_CURRENT_LEVEL)
            .unlimitedLevel(UPDATED_UNLIMITED_LEVEL)
            .fetchEngine(UPDATED_FETCH_ENGINE)
            .channelId(UPDATED_CHANNEL_ID)
            .category(UPDATED_CATEGORY)
            .tag(UPDATED_TAG)
            .countryCode(UPDATED_COUNTRY_CODE)
            .languageCode(UPDATED_LANGUAGE_CODE)
            .docType(UPDATED_DOC_TYPE);

        restScrapeMockMvc.perform(put("/api/scrapes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedScrape)))
            .andExpect(status().isOk());

        // Validate the Scrape in the database
        List<Scrape> scrapeList = scrapeRepository.findAll();
        assertThat(scrapeList).hasSize(databaseSizeBeforeUpdate);
        Scrape testScrape = scrapeList.get(scrapeList.size() - 1);
        assertThat(testScrape.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testScrape.getTotalLevel()).isEqualTo(UPDATED_TOTAL_LEVEL);
        assertThat(testScrape.getArchiveLevel()).isEqualTo(UPDATED_ARCHIVE_LEVEL);
        assertThat(testScrape.getCurrentLevel()).isEqualTo(UPDATED_CURRENT_LEVEL);
        assertThat(testScrape.isUnlimitedLevel()).isEqualTo(UPDATED_UNLIMITED_LEVEL);
        assertThat(testScrape.getFetchEngine()).isEqualTo(UPDATED_FETCH_ENGINE);
        assertThat(testScrape.getChannelId()).isEqualTo(UPDATED_CHANNEL_ID);
        assertThat(testScrape.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testScrape.getTag()).isEqualTo(UPDATED_TAG);
        assertThat(testScrape.getCountryCode()).isEqualTo(UPDATED_COUNTRY_CODE);
        assertThat(testScrape.getLanguageCode()).isEqualTo(UPDATED_LANGUAGE_CODE);
        assertThat(testScrape.getDocType()).isEqualTo(UPDATED_DOC_TYPE);

        // Validate the Scrape in Elasticsearch
        verify(mockScrapeSearchRepository, times(1)).save(testScrape);
    }

    @Test
    public void updateNonExistingScrape() throws Exception {
        int databaseSizeBeforeUpdate = scrapeRepository.findAll().size();

        // Create the Scrape

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restScrapeMockMvc.perform(put("/api/scrapes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(scrape)))
            .andExpect(status().isBadRequest());

        // Validate the Scrape in the database
        List<Scrape> scrapeList = scrapeRepository.findAll();
        assertThat(scrapeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Scrape in Elasticsearch
        verify(mockScrapeSearchRepository, times(0)).save(scrape);
    }

    @Test
    public void deleteScrape() throws Exception {
        // Initialize the database
        scrapeService.save(scrape);

        int databaseSizeBeforeDelete = scrapeRepository.findAll().size();

        // Get the scrape
        restScrapeMockMvc.perform(delete("/api/scrapes/{id}", scrape.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Scrape> scrapeList = scrapeRepository.findAll();
        assertThat(scrapeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Scrape in Elasticsearch
        verify(mockScrapeSearchRepository, times(1)).deleteById(scrape.getId());
    }

    @Test
    public void searchScrape() throws Exception {
        // Initialize the database
        scrapeService.save(scrape);
        when(mockScrapeSearchRepository.search(queryStringQuery("id:" + scrape.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(scrape), PageRequest.of(0, 1), 1));
        // Search the scrape
        restScrapeMockMvc.perform(get("/api/_search/scrapes?query=id:" + scrape.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(scrape.getId())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
            .andExpect(jsonPath("$.[*].totalLevel").value(hasItem(DEFAULT_TOTAL_LEVEL)))
            .andExpect(jsonPath("$.[*].archiveLevel").value(hasItem(DEFAULT_ARCHIVE_LEVEL)))
            .andExpect(jsonPath("$.[*].currentLevel").value(hasItem(DEFAULT_CURRENT_LEVEL)))
            .andExpect(jsonPath("$.[*].unlimitedLevel").value(hasItem(DEFAULT_UNLIMITED_LEVEL.booleanValue())))
            .andExpect(jsonPath("$.[*].fetchEngine").value(hasItem(DEFAULT_FETCH_ENGINE.toString())))
            .andExpect(jsonPath("$.[*].channelId").value(hasItem(DEFAULT_CHANNEL_ID.intValue())))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].tag").value(hasItem(DEFAULT_TAG.toString())))
            .andExpect(jsonPath("$.[*].countryCode").value(hasItem(DEFAULT_COUNTRY_CODE.toString())))
            .andExpect(jsonPath("$.[*].languageCode").value(hasItem(DEFAULT_LANGUAGE_CODE.toString())))
            .andExpect(jsonPath("$.[*].docType").value(hasItem(DEFAULT_DOC_TYPE.toString())));
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Scrape.class);
        Scrape scrape1 = new Scrape();
        scrape1.setId("id1");
        Scrape scrape2 = new Scrape();
        scrape2.setId(scrape1.getId());
        assertThat(scrape1).isEqualTo(scrape2);
        scrape2.setId("id2");
        assertThat(scrape1).isNotEqualTo(scrape2);
        scrape1.setId(null);
        assertThat(scrape1).isNotEqualTo(scrape2);
    }
}
