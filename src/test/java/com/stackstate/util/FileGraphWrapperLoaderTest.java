package com.stackstate.util;

import com.stackstate.domain.GraphWrapper;
import com.stackstate.exception.CannotLoadGraphWrapperException;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class FileGraphWrapperLoaderTest {

    @Test(expected = CannotLoadGraphWrapperException.class)
    public void loadGraphWrapper_ExceptionTest() throws CannotLoadGraphWrapperException {
        GraphWrapperLoader graphWrapperLoader = new FileGraphWrapperLoader("/asd");
        graphWrapperLoader.loadGraphWrapper();
    }

    @Test
    public void loadGraphWrapper_ShouldLoadGraphWrapper() throws CannotLoadGraphWrapperException {
        GraphWrapperLoader graphWrapperLoader = new FileGraphWrapperLoader("/Users/akifbarbak/IdeaProjects/stackstate/src/test/resources/sample-initial.json");
        GraphWrapper graphWrapper = graphWrapperLoader.loadGraphWrapper();

        assertNotNull(graphWrapper);
        assertNotNull(graphWrapper.getGraph());
    }


}
