package com.iapps.ichat.model;

import java.io.Serializable;

public class BeanLog
    implements Serializable {
    private static final long serialVersionUID = -1940753474634251659L;
    private String type, event, timestamp;

    public BeanLog(String type, String event, String timestamp) {
        this.type = type;
        this.event = event;
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
