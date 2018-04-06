package com.stackstate.util;

import com.stackstate.domain.Event;
import com.stackstate.domain.EventsWrapper;
import com.stackstate.domain.GraphWrapper;
import com.stackstate.exception.CannotLoadEventsWrapperException;
import com.stackstate.exception.CannotLoadGraphWrapperException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;

public class FileEventsWrapperLoader implements EventsWrapperLoader {

    private final String filePath;

    public FileEventsWrapperLoader(String filePath) {
        this.filePath = filePath;
    }

    public EventsWrapper loadEventsWrapper() throws CannotLoadEventsWrapperException{
        return getEventsFromFile(filePath);
    }

    private EventsWrapper getEventsFromFile(String filePath) throws CannotLoadEventsWrapperException {
        ObjectMapper mapper = new ObjectMapper();
        EventsWrapper eventsWrapper = null;
        try {
            eventsWrapper = mapper.readValue(new File(filePath), EventsWrapper.class);
        } catch (IOException e) {
            throw new CannotLoadEventsWrapperException("IOException, cannot read file: " + filePath + ", make sure you give a valid json file path.");
        }
        return eventsWrapper;
    }




}
