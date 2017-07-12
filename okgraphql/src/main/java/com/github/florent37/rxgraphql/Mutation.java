package com.github.florent37.rxgraphql;

import java.util.List;

/**
 * Created by florentchampigny on 23/05/2017.
 */

public class Mutation<T> extends AbstractQuery<T> {

    public Mutation(OkGraphql okGraphql, String query) {
        super(okGraphql, "mutation", query);
    }

    public <R> Mutation<R> cast(Class<R> theClass){
        super.castClass(theClass);
        return (Mutation<R>)this;
    }

    public <R> Mutation<List<R>> castList(Class<R> theClass) {
        super.castClassList(theClass);
        return (Mutation<List<R>>)this;
    }

    public Mutation<T> field(String name, String value){
        super.injectField(name, value);
        return this;
    }

    public Mutation<T> variable(String key, Object value) {
        super.addVariable(key, value);
        return this;
    }
}
