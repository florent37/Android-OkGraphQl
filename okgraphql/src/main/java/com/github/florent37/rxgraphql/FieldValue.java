package com.github.florent37.rxgraphql;

/**
 * Created by florentchampigny on 23/05/2017.
 */

public class FieldValue {
    protected String name;
    protected String value;

    public FieldValue(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
