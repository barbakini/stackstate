package com.stackstate.util;

import com.stackstate.domain.Graph;
import com.stackstate.domain.GraphWrapper;
import com.stackstate.exception.CannotLoadGraphWrapperException;

import java.io.IOException;

public interface GraphWrapperLoader {

    GraphWrapper loadGraphWrapper() throws CannotLoadGraphWrapperException;
}
