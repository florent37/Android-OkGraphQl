package com.github.florent37.okgraphql;

import java.util.List;

/**
 * Created by florentchampigny on 23/05/2017.
 */

public class Mutation<T> extends AbstractQuery<T, Mutation<T>> {

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
}
