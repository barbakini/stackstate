package com.stackstate.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Component {

    private String id;

    private State own_state;

    private State derived_state;

    private Map<String, State> check_states;

    private Set<String> depends_on;

    private Set<String> dependency_of;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public State getOwn_state() {
        return own_state;
    }

    public void setOwn_state(State own_state) {
        this.own_state = own_state;
    }

    public State getDerived_state() {
        return derived_state;
    }

    public void setDerived_state(State derived_state) {
        this.derived_state = derived_state;
    }

    public Map<String, State> getCheck_states() {
        return check_states;
    }

    public boolean setCheck_states(Map<String, State> check_states) {
        this.check_states = check_states;
        return setNewOwnState();
    }

    public boolean updateCheckState(String checkState, State state) {
        if (check_states == null || !check_states.containsKey(checkState))
            return false;
        check_states.put(checkState, state);
        return setNewOwnState();
    }

    private boolean setNewOwnState() {
        State oldOwnState = own_state;
        final State[] highestState = {State.no_data};
        check_states.forEach((k, v) -> {
            if (v.isHigherThan(highestState[0]))
                highestState[0] = v;
        });
        own_state = highestState[0];
        if (oldOwnState != own_state)
            return true;
        return false;
    }

    public State getCheckState(String checkState) {
        return check_states.get(checkState);
    }

    public boolean hasCheckState(String checkState) {
        return check_states != null && check_states.containsKey(checkState);
    }

    public Set<String> getDepends_on() {
        return depends_on;
    }

    public void setDepends_on(Set<String> depends_on) {
        this.depends_on = depends_on;
    }

    public Set<String> getDependency_of() {
        return dependency_of;
    }

    public void setDependency_of(Set<String> dependency_of) {
        this.dependency_of = dependency_of;
    }
}
