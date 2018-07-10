package io.github.scrapery.setting.repository.search;

import io.github.scrapery.setting.domain.SiteChannel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the SiteChannel entity.
 */
public interface SiteChannelSearchRepository extends ElasticsearchRepository<SiteChannel, String> {
}
