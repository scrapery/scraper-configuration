package io.github.scrapery.setting.repository.search;

import io.github.scrapery.setting.domain.Scrape;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Scrape entity.
 */
public interface ScrapeSearchRepository extends ElasticsearchRepository<Scrape, String> {
}
