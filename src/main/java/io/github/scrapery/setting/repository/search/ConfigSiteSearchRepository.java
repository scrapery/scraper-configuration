package io.github.scrapery.setting.repository.search;

import io.github.scrapery.setting.domain.ConfigSite;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ConfigSite entity.
 */
public interface ConfigSiteSearchRepository extends ElasticsearchRepository<ConfigSite, String> {
}
