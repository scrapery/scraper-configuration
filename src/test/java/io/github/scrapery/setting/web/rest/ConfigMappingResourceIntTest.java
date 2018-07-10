package io.github.scrapery.setting.web.rest;

import io.github.scrapery.setting.ScraperSettingApp;

import io.github.scrapery.setting.domain.ConfigMapping;
import io.github.scrapery.setting.repository.ConfigMappingRepository;
import io.github.scrapery.setting.repository.search.ConfigMappingSearchRepository;
import io.github.scrapery.setting.service.ConfigMappingService;
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
 * Test class for the ConfigMappingResource REST controller.
 *
 * @see ConfigMappingResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ScraperSettingApp.class)
public class ConfigMappingResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SELECTOR = "AAAAAAAAAA";
    private static final String UPDATED_SELECTOR = "BBBBBBBBBB";

    private static final String DEFAULT_HOST = "AAAAAAAAAA";
    private static final String UPDATED_HOST = "BBBBBBBBBB";

    private static final String DEFAULT_CONFIG_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CONFIG_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ATTR = "AAAAAAAAAA";
    private static final String UPDATED_ATTR = "BBBBBBBBBB";

    private static final ConfigDataType DEFAULT_DATA_TYPE = ConfigDataType.INTEGER;
    private static final ConfigDataType UPDATED_DATA_TYPE = ConfigDataType.STRING;

    @Autowired
    private ConfigMappingRepository configMappingRepository;

    

    @Autowired
    private ConfigMappingService configMappingService;

    /**
     * This repository is mocked in the io.github.scrapery.setting.repository.search test package.
     *
     * @see io.github.scrapery.setting.repository.search.ConfigMappingSearchRepositoryMockConfiguration
     */
    @Autowired
    private ConfigMappingSearchRepository mockConfigMappingSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restConfigMappingMockMvc;

    private ConfigMapping configMapping;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ConfigMappingResource configMappingResource = new ConfigMappingResource(configMappingService);
        this.restConfigMappingMockMvc = MockMvcBuilders.standaloneSetup(configMappingResource)
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
    public static ConfigMapping createEntity() {
        ConfigMapping configMapping = new ConfigMapping()
            .name(DEFAULT_NAME)
            .selector(DEFAULT_SELECTOR)
            .host(DEFAULT_HOST)
            .configName(DEFAULT_CONFIG_NAME)
            .attr(DEFAULT_ATTR)
            .dataType(DEFAULT_DATA_TYPE);
        return configMapping;
    }

    @Before
    public void initTest() {
        configMappingRepository.deleteAll();
        configMapping = createEntity();
    }

    @Test
    public void createConfigMapping() throws Exception {
        int databaseSizeBeforeCreate = configMappingRepository.findAll().size();

        // Create the ConfigMapping
        restConfigMappingMockMvc.perform(post("/api/config-mappings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configMapping)))
            .andExpect(status().isCreated());

        // Validate the ConfigMapping in the database
        List<ConfigMapping> configMappingList = configMappingRepository.findAll();
        assertThat(configMappingList).hasSize(databaseSizeBeforeCreate + 1);
        ConfigMapping testConfigMapping = configMappingList.get(configMappingList.size() - 1);
        assertThat(testConfigMapping.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testConfigMapping.getSelector()).isEqualTo(DEFAULT_SELECTOR);
        assertThat(testConfigMapping.getHost()).isEqualTo(DEFAULT_HOST);
        assertThat(testConfigMapping.getConfigName()).isEqualTo(DEFAULT_CONFIG_NAME);
        assertThat(testConfigMapping.getAttr()).isEqualTo(DEFAULT_ATTR);
        assertThat(testConfigMapping.getDataType()).isEqualTo(DEFAULT_DATA_TYPE);

        // Validate the ConfigMapping in Elasticsearch
        verify(mockConfigMappingSearchRepository, times(1)).save(testConfigMapping);
    }

    @Test
    public void createConfigMappingWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = configMappingRepository.findAll().size();

        // Create the ConfigMapping with an existing ID
        configMapping.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restConfigMappingMockMvc.perform(post("/api/config-mappings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configMapping)))
            .andExpect(status().isBadRequest());

        // Validate the ConfigMapping in the database
        List<ConfigMapping> configMappingList = configMappingRepository.findAll();
        assertThat(configMappingList).hasSize(databaseSizeBeforeCreate);

        // Validate the ConfigMapping in Elasticsearch
        verify(mockConfigMappingSearchRepository, times(0)).save(configMapping);
    }

    @Test
    public void getAllConfigMappings() throws Exception {
        // Initialize the database
        configMappingRepository.save(configMapping);

        // Get all the configMappingList
        restConfigMappingMockMvc.perform(get("/api/config-mappings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(configMapping.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].selector").value(hasItem(DEFAULT_SELECTOR.toString())))
            .andExpect(jsonPath("$.[*].host").value(hasItem(DEFAULT_HOST.toString())))
            .andExpect(jsonPath("$.[*].configName").value(hasItem(DEFAULT_CONFIG_NAME.toString())))
            .andExpect(jsonPath("$.[*].attr").value(hasItem(DEFAULT_ATTR.toString())))
            .andExpect(jsonPath("$.[*].dataType").value(hasItem(DEFAULT_DATA_TYPE.toString())));
    }
    

    @Test
    public void getConfigMapping() throws Exception {
        // Initialize the database
        configMappingRepository.save(configMapping);

        // Get the configMapping
        restConfigMappingMockMvc.perform(get("/api/config-mappings/{id}", configMapping.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(configMapping.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.selector").value(DEFAULT_SELECTOR.toString()))
            .andExpect(jsonPath("$.host").value(DEFAULT_HOST.toString()))
            .andExpect(jsonPath("$.configName").value(DEFAULT_CONFIG_NAME.toString()))
            .andExpect(jsonPath("$.attr").value(DEFAULT_ATTR.toString()))
            .andExpect(jsonPath("$.dataType").value(DEFAULT_DATA_TYPE.toString()));
    }
    @Test
    public void getNonExistingConfigMapping() throws Exception {
        // Get the configMapping
        restConfigMappingMockMvc.perform(get("/api/config-mappings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateConfigMapping() throws Exception {
        // Initialize the database
        configMappingService.save(configMapping);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockConfigMappingSearchRepository);

        int databaseSizeBeforeUpdate = configMappingRepository.findAll().size();

        // Update the configMapping
        ConfigMapping updatedConfigMapping = configMappingRepository.findById(configMapping.getId()).get();
        updatedConfigMapping
            .name(UPDATED_NAME)
            .selector(UPDATED_SELECTOR)
            .host(UPDATED_HOST)
            .configName(UPDATED_CONFIG_NAME)
            .attr(UPDATED_ATTR)
            .dataType(UPDATED_DATA_TYPE);

        restConfigMappingMockMvc.perform(put("/api/config-mappings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedConfigMapping)))
            .andExpect(status().isOk());

        // Validate the ConfigMapping in the database
        List<ConfigMapping> configMappingList = configMappingRepository.findAll();
        assertThat(configMappingList).hasSize(databaseSizeBeforeUpdate);
        ConfigMapping testConfigMapping = configMappingList.get(configMappingList.size() - 1);
        assertThat(testConfigMapping.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testConfigMapping.getSelector()).isEqualTo(UPDATED_SELECTOR);
        assertThat(testConfigMapping.getHost()).isEqualTo(UPDATED_HOST);
        assertThat(testConfigMapping.getConfigName()).isEqualTo(UPDATED_CONFIG_NAME);
        assertThat(testConfigMapping.getAttr()).isEqualTo(UPDATED_ATTR);
        assertThat(testConfigMapping.getDataType()).isEqualTo(UPDATED_DATA_TYPE);

        // Validate the ConfigMapping in Elasticsearch
        verify(mockConfigMappingSearchRepository, times(1)).save(testConfigMapping);
    }

    @Test
    public void updateNonExistingConfigMapping() throws Exception {
        int databaseSizeBeforeUpdate = configMappingRepository.findAll().size();

        // Create the ConfigMapping

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restConfigMappingMockMvc.perform(put("/api/config-mappings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configMapping)))
            .andExpect(status().isBadRequest());

        // Validate the ConfigMapping in the database
        List<ConfigMapping> configMappingList = configMappingRepository.findAll();
        assertThat(configMappingList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ConfigMapping in Elasticsearch
        verify(mockConfigMappingSearchRepository, times(0)).save(configMapping);
    }

    @Test
    public void deleteConfigMapping() throws Exception {
        // Initialize the database
        configMappingService.save(configMapping);

        int databaseSizeBeforeDelete = configMappingRepository.findAll().size();

        // Get the configMapping
        restConfigMappingMockMvc.perform(delete("/api/config-mappings/{id}", configMapping.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ConfigMapping> configMappingList = configMappingRepository.findAll();
        assertThat(configMappingList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ConfigMapping in Elasticsearch
        verify(mockConfigMappingSearchRepository, times(1)).deleteById(configMapping.getId());
    }

    @Test
    public void searchConfigMapping() throws Exception {
        // Initialize the database
        configMappingService.save(configMapping);
        when(mockConfigMappingSearchRepository.search(queryStringQuery("id:" + configMapping.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(configMapping), PageRequest.of(0, 1), 1));
        // Search the configMapping
        restConfigMappingMockMvc.perform(get("/api/_search/config-mappings?query=id:" + configMapping.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(configMapping.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].selector").value(hasItem(DEFAULT_SELECTOR.toString())))
            .andExpect(jsonPath("$.[*].host").value(hasItem(DEFAULT_HOST.toString())))
            .andExpect(jsonPath("$.[*].configName").value(hasItem(DEFAULT_CONFIG_NAME.toString())))
            .andExpect(jsonPath("$.[*].attr").value(hasItem(DEFAULT_ATTR.toString())))
            .andExpect(jsonPath("$.[*].dataType").value(hasItem(DEFAULT_DATA_TYPE.toString())));
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfigMapping.class);
        ConfigMapping configMapping1 = new ConfigMapping();
        configMapping1.setId("id1");
        ConfigMapping configMapping2 = new ConfigMapping();
        configMapping2.setId(configMapping1.getId());
        assertThat(configMapping1).isEqualTo(configMapping2);
        configMapping2.setId("id2");
        assertThat(configMapping1).isNotEqualTo(configMapping2);
        configMapping1.setId(null);
        assertThat(configMapping1).isNotEqualTo(configMapping2);
    }
}
