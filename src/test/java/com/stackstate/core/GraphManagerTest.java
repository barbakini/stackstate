package com.stackstate.core;

import com.stackstate.domain.Component;
import com.stackstate.domain.Event;
import com.stackstate.domain.Graph;
import com.stackstate.domain.State;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class GraphManagerTest {

    private GraphManager getGraphManager() {
        Graph graph = new Graph();

        Component component1 = new Component();
        component1.setId("A");
        component1.setOwn_state(State.no_data);
        component1.setDerived_state(State.no_data);
        Map<String, State> checkStates = new Hashtable<>();
        checkStates.put("state1", State.no_data);
        checkStates.put("state2", State.no_data);
        component1.setCheck_states(checkStates);
        component1.setDepends_on(new HashSet<>(Arrays.asList("B")));

        Component component2 = new Component();
        component2.setId("B");
        component2.setOwn_state(State.no_data);
        component2.setDerived_state(State.no_data);
        checkStates = new Hashtable<>();
        checkStates.put("state1", State.no_data);
        checkStates.put("state2", State.no_data);
        component2.setCheck_states(checkStates);
        component2.setDependency_of(new HashSet<>(Arrays.asList("A", "C")));
        component2.setDepends_on(new HashSet<>(Arrays.asList("C")));

        Component component3 = new Component();
        component3.setId("C");
        component3.setOwn_state(State.no_data);
        component3.setDerived_state(State.no_data);
        checkStates = new Hashtable<>();
        checkStates.put("state1", State.no_data);
        checkStates.put("state2", State.no_data);
        component3.setCheck_states(checkStates);
        component3.setDependency_of(new HashSet<>(Arrays.asList("B")));
        component3.setDepends_on(new HashSet<>(Arrays.asList("B")));

        graph.setComponents(new ArrayList<>(Arrays.asList(component1, component2, component3)));

        return new GraphManager(graph);
    }

    @Test
    public void handleEvent_ShouldUpdateCheckStateAndOwnState() {
        GraphManager graphManager = getGraphManager();
        Event event = new Event();
        event.setTimestamp(1);
        event.setComponent("A");
        event.setCheck_state("state1");
        event.setState(State.clear);

        graphManager.handleEvent(event);

        assertEquals(State.clear, graphManager.getGraph().getComponents().get(0).getCheckState("state1"));
        assertEquals(State.clear, graphManager.getGraph().getComponents().get(0).getOwn_state());
        assertEquals(State.no_data, graphManager.getGraph().getComponents().get(0).getDerived_state());

        event.setCheck_state("state2");
        graphManager.handleEvent(event); // for branch coverage

        assertEquals(State.clear, graphManager.getGraph().getComponents().get(0).getCheckState("state2"));
        assertEquals(State.clear, graphManager.getGraph().getComponents().get(0).getOwn_state());
        assertEquals(State.no_data, graphManager.getGraph().getComponents().get(0).getDerived_state());
    }

    @Test
    public void handleEvent_ShouldUpdateCheckStateOwnStateAndDerivedState() {
        GraphManager graphManager = getGraphManager();
        Event event = new Event();
        event.setTimestamp(1);
        event.setComponent("A");
        event.setCheck_state("state1");
        event.setState(State.warning);

        graphManager.handleEvent(event);

        assertEquals(State.warning, graphManager.getGraph().getComponents().get(0).getCheckState("state1"));
        assertEquals(State.warning, graphManager.getGraph().getComponents().get(0).getOwn_state());
        assertEquals(State.warning, graphManager.getGraph().getComponents().get(0).getDerived_state());
    }

    @Test
    public void handleEvent_ShouldUpdateDependantsDerivedState() {
        GraphManager graphManager = getGraphManager();
        Event event = new Event();
        event.setTimestamp(1);
        event.setComponent("C");
        event.setCheck_state("state1");
        event.setState(State.warning);

        graphManager.handleEvent(event);

        assertEquals(State.warning, graphManager.getGraph().getComponents().get(0).getDerived_state());
        assertEquals(State.no_data, graphManager.getGraph().getComponents().get(0).getOwn_state());
        assertEquals(State.warning, graphManager.getGraph().getComponents().get(1).getDerived_state());
        assertEquals(State.no_data, graphManager.getGraph().getComponents().get(1).getOwn_state());
        assertEquals(State.warning, graphManager.getGraph().getComponents().get(2).getDerived_state());
        assertEquals(State.warning, graphManager.getGraph().getComponents().get(2).getOwn_state());

        event.setComponent("B");
        event.setState(State.alert);
        graphManager.handleEvent(event);

        assertEquals(State.alert, graphManager.getGraph().getComponents().get(0).getDerived_state());
        assertEquals(State.no_data, graphManager.getGraph().getComponents().get(0).getOwn_state());
        assertEquals(State.alert, graphManager.getGraph().getComponents().get(1).getDerived_state());
        assertEquals(State.alert, graphManager.getGraph().getComponents().get(1).getOwn_state());
        assertEquals(State.alert, graphManager.getGraph().getComponents().get(2).getDerived_state());
        assertEquals(State.warning, graphManager.getGraph().getComponents().get(2).getOwn_state());
    }

    @Test
    public void handleEvent_ShouldDiscardEvent_IfEventIsObsolete() {
        GraphManager graphManager = getGraphManager();
        Event event = new Event();
        event.setTimestamp(2);
        event.setComponent("A");
        event.setCheck_state("state1");
        event.setState(State.clear);

        graphManager.handleEvent(event);
        event.setTimestamp(1);
        event.setState(State.alert);
        graphManager.handleEvent(event);

        assertEquals(State.clear, graphManager.getGraph().getComponents().get(0).getCheckState("state1"));
        assertEquals(State.clear, graphManager.getGraph().getComponents().get(0).getOwn_state());
        assertEquals(State.no_data, graphManager.getGraph().getComponents().get(0).getDerived_state());
    }

    @Test
    public void handleEvent_ShouldDiscardEvent_IfEventIsInvalid() {
        GraphManager graphManager = getGraphManager();
        Event event = new Event();
        event.setTimestamp(2);
        event.setComponent("A2");
        event.setCheck_state("state1");
        event.setState(State.clear);

        graphManager.handleEvent(event);

        assertEquals(State.no_data, graphManager.getGraph().getComponents().get(0).getCheckState("state1"));
        assertEquals(State.no_data, graphManager.getGraph().getComponents().get(0).getOwn_state());
        assertEquals(State.no_data, graphManager.getGraph().getComponents().get(0).getDerived_state());
        assertEquals(State.no_data, graphManager.getGraph().getComponents().get(1).getCheckState("state1"));
        assertEquals(State.no_data, graphManager.getGraph().getComponents().get(1).getOwn_state());
        assertEquals(State.no_data, graphManager.getGraph().getComponents().get(1).getDerived_state());
    }
}
