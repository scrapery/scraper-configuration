package io.github.scrapery.setting.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import io.github.scrapery.setting.domain.enumeration.DocType;

import io.github.scrapery.setting.domain.enumeration.FetchEngine;

/**
 * A ScrapeChannel.
 */
@Document(collection = "scrape_channel")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "scrapechannel")
public class ScrapeChannel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("url")
    private String url;

    @Field("content_type")
    private DocType contentType;

    @Field("total_level")
    private Integer totalLevel;

    @Field("archive_level")
    private Integer archiveLevel;

    @Field("fetch_engine")
    private FetchEngine fetchEngine;

    @Field("category")
    private String category;

    @Field("tag")
    private String tag;

    @Field("category_slug")
    private String categorySlug;

    @Field("tag_slug")
    private String tagSlug;

    @Field("country_code")
    private String countryCode;

    @Field("language_code")
    private String languageCode;

    @Field("target_queue_channel")
    private String targetQueueChannel;

    @Field("channel_total_level")
    private Integer channelTotalLevel;

    @Field("channel_archive_level")
    private Integer channelArchiveLevel;

    @Field("channel_fetch_engine")
    private FetchEngine channelFetchEngine;


    private Set<ConfigGroup> configGroups = new HashSet<>();


    private Set<ConfigGroup> targetGroups = new HashSet<>();

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

    public ScrapeChannel url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public DocType getContentType() {
        return contentType;
    }

    public ScrapeChannel contentType(DocType contentType) {
        this.contentType = contentType;
        return this;
    }

    public void setContentType(DocType contentType) {
        this.contentType = contentType;
    }

    public Integer getTotalLevel() {
        return totalLevel;
    }

    public ScrapeChannel totalLevel(Integer totalLevel) {
        this.totalLevel = totalLevel;
        return this;
    }

    public void setTotalLevel(Integer totalLevel) {
        this.totalLevel = totalLevel;
    }

    public Integer getArchiveLevel() {
        return archiveLevel;
    }

    public ScrapeChannel archiveLevel(Integer archiveLevel) {
        this.archiveLevel = archiveLevel;
        return this;
    }

    public void setArchiveLevel(Integer archiveLevel) {
        this.archiveLevel = archiveLevel;
    }

    public FetchEngine getFetchEngine() {
        return fetchEngine;
    }

    public ScrapeChannel fetchEngine(FetchEngine fetchEngine) {
        this.fetchEngine = fetchEngine;
        return this;
    }

    public void setFetchEngine(FetchEngine fetchEngine) {
        this.fetchEngine = fetchEngine;
    }

    public String getCategory() {
        return category;
    }

    public ScrapeChannel category(String category) {
        this.category = category;
        return this;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTag() {
        return tag;
    }

    public ScrapeChannel tag(String tag) {
        this.tag = tag;
        return this;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getCategorySlug() {
        return categorySlug;
    }

    public ScrapeChannel categorySlug(String categorySlug) {
        this.categorySlug = categorySlug;
        return this;
    }

    public void setCategorySlug(String categorySlug) {
        this.categorySlug = categorySlug;
    }

    public String getTagSlug() {
        return tagSlug;
    }

    public ScrapeChannel tagSlug(String tagSlug) {
        this.tagSlug = tagSlug;
        return this;
    }

    public void setTagSlug(String tagSlug) {
        this.tagSlug = tagSlug;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public ScrapeChannel countryCode(String countryCode) {
        this.countryCode = countryCode;
        return this;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public ScrapeChannel languageCode(String languageCode) {
        this.languageCode = languageCode;
        return this;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getTargetQueueChannel() {
        return targetQueueChannel;
    }

    public ScrapeChannel targetQueueChannel(String targetQueueChannel) {
        this.targetQueueChannel = targetQueueChannel;
        return this;
    }

    public void setTargetQueueChannel(String targetQueueChannel) {
        this.targetQueueChannel = targetQueueChannel;
    }

    public Integer getChannelTotalLevel() {
        return channelTotalLevel;
    }

    public ScrapeChannel channelTotalLevel(Integer channelTotalLevel) {
        this.channelTotalLevel = channelTotalLevel;
        return this;
    }

    public void setChannelTotalLevel(Integer channelTotalLevel) {
        this.channelTotalLevel = channelTotalLevel;
    }

    public Integer getChannelArchiveLevel() {
        return channelArchiveLevel;
    }

    public ScrapeChannel channelArchiveLevel(Integer channelArchiveLevel) {
        this.channelArchiveLevel = channelArchiveLevel;
        return this;
    }

    public void setChannelArchiveLevel(Integer channelArchiveLevel) {
        this.channelArchiveLevel = channelArchiveLevel;
    }

    public FetchEngine getChannelFetchEngine() {
        return channelFetchEngine;
    }

    public ScrapeChannel channelFetchEngine(FetchEngine channelFetchEngine) {
        this.channelFetchEngine = channelFetchEngine;
        return this;
    }

    public void setChannelFetchEngine(FetchEngine channelFetchEngine) {
        this.channelFetchEngine = channelFetchEngine;
    }

    public Set<ConfigGroup> getConfigGroups() {
        return configGroups;
    }

    public ScrapeChannel configGroups(Set<ConfigGroup> configGroups) {
        this.configGroups = configGroups;
        return this;
    }

    public ScrapeChannel addConfigGroup(ConfigGroup configGroup) {
        this.configGroups.add(configGroup);
        return this;
    }

    public ScrapeChannel removeConfigGroup(ConfigGroup configGroup) {
        this.configGroups.remove(configGroup);
        return this;
    }

    public void setConfigGroups(Set<ConfigGroup> configGroups) {
        this.configGroups = configGroups;
    }

    public Set<ConfigGroup> getTargetGroups() {
        return targetGroups;
    }

    public ScrapeChannel targetGroups(Set<ConfigGroup> configGroups) {
        this.targetGroups = configGroups;
        return this;
    }

    public ScrapeChannel addTargetGroup(ConfigGroup configGroup) {
        this.targetGroups.add(configGroup);
        return this;
    }

    public ScrapeChannel removeTargetGroup(ConfigGroup configGroup) {
        this.targetGroups.remove(configGroup);
        return this;
    }

    public void setTargetGroups(Set<ConfigGroup> configGroups) {
        this.targetGroups = configGroups;
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
        ScrapeChannel scrapeChannel = (ScrapeChannel) o;
        if (scrapeChannel.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), scrapeChannel.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ScrapeChannel{" +
            "id=" + getId() +
            ", url='" + getUrl() + "'" +
            ", contentType='" + getContentType() + "'" +
            ", totalLevel=" + getTotalLevel() +
            ", archiveLevel=" + getArchiveLevel() +
            ", fetchEngine='" + getFetchEngine() + "'" +
            ", category='" + getCategory() + "'" +
            ", tag='" + getTag() + "'" +
            ", categorySlug='" + getCategorySlug() + "'" +
            ", tagSlug='" + getTagSlug() + "'" +
            ", countryCode='" + getCountryCode() + "'" +
            ", languageCode='" + getLanguageCode() + "'" +
            ", targetQueueChannel='" + getTargetQueueChannel() + "'" +
            ", channelTotalLevel=" + getChannelTotalLevel() +
            ", channelArchiveLevel=" + getChannelArchiveLevel() +
            ", channelFetchEngine='" + getChannelFetchEngine() + "'" +
            "}";
    }
}
