package io.github.scrapery.setting.repository.search;

import io.github.scrapery.setting.domain.Demo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Demo entity.
 */
public interface DemoSearchRepository extends ElasticsearchRepository<Demo, String> {
}
