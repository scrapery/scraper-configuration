package io.github.scrapery.setting.web.rest;

import io.github.scrapery.setting.ScraperSettingApp;

import io.github.scrapery.setting.domain.Demo;
import io.github.scrapery.setting.repository.DemoRepository;
import io.github.scrapery.setting.repository.search.DemoSearchRepository;
import io.github.scrapery.setting.service.DemoService;
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
 * Test class for the DemoResource REST controller.
 *
 * @see DemoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ScraperSettingApp.class)
public class DemoResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    @Autowired
    private DemoRepository demoRepository;

    

    @Autowired
    private DemoService demoService;

    /**
     * This repository is mocked in the io.github.scrapery.setting.repository.search test package.
     *
     * @see io.github.scrapery.setting.repository.search.DemoSearchRepositoryMockConfiguration
     */
    @Autowired
    private DemoSearchRepository mockDemoSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restDemoMockMvc;

    private Demo demo;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DemoResource demoResource = new DemoResource(demoService);
        this.restDemoMockMvc = MockMvcBuilders.standaloneSetup(demoResource)
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
    public static Demo createEntity() {
        Demo demo = new Demo()
            .title(DEFAULT_TITLE);
        return demo;
    }

    @Before
    public void initTest() {
        demoRepository.deleteAll();
        demo = createEntity();
    }

    @Test
    public void createDemo() throws Exception {
        int databaseSizeBeforeCreate = demoRepository.findAll().size();

        // Create the Demo
        restDemoMockMvc.perform(post("/api/demos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(demo)))
            .andExpect(status().isCreated());

        // Validate the Demo in the database
        List<Demo> demoList = demoRepository.findAll();
        assertThat(demoList).hasSize(databaseSizeBeforeCreate + 1);
        Demo testDemo = demoList.get(demoList.size() - 1);
        assertThat(testDemo.getTitle()).isEqualTo(DEFAULT_TITLE);

        // Validate the Demo in Elasticsearch
        verify(mockDemoSearchRepository, times(1)).save(testDemo);
    }

    @Test
    public void createDemoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = demoRepository.findAll().size();

        // Create the Demo with an existing ID
        demo.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restDemoMockMvc.perform(post("/api/demos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(demo)))
            .andExpect(status().isBadRequest());

        // Validate the Demo in the database
        List<Demo> demoList = demoRepository.findAll();
        assertThat(demoList).hasSize(databaseSizeBeforeCreate);

        // Validate the Demo in Elasticsearch
        verify(mockDemoSearchRepository, times(0)).save(demo);
    }

    @Test
    public void getAllDemos() throws Exception {
        // Initialize the database
        demoRepository.save(demo);

        // Get all the demoList
        restDemoMockMvc.perform(get("/api/demos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(demo.getId())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())));
    }
    

    @Test
    public void getDemo() throws Exception {
        // Initialize the database
        demoRepository.save(demo);

        // Get the demo
        restDemoMockMvc.perform(get("/api/demos/{id}", demo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(demo.getId()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()));
    }
    @Test
    public void getNonExistingDemo() throws Exception {
        // Get the demo
        restDemoMockMvc.perform(get("/api/demos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateDemo() throws Exception {
        // Initialize the database
        demoService.save(demo);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockDemoSearchRepository);

        int databaseSizeBeforeUpdate = demoRepository.findAll().size();

        // Update the demo
        Demo updatedDemo = demoRepository.findById(demo.getId()).get();
        updatedDemo
            .title(UPDATED_TITLE);

        restDemoMockMvc.perform(put("/api/demos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDemo)))
            .andExpect(status().isOk());

        // Validate the Demo in the database
        List<Demo> demoList = demoRepository.findAll();
        assertThat(demoList).hasSize(databaseSizeBeforeUpdate);
        Demo testDemo = demoList.get(demoList.size() - 1);
        assertThat(testDemo.getTitle()).isEqualTo(UPDATED_TITLE);

        // Validate the Demo in Elasticsearch
        verify(mockDemoSearchRepository, times(1)).save(testDemo);
    }

    @Test
    public void updateNonExistingDemo() throws Exception {
        int databaseSizeBeforeUpdate = demoRepository.findAll().size();

        // Create the Demo

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDemoMockMvc.perform(put("/api/demos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(demo)))
            .andExpect(status().isBadRequest());

        // Validate the Demo in the database
        List<Demo> demoList = demoRepository.findAll();
        assertThat(demoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Demo in Elasticsearch
        verify(mockDemoSearchRepository, times(0)).save(demo);
    }

    @Test
    public void deleteDemo() throws Exception {
        // Initialize the database
        demoService.save(demo);

        int databaseSizeBeforeDelete = demoRepository.findAll().size();

        // Get the demo
        restDemoMockMvc.perform(delete("/api/demos/{id}", demo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Demo> demoList = demoRepository.findAll();
        assertThat(demoList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Demo in Elasticsearch
        verify(mockDemoSearchRepository, times(1)).deleteById(demo.getId());
    }

    @Test
    public void searchDemo() throws Exception {
        // Initialize the database
        demoService.save(demo);
        when(mockDemoSearchRepository.search(queryStringQuery("id:" + demo.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(demo), PageRequest.of(0, 1), 1));
        // Search the demo
        restDemoMockMvc.perform(get("/api/_search/demos?query=id:" + demo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(demo.getId())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())));
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Demo.class);
        Demo demo1 = new Demo();
        demo1.setId("id1");
        Demo demo2 = new Demo();
        demo2.setId(demo1.getId());
        assertThat(demo1).isEqualTo(demo2);
        demo2.setId("id2");
        assertThat(demo1).isNotEqualTo(demo2);
        demo1.setId(null);
        assertThat(demo1).isNotEqualTo(demo2);
    }
}
