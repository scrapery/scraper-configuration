package io.github.scrapery.setting.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Objects;

import io.github.scrapery.setting.domain.enumeration.CrawlStatus;

/**
 * A Link.
 */
@Document(collection = "link")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "link")
public class Link implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("url")
    private String url;

    @Field("scrape_data_id")
    private Long scrapeDataId;

    @Field("scrape_id")
    private Long scrapeId;

    @Field("current_level")
    private Integer currentLevel;

    @Field("scrape_url")
    private String scrapeUrl;

    @Field("parent_url")
    private String parentUrl;

    @Field("scrape_result_id")
    private Long scrapeResultId;

    @Field("scrape_result_path")
    private String scrapeResultPath;

    @Field("scrape_r_esult_content_type")
    private String scrapeREsultContentType;

    @Field("crawl_status")
    private CrawlStatus crawlStatus;

    @Field("internal_url")
    private String internalUrl;

    // simlife-needle-entity-add-field - Simlife will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public Link url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getScrapeDataId() {
        return scrapeDataId;
    }

    public Link scrapeDataId(Long scrapeDataId) {
        this.scrapeDataId = scrapeDataId;
        return this;
    }

    public void setScrapeDataId(Long scrapeDataId) {
        this.scrapeDataId = scrapeDataId;
    }

    public Long getScrapeId() {
        return scrapeId;
    }

    public Link scrapeId(Long scrapeId) {
        this.scrapeId = scrapeId;
        return this;
    }

    public void setScrapeId(Long scrapeId) {
        this.scrapeId = scrapeId;
    }

    public Integer getCurrentLevel() {
        return currentLevel;
    }

    public Link currentLevel(Integer currentLevel) {
        this.currentLevel = currentLevel;
        return this;
    }

    public void setCurrentLevel(Integer currentLevel) {
        this.currentLevel = currentLevel;
    }

    public String getScrapeUrl() {
        return scrapeUrl;
    }

    public Link scrapeUrl(String scrapeUrl) {
        this.scrapeUrl = scrapeUrl;
        return this;
    }

    public void setScrapeUrl(String scrapeUrl) {
        this.scrapeUrl = scrapeUrl;
    }

    public String getParentUrl() {
        return parentUrl;
    }

    public Link parentUrl(String parentUrl) {
        this.parentUrl = parentUrl;
        return this;
    }

    public void setParentUrl(String parentUrl) {
        this.parentUrl = parentUrl;
    }

    public Long getScrapeResultId() {
        return scrapeResultId;
    }

    public Link scrapeResultId(Long scrapeResultId) {
        this.scrapeResultId = scrapeResultId;
        return this;
    }

    public void setScrapeResultId(Long scrapeResultId) {
        this.scrapeResultId = scrapeResultId;
    }

    public String getScrapeResultPath() {
        return scrapeResultPath;
    }

    public Link scrapeResultPath(String scrapeResultPath) {
        this.scrapeResultPath = scrapeResultPath;
        return this;
    }

    public void setScrapeResultPath(String scrapeResultPath) {
        this.scrapeResultPath = scrapeResultPath;
    }

    public String getScrapeREsultContentType() {
        return scrapeREsultContentType;
    }

    public Link scrapeREsultContentType(String scrapeREsultContentType) {
        this.scrapeREsultContentType = scrapeREsultContentType;
        return this;
    }

    public void setScrapeREsultContentType(String scrapeREsultContentType) {
        this.scrapeREsultContentType = scrapeREsultContentType;
    }

    public CrawlStatus getCrawlStatus() {
        return crawlStatus;
    }

    public Link crawlStatus(CrawlStatus crawlStatus) {
        this.crawlStatus = crawlStatus;
        return this;
    }

    public void setCrawlStatus(CrawlStatus crawlStatus) {
        this.crawlStatus = crawlStatus;
    }

    public String getInternalUrl() {
        return internalUrl;
    }

    public Link internalUrl(String internalUrl) {
        this.internalUrl = internalUrl;
        return this;
    }

    public void setInternalUrl(String internalUrl) {
        this.internalUrl = internalUrl;
    }
    // simlife-needle-entity-add-getters-setters - Simlife will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Link link = (Link) o;
        if (link.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), link.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Link{" +
            "id=" + getId() +
            ", url='" + getUrl() + "'" +
            ", scrapeDataId=" + getScrapeDataId() +
            ", scrapeId=" + getScrapeId() +
            ", currentLevel=" + getCurrentLevel() +
            ", scrapeUrl='" + getScrapeUrl() + "'" +
            ", parentUrl='" + getParentUrl() + "'" +
            ", scrapeResultId=" + getScrapeResultId() +
            ", scrapeResultPath='" + getScrapeResultPath() + "'" +
            ", scrapeREsultContentType='" + getScrapeREsultContentType() + "'" +
            ", crawlStatus='" + getCrawlStatus() + "'" +
            ", internalUrl='" + getInternalUrl() + "'" +
            "}";
    }
}
