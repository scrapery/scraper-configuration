package io.github.scrapery.setting.web.rest;

import io.github.scrapery.setting.ScraperSettingApp;

import io.github.scrapery.setting.domain.SpiderScheduler;
import io.github.scrapery.setting.repository.SpiderSchedulerRepository;
import io.github.scrapery.setting.repository.search.SpiderSchedulerSearchRepository;
import io.github.scrapery.setting.service.SpiderSchedulerService;
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

import io.github.scrapery.setting.domain.enumeration.ChannelAppType;
/**
 * Test class for the SpiderSchedulerResource REST controller.
 *
 * @see SpiderSchedulerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ScraperSettingApp.class)
public class SpiderSchedulerResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SCHEDULE = "AAAAAAAAAA";
    private static final String UPDATED_SCHEDULE = "BBBBBBBBBB";

    private static final String DEFAULT_SCHEDULE_TIME_ZONE = "AAAAAAAAAA";
    private static final String UPDATED_SCHEDULE_TIME_ZONE = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY_CODE = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CHANNEL_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_CHANNEL_CATEGORY = "BBBBBBBBBB";

    private static final String DEFAULT_JSON_META = "AAAAAAAAAA";
    private static final String UPDATED_JSON_META = "BBBBBBBBBB";

    private static final ChannelAppType DEFAULT_CHANNEL_APP_TYPE = ChannelAppType.NEWS;
    private static final ChannelAppType UPDATED_CHANNEL_APP_TYPE = ChannelAppType.VIDEO;

    private static final String DEFAULT_JOB_KEY = "AAAAAAAAAA";
    private static final String UPDATED_JOB_KEY = "BBBBBBBBBB";

    @Autowired
    private SpiderSchedulerRepository spiderSchedulerRepository;

    

    @Autowired
    private SpiderSchedulerService spiderSchedulerService;

    /**
     * This repository is mocked in the io.github.scrapery.setting.repository.search test package.
     *
     * @see io.github.scrapery.setting.repository.search.SpiderSchedulerSearchRepositoryMockConfiguration
     */
    @Autowired
    private SpiderSchedulerSearchRepository mockSpiderSchedulerSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restSpiderSchedulerMockMvc;

    private SpiderScheduler spiderScheduler;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SpiderSchedulerResource spiderSchedulerResource = new SpiderSchedulerResource(spiderSchedulerService);
        this.restSpiderSchedulerMockMvc = MockMvcBuilders.standaloneSetup(spiderSchedulerResource)
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
    public static SpiderScheduler createEntity() {
        SpiderScheduler spiderScheduler = new SpiderScheduler()
            .name(DEFAULT_NAME)
            .schedule(DEFAULT_SCHEDULE)
            .scheduleTimeZone(DEFAULT_SCHEDULE_TIME_ZONE)
            .countryCode(DEFAULT_COUNTRY_CODE)
            .channelCategory(DEFAULT_CHANNEL_CATEGORY)
            .jsonMeta(DEFAULT_JSON_META)
            .channelAppType(DEFAULT_CHANNEL_APP_TYPE)
            .jobKey(DEFAULT_JOB_KEY);
        return spiderScheduler;
    }

    @Before
    public void initTest() {
        spiderSchedulerRepository.deleteAll();
        spiderScheduler = createEntity();
    }

    @Test
    public void createSpiderScheduler() throws Exception {
        int databaseSizeBeforeCreate = spiderSchedulerRepository.findAll().size();

        // Create the SpiderScheduler
        restSpiderSchedulerMockMvc.perform(post("/api/spider-schedulers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(spiderScheduler)))
            .andExpect(status().isCreated());

        // Validate the SpiderScheduler in the database
        List<SpiderScheduler> spiderSchedulerList = spiderSchedulerRepository.findAll();
        assertThat(spiderSchedulerList).hasSize(databaseSizeBeforeCreate + 1);
        SpiderScheduler testSpiderScheduler = spiderSchedulerList.get(spiderSchedulerList.size() - 1);
        assertThat(testSpiderScheduler.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSpiderScheduler.getSchedule()).isEqualTo(DEFAULT_SCHEDULE);
        assertThat(testSpiderScheduler.getScheduleTimeZone()).isEqualTo(DEFAULT_SCHEDULE_TIME_ZONE);
        assertThat(testSpiderScheduler.getCountryCode()).isEqualTo(DEFAULT_COUNTRY_CODE);
        assertThat(testSpiderScheduler.getChannelCategory()).isEqualTo(DEFAULT_CHANNEL_CATEGORY);
        assertThat(testSpiderScheduler.getJsonMeta()).isEqualTo(DEFAULT_JSON_META);
        assertThat(testSpiderScheduler.getChannelAppType()).isEqualTo(DEFAULT_CHANNEL_APP_TYPE);
        assertThat(testSpiderScheduler.getJobKey()).isEqualTo(DEFAULT_JOB_KEY);

        // Validate the SpiderScheduler in Elasticsearch
        verify(mockSpiderSchedulerSearchRepository, times(1)).save(testSpiderScheduler);
    }

    @Test
    public void createSpiderSchedulerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = spiderSchedulerRepository.findAll().size();

        // Create the SpiderScheduler with an existing ID
        spiderScheduler.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restSpiderSchedulerMockMvc.perform(post("/api/spider-schedulers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(spiderScheduler)))
            .andExpect(status().isBadRequest());

        // Validate the SpiderScheduler in the database
        List<SpiderScheduler> spiderSchedulerList = spiderSchedulerRepository.findAll();
        assertThat(spiderSchedulerList).hasSize(databaseSizeBeforeCreate);

        // Validate the SpiderScheduler in Elasticsearch
        verify(mockSpiderSchedulerSearchRepository, times(0)).save(spiderScheduler);
    }

    @Test
    public void getAllSpiderSchedulers() throws Exception {
        // Initialize the database
        spiderSchedulerRepository.save(spiderScheduler);

        // Get all the spiderSchedulerList
        restSpiderSchedulerMockMvc.perform(get("/api/spider-schedulers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(spiderScheduler.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].schedule").value(hasItem(DEFAULT_SCHEDULE.toString())))
            .andExpect(jsonPath("$.[*].scheduleTimeZone").value(hasItem(DEFAULT_SCHEDULE_TIME_ZONE.toString())))
            .andExpect(jsonPath("$.[*].countryCode").value(hasItem(DEFAULT_COUNTRY_CODE.toString())))
            .andExpect(jsonPath("$.[*].channelCategory").value(hasItem(DEFAULT_CHANNEL_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].jsonMeta").value(hasItem(DEFAULT_JSON_META.toString())))
            .andExpect(jsonPath("$.[*].channelAppType").value(hasItem(DEFAULT_CHANNEL_APP_TYPE.toString())))
            .andExpect(jsonPath("$.[*].jobKey").value(hasItem(DEFAULT_JOB_KEY.toString())));
    }
    

    @Test
    public void getSpiderScheduler() throws Exception {
        // Initialize the database
        spiderSchedulerRepository.save(spiderScheduler);

        // Get the spiderScheduler
        restSpiderSchedulerMockMvc.perform(get("/api/spider-schedulers/{id}", spiderScheduler.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(spiderScheduler.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.schedule").value(DEFAULT_SCHEDULE.toString()))
            .andExpect(jsonPath("$.scheduleTimeZone").value(DEFAULT_SCHEDULE_TIME_ZONE.toString()))
            .andExpect(jsonPath("$.countryCode").value(DEFAULT_COUNTRY_CODE.toString()))
            .andExpect(jsonPath("$.channelCategory").value(DEFAULT_CHANNEL_CATEGORY.toString()))
            .andExpect(jsonPath("$.jsonMeta").value(DEFAULT_JSON_META.toString()))
            .andExpect(jsonPath("$.channelAppType").value(DEFAULT_CHANNEL_APP_TYPE.toString()))
            .andExpect(jsonPath("$.jobKey").value(DEFAULT_JOB_KEY.toString()));
    }
    @Test
    public void getNonExistingSpiderScheduler() throws Exception {
        // Get the spiderScheduler
        restSpiderSchedulerMockMvc.perform(get("/api/spider-schedulers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateSpiderScheduler() throws Exception {
        // Initialize the database
        spiderSchedulerService.save(spiderScheduler);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockSpiderSchedulerSearchRepository);

        int databaseSizeBeforeUpdate = spiderSchedulerRepository.findAll().size();

        // Update the spiderScheduler
        SpiderScheduler updatedSpiderScheduler = spiderSchedulerRepository.findById(spiderScheduler.getId()).get();
        updatedSpiderScheduler
            .name(UPDATED_NAME)
            .schedule(UPDATED_SCHEDULE)
            .scheduleTimeZone(UPDATED_SCHEDULE_TIME_ZONE)
            .countryCode(UPDATED_COUNTRY_CODE)
            .channelCategory(UPDATED_CHANNEL_CATEGORY)
            .jsonMeta(UPDATED_JSON_META)
            .channelAppType(UPDATED_CHANNEL_APP_TYPE)
            .jobKey(UPDATED_JOB_KEY);

        restSpiderSchedulerMockMvc.perform(put("/api/spider-schedulers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSpiderScheduler)))
            .andExpect(status().isOk());

        // Validate the SpiderScheduler in the database
        List<SpiderScheduler> spiderSchedulerList = spiderSchedulerRepository.findAll();
        assertThat(spiderSchedulerList).hasSize(databaseSizeBeforeUpdate);
        SpiderScheduler testSpiderScheduler = spiderSchedulerList.get(spiderSchedulerList.size() - 1);
        assertThat(testSpiderScheduler.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSpiderScheduler.getSchedule()).isEqualTo(UPDATED_SCHEDULE);
        assertThat(testSpiderScheduler.getScheduleTimeZone()).isEqualTo(UPDATED_SCHEDULE_TIME_ZONE);
        assertThat(testSpiderScheduler.getCountryCode()).isEqualTo(UPDATED_COUNTRY_CODE);
        assertThat(testSpiderScheduler.getChannelCategory()).isEqualTo(UPDATED_CHANNEL_CATEGORY);
        assertThat(testSpiderScheduler.getJsonMeta()).isEqualTo(UPDATED_JSON_META);
        assertThat(testSpiderScheduler.getChannelAppType()).isEqualTo(UPDATED_CHANNEL_APP_TYPE);
        assertThat(testSpiderScheduler.getJobKey()).isEqualTo(UPDATED_JOB_KEY);

        // Validate the SpiderScheduler in Elasticsearch
        verify(mockSpiderSchedulerSearchRepository, times(1)).save(testSpiderScheduler);
    }

    @Test
    public void updateNonExistingSpiderScheduler() throws Exception {
        int databaseSizeBeforeUpdate = spiderSchedulerRepository.findAll().size();

        // Create the SpiderScheduler

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSpiderSchedulerMockMvc.perform(put("/api/spider-schedulers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(spiderScheduler)))
            .andExpect(status().isBadRequest());

        // Validate the SpiderScheduler in the database
        List<SpiderScheduler> spiderSchedulerList = spiderSchedulerRepository.findAll();
        assertThat(spiderSchedulerList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SpiderScheduler in Elasticsearch
        verify(mockSpiderSchedulerSearchRepository, times(0)).save(spiderScheduler);
    }

    @Test
    public void deleteSpiderScheduler() throws Exception {
        // Initialize the database
        spiderSchedulerService.save(spiderScheduler);

        int databaseSizeBeforeDelete = spiderSchedulerRepository.findAll().size();

        // Get the spiderScheduler
        restSpiderSchedulerMockMvc.perform(delete("/api/spider-schedulers/{id}", spiderScheduler.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SpiderScheduler> spiderSchedulerList = spiderSchedulerRepository.findAll();
        assertThat(spiderSchedulerList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the SpiderScheduler in Elasticsearch
        verify(mockSpiderSchedulerSearchRepository, times(1)).deleteById(spiderScheduler.getId());
    }

    @Test
    public void searchSpiderScheduler() throws Exception {
        // Initialize the database
        spiderSchedulerService.save(spiderScheduler);
        when(mockSpiderSchedulerSearchRepository.search(queryStringQuery("id:" + spiderScheduler.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(spiderScheduler), PageRequest.of(0, 1), 1));
        // Search the spiderScheduler
        restSpiderSchedulerMockMvc.perform(get("/api/_search/spider-schedulers?query=id:" + spiderScheduler.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(spiderScheduler.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].schedule").value(hasItem(DEFAULT_SCHEDULE.toString())))
            .andExpect(jsonPath("$.[*].scheduleTimeZone").value(hasItem(DEFAULT_SCHEDULE_TIME_ZONE.toString())))
            .andExpect(jsonPath("$.[*].countryCode").value(hasItem(DEFAULT_COUNTRY_CODE.toString())))
            .andExpect(jsonPath("$.[*].channelCategory").value(hasItem(DEFAULT_CHANNEL_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].jsonMeta").value(hasItem(DEFAULT_JSON_META.toString())))
            .andExpect(jsonPath("$.[*].channelAppType").value(hasItem(DEFAULT_CHANNEL_APP_TYPE.toString())))
            .andExpect(jsonPath("$.[*].jobKey").value(hasItem(DEFAULT_JOB_KEY.toString())));
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SpiderScheduler.class);
        SpiderScheduler spiderScheduler1 = new SpiderScheduler();
        spiderScheduler1.setId("id1");
        SpiderScheduler spiderScheduler2 = new SpiderScheduler();
        spiderScheduler2.setId(spiderScheduler1.getId());
        assertThat(spiderScheduler1).isEqualTo(spiderScheduler2);
        spiderScheduler2.setId("id2");
        assertThat(spiderScheduler1).isNotEqualTo(spiderScheduler2);
        spiderScheduler1.setId(null);
        assertThat(spiderScheduler1).isNotEqualTo(spiderScheduler2);
    }
}
