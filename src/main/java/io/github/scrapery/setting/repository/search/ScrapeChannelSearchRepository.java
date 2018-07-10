package io.github.scrapery.setting.repository.search;

import io.github.scrapery.setting.domain.ScrapeChannel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ScrapeChannel entity.
 */
public interface ScrapeChannelSearchRepository extends ElasticsearchRepository<ScrapeChannel, String> {
}
