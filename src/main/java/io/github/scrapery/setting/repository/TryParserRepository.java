package io.github.scrapery.setting.repository;

import io.github.scrapery.setting.domain.TryParser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the TryParser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TryParserRepository extends MongoRepository<TryParser, String> {

}
