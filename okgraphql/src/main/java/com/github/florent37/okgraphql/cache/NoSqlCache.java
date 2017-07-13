package com.github.florent37.okgraphql.cache;

import com.github.florent37.androidnosql.AndroidNoSql;
import com.github.florent37.androidnosql.NoSql;
import com.github.florent37.okgraphql.AppContextFinder;

/**
 * Created by florentchampigny on 13/07/2017.
 */

public class NoSqlCache implements Cache {

    private final NoSql noSql;

    public NoSqlCache(NoSql noSql) {
        this.noSql = noSql;
    }

    public NoSqlCache() {
        AndroidNoSql.initWithDefault(AppContextFinder.getContext());
        this.noSql = NoSql.getInstance();
    }

    @Override
    public <T> T get(String key) {
        return null;
    }

    @Override
    public void contains(String key) {

    }

    @Override
    public <T> void save(String key, T value) {

    }
}
