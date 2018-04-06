package com.stackstate.core;

import com.stackstate.domain.Component;
import com.stackstate.domain.Event;
import com.stackstate.domain.Graph;
import com.stackstate.domain.State;

import java.util.*;

public class GraphManager {

    private final Graph graph;

    private final Map<String, Component> componentMap = new HashMap<>();

    private final Map<String, Long> lastHandledEventsTimestamps = new HashMap<>();
    ;

    private final Map<String, Set<String>> dependantsMap = new HashMap<>();

    private final Map<String, Set<String>> dependsMap = new HashMap<>();


    public GraphManager(Graph graph) {
        this.graph = graph;
        init();
    }

    private void init() {
        graph.getComponents().forEach(component -> {
            componentMap.put(component.getId(), component);
        });
        initDependantsMap();
        initDependsMap();
    }

    private void initDependsMap() {
        graph.getComponents().forEach(component -> {
            if (component.getDepends_on() != null) {
                dependsMap.put(component.getId(), new HashSet<>(component.getDepends_on()));
                Set<String> workedComponents = new HashSet<>();
                Stack<String> componentsNeedWork = new Stack<>();
                componentsNeedWork.addAll(component.getDepends_on());
                while (!componentsNeedWork.isEmpty()) {
                    String componentId = componentsNeedWork.pop();
                    if (workedComponents.contains(componentId))
                        continue;

                    Component dependComp = componentMap.get(componentId);
                    if (dependComp.getDepends_on() != null) {
                        dependComp.getDepends_on().forEach(compId -> {
                            dependsMap.get(component.getId()).add(compId);
                            componentsNeedWork.add(compId);
                        });
                    }
                    workedComponents.add(componentId);
                }
                dependsMap.get(component.getId()).remove(component.getId());
            }
        });
    }

    private void initDependantsMap() {
        graph.getComponents().forEach(component -> {
            if (component.getDependency_of() != null) {
                dependantsMap.put(component.getId(), new HashSet<>(component.getDependency_of()));
                Set<String> workedComponents = new HashSet<>();
                Stack<String> componentsNeedWork = new Stack<>();
                componentsNeedWork.addAll(component.getDependency_of());
                while (!componentsNeedWork.isEmpty()) {
                    String componentId = componentsNeedWork.pop();
                    if (workedComponents.contains(componentId))
                        continue;

                    Component dependantComp = componentMap.get(componentId);
                    if (dependantComp.getDependency_of() != null) {
                        dependantComp.getDependency_of().forEach(compId -> {
                            dependantsMap.get(component.getId()).add(compId);
                            componentsNeedWork.add(compId);
                        });
                    }
                    workedComponents.add(componentId);
                }
                dependantsMap.get(component.getId()).remove(component.getId());
            }
        });
    }

    public Graph getGraph() {
        return graph;
    }

    public void handleEvent(Event event) {
        if (!isEventValid(event) || isEventObsolete(event))
            return;

        Component component = componentMap.get(event.getComponent());
        boolean ownStateChanged = component.updateCheckState(event.getCheck_state(), event.getState());
        if (ownStateChanged) { // && oldOwnState < derivedState
            updateDerivedStateOf(component.getId());
            updateDerivedStateOfDependants(component.getId());
        }
        lastHandledEventsTimestamps.put(event.getComponent() + event.getCheck_state(), event.getTimestamp());
    }

    private void updateDerivedStateOf(String componentId) {
        Component component = componentMap.get(componentId);
        final State[] highestState = {(component.getOwn_state().isDerivable()) ? component.getOwn_state() : State.no_data};
        if (dependsMap.containsKey(componentId)) {
            dependsMap.get(componentId).forEach(compId -> {
                if (componentMap.get(compId).getOwn_state().isHigherThan(highestState[0]))
                    highestState[0] = componentMap.get(compId).getOwn_state();
            });
        }
        component.setDerived_state(highestState[0]);
    }

    private void updateDerivedStateOfDependants(String componentId) {
        if (dependantsMap.get(componentId) != null)
            dependantsMap.get(componentId).forEach(compId -> {
                updateDerivedStateOf(compId);
            });
    }


    private boolean isEventValid(Event event) {
        Component component = componentMap.get(event.getComponent());
        if (component == null || !component.hasCheckState(event.getCheck_state()))
            return false;
        return true;
    }

    private boolean isEventObsolete(Event event) {
        String key = event.getComponent() + event.getCheck_state();
        if (lastHandledEventsTimestamps.containsKey(key) && lastHandledEventsTimestamps.get(key) > event.getTimestamp())
            return true;
        return false;
    }


}
