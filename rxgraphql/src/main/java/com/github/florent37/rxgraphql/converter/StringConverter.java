package com.github.florent37.rxgraphql.converter;

/**
 * Created by florentchampigny on 10/07/2017.
 */

public class StringConverter implements Converter {
    @Override
    public <T> BodyConverter<T> bodyConverter() {
        return new BodyConverter<T>() {
            @Override
            public T convert(String json, Class<T> classToCast, boolean toList) {
                return (T) json;
            }
        };
    }
}
