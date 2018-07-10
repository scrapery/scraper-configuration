package io.github.scrapery.setting.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Objects;

import io.github.scrapery.setting.domain.enumeration.TryFetchEngine;

import io.github.scrapery.setting.domain.enumeration.DocType;

/**
 * A TryParser.
 */
@Document(collection = "try_parser")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "tryparser")
public class TryParser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("url")
    private String url;

    @Field("user_agent")
    private String userAgent;

    @Field("html_content")
    private String htmlContent;

    @Field("parsed_content")
    private String parsedContent;

    @Field("selector")
    private String selector;

    @Field("selector_result")
    private String selectorResult;

    @Field("fetch_engine")
    private TryFetchEngine fetchEngine;

    @Field("attribute_selector")
    private String attributeSelector;

    @Field("doc_type")
    private DocType docType;

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

    public TryParser url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public TryParser userAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public TryParser htmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
        return this;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    public String getParsedContent() {
        return parsedContent;
    }

    public TryParser parsedContent(String parsedContent) {
        this.parsedContent = parsedContent;
        return this;
    }

    public void setParsedContent(String parsedContent) {
        this.parsedContent = parsedContent;
    }

    public String getSelector() {
        return selector;
    }

    public TryParser selector(String selector) {
        this.selector = selector;
        return this;
    }

    public void setSelector(String selector) {
        this.selector = selector;
    }

    public String getSelectorResult() {
        return selectorResult;
    }

    public TryParser selectorResult(String selectorResult) {
        this.selectorResult = selectorResult;
        return this;
    }

    public void setSelectorResult(String selectorResult) {
        this.selectorResult = selectorResult;
    }

    public TryFetchEngine getFetchEngine() {
        return fetchEngine;
    }

    public TryParser fetchEngine(TryFetchEngine fetchEngine) {
        this.fetchEngine = fetchEngine;
        return this;
    }

    public void setFetchEngine(TryFetchEngine fetchEngine) {
        this.fetchEngine = fetchEngine;
    }

    public String getAttributeSelector() {
        return attributeSelector;
    }

    public TryParser attributeSelector(String attributeSelector) {
        this.attributeSelector = attributeSelector;
        return this;
    }

    public void setAttributeSelector(String attributeSelector) {
        this.attributeSelector = attributeSelector;
    }

    public DocType getDocType() {
        return docType;
    }

    public TryParser docType(DocType docType) {
        this.docType = docType;
        return this;
    }

    public void setDocType(DocType docType) {
        this.docType = docType;
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
        TryParser tryParser = (TryParser) o;
        if (tryParser.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tryParser.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TryParser{" +
            "id=" + getId() +
            ", url='" + getUrl() + "'" +
            ", userAgent='" + getUserAgent() + "'" +
            ", htmlContent='" + getHtmlContent() + "'" +
            ", parsedContent='" + getParsedContent() + "'" +
            ", selector='" + getSelector() + "'" +
            ", selectorResult='" + getSelectorResult() + "'" +
            ", fetchEngine='" + getFetchEngine() + "'" +
            ", attributeSelector='" + getAttributeSelector() + "'" +
            ", docType='" + getDocType() + "'" +
            "}";
    }
}
