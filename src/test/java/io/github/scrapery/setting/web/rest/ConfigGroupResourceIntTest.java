package io.github.scrapery.setting.web.rest;

import io.github.scrapery.setting.ScraperSettingApp;

import io.github.scrapery.setting.domain.ConfigGroup;
import io.github.scrapery.setting.repository.ConfigGroupRepository;
import io.github.scrapery.setting.repository.search.ConfigGroupSearchRepository;
import io.github.scrapery.setting.service.ConfigGroupService;
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

import io.github.scrapery.setting.domain.enumeration.ExpectResultType;
/**
 * Test class for the ConfigGroupResource REST controller.
 *
 * @see ConfigGroupResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ScraperSettingApp.class)
public class ConfigGroupResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_HOST = "AAAAAAAAAA";
    private static final String UPDATED_HOST = "BBBBBBBBBB";

    private static final Integer DEFAULT_CURRENT_LEVEL = 1;
    private static final Integer UPDATED_CURRENT_LEVEL = 2;

    private static final ExpectResultType DEFAULT_EXPECT_RESULT_TYPE = ExpectResultType.OBJECT;
    private static final ExpectResultType UPDATED_EXPECT_RESULT_TYPE = ExpectResultType.ARRAY;

    @Autowired
    private ConfigGroupRepository configGroupRepository;
    @Mock
    private ConfigGroupRepository configGroupRepositoryMock;
    
    @Mock
    private ConfigGroupService configGroupServiceMock;

    @Autowired
    private ConfigGroupService configGroupService;

    /**
     * This repository is mocked in the io.github.scrapery.setting.repository.search test package.
     *
     * @see io.github.scrapery.setting.repository.search.ConfigGroupSearchRepositoryMockConfiguration
     */
    @Autowired
    private ConfigGroupSearchRepository mockConfigGroupSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restConfigGroupMockMvc;

    private ConfigGroup configGroup;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ConfigGroupResource configGroupResource = new ConfigGroupResource(configGroupService);
        this.restConfigGroupMockMvc = MockMvcBuilders.standaloneSetup(configGroupResource)
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
    public static ConfigGroup createEntity() {
        ConfigGroup configGroup = new ConfigGroup()
            .name(DEFAULT_NAME)
            .host(DEFAULT_HOST)
            .currentLevel(DEFAULT_CURRENT_LEVEL)
            .expectResultType(DEFAULT_EXPECT_RESULT_TYPE);
        return configGroup;
    }

    @Before
    public void initTest() {
        configGroupRepository.deleteAll();
        configGroup = createEntity();
    }

    @Test
    public void createConfigGroup() throws Exception {
        int databaseSizeBeforeCreate = configGroupRepository.findAll().size();

        // Create the ConfigGroup
        restConfigGroupMockMvc.perform(post("/api/config-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configGroup)))
            .andExpect(status().isCreated());

        // Validate the ConfigGroup in the database
        List<ConfigGroup> configGroupList = configGroupRepository.findAll();
        assertThat(configGroupList).hasSize(databaseSizeBeforeCreate + 1);
        ConfigGroup testConfigGroup = configGroupList.get(configGroupList.size() - 1);
        assertThat(testConfigGroup.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testConfigGroup.getHost()).isEqualTo(DEFAULT_HOST);
        assertThat(testConfigGroup.getCurrentLevel()).isEqualTo(DEFAULT_CURRENT_LEVEL);
        assertThat(testConfigGroup.getExpectResultType()).isEqualTo(DEFAULT_EXPECT_RESULT_TYPE);

        // Validate the ConfigGroup in Elasticsearch
        verify(mockConfigGroupSearchRepository, times(1)).save(testConfigGroup);
    }

    @Test
    public void createConfigGroupWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = configGroupRepository.findAll().size();

        // Create the ConfigGroup with an existing ID
        configGroup.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restConfigGroupMockMvc.perform(post("/api/config-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configGroup)))
            .andExpect(status().isBadRequest());

        // Validate the ConfigGroup in the database
        List<ConfigGroup> configGroupList = configGroupRepository.findAll();
        assertThat(configGroupList).hasSize(databaseSizeBeforeCreate);

        // Validate the ConfigGroup in Elasticsearch
        verify(mockConfigGroupSearchRepository, times(0)).save(configGroup);
    }

    @Test
    public void getAllConfigGroups() throws Exception {
        // Initialize the database
        configGroupRepository.save(configGroup);

        // Get all the configGroupList
        restConfigGroupMockMvc.perform(get("/api/config-groups?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(configGroup.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].host").value(hasItem(DEFAULT_HOST.toString())))
            .andExpect(jsonPath("$.[*].currentLevel").value(hasItem(DEFAULT_CURRENT_LEVEL)))
            .andExpect(jsonPath("$.[*].expectResultType").value(hasItem(DEFAULT_EXPECT_RESULT_TYPE.toString())));
    }
    
    public void getAllConfigGroupsWithEagerRelationshipsIsEnabled() throws Exception {
        ConfigGroupResource configGroupResource = new ConfigGroupResource(configGroupServiceMock);
        when(configGroupServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restConfigGroupMockMvc = MockMvcBuilders.standaloneSetup(configGroupResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restConfigGroupMockMvc.perform(get("/api/config-groups?eagerload=true"))
        .andExpect(status().isOk());

        verify(configGroupServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    public void getAllConfigGroupsWithEagerRelationshipsIsNotEnabled() throws Exception {
        ConfigGroupResource configGroupResource = new ConfigGroupResource(configGroupServiceMock);
            when(configGroupServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restConfigGroupMockMvc = MockMvcBuilders.standaloneSetup(configGroupResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restConfigGroupMockMvc.perform(get("/api/config-groups?eagerload=true"))
        .andExpect(status().isOk());

            verify(configGroupServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    public void getConfigGroup() throws Exception {
        // Initialize the database
        configGroupRepository.save(configGroup);

        // Get the configGroup
        restConfigGroupMockMvc.perform(get("/api/config-groups/{id}", configGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(configGroup.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.host").value(DEFAULT_HOST.toString()))
            .andExpect(jsonPath("$.currentLevel").value(DEFAULT_CURRENT_LEVEL))
            .andExpect(jsonPath("$.expectResultType").value(DEFAULT_EXPECT_RESULT_TYPE.toString()));
    }
    @Test
    public void getNonExistingConfigGroup() throws Exception {
        // Get the configGroup
        restConfigGroupMockMvc.perform(get("/api/config-groups/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateConfigGroup() throws Exception {
        // Initialize the database
        configGroupService.save(configGroup);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockConfigGroupSearchRepository);

        int databaseSizeBeforeUpdate = configGroupRepository.findAll().size();

        // Update the configGroup
        ConfigGroup updatedConfigGroup = configGroupRepository.findById(configGroup.getId()).get();
        updatedConfigGroup
            .name(UPDATED_NAME)
            .host(UPDATED_HOST)
            .currentLevel(UPDATED_CURRENT_LEVEL)
            .expectResultType(UPDATED_EXPECT_RESULT_TYPE);

        restConfigGroupMockMvc.perform(put("/api/config-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedConfigGroup)))
            .andExpect(status().isOk());

        // Validate the ConfigGroup in the database
        List<ConfigGroup> configGroupList = configGroupRepository.findAll();
        assertThat(configGroupList).hasSize(databaseSizeBeforeUpdate);
        ConfigGroup testConfigGroup = configGroupList.get(configGroupList.size() - 1);
        assertThat(testConfigGroup.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testConfigGroup.getHost()).isEqualTo(UPDATED_HOST);
        assertThat(testConfigGroup.getCurrentLevel()).isEqualTo(UPDATED_CURRENT_LEVEL);
        assertThat(testConfigGroup.getExpectResultType()).isEqualTo(UPDATED_EXPECT_RESULT_TYPE);

        // Validate the ConfigGroup in Elasticsearch
        verify(mockConfigGroupSearchRepository, times(1)).save(testConfigGroup);
    }

    @Test
    public void updateNonExistingConfigGroup() throws Exception {
        int databaseSizeBeforeUpdate = configGroupRepository.findAll().size();

        // Create the ConfigGroup

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restConfigGroupMockMvc.perform(put("/api/config-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configGroup)))
            .andExpect(status().isBadRequest());

        // Validate the ConfigGroup in the database
        List<ConfigGroup> configGroupList = configGroupRepository.findAll();
        assertThat(configGroupList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ConfigGroup in Elasticsearch
        verify(mockConfigGroupSearchRepository, times(0)).save(configGroup);
    }

    @Test
    public void deleteConfigGroup() throws Exception {
        // Initialize the database
        configGroupService.save(configGroup);

        int databaseSizeBeforeDelete = configGroupRepository.findAll().size();

        // Get the configGroup
        restConfigGroupMockMvc.perform(delete("/api/config-groups/{id}", configGroup.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ConfigGroup> configGroupList = configGroupRepository.findAll();
        assertThat(configGroupList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ConfigGroup in Elasticsearch
        verify(mockConfigGroupSearchRepository, times(1)).deleteById(configGroup.getId());
    }

    @Test
    public void searchConfigGroup() throws Exception {
        // Initialize the database
        configGroupService.save(configGroup);
        when(mockConfigGroupSearchRepository.search(queryStringQuery("id:" + configGroup.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(configGroup), PageRequest.of(0, 1), 1));
        // Search the configGroup
        restConfigGroupMockMvc.perform(get("/api/_search/config-groups?query=id:" + configGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(configGroup.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].host").value(hasItem(DEFAULT_HOST.toString())))
            .andExpect(jsonPath("$.[*].currentLevel").value(hasItem(DEFAULT_CURRENT_LEVEL)))
            .andExpect(jsonPath("$.[*].expectResultType").value(hasItem(DEFAULT_EXPECT_RESULT_TYPE.toString())));
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfigGroup.class);
        ConfigGroup configGroup1 = new ConfigGroup();
        configGroup1.setId("id1");
        ConfigGroup configGroup2 = new ConfigGroup();
        configGroup2.setId(configGroup1.getId());
        assertThat(configGroup1).isEqualTo(configGroup2);
        configGroup2.setId("id2");
        assertThat(configGroup1).isNotEqualTo(configGroup2);
        configGroup1.setId(null);
        assertThat(configGroup1).isNotEqualTo(configGroup2);
    }
}
