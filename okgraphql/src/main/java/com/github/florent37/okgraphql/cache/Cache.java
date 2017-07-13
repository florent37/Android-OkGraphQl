package com.github.florent37.okgraphql.cache;

/**
 * Created by florentchampigny on 24/05/2017.
 */

public interface Cache {
    <T> T get(String key);

    void contains(String key);

    <T> void save(String key, T value);
}
