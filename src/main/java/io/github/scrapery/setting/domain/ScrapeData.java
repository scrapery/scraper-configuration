package io.github.scrapery.setting.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Objects;

import io.github.scrapery.setting.domain.enumeration.ConfigDataType;

/**
 * A ScrapeData.
 */
@Document(collection = "scrape_data")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "scrapedata")
public class ScrapeData implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("scrape_data_id")
    private Long scrapeDataId;

    @Field("scrape_id")
    private Long scrapeId;

    @Field("domain")
    private String domain;

    @Field("host")
    private String host;

    @Field("url")
    private String url;

    @Field("root_scrape_url")
    private String rootScrapeUrl;

    @Field("parent_url")
    private String parentUrl;

    @Field("name")
    private String name;

    @Field("selector")
    private String selector;

    @Field("attr")
    private String attr;

    @Field("data_type")
    private ConfigDataType dataType;

    @Field("data")
    private String data;

    // simlife-needle-entity-add-field - Simlife will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getScrapeDataId() {
        return scrapeDataId;
    }

    public ScrapeData scrapeDataId(Long scrapeDataId) {
        this.scrapeDataId = scrapeDataId;
        return this;
    }

    public void setScrapeDataId(Long scrapeDataId) {
        this.scrapeDataId = scrapeDataId;
    }

    public Long getScrapeId() {
        return scrapeId;
    }

    public ScrapeData scrapeId(Long scrapeId) {
        this.scrapeId = scrapeId;
        return this;
    }

    public void setScrapeId(Long scrapeId) {
        this.scrapeId = scrapeId;
    }

    public String getDomain() {
        return domain;
    }

    public ScrapeData domain(String domain) {
        this.domain = domain;
        return this;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getHost() {
        return host;
    }

    public ScrapeData host(String host) {
        this.host = host;
        return this;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUrl() {
        return url;
    }

    public ScrapeData url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRootScrapeUrl() {
        return rootScrapeUrl;
    }

    public ScrapeData rootScrapeUrl(String rootScrapeUrl) {
        this.rootScrapeUrl = rootScrapeUrl;
        return this;
    }

    public void setRootScrapeUrl(String rootScrapeUrl) {
        this.rootScrapeUrl = rootScrapeUrl;
    }

    public String getParentUrl() {
        return parentUrl;
    }

    public ScrapeData parentUrl(String parentUrl) {
        this.parentUrl = parentUrl;
        return this;
    }

    public void setParentUrl(String parentUrl) {
        this.parentUrl = parentUrl;
    }

    public String getName() {
        return name;
    }

    public ScrapeData name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSelector() {
        return selector;
    }

    public ScrapeData selector(String selector) {
        this.selector = selector;
        return this;
    }

    public void setSelector(String selector) {
        this.selector = selector;
    }

    public String getAttr() {
        return attr;
    }

    public ScrapeData attr(String attr) {
        this.attr = attr;
        return this;
    }

    public void setAttr(String attr) {
        this.attr = attr;
    }

    public ConfigDataType getDataType() {
        return dataType;
    }

    public ScrapeData dataType(ConfigDataType dataType) {
        this.dataType = dataType;
        return this;
    }

    public void setDataType(ConfigDataType dataType) {
        this.dataType = dataType;
    }

    public String getData() {
        return data;
    }

    public ScrapeData data(String data) {
        this.data = data;
        return this;
    }

    public void setData(String data) {
        this.data = data;
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
        ScrapeData scrapeData = (ScrapeData) o;
        if (scrapeData.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), scrapeData.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ScrapeData{" +
            "id=" + getId() +
            ", scrapeDataId=" + getScrapeDataId() +
            ", scrapeId=" + getScrapeId() +
            ", domain='" + getDomain() + "'" +
            ", host='" + getHost() + "'" +
            ", url='" + getUrl() + "'" +
            ", rootScrapeUrl='" + getRootScrapeUrl() + "'" +
            ", parentUrl='" + getParentUrl() + "'" +
            ", name='" + getName() + "'" +
            ", selector='" + getSelector() + "'" +
            ", attr='" + getAttr() + "'" +
            ", dataType='" + getDataType() + "'" +
            ", data='" + getData() + "'" +
            "}";
    }
}
