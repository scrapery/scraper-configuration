package io.github.scrapery.setting.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import io.github.scrapery.setting.domain.enumeration.FetchEngine;

import io.github.scrapery.setting.domain.enumeration.DocType;

/**
 * A Scrape.
 */
@Document(collection = "scrape")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "scrape")
public class Scrape implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("url")
    private String url;

    @Field("total_level")
    private Integer totalLevel;

    @Field("archive_level")
    private Integer archiveLevel;

    @Field("current_level")
    private Integer currentLevel;

    @Field("unlimited_level")
    private Boolean unlimitedLevel;

    @Field("fetch_engine")
    private FetchEngine fetchEngine;

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

    @Field("doc_type")
    private DocType docType;


    private Set<ConfigMapping> mappings = new HashSet<>();


    private Set<ConfigSiteLogin> loginActions = new HashSet<>();

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

    public Scrape url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getTotalLevel() {
        return totalLevel;
    }

    public Scrape totalLevel(Integer totalLevel) {
        this.totalLevel = totalLevel;
        return this;
    }

    public void setTotalLevel(Integer totalLevel) {
        this.totalLevel = totalLevel;
    }

    public Integer getArchiveLevel() {
        return archiveLevel;
    }

    public Scrape archiveLevel(Integer archiveLevel) {
        this.archiveLevel = archiveLevel;
        return this;
    }

    public void setArchiveLevel(Integer archiveLevel) {
        this.archiveLevel = archiveLevel;
    }

    public Integer getCurrentLevel() {
        return currentLevel;
    }

    public Scrape currentLevel(Integer currentLevel) {
        this.currentLevel = currentLevel;
        return this;
    }

    public void setCurrentLevel(Integer currentLevel) {
        this.currentLevel = currentLevel;
    }

    public Boolean isUnlimitedLevel() {
        return unlimitedLevel;
    }

    public Scrape unlimitedLevel(Boolean unlimitedLevel) {
        this.unlimitedLevel = unlimitedLevel;
        return this;
    }

    public void setUnlimitedLevel(Boolean unlimitedLevel) {
        this.unlimitedLevel = unlimitedLevel;
    }

    public FetchEngine getFetchEngine() {
        return fetchEngine;
    }

    public Scrape fetchEngine(FetchEngine fetchEngine) {
        this.fetchEngine = fetchEngine;
        return this;
    }

    public void setFetchEngine(FetchEngine fetchEngine) {
        this.fetchEngine = fetchEngine;
    }

    public Long getChannelId() {
        return channelId;
    }

    public Scrape channelId(Long channelId) {
        this.channelId = channelId;
        return this;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public String getCategory() {
        return category;
    }

    public Scrape category(String category) {
        this.category = category;
        return this;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTag() {
        return tag;
    }

    public Scrape tag(String tag) {
        this.tag = tag;
        return this;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public Scrape countryCode(String countryCode) {
        this.countryCode = countryCode;
        return this;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public Scrape languageCode(String languageCode) {
        this.languageCode = languageCode;
        return this;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public DocType getDocType() {
        return docType;
    }

    public Scrape docType(DocType docType) {
        this.docType = docType;
        return this;
    }

    public void setDocType(DocType docType) {
        this.docType = docType;
    }

    public Set<ConfigMapping> getMappings() {
        return mappings;
    }

    public Scrape mappings(Set<ConfigMapping> configMappings) {
        this.mappings = configMappings;
        return this;
    }

    public Scrape addMapping(ConfigMapping configMapping) {
        this.mappings.add(configMapping);
        return this;
    }

    public Scrape removeMapping(ConfigMapping configMapping) {
        this.mappings.remove(configMapping);
        return this;
    }

    public void setMappings(Set<ConfigMapping> configMappings) {
        this.mappings = configMappings;
    }

    public Set<ConfigSiteLogin> getLoginActions() {
        return loginActions;
    }

    public Scrape loginActions(Set<ConfigSiteLogin> configSiteLogins) {
        this.loginActions = configSiteLogins;
        return this;
    }

    public Scrape addLoginAction(ConfigSiteLogin configSiteLogin) {
        this.loginActions.add(configSiteLogin);
        return this;
    }

    public Scrape removeLoginAction(ConfigSiteLogin configSiteLogin) {
        this.loginActions.remove(configSiteLogin);
        return this;
    }

    public void setLoginActions(Set<ConfigSiteLogin> configSiteLogins) {
        this.loginActions = configSiteLogins;
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
        Scrape scrape = (Scrape) o;
        if (scrape.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), scrape.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Scrape{" +
            "id=" + getId() +
            ", url='" + getUrl() + "'" +
            ", totalLevel=" + getTotalLevel() +
            ", archiveLevel=" + getArchiveLevel() +
            ", currentLevel=" + getCurrentLevel() +
            ", unlimitedLevel='" + isUnlimitedLevel() + "'" +
            ", fetchEngine='" + getFetchEngine() + "'" +
            ", channelId=" + getChannelId() +
            ", category='" + getCategory() + "'" +
            ", tag='" + getTag() + "'" +
            ", countryCode='" + getCountryCode() + "'" +
            ", languageCode='" + getLanguageCode() + "'" +
            ", docType='" + getDocType() + "'" +
            "}";
    }
}
