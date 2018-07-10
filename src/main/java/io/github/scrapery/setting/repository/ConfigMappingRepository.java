package io.github.scrapery.setting.repository;

import io.github.scrapery.setting.domain.ConfigMapping;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the ConfigMapping entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConfigMappingRepository extends MongoRepository<ConfigMapping, String> {

}
