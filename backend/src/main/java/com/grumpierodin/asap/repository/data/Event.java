package com.grumpierodin.asap.repository.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@ApiModel(description="holds event from http post")
public class Event {
    @ApiModelProperty(notes="should at least 2 characters")
    @Size(min=2 , message="message should at least 2 characters")
    private final String message;

    @ApiModelProperty(notes="should be in iso format")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private final LocalDateTime timestamp;

    @ApiModelProperty(notes="should at least 2 characters")
    @Size(min=2 , message="host should at least 2 characters")
    private final String host;

    @ApiModelProperty(notes="should at least 2 characters")
    @Size(min=2 , message="application should at least 2 characters")
    private final String application;

    @Id
    private final String id;

    @OneToOne
    @JsonIgnore
    private Alert alert;

    public Event() {
        this.id = UUID.randomUUID().toString();
        this.message = "";
        this.timestamp = LocalDateTime.now();
        this.host ="";
        this.application = "";
    }
    public Event(String message) {
        this.id = UUID.randomUUID().toString();
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.host ="";
        this.application = "";
    }
    public Event(String message, String timestamp) {
        this.id = UUID.randomUUID().toString();
        this.message = "";
        this.timestamp = LocalDateTime.parse(timestamp);
        this.host ="";
        this.application = "";
    }
    public Event(String message, String timestamp, String host) {
        this.id = UUID.randomUUID().toString();
        this.message = message;
        this.timestamp = LocalDateTime.parse(timestamp);
        this.host = host;
        this.application = "";
    }
    public Event(String message, String timestamp, String host, String application) {
        this.id = UUID.randomUUID().toString();
        this.message = message;
        this.timestamp = LocalDateTime.parse(timestamp);
        this.host = host;
        this.application = application;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getHost() {
        return host;
    }

    public String getApplication() {
        return application;
    }

    public Alert getAlert() {
        return alert;
    }

    public void setAlert(Alert alert) {
        this.alert = alert;
    }

    @Override
    public String toString() {
        return "Event{" +
                "message='" + message + '\'' +
                ", timestamp=" + timestamp +
                ", host='" + host + '\'' +
                ", application='" + application + '\'' +
                '}';
    }
}
