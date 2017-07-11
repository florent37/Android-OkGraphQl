package com.github.florent37.rxgraphql;

import java.util.ArrayList;
import java.util.List;

import com.github.florent37.rxgraphql.converter.Converter;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.annotations.NonNull;

/**
 * Created by florentchampigny on 23/05/2017.
 */

public abstract class AbstractQuery<T> {

    private final String query;
    private final String prefix;
    private final RxGraphQl rxGraphQl;

    private final List<Field> fields = new ArrayList<>();
    private final List<Variable> variables = new ArrayList<>();

    private Class<T> classToCast = null;
    private boolean toList = false;

    private Callback<? super T> successCallback;
    private ErrorCallback errorCallback;

    public AbstractQuery(RxGraphQl rxGraphQl, String prefix, String query) {
        this.rxGraphQl = rxGraphQl;
        this.prefix = prefix;
        this.query = query;
    }

    protected void castClass(Class classToCast) {
        this.classToCast = classToCast;
    }

    protected void injectField(String name, String value) {
        fields.add(new Field(name, value));
    }

    protected void castClassList(Class classToCast) {
        castClass(classToCast);
        toList = true;
    }

    public String getContent() {
        final StringBuilder content = new StringBuilder();
        {
            content.append("{\"query\":")
                    .append("\"")
                    .append(prefix).append(" { ")
                    .append(query)
                    .append("}")
                    .append("\"")
                    .append(",")
                    .append("\"variables\":")
                    .append("null")
                    .append("}");
        }

        String contentString = content.toString();
        for (Field field : fields) {
            contentString = contentString.replace("@" + field.name, "\\\"" + field.value + "\\\"");
        }

        return contentString;

    }

    void onResponse(Converter converter, String json) {
        final Converter.BodyConverter<T> objectBodyConverter = converter.bodyConverter();
        final T data = objectBodyConverter.convert(json, classToCast, toList);
        successCallback.onResponse(data);
    }

    void onError(Throwable throwable) {
        errorCallback.onError(throwable);
    }

    public void enqueue(@NonNull Callback<T> callback, @NonNull ErrorCallback errorCallback) {
        this.successCallback = callback;
        this.errorCallback = errorCallback;
        rxGraphQl.enqueue(AbstractQuery.this);
    }

    public Single<T> toSingle() {
        return Single.create(new SingleOnSubscribe<T>() {
            @Override
            public void subscribe(final @NonNull SingleEmitter<T> e) throws Exception {
                enqueue(new Callback<T>() {
                            @Override
                            public void onResponse(@NonNull T data) {
                                e.onSuccess(data);
                            }
                        }
                        , new ErrorCallback() {
                            @Override
                            public void onError(@NonNull Throwable error) {
                                e.onError(error);
                            }
                        }
                );
            }
        });
    }

    public void addVariable(String key, Object value) {
        variables.add(new Variable(key, value));
    }
}
