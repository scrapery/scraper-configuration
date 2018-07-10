package io.github.scrapery.setting.repository.search;

import io.github.scrapery.setting.domain.ConfigGroup;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ConfigGroup entity.
 */
public interface ConfigGroupSearchRepository extends ElasticsearchRepository<ConfigGroup, String> {
}
