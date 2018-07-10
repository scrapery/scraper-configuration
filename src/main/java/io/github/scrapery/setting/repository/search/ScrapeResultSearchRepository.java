package io.github.scrapery.setting.repository.search;

import io.github.scrapery.setting.domain.ScrapeResult;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ScrapeResult entity.
 */
public interface ScrapeResultSearchRepository extends ElasticsearchRepository<ScrapeResult, String> {
}
