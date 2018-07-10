package io.github.scrapery.setting.repository.search;

import io.github.scrapery.setting.domain.TryParser;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the TryParser entity.
 */
public interface TryParserSearchRepository extends ElasticsearchRepository<TryParser, String> {
}
