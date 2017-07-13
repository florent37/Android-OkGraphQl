package com.github.florent37.okgraphql;

import com.github.florent37.okgraphql.converter.Converter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.annotations.NonNull;

/**
 * Created by florentchampigny on 23/05/2017.
 */

public abstract class AbstractQuery<T, QUERY extends AbstractQuery> {

    private final String query;
    private final String prefix;
    private final OkGraphql okGraphql;

    private final List<FieldValue> fieldValues = new ArrayList<>();
    private final List<VariableValues> variableValues = new ArrayList<>();
    private final List<String> fragments = new ArrayList<>();

    private Class<T> classToCast = null;
    private boolean toList = false;

    private Callback<? super T> successCallback;
    private ErrorCallback errorCallback;

    public AbstractQuery(OkGraphql okGraphql, String prefix, String query) {
        this.okGraphql = okGraphql;
        this.prefix = prefix;
        this.query = query;
    }

    protected void castClass(Class classToCast) {
        this.classToCast = classToCast;
    }

    protected QUERY field(String name, String value) {
        fieldValues.add(new FieldValue(name, value));
        return (QUERY) this;
    }

    protected void castClassList(Class classToCast) {
        castClass(classToCast);
        toList = true;
    }

    public String getContent() {
        final StringBuilder queryString = new StringBuilder();
        final StringBuilder content = new StringBuilder();
        {
            queryString.append("{\"query\":")
                    .append("\"")
                    .append(prefix);

            content.append(query);

            for (String fragment : fragments) {
                content.append("\n")
                        .append("fragment ")
                        .append(fragment);
            }

            queryString.append("\"")
                    .append(content)
                    .append(",")
                    .append("\"variableValues\":");
            if (variableValues.isEmpty()) {
                queryString.append("null");
            } else {
                queryString.append("{");
                final int size = variableValues.size();
                for (int i = 0; i < size; i++) {
                    final VariableValues variableValues = this.variableValues.get(i);
                    queryString.append("\"").append(variableValues.name).append("\":");

                    final Object value = variableValues.value;
                    if (value == null) {
                        queryString.append("null");
                    } else if (value instanceof Number || value instanceof Boolean) {
                        queryString.append(value.toString());
                    } else {
                        queryString.append("\"").append(value.toString()).append("\"");
                    }
                    if (i != size - 1) {
                        queryString.append(",");
                    }
                }
                queryString.append("}");
            }
            queryString.append("}");
        }

        String contentString = queryString.toString();
        for (FieldValue fieldValue : fieldValues) {
            contentString = contentString.replace("@" + fieldValue.name, "\\\"" + fieldValue.value + "\\\"");
        }

        return contentString;

    }

    void onResponse(Converter converter, String json) {
        if (String.class.equals(classToCast)) {
            successCallback.onResponse((T) json);
        } else { //convert only if cast != string
            final Converter.BodyConverter<T> objectBodyConverter = converter.bodyConverter();
            final T data = objectBodyConverter.convert(json, classToCast, toList);
            successCallback.onResponse(data);
        }
    }

    void onError(Throwable throwable) {
        errorCallback.onError(throwable);
    }

    public void enqueue(@NonNull Callback<T> callback, @NonNull ErrorCallback errorCallback) {
        this.successCallback = callback;
        this.errorCallback = errorCallback;
        okGraphql.enqueue(AbstractQuery.this);
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

    public QUERY variable(String key, Object value) {
        variableValues.add(new VariableValues(key, value));
        return (QUERY) this;
    }

    public QUERY fragment(String fragment) {
        fragments.add(fragment);
        return (QUERY) this;
    }
}
