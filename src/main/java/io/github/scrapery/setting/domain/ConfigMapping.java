package io.github.scrapery.setting.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Objects;

import io.github.scrapery.setting.domain.enumeration.ConfigDataType;

/**
 * A ConfigMapping.
 */
@Document(collection = "config_mapping")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "configmapping")
public class ConfigMapping implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("name")
    private String name;

    @Field("selector")
    private String selector;

    @Field("host")
    private String host;

    @Field("config_name")
    private String configName;

    @Field("attr")
    private String attr;

    @Field("data_type")
    private ConfigDataType dataType;

    // simlife-needle-entity-add-field - Simlife will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public ConfigMapping name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSelector() {
        return selector;
    }

    public ConfigMapping selector(String selector) {
        this.selector = selector;
        return this;
    }

    public void setSelector(String selector) {
        this.selector = selector;
    }

    public String getHost() {
        return host;
    }

    public ConfigMapping host(String host) {
        this.host = host;
        return this;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getConfigName() {
        return configName;
    }

    public ConfigMapping configName(String configName) {
        this.configName = configName;
        return this;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getAttr() {
        return attr;
    }

    public ConfigMapping attr(String attr) {
        this.attr = attr;
        return this;
    }

    public void setAttr(String attr) {
        this.attr = attr;
    }

    public ConfigDataType getDataType() {
        return dataType;
    }

    public ConfigMapping dataType(ConfigDataType dataType) {
        this.dataType = dataType;
        return this;
    }

    public void setDataType(ConfigDataType dataType) {
        this.dataType = dataType;
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
        ConfigMapping configMapping = (ConfigMapping) o;
        if (configMapping.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), configMapping.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ConfigMapping{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", selector='" + getSelector() + "'" +
            ", host='" + getHost() + "'" +
            ", configName='" + getConfigName() + "'" +
            ", attr='" + getAttr() + "'" +
            ", dataType='" + getDataType() + "'" +
            "}";
    }
}
