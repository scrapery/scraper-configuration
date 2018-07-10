package io.github.scrapery.setting.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Objects;

import io.github.scrapery.setting.domain.enumeration.Action;

import io.github.scrapery.setting.domain.enumeration.SeleniumActionGetContent;

/**
 * A FetchSiteAction.
 */
@Document(collection = "fetch_site_action")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "fetchsiteaction")
public class FetchSiteAction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("url")
    private String url;

    @Field("domain")
    private String domain;

    @Field("host")
    private String host;

    @Field("active_level")
    private Integer activeLevel;

    @Field("selector_action")
    private String selectorAction;

    @Field("selector_action_attr")
    private String selectorActionAttr;

    @Field("action")
    private Action action;

    @Field("total_actions")
    private Integer totalActions;

    @Field("selenium_action_get_content")
    private SeleniumActionGetContent seleniumActionGetContent;

    @Field("selector_next_page_urls_name")
    private String selectorNextPageUrlsName;

    @Field("selector_next_page_urls_name_attr")
    private String selectorNextPageUrlsNameAttr;

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

    public FetchSiteAction url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDomain() {
        return domain;
    }

    public FetchSiteAction domain(String domain) {
        this.domain = domain;
        return this;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getHost() {
        return host;
    }

    public FetchSiteAction host(String host) {
        this.host = host;
        return this;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getActiveLevel() {
        return activeLevel;
    }

    public FetchSiteAction activeLevel(Integer activeLevel) {
        this.activeLevel = activeLevel;
        return this;
    }

    public void setActiveLevel(Integer activeLevel) {
        this.activeLevel = activeLevel;
    }

    public String getSelectorAction() {
        return selectorAction;
    }

    public FetchSiteAction selectorAction(String selectorAction) {
        this.selectorAction = selectorAction;
        return this;
    }

    public void setSelectorAction(String selectorAction) {
        this.selectorAction = selectorAction;
    }

    public String getSelectorActionAttr() {
        return selectorActionAttr;
    }

    public FetchSiteAction selectorActionAttr(String selectorActionAttr) {
        this.selectorActionAttr = selectorActionAttr;
        return this;
    }

    public void setSelectorActionAttr(String selectorActionAttr) {
        this.selectorActionAttr = selectorActionAttr;
    }

    public Action getAction() {
        return action;
    }

    public FetchSiteAction action(Action action) {
        this.action = action;
        return this;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public Integer getTotalActions() {
        return totalActions;
    }

    public FetchSiteAction totalActions(Integer totalActions) {
        this.totalActions = totalActions;
        return this;
    }

    public void setTotalActions(Integer totalActions) {
        this.totalActions = totalActions;
    }

    public SeleniumActionGetContent getSeleniumActionGetContent() {
        return seleniumActionGetContent;
    }

    public FetchSiteAction seleniumActionGetContent(SeleniumActionGetContent seleniumActionGetContent) {
        this.seleniumActionGetContent = seleniumActionGetContent;
        return this;
    }

    public void setSeleniumActionGetContent(SeleniumActionGetContent seleniumActionGetContent) {
        this.seleniumActionGetContent = seleniumActionGetContent;
    }

    public String getSelectorNextPageUrlsName() {
        return selectorNextPageUrlsName;
    }

    public FetchSiteAction selectorNextPageUrlsName(String selectorNextPageUrlsName) {
        this.selectorNextPageUrlsName = selectorNextPageUrlsName;
        return this;
    }

    public void setSelectorNextPageUrlsName(String selectorNextPageUrlsName) {
        this.selectorNextPageUrlsName = selectorNextPageUrlsName;
    }

    public String getSelectorNextPageUrlsNameAttr() {
        return selectorNextPageUrlsNameAttr;
    }

    public FetchSiteAction selectorNextPageUrlsNameAttr(String selectorNextPageUrlsNameAttr) {
        this.selectorNextPageUrlsNameAttr = selectorNextPageUrlsNameAttr;
        return this;
    }

    public void setSelectorNextPageUrlsNameAttr(String selectorNextPageUrlsNameAttr) {
        this.selectorNextPageUrlsNameAttr = selectorNextPageUrlsNameAttr;
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
        FetchSiteAction fetchSiteAction = (FetchSiteAction) o;
        if (fetchSiteAction.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), fetchSiteAction.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FetchSiteAction{" +
            "id=" + getId() +
            ", url='" + getUrl() + "'" +
            ", domain='" + getDomain() + "'" +
            ", host='" + getHost() + "'" +
            ", activeLevel=" + getActiveLevel() +
            ", selectorAction='" + getSelectorAction() + "'" +
            ", selectorActionAttr='" + getSelectorActionAttr() + "'" +
            ", action='" + getAction() + "'" +
            ", totalActions=" + getTotalActions() +
            ", seleniumActionGetContent='" + getSeleniumActionGetContent() + "'" +
            ", selectorNextPageUrlsName='" + getSelectorNextPageUrlsName() + "'" +
            ", selectorNextPageUrlsNameAttr='" + getSelectorNextPageUrlsNameAttr() + "'" +
            "}";
    }
}
