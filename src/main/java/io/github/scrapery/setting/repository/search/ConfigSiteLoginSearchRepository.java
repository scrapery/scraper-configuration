package io.github.scrapery.setting.repository.search;

import io.github.scrapery.setting.domain.ConfigSiteLogin;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ConfigSiteLogin entity.
 */
public interface ConfigSiteLoginSearchRepository extends ElasticsearchRepository<ConfigSiteLogin, String> {
}
