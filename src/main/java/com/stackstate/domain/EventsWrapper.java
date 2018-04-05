package com.stackstate.domain;

import java.util.List;

public class EventsWrapper {

    private List<Event> events;


    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}
