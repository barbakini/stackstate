package com.stackstate.domain;

public enum State {
    no_data,
    clear,
    warning,
    alert;

    public boolean isHigherThan(State state) {
        return this.compareTo(state) > 0;
    }

    public boolean isDerivable() {
        return this.compareTo(State.clear) > 0;
    }

}
