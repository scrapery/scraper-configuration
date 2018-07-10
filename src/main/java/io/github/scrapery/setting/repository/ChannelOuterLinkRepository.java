package io.github.scrapery.setting.repository;

import io.github.scrapery.setting.domain.ChannelOuterLink;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the ChannelOuterLink entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChannelOuterLinkRepository extends MongoRepository<ChannelOuterLink, String> {

}
