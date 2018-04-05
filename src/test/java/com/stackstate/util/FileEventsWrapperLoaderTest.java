package com.stackstate.util;

import com.stackstate.domain.EventsWrapper;
import com.stackstate.domain.GraphWrapper;
import com.stackstate.exception.CannotLoadEventsWrapperException;
import com.stackstate.exception.CannotLoadGraphWrapperException;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class FileEventsWrapperLoaderTest {

    @Test(expected = CannotLoadEventsWrapperException.class)
    public void loadEventsWrapper_ExceptionTest() throws CannotLoadEventsWrapperException {
        EventsWrapperLoader eventsWrapperLoader = new FileEventsWrapperLoader("/asd");
        eventsWrapperLoader.loadEventsWrapper();
    }

    @Test
    public void loadEventsWrapper_ShouldLoadEventsWrapper() throws CannotLoadEventsWrapperException {
        EventsWrapperLoader eventsWrapperLoader = new FileEventsWrapperLoader("/Users/akifbarbak/IdeaProjects/stackstate/src/test/resources/sample-events.json");
        EventsWrapper eventsWrapper = eventsWrapperLoader.loadEventsWrapper();

        assertNotNull(eventsWrapper);
        assertNotNull(eventsWrapper.getEvents());
    }


}
