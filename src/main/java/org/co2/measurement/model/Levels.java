package org.co2.measurement.model;

public enum Levels {
    OK("OK"), WARN("WARN"), ALERT("ALERT");

    private final String alert;

    Levels(final String alert) {
        this.alert = alert;
    }

    @Override
    public String toString() {
        return alert;
    }
}
