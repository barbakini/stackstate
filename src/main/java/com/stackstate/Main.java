package com.stackstate;

import com.stackstate.core.GraphManager;
import com.stackstate.domain.Event;
import com.stackstate.domain.EventsWrapper;
import com.stackstate.domain.GraphWrapper;
import com.stackstate.domain.State;
import com.stackstate.exception.CannotLoadEventsWrapperException;
import com.stackstate.exception.CannotLoadGraphWrapperException;
import com.stackstate.util.EventsWrapperLoader;
import com.stackstate.util.FileEventsWrapperLoader;
import com.stackstate.util.FileGraphWrapperLoader;
import com.stackstate.util.GraphWrapperLoader;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.IOException;
import java.util.Comparator;

public class Main {

    public static void main(String[] args) throws CannotLoadGraphWrapperException, CannotLoadEventsWrapperException, IOException {

        GraphWrapperLoader fileGraphWrapperReader = new FileGraphWrapperLoader(args[0]);
        GraphWrapper graphWrapper = fileGraphWrapperReader.loadGraphWrapper();

        EventsWrapperLoader eventsWrapperLoader = new FileEventsWrapperLoader(args[1]);
        EventsWrapper eventsWrapper = eventsWrapperLoader.loadEventsWrapper();

        GraphManager graphManager = new GraphManager(graphWrapper.getGraph());

        eventsWrapper.getEvents().forEach(event -> {
            graphManager.handleEvent(event);
        });

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(graphWrapper));
    }
}
