package io.github.scrapery.setting.repository.search;

import io.github.scrapery.setting.domain.ConfigMapping;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ConfigMapping entity.
 */
public interface ConfigMappingSearchRepository extends ElasticsearchRepository<ConfigMapping, String> {
}
