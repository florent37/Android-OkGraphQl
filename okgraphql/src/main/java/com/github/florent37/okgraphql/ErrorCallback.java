package com.github.florent37.okgraphql;

import io.reactivex.annotations.NonNull;

/**
 * Created by florentchampigny on 23/05/2017.
 */

public interface ErrorCallback {
    void onError(@NonNull Throwable error);
}
