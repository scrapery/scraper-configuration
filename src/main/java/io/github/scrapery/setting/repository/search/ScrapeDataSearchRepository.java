package io.github.scrapery.setting.repository.search;

import io.github.scrapery.setting.domain.ScrapeData;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ScrapeData entity.
 */
public interface ScrapeDataSearchRepository extends ElasticsearchRepository<ScrapeData, String> {
}
