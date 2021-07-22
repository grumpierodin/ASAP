package com.grumpierodin.asap.repository.data;

import io.swagger.annotations.ApiModel;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@ApiModel(description="holds alert filter in rule")
public class AlertFilter {
    @Id
    private String filterId = UUID.randomUUID().toString();
    private long expirySeconds =30;
    private long expiryThreshold =6;
    AlertFilterType alertFilterType=AlertFilterType.Suppress;
    private String filterExpression;

    public AlertFilter() {

    }

    public String getFilterId() {
        return filterId;
    }

    public void setFilterId(String filterId) {
        this.filterId = filterId;
    }

    public long getExpirySeconds() {
        return expirySeconds;
    }

    public void setExpirySeconds(long expirySeconds) {
        this.expirySeconds = expirySeconds;
    }

    public long getExpiryThreshold() {
        return expiryThreshold;
    }

    public void setExpiryThreshold(long expiryThreshold) {
        this.expiryThreshold = expiryThreshold;
    }

    public AlertFilterType getAlertFilterType() {
        return alertFilterType;
    }

    public void setAlertFilterType(AlertFilterType alertFilterType) {
        this.alertFilterType = alertFilterType;
    }

    public String getFilterExpression() {
        return filterExpression;
    }

    public void setFilterExpression(String filterExpression) {
        this.filterExpression = filterExpression;
    }

    public AlertFilter(String filterId, long expirySeconds, long expiryThreshold, AlertFilterType alertFilterType, String filterExpression) {
        this.filterId = filterId;
        this.expirySeconds = expirySeconds;
        this.expiryThreshold = expiryThreshold;
        this.alertFilterType = alertFilterType;
        this.filterExpression = filterExpression;
    }

    @Override
    public String toString() {
        return "AlertFilter{" +
                "filterExpression='" + filterExpression + '\'' +
                ", expirySeconds=" + expirySeconds +
                ", expiryThreshold=" + expiryThreshold +
                ", alertFilterType=" + alertFilterType +
                '}';
    }
}
