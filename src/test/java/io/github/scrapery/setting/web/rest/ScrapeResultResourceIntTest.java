package io.github.scrapery.setting.web.rest;

import io.github.scrapery.setting.ScraperSettingApp;

import io.github.scrapery.setting.domain.ScrapeResult;
import io.github.scrapery.setting.repository.ScrapeResultRepository;
import io.github.scrapery.setting.repository.search.ScrapeResultSearchRepository;
import io.github.scrapery.setting.service.ScrapeResultService;
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
import org.springframework.util.Base64Utils;

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
 * Test class for the ScrapeResultResource REST controller.
 *
 * @see ScrapeResultResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ScraperSettingApp.class)
public class ScrapeResultResourceIntTest {

    private static final Long DEFAULT_SCRAPE_ID = 1L;
    private static final Long UPDATED_SCRAPE_ID = 2L;

    private static final byte[] DEFAULT_CONTENT = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_CONTENT = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_CONTENT_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_CONTENT_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_PATH = "AAAAAAAAAA";
    private static final String UPDATED_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_TARGET_OBJECT = "AAAAAAAAAA";
    private static final String UPDATED_TARGET_OBJECT = "BBBBBBBBBB";

    private static final String DEFAULT_TARGET_QUEUE = "AAAAAAAAAA";
    private static final String UPDATED_TARGET_QUEUE = "BBBBBBBBBB";

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

    @Autowired
    private ScrapeResultRepository scrapeResultRepository;

    

    @Autowired
    private ScrapeResultService scrapeResultService;

    /**
     * This repository is mocked in the io.github.scrapery.setting.repository.search test package.
     *
     * @see io.github.scrapery.setting.repository.search.ScrapeResultSearchRepositoryMockConfiguration
     */
    @Autowired
    private ScrapeResultSearchRepository mockScrapeResultSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restScrapeResultMockMvc;

    private ScrapeResult scrapeResult;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ScrapeResultResource scrapeResultResource = new ScrapeResultResource(scrapeResultService);
        this.restScrapeResultMockMvc = MockMvcBuilders.standaloneSetup(scrapeResultResource)
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
    public static ScrapeResult createEntity() {
        ScrapeResult scrapeResult = new ScrapeResult()
            .scrapeId(DEFAULT_SCRAPE_ID)
            .content(DEFAULT_CONTENT)
            .contentContentType(DEFAULT_CONTENT_CONTENT_TYPE)
            .path(DEFAULT_PATH)
            .targetObject(DEFAULT_TARGET_OBJECT)
            .targetQueue(DEFAULT_TARGET_QUEUE)
            .channelId(DEFAULT_CHANNEL_ID)
            .category(DEFAULT_CATEGORY)
            .tag(DEFAULT_TAG)
            .countryCode(DEFAULT_COUNTRY_CODE)
            .languageCode(DEFAULT_LANGUAGE_CODE);
        return scrapeResult;
    }

    @Before
    public void initTest() {
        scrapeResultRepository.deleteAll();
        scrapeResult = createEntity();
    }

    @Test
    public void createScrapeResult() throws Exception {
        int databaseSizeBeforeCreate = scrapeResultRepository.findAll().size();

        // Create the ScrapeResult
        restScrapeResultMockMvc.perform(post("/api/scrape-results")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(scrapeResult)))
            .andExpect(status().isCreated());

        // Validate the ScrapeResult in the database
        List<ScrapeResult> scrapeResultList = scrapeResultRepository.findAll();
        assertThat(scrapeResultList).hasSize(databaseSizeBeforeCreate + 1);
        ScrapeResult testScrapeResult = scrapeResultList.get(scrapeResultList.size() - 1);
        assertThat(testScrapeResult.getScrapeId()).isEqualTo(DEFAULT_SCRAPE_ID);
        assertThat(testScrapeResult.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testScrapeResult.getContentContentType()).isEqualTo(DEFAULT_CONTENT_CONTENT_TYPE);
        assertThat(testScrapeResult.getPath()).isEqualTo(DEFAULT_PATH);
        assertThat(testScrapeResult.getTargetObject()).isEqualTo(DEFAULT_TARGET_OBJECT);
        assertThat(testScrapeResult.getTargetQueue()).isEqualTo(DEFAULT_TARGET_QUEUE);
        assertThat(testScrapeResult.getChannelId()).isEqualTo(DEFAULT_CHANNEL_ID);
        assertThat(testScrapeResult.getCategory()).isEqualTo(DEFAULT_CATEGORY);
        assertThat(testScrapeResult.getTag()).isEqualTo(DEFAULT_TAG);
        assertThat(testScrapeResult.getCountryCode()).isEqualTo(DEFAULT_COUNTRY_CODE);
        assertThat(testScrapeResult.getLanguageCode()).isEqualTo(DEFAULT_LANGUAGE_CODE);

        // Validate the ScrapeResult in Elasticsearch
        verify(mockScrapeResultSearchRepository, times(1)).save(testScrapeResult);
    }

    @Test
    public void createScrapeResultWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = scrapeResultRepository.findAll().size();

        // Create the ScrapeResult with an existing ID
        scrapeResult.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restScrapeResultMockMvc.perform(post("/api/scrape-results")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(scrapeResult)))
            .andExpect(status().isBadRequest());

        // Validate the ScrapeResult in the database
        List<ScrapeResult> scrapeResultList = scrapeResultRepository.findAll();
        assertThat(scrapeResultList).hasSize(databaseSizeBeforeCreate);

        // Validate the ScrapeResult in Elasticsearch
        verify(mockScrapeResultSearchRepository, times(0)).save(scrapeResult);
    }

    @Test
    public void getAllScrapeResults() throws Exception {
        // Initialize the database
        scrapeResultRepository.save(scrapeResult);

        // Get all the scrapeResultList
        restScrapeResultMockMvc.perform(get("/api/scrape-results?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(scrapeResult.getId())))
            .andExpect(jsonPath("$.[*].scrapeId").value(hasItem(DEFAULT_SCRAPE_ID.intValue())))
            .andExpect(jsonPath("$.[*].contentContentType").value(hasItem(DEFAULT_CONTENT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(Base64Utils.encodeToString(DEFAULT_CONTENT))))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH.toString())))
            .andExpect(jsonPath("$.[*].targetObject").value(hasItem(DEFAULT_TARGET_OBJECT.toString())))
            .andExpect(jsonPath("$.[*].targetQueue").value(hasItem(DEFAULT_TARGET_QUEUE.toString())))
            .andExpect(jsonPath("$.[*].channelId").value(hasItem(DEFAULT_CHANNEL_ID.intValue())))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].tag").value(hasItem(DEFAULT_TAG.toString())))
            .andExpect(jsonPath("$.[*].countryCode").value(hasItem(DEFAULT_COUNTRY_CODE.toString())))
            .andExpect(jsonPath("$.[*].languageCode").value(hasItem(DEFAULT_LANGUAGE_CODE.toString())));
    }
    

    @Test
    public void getScrapeResult() throws Exception {
        // Initialize the database
        scrapeResultRepository.save(scrapeResult);

        // Get the scrapeResult
        restScrapeResultMockMvc.perform(get("/api/scrape-results/{id}", scrapeResult.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(scrapeResult.getId()))
            .andExpect(jsonPath("$.scrapeId").value(DEFAULT_SCRAPE_ID.intValue()))
            .andExpect(jsonPath("$.contentContentType").value(DEFAULT_CONTENT_CONTENT_TYPE))
            .andExpect(jsonPath("$.content").value(Base64Utils.encodeToString(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.path").value(DEFAULT_PATH.toString()))
            .andExpect(jsonPath("$.targetObject").value(DEFAULT_TARGET_OBJECT.toString()))
            .andExpect(jsonPath("$.targetQueue").value(DEFAULT_TARGET_QUEUE.toString()))
            .andExpect(jsonPath("$.channelId").value(DEFAULT_CHANNEL_ID.intValue()))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY.toString()))
            .andExpect(jsonPath("$.tag").value(DEFAULT_TAG.toString()))
            .andExpect(jsonPath("$.countryCode").value(DEFAULT_COUNTRY_CODE.toString()))
            .andExpect(jsonPath("$.languageCode").value(DEFAULT_LANGUAGE_CODE.toString()));
    }
    @Test
    public void getNonExistingScrapeResult() throws Exception {
        // Get the scrapeResult
        restScrapeResultMockMvc.perform(get("/api/scrape-results/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateScrapeResult() throws Exception {
        // Initialize the database
        scrapeResultService.save(scrapeResult);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockScrapeResultSearchRepository);

        int databaseSizeBeforeUpdate = scrapeResultRepository.findAll().size();

        // Update the scrapeResult
        ScrapeResult updatedScrapeResult = scrapeResultRepository.findById(scrapeResult.getId()).get();
        updatedScrapeResult
            .scrapeId(UPDATED_SCRAPE_ID)
            .content(UPDATED_CONTENT)
            .contentContentType(UPDATED_CONTENT_CONTENT_TYPE)
            .path(UPDATED_PATH)
            .targetObject(UPDATED_TARGET_OBJECT)
            .targetQueue(UPDATED_TARGET_QUEUE)
            .channelId(UPDATED_CHANNEL_ID)
            .category(UPDATED_CATEGORY)
            .tag(UPDATED_TAG)
            .countryCode(UPDATED_COUNTRY_CODE)
            .languageCode(UPDATED_LANGUAGE_CODE);

        restScrapeResultMockMvc.perform(put("/api/scrape-results")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedScrapeResult)))
            .andExpect(status().isOk());

        // Validate the ScrapeResult in the database
        List<ScrapeResult> scrapeResultList = scrapeResultRepository.findAll();
        assertThat(scrapeResultList).hasSize(databaseSizeBeforeUpdate);
        ScrapeResult testScrapeResult = scrapeResultList.get(scrapeResultList.size() - 1);
        assertThat(testScrapeResult.getScrapeId()).isEqualTo(UPDATED_SCRAPE_ID);
        assertThat(testScrapeResult.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testScrapeResult.getContentContentType()).isEqualTo(UPDATED_CONTENT_CONTENT_TYPE);
        assertThat(testScrapeResult.getPath()).isEqualTo(UPDATED_PATH);
        assertThat(testScrapeResult.getTargetObject()).isEqualTo(UPDATED_TARGET_OBJECT);
        assertThat(testScrapeResult.getTargetQueue()).isEqualTo(UPDATED_TARGET_QUEUE);
        assertThat(testScrapeResult.getChannelId()).isEqualTo(UPDATED_CHANNEL_ID);
        assertThat(testScrapeResult.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testScrapeResult.getTag()).isEqualTo(UPDATED_TAG);
        assertThat(testScrapeResult.getCountryCode()).isEqualTo(UPDATED_COUNTRY_CODE);
        assertThat(testScrapeResult.getLanguageCode()).isEqualTo(UPDATED_LANGUAGE_CODE);

        // Validate the ScrapeResult in Elasticsearch
        verify(mockScrapeResultSearchRepository, times(1)).save(testScrapeResult);
    }

    @Test
    public void updateNonExistingScrapeResult() throws Exception {
        int databaseSizeBeforeUpdate = scrapeResultRepository.findAll().size();

        // Create the ScrapeResult

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restScrapeResultMockMvc.perform(put("/api/scrape-results")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(scrapeResult)))
            .andExpect(status().isBadRequest());

        // Validate the ScrapeResult in the database
        List<ScrapeResult> scrapeResultList = scrapeResultRepository.findAll();
        assertThat(scrapeResultList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ScrapeResult in Elasticsearch
        verify(mockScrapeResultSearchRepository, times(0)).save(scrapeResult);
    }

    @Test
    public void deleteScrapeResult() throws Exception {
        // Initialize the database
        scrapeResultService.save(scrapeResult);

        int databaseSizeBeforeDelete = scrapeResultRepository.findAll().size();

        // Get the scrapeResult
        restScrapeResultMockMvc.perform(delete("/api/scrape-results/{id}", scrapeResult.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ScrapeResult> scrapeResultList = scrapeResultRepository.findAll();
        assertThat(scrapeResultList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ScrapeResult in Elasticsearch
        verify(mockScrapeResultSearchRepository, times(1)).deleteById(scrapeResult.getId());
    }

    @Test
    public void searchScrapeResult() throws Exception {
        // Initialize the database
        scrapeResultService.save(scrapeResult);
        when(mockScrapeResultSearchRepository.search(queryStringQuery("id:" + scrapeResult.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(scrapeResult), PageRequest.of(0, 1), 1));
        // Search the scrapeResult
        restScrapeResultMockMvc.perform(get("/api/_search/scrape-results?query=id:" + scrapeResult.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(scrapeResult.getId())))
            .andExpect(jsonPath("$.[*].scrapeId").value(hasItem(DEFAULT_SCRAPE_ID.intValue())))
            .andExpect(jsonPath("$.[*].contentContentType").value(hasItem(DEFAULT_CONTENT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(Base64Utils.encodeToString(DEFAULT_CONTENT))))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH.toString())))
            .andExpect(jsonPath("$.[*].targetObject").value(hasItem(DEFAULT_TARGET_OBJECT.toString())))
            .andExpect(jsonPath("$.[*].targetQueue").value(hasItem(DEFAULT_TARGET_QUEUE.toString())))
            .andExpect(jsonPath("$.[*].channelId").value(hasItem(DEFAULT_CHANNEL_ID.intValue())))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].tag").value(hasItem(DEFAULT_TAG.toString())))
            .andExpect(jsonPath("$.[*].countryCode").value(hasItem(DEFAULT_COUNTRY_CODE.toString())))
            .andExpect(jsonPath("$.[*].languageCode").value(hasItem(DEFAULT_LANGUAGE_CODE.toString())));
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ScrapeResult.class);
        ScrapeResult scrapeResult1 = new ScrapeResult();
        scrapeResult1.setId("id1");
        ScrapeResult scrapeResult2 = new ScrapeResult();
        scrapeResult2.setId(scrapeResult1.getId());
        assertThat(scrapeResult1).isEqualTo(scrapeResult2);
        scrapeResult2.setId("id2");
        assertThat(scrapeResult1).isNotEqualTo(scrapeResult2);
        scrapeResult1.setId(null);
        assertThat(scrapeResult1).isNotEqualTo(scrapeResult2);
    }
}
