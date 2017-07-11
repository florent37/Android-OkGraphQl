package com.github.florent37.rxgraphql;

import io.reactivex.annotations.NonNull;

/**
 * Created by florentchampigny on 23/05/2017.
 */

public interface ErrorCallback {
    void onError(@NonNull Throwable error);
}
