package com.stackstate.util;

import com.stackstate.domain.GraphWrapper;
import com.stackstate.exception.CannotLoadGraphWrapperException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class FileGraphWrapperLoader implements GraphWrapperLoader {

    private final String filePath;

    public FileGraphWrapperLoader(String filePath) {
        this.filePath = filePath;
    }

    public GraphWrapper loadGraphWrapper() throws CannotLoadGraphWrapperException{
        return getGraphFromFile(filePath);
    }

    private GraphWrapper getGraphFromFile(String filePath) throws CannotLoadGraphWrapperException {
        ObjectMapper mapper = new ObjectMapper();
        GraphWrapper graphWrapper = null;
        try {
            graphWrapper = mapper.readValue(new File(filePath), GraphWrapper.class);
        } catch (IOException e) {
            throw new CannotLoadGraphWrapperException("IOException, cannot read file: " + filePath + ", make sure you give a valid json file path.");
        }
        return graphWrapper;
    }




}
