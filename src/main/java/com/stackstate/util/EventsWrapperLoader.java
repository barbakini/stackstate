package com.stackstate.util;

import com.stackstate.domain.EventsWrapper;
import com.stackstate.domain.GraphWrapper;
import com.stackstate.exception.CannotLoadEventsWrapperException;
import com.stackstate.exception.CannotLoadGraphWrapperException;

public interface EventsWrapperLoader {

    EventsWrapper loadEventsWrapper() throws CannotLoadEventsWrapperException;
}
