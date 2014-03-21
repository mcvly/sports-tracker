package org.mcvly.tracker.model;

/**
 * @author mcvly
 * @since 3/19/14
 */
public enum TrainingType {

    RUN("running"),
    SWIM("swimming"),
    CYCLE("cycling"),
    GYM("gymming"),
    PRESS("pressing");

    private String name;

    TrainingType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
