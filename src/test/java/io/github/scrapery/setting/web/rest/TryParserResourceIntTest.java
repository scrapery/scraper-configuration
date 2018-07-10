package io.github.scrapery.setting.web.rest;

import io.github.scrapery.setting.ScraperSettingApp;

import io.github.scrapery.setting.domain.TryParser;
import io.github.scrapery.setting.repository.TryParserRepository;
import io.github.scrapery.setting.repository.search.TryParserSearchRepository;
import io.github.scrapery.setting.service.TryParserService;
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

import io.github.scrapery.setting.domain.enumeration.TryFetchEngine;
import io.github.scrapery.setting.domain.enumeration.DocType;
/**
 * Test class for the TryParserResource REST controller.
 *
 * @see TryParserResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ScraperSettingApp.class)
public class TryParserResourceIntTest {

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String DEFAULT_USER_AGENT = "AAAAAAAAAA";
    private static final String UPDATED_USER_AGENT = "BBBBBBBBBB";

    private static final String DEFAULT_HTML_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_HTML_CONTENT = "BBBBBBBBBB";

    private static final String DEFAULT_PARSED_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_PARSED_CONTENT = "BBBBBBBBBB";

    private static final String DEFAULT_SELECTOR = "AAAAAAAAAA";
    private static final String UPDATED_SELECTOR = "BBBBBBBBBB";

    private static final String DEFAULT_SELECTOR_RESULT = "AAAAAAAAAA";
    private static final String UPDATED_SELECTOR_RESULT = "BBBBBBBBBB";

    private static final TryFetchEngine DEFAULT_FETCH_ENGINE = TryFetchEngine.HTTP;
    private static final TryFetchEngine UPDATED_FETCH_ENGINE = TryFetchEngine.SELENIUM;

    private static final String DEFAULT_ATTRIBUTE_SELECTOR = "AAAAAAAAAA";
    private static final String UPDATED_ATTRIBUTE_SELECTOR = "BBBBBBBBBB";

    private static final DocType DEFAULT_DOC_TYPE = DocType.HTML;
    private static final DocType UPDATED_DOC_TYPE = DocType.XML;

    @Autowired
    private TryParserRepository tryParserRepository;

    

    @Autowired
    private TryParserService tryParserService;

    /**
     * This repository is mocked in the io.github.scrapery.setting.repository.search test package.
     *
     * @see io.github.scrapery.setting.repository.search.TryParserSearchRepositoryMockConfiguration
     */
    @Autowired
    private TryParserSearchRepository mockTryParserSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restTryParserMockMvc;

    private TryParser tryParser;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TryParserResource tryParserResource = new TryParserResource(tryParserService);
        this.restTryParserMockMvc = MockMvcBuilders.standaloneSetup(tryParserResource)
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
    public static TryParser createEntity() {
        TryParser tryParser = new TryParser()
            .url(DEFAULT_URL)
            .userAgent(DEFAULT_USER_AGENT)
            .htmlContent(DEFAULT_HTML_CONTENT)
            .parsedContent(DEFAULT_PARSED_CONTENT)
            .selector(DEFAULT_SELECTOR)
            .selectorResult(DEFAULT_SELECTOR_RESULT)
            .fetchEngine(DEFAULT_FETCH_ENGINE)
            .attributeSelector(DEFAULT_ATTRIBUTE_SELECTOR)
            .docType(DEFAULT_DOC_TYPE);
        return tryParser;
    }

    @Before
    public void initTest() {
        tryParserRepository.deleteAll();
        tryParser = createEntity();
    }

    @Test
    public void createTryParser() throws Exception {
        int databaseSizeBeforeCreate = tryParserRepository.findAll().size();

        // Create the TryParser
        restTryParserMockMvc.perform(post("/api/try-parsers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tryParser)))
            .andExpect(status().isCreated());

        // Validate the TryParser in the database
        List<TryParser> tryParserList = tryParserRepository.findAll();
        assertThat(tryParserList).hasSize(databaseSizeBeforeCreate + 1);
        TryParser testTryParser = tryParserList.get(tryParserList.size() - 1);
        assertThat(testTryParser.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testTryParser.getUserAgent()).isEqualTo(DEFAULT_USER_AGENT);
        assertThat(testTryParser.getHtmlContent()).isEqualTo(DEFAULT_HTML_CONTENT);
        assertThat(testTryParser.getParsedContent()).isEqualTo(DEFAULT_PARSED_CONTENT);
        assertThat(testTryParser.getSelector()).isEqualTo(DEFAULT_SELECTOR);
        assertThat(testTryParser.getSelectorResult()).isEqualTo(DEFAULT_SELECTOR_RESULT);
        assertThat(testTryParser.getFetchEngine()).isEqualTo(DEFAULT_FETCH_ENGINE);
        assertThat(testTryParser.getAttributeSelector()).isEqualTo(DEFAULT_ATTRIBUTE_SELECTOR);
        assertThat(testTryParser.getDocType()).isEqualTo(DEFAULT_DOC_TYPE);

        // Validate the TryParser in Elasticsearch
        verify(mockTryParserSearchRepository, times(1)).save(testTryParser);
    }

    @Test
    public void createTryParserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tryParserRepository.findAll().size();

        // Create the TryParser with an existing ID
        tryParser.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restTryParserMockMvc.perform(post("/api/try-parsers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tryParser)))
            .andExpect(status().isBadRequest());

        // Validate the TryParser in the database
        List<TryParser> tryParserList = tryParserRepository.findAll();
        assertThat(tryParserList).hasSize(databaseSizeBeforeCreate);

        // Validate the TryParser in Elasticsearch
        verify(mockTryParserSearchRepository, times(0)).save(tryParser);
    }

    @Test
    public void getAllTryParsers() throws Exception {
        // Initialize the database
        tryParserRepository.save(tryParser);

        // Get all the tryParserList
        restTryParserMockMvc.perform(get("/api/try-parsers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tryParser.getId())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
            .andExpect(jsonPath("$.[*].userAgent").value(hasItem(DEFAULT_USER_AGENT.toString())))
            .andExpect(jsonPath("$.[*].htmlContent").value(hasItem(DEFAULT_HTML_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].parsedContent").value(hasItem(DEFAULT_PARSED_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].selector").value(hasItem(DEFAULT_SELECTOR.toString())))
            .andExpect(jsonPath("$.[*].selectorResult").value(hasItem(DEFAULT_SELECTOR_RESULT.toString())))
            .andExpect(jsonPath("$.[*].fetchEngine").value(hasItem(DEFAULT_FETCH_ENGINE.toString())))
            .andExpect(jsonPath("$.[*].attributeSelector").value(hasItem(DEFAULT_ATTRIBUTE_SELECTOR.toString())))
            .andExpect(jsonPath("$.[*].docType").value(hasItem(DEFAULT_DOC_TYPE.toString())));
    }
    

    @Test
    public void getTryParser() throws Exception {
        // Initialize the database
        tryParserRepository.save(tryParser);

        // Get the tryParser
        restTryParserMockMvc.perform(get("/api/try-parsers/{id}", tryParser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tryParser.getId()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()))
            .andExpect(jsonPath("$.userAgent").value(DEFAULT_USER_AGENT.toString()))
            .andExpect(jsonPath("$.htmlContent").value(DEFAULT_HTML_CONTENT.toString()))
            .andExpect(jsonPath("$.parsedContent").value(DEFAULT_PARSED_CONTENT.toString()))
            .andExpect(jsonPath("$.selector").value(DEFAULT_SELECTOR.toString()))
            .andExpect(jsonPath("$.selectorResult").value(DEFAULT_SELECTOR_RESULT.toString()))
            .andExpect(jsonPath("$.fetchEngine").value(DEFAULT_FETCH_ENGINE.toString()))
            .andExpect(jsonPath("$.attributeSelector").value(DEFAULT_ATTRIBUTE_SELECTOR.toString()))
            .andExpect(jsonPath("$.docType").value(DEFAULT_DOC_TYPE.toString()));
    }
    @Test
    public void getNonExistingTryParser() throws Exception {
        // Get the tryParser
        restTryParserMockMvc.perform(get("/api/try-parsers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateTryParser() throws Exception {
        // Initialize the database
        tryParserService.save(tryParser);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockTryParserSearchRepository);

        int databaseSizeBeforeUpdate = tryParserRepository.findAll().size();

        // Update the tryParser
        TryParser updatedTryParser = tryParserRepository.findById(tryParser.getId()).get();
        updatedTryParser
            .url(UPDATED_URL)
            .userAgent(UPDATED_USER_AGENT)
            .htmlContent(UPDATED_HTML_CONTENT)
            .parsedContent(UPDATED_PARSED_CONTENT)
            .selector(UPDATED_SELECTOR)
            .selectorResult(UPDATED_SELECTOR_RESULT)
            .fetchEngine(UPDATED_FETCH_ENGINE)
            .attributeSelector(UPDATED_ATTRIBUTE_SELECTOR)
            .docType(UPDATED_DOC_TYPE);

        restTryParserMockMvc.perform(put("/api/try-parsers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTryParser)))
            .andExpect(status().isOk());

        // Validate the TryParser in the database
        List<TryParser> tryParserList = tryParserRepository.findAll();
        assertThat(tryParserList).hasSize(databaseSizeBeforeUpdate);
        TryParser testTryParser = tryParserList.get(tryParserList.size() - 1);
        assertThat(testTryParser.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testTryParser.getUserAgent()).isEqualTo(UPDATED_USER_AGENT);
        assertThat(testTryParser.getHtmlContent()).isEqualTo(UPDATED_HTML_CONTENT);
        assertThat(testTryParser.getParsedContent()).isEqualTo(UPDATED_PARSED_CONTENT);
        assertThat(testTryParser.getSelector()).isEqualTo(UPDATED_SELECTOR);
        assertThat(testTryParser.getSelectorResult()).isEqualTo(UPDATED_SELECTOR_RESULT);
        assertThat(testTryParser.getFetchEngine()).isEqualTo(UPDATED_FETCH_ENGINE);
        assertThat(testTryParser.getAttributeSelector()).isEqualTo(UPDATED_ATTRIBUTE_SELECTOR);
        assertThat(testTryParser.getDocType()).isEqualTo(UPDATED_DOC_TYPE);

        // Validate the TryParser in Elasticsearch
        verify(mockTryParserSearchRepository, times(1)).save(testTryParser);
    }

    @Test
    public void updateNonExistingTryParser() throws Exception {
        int databaseSizeBeforeUpdate = tryParserRepository.findAll().size();

        // Create the TryParser

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTryParserMockMvc.perform(put("/api/try-parsers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tryParser)))
            .andExpect(status().isBadRequest());

        // Validate the TryParser in the database
        List<TryParser> tryParserList = tryParserRepository.findAll();
        assertThat(tryParserList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TryParser in Elasticsearch
        verify(mockTryParserSearchRepository, times(0)).save(tryParser);
    }

    @Test
    public void deleteTryParser() throws Exception {
        // Initialize the database
        tryParserService.save(tryParser);

        int databaseSizeBeforeDelete = tryParserRepository.findAll().size();

        // Get the tryParser
        restTryParserMockMvc.perform(delete("/api/try-parsers/{id}", tryParser.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<TryParser> tryParserList = tryParserRepository.findAll();
        assertThat(tryParserList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the TryParser in Elasticsearch
        verify(mockTryParserSearchRepository, times(1)).deleteById(tryParser.getId());
    }

    @Test
    public void searchTryParser() throws Exception {
        // Initialize the database
        tryParserService.save(tryParser);
        when(mockTryParserSearchRepository.search(queryStringQuery("id:" + tryParser.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(tryParser), PageRequest.of(0, 1), 1));
        // Search the tryParser
        restTryParserMockMvc.perform(get("/api/_search/try-parsers?query=id:" + tryParser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tryParser.getId())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
            .andExpect(jsonPath("$.[*].userAgent").value(hasItem(DEFAULT_USER_AGENT.toString())))
            .andExpect(jsonPath("$.[*].htmlContent").value(hasItem(DEFAULT_HTML_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].parsedContent").value(hasItem(DEFAULT_PARSED_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].selector").value(hasItem(DEFAULT_SELECTOR.toString())))
            .andExpect(jsonPath("$.[*].selectorResult").value(hasItem(DEFAULT_SELECTOR_RESULT.toString())))
            .andExpect(jsonPath("$.[*].fetchEngine").value(hasItem(DEFAULT_FETCH_ENGINE.toString())))
            .andExpect(jsonPath("$.[*].attributeSelector").value(hasItem(DEFAULT_ATTRIBUTE_SELECTOR.toString())))
            .andExpect(jsonPath("$.[*].docType").value(hasItem(DEFAULT_DOC_TYPE.toString())));
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TryParser.class);
        TryParser tryParser1 = new TryParser();
        tryParser1.setId("id1");
        TryParser tryParser2 = new TryParser();
        tryParser2.setId(tryParser1.getId());
        assertThat(tryParser1).isEqualTo(tryParser2);
        tryParser2.setId("id2");
        assertThat(tryParser1).isNotEqualTo(tryParser2);
        tryParser1.setId(null);
        assertThat(tryParser1).isNotEqualTo(tryParser2);
    }
}
