package com.stackstate.domain;

import java.util.List;

public class Graph {

    private List<Component> components;

    public void setComponents(List<Component> components) {
        this.components = components;
    }

    public List<Component> getComponents() {
        return components;
    }

}
