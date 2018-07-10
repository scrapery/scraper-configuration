package io.github.scrapery.setting.repository.search;

import io.github.scrapery.setting.domain.Link;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Link entity.
 */
public interface LinkSearchRepository extends ElasticsearchRepository<Link, String> {
}
