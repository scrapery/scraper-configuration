package io.github.scrapery.setting.web.rest;

import io.github.scrapery.setting.ScraperSettingApp;

import io.github.scrapery.setting.domain.ConfigSite;
import io.github.scrapery.setting.repository.ConfigSiteRepository;
import io.github.scrapery.setting.repository.search.ConfigSiteSearchRepository;
import io.github.scrapery.setting.service.ConfigSiteService;
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
/**
 * Test class for the ConfigSiteResource REST controller.
 *
 * @see ConfigSiteResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ScraperSettingApp.class)
public class ConfigSiteResourceIntTest {

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_HOST = "AAAAAAAAAA";
    private static final String UPDATED_HOST = "BBBBBBBBBB";

    private static final String DEFAULT_CONFIG_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CONFIG_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_TOTAL_LEVEL = 1;
    private static final Integer UPDATED_TOTAL_LEVEL = 2;

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    private static final FetchEngine DEFAULT_FETCH_ENGINE = FetchEngine.SELENIUM;
    private static final FetchEngine UPDATED_FETCH_ENGINE = FetchEngine.HTTP;

    @Autowired
    private ConfigSiteRepository configSiteRepository;
    @Mock
    private ConfigSiteRepository configSiteRepositoryMock;
    
    @Mock
    private ConfigSiteService configSiteServiceMock;

    @Autowired
    private ConfigSiteService configSiteService;

    /**
     * This repository is mocked in the io.github.scrapery.setting.repository.search test package.
     *
     * @see io.github.scrapery.setting.repository.search.ConfigSiteSearchRepositoryMockConfiguration
     */
    @Autowired
    private ConfigSiteSearchRepository mockConfigSiteSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restConfigSiteMockMvc;

    private ConfigSite configSite;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ConfigSiteResource configSiteResource = new ConfigSiteResource(configSiteService);
        this.restConfigSiteMockMvc = MockMvcBuilders.standaloneSetup(configSiteResource)
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
    public static ConfigSite createEntity() {
        ConfigSite configSite = new ConfigSite()
            .url(DEFAULT_URL)
            .name(DEFAULT_NAME)
            .host(DEFAULT_HOST)
            .configName(DEFAULT_CONFIG_NAME)
            .totalLevel(DEFAULT_TOTAL_LEVEL)
            .userId(DEFAULT_USER_ID)
            .fetchEngine(DEFAULT_FETCH_ENGINE);
        return configSite;
    }

    @Before
    public void initTest() {
        configSiteRepository.deleteAll();
        configSite = createEntity();
    }

    @Test
    public void createConfigSite() throws Exception {
        int databaseSizeBeforeCreate = configSiteRepository.findAll().size();

        // Create the ConfigSite
        restConfigSiteMockMvc.perform(post("/api/config-sites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configSite)))
            .andExpect(status().isCreated());

        // Validate the ConfigSite in the database
        List<ConfigSite> configSiteList = configSiteRepository.findAll();
        assertThat(configSiteList).hasSize(databaseSizeBeforeCreate + 1);
        ConfigSite testConfigSite = configSiteList.get(configSiteList.size() - 1);
        assertThat(testConfigSite.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testConfigSite.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testConfigSite.getHost()).isEqualTo(DEFAULT_HOST);
        assertThat(testConfigSite.getConfigName()).isEqualTo(DEFAULT_CONFIG_NAME);
        assertThat(testConfigSite.getTotalLevel()).isEqualTo(DEFAULT_TOTAL_LEVEL);
        assertThat(testConfigSite.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testConfigSite.getFetchEngine()).isEqualTo(DEFAULT_FETCH_ENGINE);

        // Validate the ConfigSite in Elasticsearch
        verify(mockConfigSiteSearchRepository, times(1)).save(testConfigSite);
    }

    @Test
    public void createConfigSiteWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = configSiteRepository.findAll().size();

        // Create the ConfigSite with an existing ID
        configSite.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restConfigSiteMockMvc.perform(post("/api/config-sites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configSite)))
            .andExpect(status().isBadRequest());

        // Validate the ConfigSite in the database
        List<ConfigSite> configSiteList = configSiteRepository.findAll();
        assertThat(configSiteList).hasSize(databaseSizeBeforeCreate);

        // Validate the ConfigSite in Elasticsearch
        verify(mockConfigSiteSearchRepository, times(0)).save(configSite);
    }

    @Test
    public void getAllConfigSites() throws Exception {
        // Initialize the database
        configSiteRepository.save(configSite);

        // Get all the configSiteList
        restConfigSiteMockMvc.perform(get("/api/config-sites?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(configSite.getId())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].host").value(hasItem(DEFAULT_HOST.toString())))
            .andExpect(jsonPath("$.[*].configName").value(hasItem(DEFAULT_CONFIG_NAME.toString())))
            .andExpect(jsonPath("$.[*].totalLevel").value(hasItem(DEFAULT_TOTAL_LEVEL)))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].fetchEngine").value(hasItem(DEFAULT_FETCH_ENGINE.toString())));
    }
    
    public void getAllConfigSitesWithEagerRelationshipsIsEnabled() throws Exception {
        ConfigSiteResource configSiteResource = new ConfigSiteResource(configSiteServiceMock);
        when(configSiteServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restConfigSiteMockMvc = MockMvcBuilders.standaloneSetup(configSiteResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restConfigSiteMockMvc.perform(get("/api/config-sites?eagerload=true"))
        .andExpect(status().isOk());

        verify(configSiteServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    public void getAllConfigSitesWithEagerRelationshipsIsNotEnabled() throws Exception {
        ConfigSiteResource configSiteResource = new ConfigSiteResource(configSiteServiceMock);
            when(configSiteServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restConfigSiteMockMvc = MockMvcBuilders.standaloneSetup(configSiteResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restConfigSiteMockMvc.perform(get("/api/config-sites?eagerload=true"))
        .andExpect(status().isOk());

            verify(configSiteServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    public void getConfigSite() throws Exception {
        // Initialize the database
        configSiteRepository.save(configSite);

        // Get the configSite
        restConfigSiteMockMvc.perform(get("/api/config-sites/{id}", configSite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(configSite.getId()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.host").value(DEFAULT_HOST.toString()))
            .andExpect(jsonPath("$.configName").value(DEFAULT_CONFIG_NAME.toString()))
            .andExpect(jsonPath("$.totalLevel").value(DEFAULT_TOTAL_LEVEL))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.fetchEngine").value(DEFAULT_FETCH_ENGINE.toString()));
    }
    @Test
    public void getNonExistingConfigSite() throws Exception {
        // Get the configSite
        restConfigSiteMockMvc.perform(get("/api/config-sites/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateConfigSite() throws Exception {
        // Initialize the database
        configSiteService.save(configSite);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockConfigSiteSearchRepository);

        int databaseSizeBeforeUpdate = configSiteRepository.findAll().size();

        // Update the configSite
        ConfigSite updatedConfigSite = configSiteRepository.findById(configSite.getId()).get();
        updatedConfigSite
            .url(UPDATED_URL)
            .name(UPDATED_NAME)
            .host(UPDATED_HOST)
            .configName(UPDATED_CONFIG_NAME)
            .totalLevel(UPDATED_TOTAL_LEVEL)
            .userId(UPDATED_USER_ID)
            .fetchEngine(UPDATED_FETCH_ENGINE);

        restConfigSiteMockMvc.perform(put("/api/config-sites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedConfigSite)))
            .andExpect(status().isOk());

        // Validate the ConfigSite in the database
        List<ConfigSite> configSiteList = configSiteRepository.findAll();
        assertThat(configSiteList).hasSize(databaseSizeBeforeUpdate);
        ConfigSite testConfigSite = configSiteList.get(configSiteList.size() - 1);
        assertThat(testConfigSite.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testConfigSite.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testConfigSite.getHost()).isEqualTo(UPDATED_HOST);
        assertThat(testConfigSite.getConfigName()).isEqualTo(UPDATED_CONFIG_NAME);
        assertThat(testConfigSite.getTotalLevel()).isEqualTo(UPDATED_TOTAL_LEVEL);
        assertThat(testConfigSite.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testConfigSite.getFetchEngine()).isEqualTo(UPDATED_FETCH_ENGINE);

        // Validate the ConfigSite in Elasticsearch
        verify(mockConfigSiteSearchRepository, times(1)).save(testConfigSite);
    }

    @Test
    public void updateNonExistingConfigSite() throws Exception {
        int databaseSizeBeforeUpdate = configSiteRepository.findAll().size();

        // Create the ConfigSite

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restConfigSiteMockMvc.perform(put("/api/config-sites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configSite)))
            .andExpect(status().isBadRequest());

        // Validate the ConfigSite in the database
        List<ConfigSite> configSiteList = configSiteRepository.findAll();
        assertThat(configSiteList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ConfigSite in Elasticsearch
        verify(mockConfigSiteSearchRepository, times(0)).save(configSite);
    }

    @Test
    public void deleteConfigSite() throws Exception {
        // Initialize the database
        configSiteService.save(configSite);

        int databaseSizeBeforeDelete = configSiteRepository.findAll().size();

        // Get the configSite
        restConfigSiteMockMvc.perform(delete("/api/config-sites/{id}", configSite.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ConfigSite> configSiteList = configSiteRepository.findAll();
        assertThat(configSiteList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ConfigSite in Elasticsearch
        verify(mockConfigSiteSearchRepository, times(1)).deleteById(configSite.getId());
    }

    @Test
    public void searchConfigSite() throws Exception {
        // Initialize the database
        configSiteService.save(configSite);
        when(mockConfigSiteSearchRepository.search(queryStringQuery("id:" + configSite.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(configSite), PageRequest.of(0, 1), 1));
        // Search the configSite
        restConfigSiteMockMvc.perform(get("/api/_search/config-sites?query=id:" + configSite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(configSite.getId())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].host").value(hasItem(DEFAULT_HOST.toString())))
            .andExpect(jsonPath("$.[*].configName").value(hasItem(DEFAULT_CONFIG_NAME.toString())))
            .andExpect(jsonPath("$.[*].totalLevel").value(hasItem(DEFAULT_TOTAL_LEVEL)))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].fetchEngine").value(hasItem(DEFAULT_FETCH_ENGINE.toString())));
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfigSite.class);
        ConfigSite configSite1 = new ConfigSite();
        configSite1.setId("id1");
        ConfigSite configSite2 = new ConfigSite();
        configSite2.setId(configSite1.getId());
        assertThat(configSite1).isEqualTo(configSite2);
        configSite2.setId("id2");
        assertThat(configSite1).isNotEqualTo(configSite2);
        configSite1.setId(null);
        assertThat(configSite1).isNotEqualTo(configSite2);
    }
}
