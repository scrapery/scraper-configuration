package io.github.scrapery.setting.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Objects;

/**
 * A ScrapeResult.
 */
@Document(collection = "scrape_result")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "scraperesult")
public class ScrapeResult implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("scrape_id")
    private Long scrapeId;

    @Field("content")
    private byte[] content;

    @Field("content_content_type")
    private String contentContentType;

    @Field("path")
    private String path;

    @Field("target_object")
    private String targetObject;

    @Field("target_queue")
    private String targetQueue;

    @Field("channel_id")
    private Long channelId;

    @Field("category")
    private String category;

    @Field("tag")
    private String tag;

    @Field("country_code")
    private String countryCode;

    @Field("language_code")
    private String languageCode;

    // simlife-needle-entity-add-field - Simlife will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getScrapeId() {
        return scrapeId;
    }

    public ScrapeResult scrapeId(Long scrapeId) {
        this.scrapeId = scrapeId;
        return this;
    }

    public void setScrapeId(Long scrapeId) {
        this.scrapeId = scrapeId;
    }

    public byte[] getContent() {
        return content;
    }

    public ScrapeResult content(byte[] content) {
        this.content = content;
        return this;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getContentContentType() {
        return contentContentType;
    }

    public ScrapeResult contentContentType(String contentContentType) {
        this.contentContentType = contentContentType;
        return this;
    }

    public void setContentContentType(String contentContentType) {
        this.contentContentType = contentContentType;
    }

    public String getPath() {
        return path;
    }

    public ScrapeResult path(String path) {
        this.path = path;
        return this;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTargetObject() {
        return targetObject;
    }

    public ScrapeResult targetObject(String targetObject) {
        this.targetObject = targetObject;
        return this;
    }

    public void setTargetObject(String targetObject) {
        this.targetObject = targetObject;
    }

    public String getTargetQueue() {
        return targetQueue;
    }

    public ScrapeResult targetQueue(String targetQueue) {
        this.targetQueue = targetQueue;
        return this;
    }

    public void setTargetQueue(String targetQueue) {
        this.targetQueue = targetQueue;
    }

    public Long getChannelId() {
        return channelId;
    }

    public ScrapeResult channelId(Long channelId) {
        this.channelId = channelId;
        return this;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public String getCategory() {
        return category;
    }

    public ScrapeResult category(String category) {
        this.category = category;
        return this;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTag() {
        return tag;
    }

    public ScrapeResult tag(String tag) {
        this.tag = tag;
        return this;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public ScrapeResult countryCode(String countryCode) {
        this.countryCode = countryCode;
        return this;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public ScrapeResult languageCode(String languageCode) {
        this.languageCode = languageCode;
        return this;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
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
        ScrapeResult scrapeResult = (ScrapeResult) o;
        if (scrapeResult.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), scrapeResult.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ScrapeResult{" +
            "id=" + getId() +
            ", scrapeId=" + getScrapeId() +
            ", content='" + getContent() + "'" +
            ", contentContentType='" + getContentContentType() + "'" +
            ", path='" + getPath() + "'" +
            ", targetObject='" + getTargetObject() + "'" +
            ", targetQueue='" + getTargetQueue() + "'" +
            ", channelId=" + getChannelId() +
            ", category='" + getCategory() + "'" +
            ", tag='" + getTag() + "'" +
            ", countryCode='" + getCountryCode() + "'" +
            ", languageCode='" + getLanguageCode() + "'" +
            "}";
    }
}
