package io.github.scrapery.setting.web.rest;

import io.github.scrapery.setting.ScraperSettingApp;

import io.github.scrapery.setting.domain.Link;
import io.github.scrapery.setting.repository.LinkRepository;
import io.github.scrapery.setting.repository.search.LinkSearchRepository;
import io.github.scrapery.setting.service.LinkService;
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

import io.github.scrapery.setting.domain.enumeration.CrawlStatus;
/**
 * Test class for the LinkResource REST controller.
 *
 * @see LinkResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ScraperSettingApp.class)
public class LinkResourceIntTest {

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final Long DEFAULT_SCRAPE_DATA_ID = 1L;
    private static final Long UPDATED_SCRAPE_DATA_ID = 2L;

    private static final Long DEFAULT_SCRAPE_ID = 1L;
    private static final Long UPDATED_SCRAPE_ID = 2L;

    private static final Integer DEFAULT_CURRENT_LEVEL = 1;
    private static final Integer UPDATED_CURRENT_LEVEL = 2;

    private static final String DEFAULT_SCRAPE_URL = "AAAAAAAAAA";
    private static final String UPDATED_SCRAPE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_PARENT_URL = "AAAAAAAAAA";
    private static final String UPDATED_PARENT_URL = "BBBBBBBBBB";

    private static final Long DEFAULT_SCRAPE_RESULT_ID = 1L;
    private static final Long UPDATED_SCRAPE_RESULT_ID = 2L;

    private static final String DEFAULT_SCRAPE_RESULT_PATH = "AAAAAAAAAA";
    private static final String UPDATED_SCRAPE_RESULT_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_SCRAPE_R_ESULT_CONTENT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_SCRAPE_R_ESULT_CONTENT_TYPE = "BBBBBBBBBB";

    private static final CrawlStatus DEFAULT_CRAWL_STATUS = CrawlStatus.SUCCESS;
    private static final CrawlStatus UPDATED_CRAWL_STATUS = CrawlStatus.FALSE;

    private static final String DEFAULT_INTERNAL_URL = "AAAAAAAAAA";
    private static final String UPDATED_INTERNAL_URL = "BBBBBBBBBB";

    @Autowired
    private LinkRepository linkRepository;

    

    @Autowired
    private LinkService linkService;

    /**
     * This repository is mocked in the io.github.scrapery.setting.repository.search test package.
     *
     * @see io.github.scrapery.setting.repository.search.LinkSearchRepositoryMockConfiguration
     */
    @Autowired
    private LinkSearchRepository mockLinkSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restLinkMockMvc;

    private Link link;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LinkResource linkResource = new LinkResource(linkService);
        this.restLinkMockMvc = MockMvcBuilders.standaloneSetup(linkResource)
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
    public static Link createEntity() {
        Link link = new Link()
            .url(DEFAULT_URL)
            .scrapeDataId(DEFAULT_SCRAPE_DATA_ID)
            .scrapeId(DEFAULT_SCRAPE_ID)
            .currentLevel(DEFAULT_CURRENT_LEVEL)
            .scrapeUrl(DEFAULT_SCRAPE_URL)
            .parentUrl(DEFAULT_PARENT_URL)
            .scrapeResultId(DEFAULT_SCRAPE_RESULT_ID)
            .scrapeResultPath(DEFAULT_SCRAPE_RESULT_PATH)
            .scrapeREsultContentType(DEFAULT_SCRAPE_R_ESULT_CONTENT_TYPE)
            .crawlStatus(DEFAULT_CRAWL_STATUS)
            .internalUrl(DEFAULT_INTERNAL_URL);
        return link;
    }

    @Before
    public void initTest() {
        linkRepository.deleteAll();
        link = createEntity();
    }

    @Test
    public void createLink() throws Exception {
        int databaseSizeBeforeCreate = linkRepository.findAll().size();

        // Create the Link
        restLinkMockMvc.perform(post("/api/links")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(link)))
            .andExpect(status().isCreated());

        // Validate the Link in the database
        List<Link> linkList = linkRepository.findAll();
        assertThat(linkList).hasSize(databaseSizeBeforeCreate + 1);
        Link testLink = linkList.get(linkList.size() - 1);
        assertThat(testLink.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testLink.getScrapeDataId()).isEqualTo(DEFAULT_SCRAPE_DATA_ID);
        assertThat(testLink.getScrapeId()).isEqualTo(DEFAULT_SCRAPE_ID);
        assertThat(testLink.getCurrentLevel()).isEqualTo(DEFAULT_CURRENT_LEVEL);
        assertThat(testLink.getScrapeUrl()).isEqualTo(DEFAULT_SCRAPE_URL);
        assertThat(testLink.getParentUrl()).isEqualTo(DEFAULT_PARENT_URL);
        assertThat(testLink.getScrapeResultId()).isEqualTo(DEFAULT_SCRAPE_RESULT_ID);
        assertThat(testLink.getScrapeResultPath()).isEqualTo(DEFAULT_SCRAPE_RESULT_PATH);
        assertThat(testLink.getScrapeREsultContentType()).isEqualTo(DEFAULT_SCRAPE_R_ESULT_CONTENT_TYPE);
        assertThat(testLink.getCrawlStatus()).isEqualTo(DEFAULT_CRAWL_STATUS);
        assertThat(testLink.getInternalUrl()).isEqualTo(DEFAULT_INTERNAL_URL);

        // Validate the Link in Elasticsearch
        verify(mockLinkSearchRepository, times(1)).save(testLink);
    }

    @Test
    public void createLinkWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = linkRepository.findAll().size();

        // Create the Link with an existing ID
        link.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restLinkMockMvc.perform(post("/api/links")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(link)))
            .andExpect(status().isBadRequest());

        // Validate the Link in the database
        List<Link> linkList = linkRepository.findAll();
        assertThat(linkList).hasSize(databaseSizeBeforeCreate);

        // Validate the Link in Elasticsearch
        verify(mockLinkSearchRepository, times(0)).save(link);
    }

    @Test
    public void getAllLinks() throws Exception {
        // Initialize the database
        linkRepository.save(link);

        // Get all the linkList
        restLinkMockMvc.perform(get("/api/links?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(link.getId())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
            .andExpect(jsonPath("$.[*].scrapeDataId").value(hasItem(DEFAULT_SCRAPE_DATA_ID.intValue())))
            .andExpect(jsonPath("$.[*].scrapeId").value(hasItem(DEFAULT_SCRAPE_ID.intValue())))
            .andExpect(jsonPath("$.[*].currentLevel").value(hasItem(DEFAULT_CURRENT_LEVEL)))
            .andExpect(jsonPath("$.[*].scrapeUrl").value(hasItem(DEFAULT_SCRAPE_URL.toString())))
            .andExpect(jsonPath("$.[*].parentUrl").value(hasItem(DEFAULT_PARENT_URL.toString())))
            .andExpect(jsonPath("$.[*].scrapeResultId").value(hasItem(DEFAULT_SCRAPE_RESULT_ID.intValue())))
            .andExpect(jsonPath("$.[*].scrapeResultPath").value(hasItem(DEFAULT_SCRAPE_RESULT_PATH.toString())))
            .andExpect(jsonPath("$.[*].scrapeREsultContentType").value(hasItem(DEFAULT_SCRAPE_R_ESULT_CONTENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].crawlStatus").value(hasItem(DEFAULT_CRAWL_STATUS.toString())))
            .andExpect(jsonPath("$.[*].internalUrl").value(hasItem(DEFAULT_INTERNAL_URL.toString())));
    }
    

    @Test
    public void getLink() throws Exception {
        // Initialize the database
        linkRepository.save(link);

        // Get the link
        restLinkMockMvc.perform(get("/api/links/{id}", link.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(link.getId()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()))
            .andExpect(jsonPath("$.scrapeDataId").value(DEFAULT_SCRAPE_DATA_ID.intValue()))
            .andExpect(jsonPath("$.scrapeId").value(DEFAULT_SCRAPE_ID.intValue()))
            .andExpect(jsonPath("$.currentLevel").value(DEFAULT_CURRENT_LEVEL))
            .andExpect(jsonPath("$.scrapeUrl").value(DEFAULT_SCRAPE_URL.toString()))
            .andExpect(jsonPath("$.parentUrl").value(DEFAULT_PARENT_URL.toString()))
            .andExpect(jsonPath("$.scrapeResultId").value(DEFAULT_SCRAPE_RESULT_ID.intValue()))
            .andExpect(jsonPath("$.scrapeResultPath").value(DEFAULT_SCRAPE_RESULT_PATH.toString()))
            .andExpect(jsonPath("$.scrapeREsultContentType").value(DEFAULT_SCRAPE_R_ESULT_CONTENT_TYPE.toString()))
            .andExpect(jsonPath("$.crawlStatus").value(DEFAULT_CRAWL_STATUS.toString()))
            .andExpect(jsonPath("$.internalUrl").value(DEFAULT_INTERNAL_URL.toString()));
    }
    @Test
    public void getNonExistingLink() throws Exception {
        // Get the link
        restLinkMockMvc.perform(get("/api/links/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateLink() throws Exception {
        // Initialize the database
        linkService.save(link);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockLinkSearchRepository);

        int databaseSizeBeforeUpdate = linkRepository.findAll().size();

        // Update the link
        Link updatedLink = linkRepository.findById(link.getId()).get();
        updatedLink
            .url(UPDATED_URL)
            .scrapeDataId(UPDATED_SCRAPE_DATA_ID)
            .scrapeId(UPDATED_SCRAPE_ID)
            .currentLevel(UPDATED_CURRENT_LEVEL)
            .scrapeUrl(UPDATED_SCRAPE_URL)
            .parentUrl(UPDATED_PARENT_URL)
            .scrapeResultId(UPDATED_SCRAPE_RESULT_ID)
            .scrapeResultPath(UPDATED_SCRAPE_RESULT_PATH)
            .scrapeREsultContentType(UPDATED_SCRAPE_R_ESULT_CONTENT_TYPE)
            .crawlStatus(UPDATED_CRAWL_STATUS)
            .internalUrl(UPDATED_INTERNAL_URL);

        restLinkMockMvc.perform(put("/api/links")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedLink)))
            .andExpect(status().isOk());

        // Validate the Link in the database
        List<Link> linkList = linkRepository.findAll();
        assertThat(linkList).hasSize(databaseSizeBeforeUpdate);
        Link testLink = linkList.get(linkList.size() - 1);
        assertThat(testLink.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testLink.getScrapeDataId()).isEqualTo(UPDATED_SCRAPE_DATA_ID);
        assertThat(testLink.getScrapeId()).isEqualTo(UPDATED_SCRAPE_ID);
        assertThat(testLink.getCurrentLevel()).isEqualTo(UPDATED_CURRENT_LEVEL);
        assertThat(testLink.getScrapeUrl()).isEqualTo(UPDATED_SCRAPE_URL);
        assertThat(testLink.getParentUrl()).isEqualTo(UPDATED_PARENT_URL);
        assertThat(testLink.getScrapeResultId()).isEqualTo(UPDATED_SCRAPE_RESULT_ID);
        assertThat(testLink.getScrapeResultPath()).isEqualTo(UPDATED_SCRAPE_RESULT_PATH);
        assertThat(testLink.getScrapeREsultContentType()).isEqualTo(UPDATED_SCRAPE_R_ESULT_CONTENT_TYPE);
        assertThat(testLink.getCrawlStatus()).isEqualTo(UPDATED_CRAWL_STATUS);
        assertThat(testLink.getInternalUrl()).isEqualTo(UPDATED_INTERNAL_URL);

        // Validate the Link in Elasticsearch
        verify(mockLinkSearchRepository, times(1)).save(testLink);
    }

    @Test
    public void updateNonExistingLink() throws Exception {
        int databaseSizeBeforeUpdate = linkRepository.findAll().size();

        // Create the Link

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restLinkMockMvc.perform(put("/api/links")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(link)))
            .andExpect(status().isBadRequest());

        // Validate the Link in the database
        List<Link> linkList = linkRepository.findAll();
        assertThat(linkList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Link in Elasticsearch
        verify(mockLinkSearchRepository, times(0)).save(link);
    }

    @Test
    public void deleteLink() throws Exception {
        // Initialize the database
        linkService.save(link);

        int databaseSizeBeforeDelete = linkRepository.findAll().size();

        // Get the link
        restLinkMockMvc.perform(delete("/api/links/{id}", link.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Link> linkList = linkRepository.findAll();
        assertThat(linkList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Link in Elasticsearch
        verify(mockLinkSearchRepository, times(1)).deleteById(link.getId());
    }

    @Test
    public void searchLink() throws Exception {
        // Initialize the database
        linkService.save(link);
        when(mockLinkSearchRepository.search(queryStringQuery("id:" + link.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(link), PageRequest.of(0, 1), 1));
        // Search the link
        restLinkMockMvc.perform(get("/api/_search/links?query=id:" + link.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(link.getId())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
            .andExpect(jsonPath("$.[*].scrapeDataId").value(hasItem(DEFAULT_SCRAPE_DATA_ID.intValue())))
            .andExpect(jsonPath("$.[*].scrapeId").value(hasItem(DEFAULT_SCRAPE_ID.intValue())))
            .andExpect(jsonPath("$.[*].currentLevel").value(hasItem(DEFAULT_CURRENT_LEVEL)))
            .andExpect(jsonPath("$.[*].scrapeUrl").value(hasItem(DEFAULT_SCRAPE_URL.toString())))
            .andExpect(jsonPath("$.[*].parentUrl").value(hasItem(DEFAULT_PARENT_URL.toString())))
            .andExpect(jsonPath("$.[*].scrapeResultId").value(hasItem(DEFAULT_SCRAPE_RESULT_ID.intValue())))
            .andExpect(jsonPath("$.[*].scrapeResultPath").value(hasItem(DEFAULT_SCRAPE_RESULT_PATH.toString())))
            .andExpect(jsonPath("$.[*].scrapeREsultContentType").value(hasItem(DEFAULT_SCRAPE_R_ESULT_CONTENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].crawlStatus").value(hasItem(DEFAULT_CRAWL_STATUS.toString())))
            .andExpect(jsonPath("$.[*].internalUrl").value(hasItem(DEFAULT_INTERNAL_URL.toString())));
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Link.class);
        Link link1 = new Link();
        link1.setId("id1");
        Link link2 = new Link();
        link2.setId(link1.getId());
        assertThat(link1).isEqualTo(link2);
        link2.setId("id2");
        assertThat(link1).isNotEqualTo(link2);
        link1.setId(null);
        assertThat(link1).isNotEqualTo(link2);
    }
}
