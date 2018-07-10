package io.github.scrapery.setting.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Objects;

/**
 * A ChannelOuterLink.
 */
@Document(collection = "channel_outer_link")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "channelouterlink")
public class ChannelOuterLink implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("config_name")
    private String configName;

    @Field("selector_name")
    private String selectorName;

    @Field("selector_attr")
    private String selectorAttr;

    @Field("host")
    private String host;

    @Field("url")
    private String url;

    // simlife-needle-entity-add-field - Simlife will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConfigName() {
        return configName;
    }

    public ChannelOuterLink configName(String configName) {
        this.configName = configName;
        return this;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getSelectorName() {
        return selectorName;
    }

    public ChannelOuterLink selectorName(String selectorName) {
        this.selectorName = selectorName;
        return this;
    }

    public void setSelectorName(String selectorName) {
        this.selectorName = selectorName;
    }

    public String getSelectorAttr() {
        return selectorAttr;
    }

    public ChannelOuterLink selectorAttr(String selectorAttr) {
        this.selectorAttr = selectorAttr;
        return this;
    }

    public void setSelectorAttr(String selectorAttr) {
        this.selectorAttr = selectorAttr;
    }

    public String getHost() {
        return host;
    }

    public ChannelOuterLink host(String host) {
        this.host = host;
        return this;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUrl() {
        return url;
    }

    public ChannelOuterLink url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
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
        ChannelOuterLink channelOuterLink = (ChannelOuterLink) o;
        if (channelOuterLink.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), channelOuterLink.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ChannelOuterLink{" +
            "id=" + getId() +
            ", configName='" + getConfigName() + "'" +
            ", selectorName='" + getSelectorName() + "'" +
            ", selectorAttr='" + getSelectorAttr() + "'" +
            ", host='" + getHost() + "'" +
            ", url='" + getUrl() + "'" +
            "}";
    }
}
