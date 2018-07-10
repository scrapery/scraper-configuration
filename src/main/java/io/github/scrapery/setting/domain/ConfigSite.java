package io.github.scrapery.setting.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import io.github.scrapery.setting.domain.enumeration.FetchEngine;

/**
 * A ConfigSite.
 */
@Document(collection = "config_site")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "configsite")
public class ConfigSite implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("url")
    private String url;

    @Field("name")
    private String name;

    @Field("host")
    private String host;

    @Field("config_name")
    private String configName;

    @Field("total_level")
    private Integer totalLevel;

    @Field("user_id")
    private Long userId;

    @Field("fetch_engine")
    private FetchEngine fetchEngine;


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

    public ConfigSite url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public ConfigSite name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public ConfigSite host(String host) {
        this.host = host;
        return this;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getConfigName() {
        return configName;
    }

    public ConfigSite configName(String configName) {
        this.configName = configName;
        return this;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public Integer getTotalLevel() {
        return totalLevel;
    }

    public ConfigSite totalLevel(Integer totalLevel) {
        this.totalLevel = totalLevel;
        return this;
    }

    public void setTotalLevel(Integer totalLevel) {
        this.totalLevel = totalLevel;
    }

    public Long getUserId() {
        return userId;
    }

    public ConfigSite userId(Long userId) {
        this.userId = userId;
        return this;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public FetchEngine getFetchEngine() {
        return fetchEngine;
    }

    public ConfigSite fetchEngine(FetchEngine fetchEngine) {
        this.fetchEngine = fetchEngine;
        return this;
    }

    public void setFetchEngine(FetchEngine fetchEngine) {
        this.fetchEngine = fetchEngine;
    }

    public Set<ConfigMapping> getMappings() {
        return mappings;
    }

    public ConfigSite mappings(Set<ConfigMapping> configMappings) {
        this.mappings = configMappings;
        return this;
    }

    public ConfigSite addMapping(ConfigMapping configMapping) {
        this.mappings.add(configMapping);
        return this;
    }

    public ConfigSite removeMapping(ConfigMapping configMapping) {
        this.mappings.remove(configMapping);
        return this;
    }

    public void setMappings(Set<ConfigMapping> configMappings) {
        this.mappings = configMappings;
    }

    public Set<ConfigSiteLogin> getLoginActions() {
        return loginActions;
    }

    public ConfigSite loginActions(Set<ConfigSiteLogin> configSiteLogins) {
        this.loginActions = configSiteLogins;
        return this;
    }

    public ConfigSite addLoginAction(ConfigSiteLogin configSiteLogin) {
        this.loginActions.add(configSiteLogin);
        return this;
    }

    public ConfigSite removeLoginAction(ConfigSiteLogin configSiteLogin) {
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
        ConfigSite configSite = (ConfigSite) o;
        if (configSite.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), configSite.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ConfigSite{" +
            "id=" + getId() +
            ", url='" + getUrl() + "'" +
            ", name='" + getName() + "'" +
            ", host='" + getHost() + "'" +
            ", configName='" + getConfigName() + "'" +
            ", totalLevel=" + getTotalLevel() +
            ", userId=" + getUserId() +
            ", fetchEngine='" + getFetchEngine() + "'" +
            "}";
    }
}
