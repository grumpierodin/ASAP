package com.grumpierodin.asap.websocket.model;

import com.grumpierodin.asap.repository.data.Alert;

import java.util.Date;
public class AlertMessage {
    String type;
    String content;
    Alert alert;
    private Date timestamp;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public Alert getAlert() {
        return alert;
    }

    public void setAlert(Alert alert) {
        this.alert = alert;
    }
    public Date getTimestamp() {
        return timestamp;
    }

    public AlertMessage(String type, String content) {
        this.type = type;
        this.content = content;
        this.timestamp = new Date();
    }
    public AlertMessage(String type, Alert content) {
        this.type = type;
        this.alert = content;
        this.timestamp = new Date();
    }

    @Override
    public String toString() {
        return "AlertMessage{" +
                "type='" + type + '\'' +
                ", content='" + content + '\'' +
                ", alert=" + alert +
                ", timestamp=" + timestamp +
                '}';
    }
}
