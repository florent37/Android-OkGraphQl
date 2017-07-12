package com.github.florent37.okgraphql.converter;

/**
 * Created by florentchampigny on 24/05/2017.
 */

public interface Converter {
    interface BodyConverter<T> {
        T convert(String json, Class<T> classToCast, boolean toList);
    }

    <T> BodyConverter<T> bodyConverter();
}
