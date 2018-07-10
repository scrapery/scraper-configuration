package io.github.scrapery.setting.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of SiteChannelSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class SiteChannelSearchRepositoryMockConfiguration {

    @MockBean
    private SiteChannelSearchRepository mockSiteChannelSearchRepository;

}
