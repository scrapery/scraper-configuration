package io.github.scrapery.setting.repository;

import io.github.scrapery.setting.domain.FetchSiteAction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the FetchSiteAction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FetchSiteActionRepository extends MongoRepository<FetchSiteAction, String> {

}
