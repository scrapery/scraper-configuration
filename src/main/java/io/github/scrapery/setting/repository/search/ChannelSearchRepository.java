package io.github.scrapery.setting.repository.search;

import io.github.scrapery.setting.domain.Channel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Channel entity.
 */
public interface ChannelSearchRepository extends ElasticsearchRepository<Channel, String> {
}
