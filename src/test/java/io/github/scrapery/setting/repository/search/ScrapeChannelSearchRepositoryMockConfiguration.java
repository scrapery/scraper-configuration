package io.github.scrapery.setting.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of ScrapeChannelSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class ScrapeChannelSearchRepositoryMockConfiguration {

    @MockBean
    private ScrapeChannelSearchRepository mockScrapeChannelSearchRepository;

}
