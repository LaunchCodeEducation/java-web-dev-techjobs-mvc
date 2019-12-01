package org.launchcode.javawebdevtechjobsmvc.models;

import java.util.Objects;

public abstract class JobField {
    private int id;
    private static int nextId = 1;
    private String value;

    // Constructor assigns a unique ID number to each new object.
    public JobField() {
        id = nextId;
        nextId++;
    }

    // Constructor that takes a string as a parameter and assigns it to the 'value' field, and also
    // calls the empty constructor in order to initialize the 'id' field.
    public JobField(String value) {
        this();
        this.value = value;
    }

    // Custom toString() method that returns the data stored in 'value'.
    @Override
    public String toString() {
        return value;
    }

    // Custom equals and hashCode methods. Two objects are "equal" when their id fields match.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JobField)) return false;
        JobField jobField = (JobField) o;
        return id == jobField.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // Getters and setters.
    public int getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
