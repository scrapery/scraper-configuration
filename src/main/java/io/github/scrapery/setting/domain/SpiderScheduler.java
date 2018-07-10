package io.github.scrapery.setting.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Objects;

import io.github.scrapery.setting.domain.enumeration.ChannelAppType;

/**
 * A SpiderScheduler.
 */
@Document(collection = "spider_scheduler")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "spiderscheduler")
public class SpiderScheduler implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("name")
    private String name;

    @Field("schedule")
    private String schedule;

    @Field("schedule_time_zone")
    private String scheduleTimeZone;

    @Field("country_code")
    private String countryCode;

    @Field("channel_category")
    private String channelCategory;

    @Field("json_meta")
    private String jsonMeta;

    @Field("channel_app_type")
    private ChannelAppType channelAppType;

    @Field("job_key")
    private String jobKey;

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

    public SpiderScheduler name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchedule() {
        return schedule;
    }

    public SpiderScheduler schedule(String schedule) {
        this.schedule = schedule;
        return this;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getScheduleTimeZone() {
        return scheduleTimeZone;
    }

    public SpiderScheduler scheduleTimeZone(String scheduleTimeZone) {
        this.scheduleTimeZone = scheduleTimeZone;
        return this;
    }

    public void setScheduleTimeZone(String scheduleTimeZone) {
        this.scheduleTimeZone = scheduleTimeZone;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public SpiderScheduler countryCode(String countryCode) {
        this.countryCode = countryCode;
        return this;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getChannelCategory() {
        return channelCategory;
    }

    public SpiderScheduler channelCategory(String channelCategory) {
        this.channelCategory = channelCategory;
        return this;
    }

    public void setChannelCategory(String channelCategory) {
        this.channelCategory = channelCategory;
    }

    public String getJsonMeta() {
        return jsonMeta;
    }

    public SpiderScheduler jsonMeta(String jsonMeta) {
        this.jsonMeta = jsonMeta;
        return this;
    }

    public void setJsonMeta(String jsonMeta) {
        this.jsonMeta = jsonMeta;
    }

    public ChannelAppType getChannelAppType() {
        return channelAppType;
    }

    public SpiderScheduler channelAppType(ChannelAppType channelAppType) {
        this.channelAppType = channelAppType;
        return this;
    }

    public void setChannelAppType(ChannelAppType channelAppType) {
        this.channelAppType = channelAppType;
    }

    public String getJobKey() {
        return jobKey;
    }

    public SpiderScheduler jobKey(String jobKey) {
        this.jobKey = jobKey;
        return this;
    }

    public void setJobKey(String jobKey) {
        this.jobKey = jobKey;
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
        SpiderScheduler spiderScheduler = (SpiderScheduler) o;
        if (spiderScheduler.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), spiderScheduler.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SpiderScheduler{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", schedule='" + getSchedule() + "'" +
            ", scheduleTimeZone='" + getScheduleTimeZone() + "'" +
            ", countryCode='" + getCountryCode() + "'" +
            ", channelCategory='" + getChannelCategory() + "'" +
            ", jsonMeta='" + getJsonMeta() + "'" +
            ", channelAppType='" + getChannelAppType() + "'" +
            ", jobKey='" + getJobKey() + "'" +
            "}";
    }
}
