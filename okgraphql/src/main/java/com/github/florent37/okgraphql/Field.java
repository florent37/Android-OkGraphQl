package com.github.florent37.okgraphql;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by florentchampigny on 12/07/2017.
 */

public class Field {
    private final String name;
    private final List<Object> field = new ArrayList<>();
    private String argument = null;

    private Field(String name) {
        this.name = name;
    }

    public static Field newField() {
        return new Field("");
    }

    public static Field newField(String name) {
        return new Field(name);
    }

    public Field argument(String argument) {
        this.argument = argument;
        return this;
    }

    public Field field(Object child) {
        field.add(child);
        return this;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(name);
        if (argument != null) {
            sb
                    .append("(")
                    .append(argument)
                    .append(")");
        }
        sb.append(" {\n");
        for (Object o : field) {
            if (o != null) {
                sb.append(o.toString());
                sb.append("\n");
            }
        }
        sb.append("}\n");
        return sb.toString();
    }
}
