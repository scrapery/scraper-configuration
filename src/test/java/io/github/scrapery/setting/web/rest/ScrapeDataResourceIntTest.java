package io.github.scrapery.setting.web.rest;

import io.github.scrapery.setting.ScraperSettingApp;

import io.github.scrapery.setting.domain.ScrapeData;
import io.github.scrapery.setting.repository.ScrapeDataRepository;
import io.github.scrapery.setting.repository.search.ScrapeDataSearchRepository;
import io.github.scrapery.setting.service.ScrapeDataService;
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

import io.github.scrapery.setting.domain.enumeration.ConfigDataType;
/**
 * Test class for the ScrapeDataResource REST controller.
 *
 * @see ScrapeDataResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ScraperSettingApp.class)
public class ScrapeDataResourceIntTest {

    private static final Long DEFAULT_SCRAPE_DATA_ID = 1L;
    private static final Long UPDATED_SCRAPE_DATA_ID = 2L;

    private static final Long DEFAULT_SCRAPE_ID = 1L;
    private static final Long UPDATED_SCRAPE_ID = 2L;

    private static final String DEFAULT_DOMAIN = "AAAAAAAAAA";
    private static final String UPDATED_DOMAIN = "BBBBBBBBBB";

    private static final String DEFAULT_HOST = "AAAAAAAAAA";
    private static final String UPDATED_HOST = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String DEFAULT_ROOT_SCRAPE_URL = "AAAAAAAAAA";
    private static final String UPDATED_ROOT_SCRAPE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_PARENT_URL = "AAAAAAAAAA";
    private static final String UPDATED_PARENT_URL = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SELECTOR = "AAAAAAAAAA";
    private static final String UPDATED_SELECTOR = "BBBBBBBBBB";

    private static final String DEFAULT_ATTR = "AAAAAAAAAA";
    private static final String UPDATED_ATTR = "BBBBBBBBBB";

    private static final ConfigDataType DEFAULT_DATA_TYPE = ConfigDataType.INTEGER;
    private static final ConfigDataType UPDATED_DATA_TYPE = ConfigDataType.STRING;

    private static final String DEFAULT_DATA = "AAAAAAAAAA";
    private static final String UPDATED_DATA = "BBBBBBBBBB";

    @Autowired
    private ScrapeDataRepository scrapeDataRepository;

    

    @Autowired
    private ScrapeDataService scrapeDataService;

    /**
     * This repository is mocked in the io.github.scrapery.setting.repository.search test package.
     *
     * @see io.github.scrapery.setting.repository.search.ScrapeDataSearchRepositoryMockConfiguration
     */
    @Autowired
    private ScrapeDataSearchRepository mockScrapeDataSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restScrapeDataMockMvc;

    private ScrapeData scrapeData;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ScrapeDataResource scrapeDataResource = new ScrapeDataResource(scrapeDataService);
        this.restScrapeDataMockMvc = MockMvcBuilders.standaloneSetup(scrapeDataResource)
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
    public static ScrapeData createEntity() {
        ScrapeData scrapeData = new ScrapeData()
            .scrapeDataId(DEFAULT_SCRAPE_DATA_ID)
            .scrapeId(DEFAULT_SCRAPE_ID)
            .domain(DEFAULT_DOMAIN)
            .host(DEFAULT_HOST)
            .url(DEFAULT_URL)
            .rootScrapeUrl(DEFAULT_ROOT_SCRAPE_URL)
            .parentUrl(DEFAULT_PARENT_URL)
            .name(DEFAULT_NAME)
            .selector(DEFAULT_SELECTOR)
            .attr(DEFAULT_ATTR)
            .dataType(DEFAULT_DATA_TYPE)
            .data(DEFAULT_DATA);
        return scrapeData;
    }

    @Before
    public void initTest() {
        scrapeDataRepository.deleteAll();
        scrapeData = createEntity();
    }

    @Test
    public void createScrapeData() throws Exception {
        int databaseSizeBeforeCreate = scrapeDataRepository.findAll().size();

        // Create the ScrapeData
        restScrapeDataMockMvc.perform(post("/api/scrape-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(scrapeData)))
            .andExpect(status().isCreated());

        // Validate the ScrapeData in the database
        List<ScrapeData> scrapeDataList = scrapeDataRepository.findAll();
        assertThat(scrapeDataList).hasSize(databaseSizeBeforeCreate + 1);
        ScrapeData testScrapeData = scrapeDataList.get(scrapeDataList.size() - 1);
        assertThat(testScrapeData.getScrapeDataId()).isEqualTo(DEFAULT_SCRAPE_DATA_ID);
        assertThat(testScrapeData.getScrapeId()).isEqualTo(DEFAULT_SCRAPE_ID);
        assertThat(testScrapeData.getDomain()).isEqualTo(DEFAULT_DOMAIN);
        assertThat(testScrapeData.getHost()).isEqualTo(DEFAULT_HOST);
        assertThat(testScrapeData.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testScrapeData.getRootScrapeUrl()).isEqualTo(DEFAULT_ROOT_SCRAPE_URL);
        assertThat(testScrapeData.getParentUrl()).isEqualTo(DEFAULT_PARENT_URL);
        assertThat(testScrapeData.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testScrapeData.getSelector()).isEqualTo(DEFAULT_SELECTOR);
        assertThat(testScrapeData.getAttr()).isEqualTo(DEFAULT_ATTR);
        assertThat(testScrapeData.getDataType()).isEqualTo(DEFAULT_DATA_TYPE);
        assertThat(testScrapeData.getData()).isEqualTo(DEFAULT_DATA);

        // Validate the ScrapeData in Elasticsearch
        verify(mockScrapeDataSearchRepository, times(1)).save(testScrapeData);
    }

    @Test
    public void createScrapeDataWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = scrapeDataRepository.findAll().size();

        // Create the ScrapeData with an existing ID
        scrapeData.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restScrapeDataMockMvc.perform(post("/api/scrape-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(scrapeData)))
            .andExpect(status().isBadRequest());

        // Validate the ScrapeData in the database
        List<ScrapeData> scrapeDataList = scrapeDataRepository.findAll();
        assertThat(scrapeDataList).hasSize(databaseSizeBeforeCreate);

        // Validate the ScrapeData in Elasticsearch
        verify(mockScrapeDataSearchRepository, times(0)).save(scrapeData);
    }

    @Test
    public void getAllScrapeData() throws Exception {
        // Initialize the database
        scrapeDataRepository.save(scrapeData);

        // Get all the scrapeDataList
        restScrapeDataMockMvc.perform(get("/api/scrape-data?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(scrapeData.getId())))
            .andExpect(jsonPath("$.[*].scrapeDataId").value(hasItem(DEFAULT_SCRAPE_DATA_ID.intValue())))
            .andExpect(jsonPath("$.[*].scrapeId").value(hasItem(DEFAULT_SCRAPE_ID.intValue())))
            .andExpect(jsonPath("$.[*].domain").value(hasItem(DEFAULT_DOMAIN.toString())))
            .andExpect(jsonPath("$.[*].host").value(hasItem(DEFAULT_HOST.toString())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
            .andExpect(jsonPath("$.[*].rootScrapeUrl").value(hasItem(DEFAULT_ROOT_SCRAPE_URL.toString())))
            .andExpect(jsonPath("$.[*].parentUrl").value(hasItem(DEFAULT_PARENT_URL.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].selector").value(hasItem(DEFAULT_SELECTOR.toString())))
            .andExpect(jsonPath("$.[*].attr").value(hasItem(DEFAULT_ATTR.toString())))
            .andExpect(jsonPath("$.[*].dataType").value(hasItem(DEFAULT_DATA_TYPE.toString())))
            .andExpect(jsonPath("$.[*].data").value(hasItem(DEFAULT_DATA.toString())));
    }
    

    @Test
    public void getScrapeData() throws Exception {
        // Initialize the database
        scrapeDataRepository.save(scrapeData);

        // Get the scrapeData
        restScrapeDataMockMvc.perform(get("/api/scrape-data/{id}", scrapeData.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(scrapeData.getId()))
            .andExpect(jsonPath("$.scrapeDataId").value(DEFAULT_SCRAPE_DATA_ID.intValue()))
            .andExpect(jsonPath("$.scrapeId").value(DEFAULT_SCRAPE_ID.intValue()))
            .andExpect(jsonPath("$.domain").value(DEFAULT_DOMAIN.toString()))
            .andExpect(jsonPath("$.host").value(DEFAULT_HOST.toString()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()))
            .andExpect(jsonPath("$.rootScrapeUrl").value(DEFAULT_ROOT_SCRAPE_URL.toString()))
            .andExpect(jsonPath("$.parentUrl").value(DEFAULT_PARENT_URL.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.selector").value(DEFAULT_SELECTOR.toString()))
            .andExpect(jsonPath("$.attr").value(DEFAULT_ATTR.toString()))
            .andExpect(jsonPath("$.dataType").value(DEFAULT_DATA_TYPE.toString()))
            .andExpect(jsonPath("$.data").value(DEFAULT_DATA.toString()));
    }
    @Test
    public void getNonExistingScrapeData() throws Exception {
        // Get the scrapeData
        restScrapeDataMockMvc.perform(get("/api/scrape-data/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateScrapeData() throws Exception {
        // Initialize the database
        scrapeDataService.save(scrapeData);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockScrapeDataSearchRepository);

        int databaseSizeBeforeUpdate = scrapeDataRepository.findAll().size();

        // Update the scrapeData
        ScrapeData updatedScrapeData = scrapeDataRepository.findById(scrapeData.getId()).get();
        updatedScrapeData
            .scrapeDataId(UPDATED_SCRAPE_DATA_ID)
            .scrapeId(UPDATED_SCRAPE_ID)
            .domain(UPDATED_DOMAIN)
            .host(UPDATED_HOST)
            .url(UPDATED_URL)
            .rootScrapeUrl(UPDATED_ROOT_SCRAPE_URL)
            .parentUrl(UPDATED_PARENT_URL)
            .name(UPDATED_NAME)
            .selector(UPDATED_SELECTOR)
            .attr(UPDATED_ATTR)
            .dataType(UPDATED_DATA_TYPE)
            .data(UPDATED_DATA);

        restScrapeDataMockMvc.perform(put("/api/scrape-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedScrapeData)))
            .andExpect(status().isOk());

        // Validate the ScrapeData in the database
        List<ScrapeData> scrapeDataList = scrapeDataRepository.findAll();
        assertThat(scrapeDataList).hasSize(databaseSizeBeforeUpdate);
        ScrapeData testScrapeData = scrapeDataList.get(scrapeDataList.size() - 1);
        assertThat(testScrapeData.getScrapeDataId()).isEqualTo(UPDATED_SCRAPE_DATA_ID);
        assertThat(testScrapeData.getScrapeId()).isEqualTo(UPDATED_SCRAPE_ID);
        assertThat(testScrapeData.getDomain()).isEqualTo(UPDATED_DOMAIN);
        assertThat(testScrapeData.getHost()).isEqualTo(UPDATED_HOST);
        assertThat(testScrapeData.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testScrapeData.getRootScrapeUrl()).isEqualTo(UPDATED_ROOT_SCRAPE_URL);
        assertThat(testScrapeData.getParentUrl()).isEqualTo(UPDATED_PARENT_URL);
        assertThat(testScrapeData.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testScrapeData.getSelector()).isEqualTo(UPDATED_SELECTOR);
        assertThat(testScrapeData.getAttr()).isEqualTo(UPDATED_ATTR);
        assertThat(testScrapeData.getDataType()).isEqualTo(UPDATED_DATA_TYPE);
        assertThat(testScrapeData.getData()).isEqualTo(UPDATED_DATA);

        // Validate the ScrapeData in Elasticsearch
        verify(mockScrapeDataSearchRepository, times(1)).save(testScrapeData);
    }

    @Test
    public void updateNonExistingScrapeData() throws Exception {
        int databaseSizeBeforeUpdate = scrapeDataRepository.findAll().size();

        // Create the ScrapeData

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restScrapeDataMockMvc.perform(put("/api/scrape-data")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(scrapeData)))
            .andExpect(status().isBadRequest());

        // Validate the ScrapeData in the database
        List<ScrapeData> scrapeDataList = scrapeDataRepository.findAll();
        assertThat(scrapeDataList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ScrapeData in Elasticsearch
        verify(mockScrapeDataSearchRepository, times(0)).save(scrapeData);
    }

    @Test
    public void deleteScrapeData() throws Exception {
        // Initialize the database
        scrapeDataService.save(scrapeData);

        int databaseSizeBeforeDelete = scrapeDataRepository.findAll().size();

        // Get the scrapeData
        restScrapeDataMockMvc.perform(delete("/api/scrape-data/{id}", scrapeData.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ScrapeData> scrapeDataList = scrapeDataRepository.findAll();
        assertThat(scrapeDataList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ScrapeData in Elasticsearch
        verify(mockScrapeDataSearchRepository, times(1)).deleteById(scrapeData.getId());
    }

    @Test
    public void searchScrapeData() throws Exception {
        // Initialize the database
        scrapeDataService.save(scrapeData);
        when(mockScrapeDataSearchRepository.search(queryStringQuery("id:" + scrapeData.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(scrapeData), PageRequest.of(0, 1), 1));
        // Search the scrapeData
        restScrapeDataMockMvc.perform(get("/api/_search/scrape-data?query=id:" + scrapeData.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(scrapeData.getId())))
            .andExpect(jsonPath("$.[*].scrapeDataId").value(hasItem(DEFAULT_SCRAPE_DATA_ID.intValue())))
            .andExpect(jsonPath("$.[*].scrapeId").value(hasItem(DEFAULT_SCRAPE_ID.intValue())))
            .andExpect(jsonPath("$.[*].domain").value(hasItem(DEFAULT_DOMAIN.toString())))
            .andExpect(jsonPath("$.[*].host").value(hasItem(DEFAULT_HOST.toString())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
            .andExpect(jsonPath("$.[*].rootScrapeUrl").value(hasItem(DEFAULT_ROOT_SCRAPE_URL.toString())))
            .andExpect(jsonPath("$.[*].parentUrl").value(hasItem(DEFAULT_PARENT_URL.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].selector").value(hasItem(DEFAULT_SELECTOR.toString())))
            .andExpect(jsonPath("$.[*].attr").value(hasItem(DEFAULT_ATTR.toString())))
            .andExpect(jsonPath("$.[*].dataType").value(hasItem(DEFAULT_DATA_TYPE.toString())))
            .andExpect(jsonPath("$.[*].data").value(hasItem(DEFAULT_DATA.toString())));
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ScrapeData.class);
        ScrapeData scrapeData1 = new ScrapeData();
        scrapeData1.setId("id1");
        ScrapeData scrapeData2 = new ScrapeData();
        scrapeData2.setId(scrapeData1.getId());
        assertThat(scrapeData1).isEqualTo(scrapeData2);
        scrapeData2.setId("id2");
        assertThat(scrapeData1).isNotEqualTo(scrapeData2);
        scrapeData1.setId(null);
        assertThat(scrapeData1).isNotEqualTo(scrapeData2);
    }
}
