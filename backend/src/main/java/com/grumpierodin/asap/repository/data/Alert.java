package com.grumpierodin.asap.repository.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.grumpierodin.asap.utils.RegExUtils;
import com.grumpierodin.asap.utils.TimeScheduleUtils;
import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import java.util.*;
@Entity
@ApiModel(description="holds alert info being processed from event/rule")
public class Alert implements Comparable<Alert>{
    //@JsonIgnore
    @OneToOne(mappedBy="alert")
    private Event event;
    @Id
    private String uuid;
    private String alertText;
    private String alertCorrelationId;
    private State alertState;
    private Level alertLevel;
    @OneToOne
    private Rule rule;
    private boolean publish;

    public Alert() {
        this.uuid = UUID.randomUUID().toString();
        this.event = new Event();
        this.alertState = State.Open;
        this.alertLevel = Level.Low;
    }
    public Alert(Event event, Rule rule) {
        this.uuid = UUID.randomUUID().toString();
        this.event = event;
        this.rule = rule;
        this.alertText = "";
        this.alertState = State.Open;
        this.alertLevel = Level.Low;
    }

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String getAlertText() {
        return alertText;
    }

    public void setAlertText(String alertText) {
        this.alertText = alertText;
    }
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getAlertCorrelationId() {
        return alertCorrelationId;
    }

    public void setAlertCorrelationId(String alertCorrelationId) {
        this.alertCorrelationId = alertCorrelationId;
    }

    public Map<String, String> getMatchedFields () {
        return RegExUtils.makeMatch(this.rule.getRegex(), this.event.getMessage());
    }

    public State getAlertState() {
        return alertState;
    }

    public void setAlertState(State alertState) {
        this.alertState = alertState;
    }

    public Level getAlertLevel() {
        return alertLevel;
    }

    public void setAlertLevel(Level alertLevel) {
        this.alertLevel = alertLevel;
    }

    public boolean isPublish() {
        return publish;
    }

    public void setPublish(boolean publish) {
        this.publish = publish;
    }

    private boolean isRuleRegexMatched() {
        Map<String, String> result = getMatchedFields();
        return !result.isEmpty();
    }

    private void renderAlertTemplates(Map<String, String> fieldList) {
        fieldList.putIfAbsent("$0", this.rule.getRegex());
        fieldList.putIfAbsent("message", this.event.getMessage());
        fieldList.putIfAbsent("application", this.event.getApplication());
        fieldList.putIfAbsent("host", this.event.getHost());
        fieldList.putIfAbsent("uuid", this.rule.getUuid());
        this.alertText = RegExUtils.renderTemplate(rule.getOutputTemplate(), fieldList);
        this.alertCorrelationId = RegExUtils.renderTemplate(rule.getCorrelationId(), fieldList);
    }
    private boolean isAlertableSchedule() {
        Collection<TimeScheduleUtils.TimeRange> range = new ArrayList<>();
        for(Schedule item : this.rule.getSchedules()) {
            range.addAll(TimeScheduleUtils.makeSchedule(this.event.getTimestamp(), item.getStartTime(), item.getEndTime(), item.getDays()));
        }
        return TimeScheduleUtils.isWithinSchedule(this.event.getTimestamp(), range);
    }
    @JsonIgnore
    public final boolean isAlertAble() {
        return isRuleRegexMatched() && isAlertableSchedule();
    }
    @JsonIgnore
    public void renderAlert() {
        if(isAlertAble()) {
            this.alertLevel=this.rule.getAlertLevel();
            this.renderAlertTemplates((getMatchedFields()));
        }
    }

    @Override
    public String toString() {
        return "Alert{" +
                "event=" + event +
                ", uuid='" + uuid + '\'' +
                ", alertText='" + alertText + '\'' +
                ", alertCorrelationId='" + alertCorrelationId + '\'' +
                ", alertState=" + alertState +
                ", alertLevel=" + alertLevel +
                ", rule=" + rule +
                ", publish=" + publish +
                '}';
    }

    @Override
    public int compareTo(Alert o) {
        return o.getEvent().getTimestamp().compareTo(getEvent().getTimestamp());
    }
//
    // actions/ filters???
    // suppress  (x/seconds)
    // clear   (regex)
    // alert (regex, (seconds)) eg after rule message if alert match not seen in seconds
    // pair (regex) eg after rule message regex  if pair regex seen send alert.
    // breach (x/seconds) if alert seen more than x in seconds then alert.
    //
    // store alert using alertCorrelationId in h2 db for each tracking??
    // then actions/filters can be db / sql query based??
    //
}
