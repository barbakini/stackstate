package com.stackstate.domain;

public class Event {

    private long timestamp;

    private String component;

    private String check_state;

    private State state;


    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getCheck_state() {
        return check_state;
    }

    public void setCheck_state(String check_state) {
        this.check_state = check_state;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

}
