package io.github.scrapery.setting.web.rest;

import io.github.scrapery.setting.ScraperSettingApp;

import io.github.scrapery.setting.domain.ConfigSiteLogin;
import io.github.scrapery.setting.repository.ConfigSiteLoginRepository;
import io.github.scrapery.setting.repository.search.ConfigSiteLoginSearchRepository;
import io.github.scrapery.setting.service.ConfigSiteLoginService;
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
 * Test class for the ConfigSiteLoginResource REST controller.
 *
 * @see ConfigSiteLoginResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ScraperSettingApp.class)
public class ConfigSiteLoginResourceIntTest {

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String DEFAULT_DOMAIN = "AAAAAAAAAA";
    private static final String UPDATED_DOMAIN = "BBBBBBBBBB";

    private static final String DEFAULT_HOST = "AAAAAAAAAA";
    private static final String UPDATED_HOST = "BBBBBBBBBB";

    private static final String DEFAULT_SELECTOR_ACTION = "AAAAAAAAAA";
    private static final String UPDATED_SELECTOR_ACTION = "BBBBBBBBBB";

    private static final String DEFAULT_SELECTOR_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_SELECTOR_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_SELECTOR_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_SELECTOR_PASSWORD = "BBBBBBBBBB";

    private static final String DEFAULT_SELECTOR_BUTTON_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_SELECTOR_BUTTON_LOGIN = "BBBBBBBBBB";

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String DEFAULT_REDIRECT_URL = "AAAAAAAAAA";
    private static final String UPDATED_REDIRECT_URL = "BBBBBBBBBB";

    @Autowired
    private ConfigSiteLoginRepository configSiteLoginRepository;

    

    @Autowired
    private ConfigSiteLoginService configSiteLoginService;

    /**
     * This repository is mocked in the io.github.scrapery.setting.repository.search test package.
     *
     * @see io.github.scrapery.setting.repository.search.ConfigSiteLoginSearchRepositoryMockConfiguration
     */
    @Autowired
    private ConfigSiteLoginSearchRepository mockConfigSiteLoginSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restConfigSiteLoginMockMvc;

    private ConfigSiteLogin configSiteLogin;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ConfigSiteLoginResource configSiteLoginResource = new ConfigSiteLoginResource(configSiteLoginService);
        this.restConfigSiteLoginMockMvc = MockMvcBuilders.standaloneSetup(configSiteLoginResource)
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
    public static ConfigSiteLogin createEntity() {
        ConfigSiteLogin configSiteLogin = new ConfigSiteLogin()
            .url(DEFAULT_URL)
            .domain(DEFAULT_DOMAIN)
            .host(DEFAULT_HOST)
            .selectorAction(DEFAULT_SELECTOR_ACTION)
            .selectorUsername(DEFAULT_SELECTOR_USERNAME)
            .selectorPassword(DEFAULT_SELECTOR_PASSWORD)
            .selectorButtonLogin(DEFAULT_SELECTOR_BUTTON_LOGIN)
            .username(DEFAULT_USERNAME)
            .password(DEFAULT_PASSWORD)
            .redirectUrl(DEFAULT_REDIRECT_URL);
        return configSiteLogin;
    }

    @Before
    public void initTest() {
        configSiteLoginRepository.deleteAll();
        configSiteLogin = createEntity();
    }

    @Test
    public void createConfigSiteLogin() throws Exception {
        int databaseSizeBeforeCreate = configSiteLoginRepository.findAll().size();

        // Create the ConfigSiteLogin
        restConfigSiteLoginMockMvc.perform(post("/api/config-site-logins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configSiteLogin)))
            .andExpect(status().isCreated());

        // Validate the ConfigSiteLogin in the database
        List<ConfigSiteLogin> configSiteLoginList = configSiteLoginRepository.findAll();
        assertThat(configSiteLoginList).hasSize(databaseSizeBeforeCreate + 1);
        ConfigSiteLogin testConfigSiteLogin = configSiteLoginList.get(configSiteLoginList.size() - 1);
        assertThat(testConfigSiteLogin.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testConfigSiteLogin.getDomain()).isEqualTo(DEFAULT_DOMAIN);
        assertThat(testConfigSiteLogin.getHost()).isEqualTo(DEFAULT_HOST);
        assertThat(testConfigSiteLogin.getSelectorAction()).isEqualTo(DEFAULT_SELECTOR_ACTION);
        assertThat(testConfigSiteLogin.getSelectorUsername()).isEqualTo(DEFAULT_SELECTOR_USERNAME);
        assertThat(testConfigSiteLogin.getSelectorPassword()).isEqualTo(DEFAULT_SELECTOR_PASSWORD);
        assertThat(testConfigSiteLogin.getSelectorButtonLogin()).isEqualTo(DEFAULT_SELECTOR_BUTTON_LOGIN);
        assertThat(testConfigSiteLogin.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testConfigSiteLogin.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testConfigSiteLogin.getRedirectUrl()).isEqualTo(DEFAULT_REDIRECT_URL);

        // Validate the ConfigSiteLogin in Elasticsearch
        verify(mockConfigSiteLoginSearchRepository, times(1)).save(testConfigSiteLogin);
    }

    @Test
    public void createConfigSiteLoginWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = configSiteLoginRepository.findAll().size();

        // Create the ConfigSiteLogin with an existing ID
        configSiteLogin.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restConfigSiteLoginMockMvc.perform(post("/api/config-site-logins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configSiteLogin)))
            .andExpect(status().isBadRequest());

        // Validate the ConfigSiteLogin in the database
        List<ConfigSiteLogin> configSiteLoginList = configSiteLoginRepository.findAll();
        assertThat(configSiteLoginList).hasSize(databaseSizeBeforeCreate);

        // Validate the ConfigSiteLogin in Elasticsearch
        verify(mockConfigSiteLoginSearchRepository, times(0)).save(configSiteLogin);
    }

    @Test
    public void getAllConfigSiteLogins() throws Exception {
        // Initialize the database
        configSiteLoginRepository.save(configSiteLogin);

        // Get all the configSiteLoginList
        restConfigSiteLoginMockMvc.perform(get("/api/config-site-logins?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(configSiteLogin.getId())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
            .andExpect(jsonPath("$.[*].domain").value(hasItem(DEFAULT_DOMAIN.toString())))
            .andExpect(jsonPath("$.[*].host").value(hasItem(DEFAULT_HOST.toString())))
            .andExpect(jsonPath("$.[*].selectorAction").value(hasItem(DEFAULT_SELECTOR_ACTION.toString())))
            .andExpect(jsonPath("$.[*].selectorUsername").value(hasItem(DEFAULT_SELECTOR_USERNAME.toString())))
            .andExpect(jsonPath("$.[*].selectorPassword").value(hasItem(DEFAULT_SELECTOR_PASSWORD.toString())))
            .andExpect(jsonPath("$.[*].selectorButtonLogin").value(hasItem(DEFAULT_SELECTOR_BUTTON_LOGIN.toString())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME.toString())))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD.toString())))
            .andExpect(jsonPath("$.[*].redirectUrl").value(hasItem(DEFAULT_REDIRECT_URL.toString())));
    }
    

    @Test
    public void getConfigSiteLogin() throws Exception {
        // Initialize the database
        configSiteLoginRepository.save(configSiteLogin);

        // Get the configSiteLogin
        restConfigSiteLoginMockMvc.perform(get("/api/config-site-logins/{id}", configSiteLogin.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(configSiteLogin.getId()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()))
            .andExpect(jsonPath("$.domain").value(DEFAULT_DOMAIN.toString()))
            .andExpect(jsonPath("$.host").value(DEFAULT_HOST.toString()))
            .andExpect(jsonPath("$.selectorAction").value(DEFAULT_SELECTOR_ACTION.toString()))
            .andExpect(jsonPath("$.selectorUsername").value(DEFAULT_SELECTOR_USERNAME.toString()))
            .andExpect(jsonPath("$.selectorPassword").value(DEFAULT_SELECTOR_PASSWORD.toString()))
            .andExpect(jsonPath("$.selectorButtonLogin").value(DEFAULT_SELECTOR_BUTTON_LOGIN.toString()))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME.toString()))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD.toString()))
            .andExpect(jsonPath("$.redirectUrl").value(DEFAULT_REDIRECT_URL.toString()));
    }
    @Test
    public void getNonExistingConfigSiteLogin() throws Exception {
        // Get the configSiteLogin
        restConfigSiteLoginMockMvc.perform(get("/api/config-site-logins/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateConfigSiteLogin() throws Exception {
        // Initialize the database
        configSiteLoginService.save(configSiteLogin);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockConfigSiteLoginSearchRepository);

        int databaseSizeBeforeUpdate = configSiteLoginRepository.findAll().size();

        // Update the configSiteLogin
        ConfigSiteLogin updatedConfigSiteLogin = configSiteLoginRepository.findById(configSiteLogin.getId()).get();
        updatedConfigSiteLogin
            .url(UPDATED_URL)
            .domain(UPDATED_DOMAIN)
            .host(UPDATED_HOST)
            .selectorAction(UPDATED_SELECTOR_ACTION)
            .selectorUsername(UPDATED_SELECTOR_USERNAME)
            .selectorPassword(UPDATED_SELECTOR_PASSWORD)
            .selectorButtonLogin(UPDATED_SELECTOR_BUTTON_LOGIN)
            .username(UPDATED_USERNAME)
            .password(UPDATED_PASSWORD)
            .redirectUrl(UPDATED_REDIRECT_URL);

        restConfigSiteLoginMockMvc.perform(put("/api/config-site-logins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedConfigSiteLogin)))
            .andExpect(status().isOk());

        // Validate the ConfigSiteLogin in the database
        List<ConfigSiteLogin> configSiteLoginList = configSiteLoginRepository.findAll();
        assertThat(configSiteLoginList).hasSize(databaseSizeBeforeUpdate);
        ConfigSiteLogin testConfigSiteLogin = configSiteLoginList.get(configSiteLoginList.size() - 1);
        assertThat(testConfigSiteLogin.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testConfigSiteLogin.getDomain()).isEqualTo(UPDATED_DOMAIN);
        assertThat(testConfigSiteLogin.getHost()).isEqualTo(UPDATED_HOST);
        assertThat(testConfigSiteLogin.getSelectorAction()).isEqualTo(UPDATED_SELECTOR_ACTION);
        assertThat(testConfigSiteLogin.getSelectorUsername()).isEqualTo(UPDATED_SELECTOR_USERNAME);
        assertThat(testConfigSiteLogin.getSelectorPassword()).isEqualTo(UPDATED_SELECTOR_PASSWORD);
        assertThat(testConfigSiteLogin.getSelectorButtonLogin()).isEqualTo(UPDATED_SELECTOR_BUTTON_LOGIN);
        assertThat(testConfigSiteLogin.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testConfigSiteLogin.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testConfigSiteLogin.getRedirectUrl()).isEqualTo(UPDATED_REDIRECT_URL);

        // Validate the ConfigSiteLogin in Elasticsearch
        verify(mockConfigSiteLoginSearchRepository, times(1)).save(testConfigSiteLogin);
    }

    @Test
    public void updateNonExistingConfigSiteLogin() throws Exception {
        int databaseSizeBeforeUpdate = configSiteLoginRepository.findAll().size();

        // Create the ConfigSiteLogin

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restConfigSiteLoginMockMvc.perform(put("/api/config-site-logins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configSiteLogin)))
            .andExpect(status().isBadRequest());

        // Validate the ConfigSiteLogin in the database
        List<ConfigSiteLogin> configSiteLoginList = configSiteLoginRepository.findAll();
        assertThat(configSiteLoginList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ConfigSiteLogin in Elasticsearch
        verify(mockConfigSiteLoginSearchRepository, times(0)).save(configSiteLogin);
    }

    @Test
    public void deleteConfigSiteLogin() throws Exception {
        // Initialize the database
        configSiteLoginService.save(configSiteLogin);

        int databaseSizeBeforeDelete = configSiteLoginRepository.findAll().size();

        // Get the configSiteLogin
        restConfigSiteLoginMockMvc.perform(delete("/api/config-site-logins/{id}", configSiteLogin.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ConfigSiteLogin> configSiteLoginList = configSiteLoginRepository.findAll();
        assertThat(configSiteLoginList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ConfigSiteLogin in Elasticsearch
        verify(mockConfigSiteLoginSearchRepository, times(1)).deleteById(configSiteLogin.getId());
    }

    @Test
    public void searchConfigSiteLogin() throws Exception {
        // Initialize the database
        configSiteLoginService.save(configSiteLogin);
        when(mockConfigSiteLoginSearchRepository.search(queryStringQuery("id:" + configSiteLogin.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(configSiteLogin), PageRequest.of(0, 1), 1));
        // Search the configSiteLogin
        restConfigSiteLoginMockMvc.perform(get("/api/_search/config-site-logins?query=id:" + configSiteLogin.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(configSiteLogin.getId())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
            .andExpect(jsonPath("$.[*].domain").value(hasItem(DEFAULT_DOMAIN.toString())))
            .andExpect(jsonPath("$.[*].host").value(hasItem(DEFAULT_HOST.toString())))
            .andExpect(jsonPath("$.[*].selectorAction").value(hasItem(DEFAULT_SELECTOR_ACTION.toString())))
            .andExpect(jsonPath("$.[*].selectorUsername").value(hasItem(DEFAULT_SELECTOR_USERNAME.toString())))
            .andExpect(jsonPath("$.[*].selectorPassword").value(hasItem(DEFAULT_SELECTOR_PASSWORD.toString())))
            .andExpect(jsonPath("$.[*].selectorButtonLogin").value(hasItem(DEFAULT_SELECTOR_BUTTON_LOGIN.toString())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME.toString())))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD.toString())))
            .andExpect(jsonPath("$.[*].redirectUrl").value(hasItem(DEFAULT_REDIRECT_URL.toString())));
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfigSiteLogin.class);
        ConfigSiteLogin configSiteLogin1 = new ConfigSiteLogin();
        configSiteLogin1.setId("id1");
        ConfigSiteLogin configSiteLogin2 = new ConfigSiteLogin();
        configSiteLogin2.setId(configSiteLogin1.getId());
        assertThat(configSiteLogin1).isEqualTo(configSiteLogin2);
        configSiteLogin2.setId("id2");
        assertThat(configSiteLogin1).isNotEqualTo(configSiteLogin2);
        configSiteLogin1.setId(null);
        assertThat(configSiteLogin1).isNotEqualTo(configSiteLogin2);
    }
}
