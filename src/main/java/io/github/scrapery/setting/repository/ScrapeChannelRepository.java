package io.github.scrapery.setting.repository;

import io.github.scrapery.setting.domain.ScrapeChannel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the ScrapeChannel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ScrapeChannelRepository extends MongoRepository<ScrapeChannel, String> {



}
