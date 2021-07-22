package com.grumpierodin.asap.repository.data;

import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
@Entity
@ApiModel(description="holds rule definition for processing event")
public class Rule {
    private final String name;
    private final String regex;
    private final String correlationId;
    @Id
    private final String uuid;
    private final String outputTemplate;
    @OneToMany(targetEntity=Schedule.class, fetch= FetchType.LAZY)
    private final Collection<Schedule> schedules;
    @OneToMany(targetEntity=AlertFilter.class, fetch= FetchType.LAZY)
    private final Collection<AlertFilter> alertFilters;
    private final Level alertLevel;

    public Rule() {
        this.name = "name";
        this.regex = "regex";
        this.correlationId = "correlationId";
        this.uuid = "uuid";
        this.outputTemplate = "outputTemplate";
        this.alertLevel = Level.Low;
        this.schedules = new ArrayList<Schedule>() ;
        this.schedules.add(new Schedule());
        this.alertFilters = new ArrayList<AlertFilter>();
    }

    public Rule(String name, String regex, String correlationId, String uuid, String outputTemplate, Level alertLevel, Collection<Schedule> schedules) {
        this.name = name;
        this.regex = regex;
        this.correlationId = correlationId;
        this.uuid = uuid;
        this.outputTemplate = outputTemplate;
        this.alertLevel = alertLevel;
        this.schedules = schedules;
        this.alertFilters = new ArrayList<AlertFilter>();
    }
    public Rule(String name, String regex, String correlationId, String uuid , String outputTemplate, Level alertLevel, ArrayList<Schedule> schedules) {
        this.name = name;
        this.regex = regex;
        this.correlationId = correlationId;
        this.uuid = uuid;
        this.outputTemplate = outputTemplate;
        this.alertLevel = alertLevel;
        this.schedules = schedules;
        this.alertFilters = new ArrayList<AlertFilter>();
    }

    public Rule(String name, String regex, String correlationId, String uuid, String outputTemplate, Level alertLevel, Collection<Schedule> schedules, Collection<AlertFilter> alertFilters) {
        this.name = name;
        this.regex = regex;
        this.correlationId = correlationId;
        this.uuid = uuid;
        this.outputTemplate = outputTemplate;
        this.alertLevel = alertLevel;
        this.schedules = schedules;
        this.alertFilters = alertFilters;
    }

    public String getName() {
        return name;
    }

    public String getRegex() {
        return regex;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public String getUuid() {
        return uuid;
    }

    public String getOutputTemplate() {
        return outputTemplate;
    }

    public Level getAlertLevel() {
        return alertLevel;
    }

    public List<Schedule> getSchedules() {
        return (List<Schedule>) schedules;
    }

    public Collection<AlertFilter> getAlertFilters() {
        return alertFilters;
    }

    @Override
    public String toString() {
        return "Rule{" +
                "name='" + name + '\'' +
                ", regex='" + regex + '\'' +
                ", correlationId='" + correlationId + '\'' +
                ", uuid='" + uuid + '\'' +
                ", outputTemplate='" + outputTemplate + '\'' +
                ", schedules=" + schedules +
                ", alertFilters=" + alertFilters +
                ", alertLevel=" + alertLevel +
                '}';
    }
}
