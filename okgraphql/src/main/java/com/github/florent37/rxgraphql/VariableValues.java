package com.github.florent37.rxgraphql;

/**
 * Created by florentchampigny on 23/05/2017.
 */

public class VariableValues {
    protected String name;
    protected Object value;

    public VariableValues(String name, Object value) {
        this.name = name;
        this.value = value;
    }
}
