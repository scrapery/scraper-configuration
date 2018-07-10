package io.github.scrapery.setting.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Objects;

/**
 * A ConfigSiteLogin.
 */
@Document(collection = "config_site_login")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "configsitelogin")
public class ConfigSiteLogin implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("url")
    private String url;

    @Field("domain")
    private String domain;

    @Field("host")
    private String host;

    @Field("selector_action")
    private String selectorAction;

    @Field("selector_username")
    private String selectorUsername;

    @Field("selector_password")
    private String selectorPassword;

    @Field("selector_button_login")
    private String selectorButtonLogin;

    @Field("username")
    private String username;

    @Field("password")
    private String password;

    @Field("redirect_url")
    private String redirectUrl;

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

    public ConfigSiteLogin url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDomain() {
        return domain;
    }

    public ConfigSiteLogin domain(String domain) {
        this.domain = domain;
        return this;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getHost() {
        return host;
    }

    public ConfigSiteLogin host(String host) {
        this.host = host;
        return this;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getSelectorAction() {
        return selectorAction;
    }

    public ConfigSiteLogin selectorAction(String selectorAction) {
        this.selectorAction = selectorAction;
        return this;
    }

    public void setSelectorAction(String selectorAction) {
        this.selectorAction = selectorAction;
    }

    public String getSelectorUsername() {
        return selectorUsername;
    }

    public ConfigSiteLogin selectorUsername(String selectorUsername) {
        this.selectorUsername = selectorUsername;
        return this;
    }

    public void setSelectorUsername(String selectorUsername) {
        this.selectorUsername = selectorUsername;
    }

    public String getSelectorPassword() {
        return selectorPassword;
    }

    public ConfigSiteLogin selectorPassword(String selectorPassword) {
        this.selectorPassword = selectorPassword;
        return this;
    }

    public void setSelectorPassword(String selectorPassword) {
        this.selectorPassword = selectorPassword;
    }

    public String getSelectorButtonLogin() {
        return selectorButtonLogin;
    }

    public ConfigSiteLogin selectorButtonLogin(String selectorButtonLogin) {
        this.selectorButtonLogin = selectorButtonLogin;
        return this;
    }

    public void setSelectorButtonLogin(String selectorButtonLogin) {
        this.selectorButtonLogin = selectorButtonLogin;
    }

    public String getUsername() {
        return username;
    }

    public ConfigSiteLogin username(String username) {
        this.username = username;
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public ConfigSiteLogin password(String password) {
        this.password = password;
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public ConfigSiteLogin redirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
        return this;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
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
        ConfigSiteLogin configSiteLogin = (ConfigSiteLogin) o;
        if (configSiteLogin.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), configSiteLogin.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ConfigSiteLogin{" +
            "id=" + getId() +
            ", url='" + getUrl() + "'" +
            ", domain='" + getDomain() + "'" +
            ", host='" + getHost() + "'" +
            ", selectorAction='" + getSelectorAction() + "'" +
            ", selectorUsername='" + getSelectorUsername() + "'" +
            ", selectorPassword='" + getSelectorPassword() + "'" +
            ", selectorButtonLogin='" + getSelectorButtonLogin() + "'" +
            ", username='" + getUsername() + "'" +
            ", password='" + getPassword() + "'" +
            ", redirectUrl='" + getRedirectUrl() + "'" +
            "}";
    }
}
