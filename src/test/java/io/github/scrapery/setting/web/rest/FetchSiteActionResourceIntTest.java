package io.github.scrapery.setting.web.rest;

import io.github.scrapery.setting.ScraperSettingApp;

import io.github.scrapery.setting.domain.FetchSiteAction;
import io.github.scrapery.setting.repository.FetchSiteActionRepository;
import io.github.scrapery.setting.repository.search.FetchSiteActionSearchRepository;
import io.github.scrapery.setting.service.FetchSiteActionService;
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

import io.github.scrapery.setting.domain.enumeration.Action;
import io.github.scrapery.setting.domain.enumeration.SeleniumActionGetContent;
/**
 * Test class for the FetchSiteActionResource REST controller.
 *
 * @see FetchSiteActionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ScraperSettingApp.class)
public class FetchSiteActionResourceIntTest {

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String DEFAULT_DOMAIN = "AAAAAAAAAA";
    private static final String UPDATED_DOMAIN = "BBBBBBBBBB";

    private static final String DEFAULT_HOST = "AAAAAAAAAA";
    private static final String UPDATED_HOST = "BBBBBBBBBB";

    private static final Integer DEFAULT_ACTIVE_LEVEL = 1;
    private static final Integer UPDATED_ACTIVE_LEVEL = 2;

    private static final String DEFAULT_SELECTOR_ACTION = "AAAAAAAAAA";
    private static final String UPDATED_SELECTOR_ACTION = "BBBBBBBBBB";

    private static final String DEFAULT_SELECTOR_ACTION_ATTR = "AAAAAAAAAA";
    private static final String UPDATED_SELECTOR_ACTION_ATTR = "BBBBBBBBBB";

    private static final Action DEFAULT_ACTION = Action.CLICK;
    private static final Action UPDATED_ACTION = Action.SCROLL;

    private static final Integer DEFAULT_TOTAL_ACTIONS = 1;
    private static final Integer UPDATED_TOTAL_ACTIONS = 2;

    private static final SeleniumActionGetContent DEFAULT_SELENIUM_ACTION_GET_CONTENT = SeleniumActionGetContent.DONE_ACTION;
    private static final SeleniumActionGetContent UPDATED_SELENIUM_ACTION_GET_CONTENT = SeleniumActionGetContent.EACH_ACTION;

    private static final String DEFAULT_SELECTOR_NEXT_PAGE_URLS_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SELECTOR_NEXT_PAGE_URLS_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SELECTOR_NEXT_PAGE_URLS_NAME_ATTR = "AAAAAAAAAA";
    private static final String UPDATED_SELECTOR_NEXT_PAGE_URLS_NAME_ATTR = "BBBBBBBBBB";

    @Autowired
    private FetchSiteActionRepository fetchSiteActionRepository;

    

    @Autowired
    private FetchSiteActionService fetchSiteActionService;

    /**
     * This repository is mocked in the io.github.scrapery.setting.repository.search test package.
     *
     * @see io.github.scrapery.setting.repository.search.FetchSiteActionSearchRepositoryMockConfiguration
     */
    @Autowired
    private FetchSiteActionSearchRepository mockFetchSiteActionSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restFetchSiteActionMockMvc;

    private FetchSiteAction fetchSiteAction;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FetchSiteActionResource fetchSiteActionResource = new FetchSiteActionResource(fetchSiteActionService);
        this.restFetchSiteActionMockMvc = MockMvcBuilders.standaloneSetup(fetchSiteActionResource)
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
    public static FetchSiteAction createEntity() {
        FetchSiteAction fetchSiteAction = new FetchSiteAction()
            .url(DEFAULT_URL)
            .domain(DEFAULT_DOMAIN)
            .host(DEFAULT_HOST)
            .activeLevel(DEFAULT_ACTIVE_LEVEL)
            .selectorAction(DEFAULT_SELECTOR_ACTION)
            .selectorActionAttr(DEFAULT_SELECTOR_ACTION_ATTR)
            .action(DEFAULT_ACTION)
            .totalActions(DEFAULT_TOTAL_ACTIONS)
            .seleniumActionGetContent(DEFAULT_SELENIUM_ACTION_GET_CONTENT)
            .selectorNextPageUrlsName(DEFAULT_SELECTOR_NEXT_PAGE_URLS_NAME)
            .selectorNextPageUrlsNameAttr(DEFAULT_SELECTOR_NEXT_PAGE_URLS_NAME_ATTR);
        return fetchSiteAction;
    }

    @Before
    public void initTest() {
        fetchSiteActionRepository.deleteAll();
        fetchSiteAction = createEntity();
    }

    @Test
    public void createFetchSiteAction() throws Exception {
        int databaseSizeBeforeCreate = fetchSiteActionRepository.findAll().size();

        // Create the FetchSiteAction
        restFetchSiteActionMockMvc.perform(post("/api/fetch-site-actions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fetchSiteAction)))
            .andExpect(status().isCreated());

        // Validate the FetchSiteAction in the database
        List<FetchSiteAction> fetchSiteActionList = fetchSiteActionRepository.findAll();
        assertThat(fetchSiteActionList).hasSize(databaseSizeBeforeCreate + 1);
        FetchSiteAction testFetchSiteAction = fetchSiteActionList.get(fetchSiteActionList.size() - 1);
        assertThat(testFetchSiteAction.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testFetchSiteAction.getDomain()).isEqualTo(DEFAULT_DOMAIN);
        assertThat(testFetchSiteAction.getHost()).isEqualTo(DEFAULT_HOST);
        assertThat(testFetchSiteAction.getActiveLevel()).isEqualTo(DEFAULT_ACTIVE_LEVEL);
        assertThat(testFetchSiteAction.getSelectorAction()).isEqualTo(DEFAULT_SELECTOR_ACTION);
        assertThat(testFetchSiteAction.getSelectorActionAttr()).isEqualTo(DEFAULT_SELECTOR_ACTION_ATTR);
        assertThat(testFetchSiteAction.getAction()).isEqualTo(DEFAULT_ACTION);
        assertThat(testFetchSiteAction.getTotalActions()).isEqualTo(DEFAULT_TOTAL_ACTIONS);
        assertThat(testFetchSiteAction.getSeleniumActionGetContent()).isEqualTo(DEFAULT_SELENIUM_ACTION_GET_CONTENT);
        assertThat(testFetchSiteAction.getSelectorNextPageUrlsName()).isEqualTo(DEFAULT_SELECTOR_NEXT_PAGE_URLS_NAME);
        assertThat(testFetchSiteAction.getSelectorNextPageUrlsNameAttr()).isEqualTo(DEFAULT_SELECTOR_NEXT_PAGE_URLS_NAME_ATTR);

        // Validate the FetchSiteAction in Elasticsearch
        verify(mockFetchSiteActionSearchRepository, times(1)).save(testFetchSiteAction);
    }

    @Test
    public void createFetchSiteActionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = fetchSiteActionRepository.findAll().size();

        // Create the FetchSiteAction with an existing ID
        fetchSiteAction.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restFetchSiteActionMockMvc.perform(post("/api/fetch-site-actions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fetchSiteAction)))
            .andExpect(status().isBadRequest());

        // Validate the FetchSiteAction in the database
        List<FetchSiteAction> fetchSiteActionList = fetchSiteActionRepository.findAll();
        assertThat(fetchSiteActionList).hasSize(databaseSizeBeforeCreate);

        // Validate the FetchSiteAction in Elasticsearch
        verify(mockFetchSiteActionSearchRepository, times(0)).save(fetchSiteAction);
    }

    @Test
    public void getAllFetchSiteActions() throws Exception {
        // Initialize the database
        fetchSiteActionRepository.save(fetchSiteAction);

        // Get all the fetchSiteActionList
        restFetchSiteActionMockMvc.perform(get("/api/fetch-site-actions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fetchSiteAction.getId())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
            .andExpect(jsonPath("$.[*].domain").value(hasItem(DEFAULT_DOMAIN.toString())))
            .andExpect(jsonPath("$.[*].host").value(hasItem(DEFAULT_HOST.toString())))
            .andExpect(jsonPath("$.[*].activeLevel").value(hasItem(DEFAULT_ACTIVE_LEVEL)))
            .andExpect(jsonPath("$.[*].selectorAction").value(hasItem(DEFAULT_SELECTOR_ACTION.toString())))
            .andExpect(jsonPath("$.[*].selectorActionAttr").value(hasItem(DEFAULT_SELECTOR_ACTION_ATTR.toString())))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION.toString())))
            .andExpect(jsonPath("$.[*].totalActions").value(hasItem(DEFAULT_TOTAL_ACTIONS)))
            .andExpect(jsonPath("$.[*].seleniumActionGetContent").value(hasItem(DEFAULT_SELENIUM_ACTION_GET_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].selectorNextPageUrlsName").value(hasItem(DEFAULT_SELECTOR_NEXT_PAGE_URLS_NAME.toString())))
            .andExpect(jsonPath("$.[*].selectorNextPageUrlsNameAttr").value(hasItem(DEFAULT_SELECTOR_NEXT_PAGE_URLS_NAME_ATTR.toString())));
    }
    

    @Test
    public void getFetchSiteAction() throws Exception {
        // Initialize the database
        fetchSiteActionRepository.save(fetchSiteAction);

        // Get the fetchSiteAction
        restFetchSiteActionMockMvc.perform(get("/api/fetch-site-actions/{id}", fetchSiteAction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(fetchSiteAction.getId()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()))
            .andExpect(jsonPath("$.domain").value(DEFAULT_DOMAIN.toString()))
            .andExpect(jsonPath("$.host").value(DEFAULT_HOST.toString()))
            .andExpect(jsonPath("$.activeLevel").value(DEFAULT_ACTIVE_LEVEL))
            .andExpect(jsonPath("$.selectorAction").value(DEFAULT_SELECTOR_ACTION.toString()))
            .andExpect(jsonPath("$.selectorActionAttr").value(DEFAULT_SELECTOR_ACTION_ATTR.toString()))
            .andExpect(jsonPath("$.action").value(DEFAULT_ACTION.toString()))
            .andExpect(jsonPath("$.totalActions").value(DEFAULT_TOTAL_ACTIONS))
            .andExpect(jsonPath("$.seleniumActionGetContent").value(DEFAULT_SELENIUM_ACTION_GET_CONTENT.toString()))
            .andExpect(jsonPath("$.selectorNextPageUrlsName").value(DEFAULT_SELECTOR_NEXT_PAGE_URLS_NAME.toString()))
            .andExpect(jsonPath("$.selectorNextPageUrlsNameAttr").value(DEFAULT_SELECTOR_NEXT_PAGE_URLS_NAME_ATTR.toString()));
    }
    @Test
    public void getNonExistingFetchSiteAction() throws Exception {
        // Get the fetchSiteAction
        restFetchSiteActionMockMvc.perform(get("/api/fetch-site-actions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateFetchSiteAction() throws Exception {
        // Initialize the database
        fetchSiteActionService.save(fetchSiteAction);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockFetchSiteActionSearchRepository);

        int databaseSizeBeforeUpdate = fetchSiteActionRepository.findAll().size();

        // Update the fetchSiteAction
        FetchSiteAction updatedFetchSiteAction = fetchSiteActionRepository.findById(fetchSiteAction.getId()).get();
        updatedFetchSiteAction
            .url(UPDATED_URL)
            .domain(UPDATED_DOMAIN)
            .host(UPDATED_HOST)
            .activeLevel(UPDATED_ACTIVE_LEVEL)
            .selectorAction(UPDATED_SELECTOR_ACTION)
            .selectorActionAttr(UPDATED_SELECTOR_ACTION_ATTR)
            .action(UPDATED_ACTION)
            .totalActions(UPDATED_TOTAL_ACTIONS)
            .seleniumActionGetContent(UPDATED_SELENIUM_ACTION_GET_CONTENT)
            .selectorNextPageUrlsName(UPDATED_SELECTOR_NEXT_PAGE_URLS_NAME)
            .selectorNextPageUrlsNameAttr(UPDATED_SELECTOR_NEXT_PAGE_URLS_NAME_ATTR);

        restFetchSiteActionMockMvc.perform(put("/api/fetch-site-actions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFetchSiteAction)))
            .andExpect(status().isOk());

        // Validate the FetchSiteAction in the database
        List<FetchSiteAction> fetchSiteActionList = fetchSiteActionRepository.findAll();
        assertThat(fetchSiteActionList).hasSize(databaseSizeBeforeUpdate);
        FetchSiteAction testFetchSiteAction = fetchSiteActionList.get(fetchSiteActionList.size() - 1);
        assertThat(testFetchSiteAction.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testFetchSiteAction.getDomain()).isEqualTo(UPDATED_DOMAIN);
        assertThat(testFetchSiteAction.getHost()).isEqualTo(UPDATED_HOST);
        assertThat(testFetchSiteAction.getActiveLevel()).isEqualTo(UPDATED_ACTIVE_LEVEL);
        assertThat(testFetchSiteAction.getSelectorAction()).isEqualTo(UPDATED_SELECTOR_ACTION);
        assertThat(testFetchSiteAction.getSelectorActionAttr()).isEqualTo(UPDATED_SELECTOR_ACTION_ATTR);
        assertThat(testFetchSiteAction.getAction()).isEqualTo(UPDATED_ACTION);
        assertThat(testFetchSiteAction.getTotalActions()).isEqualTo(UPDATED_TOTAL_ACTIONS);
        assertThat(testFetchSiteAction.getSeleniumActionGetContent()).isEqualTo(UPDATED_SELENIUM_ACTION_GET_CONTENT);
        assertThat(testFetchSiteAction.getSelectorNextPageUrlsName()).isEqualTo(UPDATED_SELECTOR_NEXT_PAGE_URLS_NAME);
        assertThat(testFetchSiteAction.getSelectorNextPageUrlsNameAttr()).isEqualTo(UPDATED_SELECTOR_NEXT_PAGE_URLS_NAME_ATTR);

        // Validate the FetchSiteAction in Elasticsearch
        verify(mockFetchSiteActionSearchRepository, times(1)).save(testFetchSiteAction);
    }

    @Test
    public void updateNonExistingFetchSiteAction() throws Exception {
        int databaseSizeBeforeUpdate = fetchSiteActionRepository.findAll().size();

        // Create the FetchSiteAction

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restFetchSiteActionMockMvc.perform(put("/api/fetch-site-actions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fetchSiteAction)))
            .andExpect(status().isBadRequest());

        // Validate the FetchSiteAction in the database
        List<FetchSiteAction> fetchSiteActionList = fetchSiteActionRepository.findAll();
        assertThat(fetchSiteActionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FetchSiteAction in Elasticsearch
        verify(mockFetchSiteActionSearchRepository, times(0)).save(fetchSiteAction);
    }

    @Test
    public void deleteFetchSiteAction() throws Exception {
        // Initialize the database
        fetchSiteActionService.save(fetchSiteAction);

        int databaseSizeBeforeDelete = fetchSiteActionRepository.findAll().size();

        // Get the fetchSiteAction
        restFetchSiteActionMockMvc.perform(delete("/api/fetch-site-actions/{id}", fetchSiteAction.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<FetchSiteAction> fetchSiteActionList = fetchSiteActionRepository.findAll();
        assertThat(fetchSiteActionList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the FetchSiteAction in Elasticsearch
        verify(mockFetchSiteActionSearchRepository, times(1)).deleteById(fetchSiteAction.getId());
    }

    @Test
    public void searchFetchSiteAction() throws Exception {
        // Initialize the database
        fetchSiteActionService.save(fetchSiteAction);
        when(mockFetchSiteActionSearchRepository.search(queryStringQuery("id:" + fetchSiteAction.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(fetchSiteAction), PageRequest.of(0, 1), 1));
        // Search the fetchSiteAction
        restFetchSiteActionMockMvc.perform(get("/api/_search/fetch-site-actions?query=id:" + fetchSiteAction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fetchSiteAction.getId())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
            .andExpect(jsonPath("$.[*].domain").value(hasItem(DEFAULT_DOMAIN.toString())))
            .andExpect(jsonPath("$.[*].host").value(hasItem(DEFAULT_HOST.toString())))
            .andExpect(jsonPath("$.[*].activeLevel").value(hasItem(DEFAULT_ACTIVE_LEVEL)))
            .andExpect(jsonPath("$.[*].selectorAction").value(hasItem(DEFAULT_SELECTOR_ACTION.toString())))
            .andExpect(jsonPath("$.[*].selectorActionAttr").value(hasItem(DEFAULT_SELECTOR_ACTION_ATTR.toString())))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION.toString())))
            .andExpect(jsonPath("$.[*].totalActions").value(hasItem(DEFAULT_TOTAL_ACTIONS)))
            .andExpect(jsonPath("$.[*].seleniumActionGetContent").value(hasItem(DEFAULT_SELENIUM_ACTION_GET_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].selectorNextPageUrlsName").value(hasItem(DEFAULT_SELECTOR_NEXT_PAGE_URLS_NAME.toString())))
            .andExpect(jsonPath("$.[*].selectorNextPageUrlsNameAttr").value(hasItem(DEFAULT_SELECTOR_NEXT_PAGE_URLS_NAME_ATTR.toString())));
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FetchSiteAction.class);
        FetchSiteAction fetchSiteAction1 = new FetchSiteAction();
        fetchSiteAction1.setId("id1");
        FetchSiteAction fetchSiteAction2 = new FetchSiteAction();
        fetchSiteAction2.setId(fetchSiteAction1.getId());
        assertThat(fetchSiteAction1).isEqualTo(fetchSiteAction2);
        fetchSiteAction2.setId("id2");
        assertThat(fetchSiteAction1).isNotEqualTo(fetchSiteAction2);
        fetchSiteAction1.setId(null);
        assertThat(fetchSiteAction1).isNotEqualTo(fetchSiteAction2);
    }
}
