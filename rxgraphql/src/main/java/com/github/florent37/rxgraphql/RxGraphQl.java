package com.github.florent37.rxgraphql;

import java.io.IOException;

import com.github.florent37.rxgraphql.converter.Converter;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by florentchampigny on 23/05/2017.
 */

public class RxGraphQl {
    private String baseUrl;

    private OkHttpClient okHttpClient = new OkHttpClient();
    private Converter converter;

    public <T> Query<T> query(String query) {
        return new Query<>(this, query);
    }

    public Mutation<String> mutation(String query) {
        return new Mutation<>(this, query);
    }

    protected <T> void enqueue(final AbstractQuery abstractQuery) {
        try {
            okHttpClient.newCall(
                    new Request.Builder()
                            .url(baseUrl)
                            .post(
                                    RequestBody.create(MediaType.parse("application/json"), abstractQuery.getContent())
                            )
                            .build())
                    .enqueue(new okhttp3.Callback() {
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            final String json = response.body().string();
                            abstractQuery.onResponse(converter, json);
                        }

                        @Override
                        public void onFailure(Call call, IOException e) {
                            abstractQuery.onError(e);
                        }
                    });
        }catch (Exception e){
            abstractQuery.onError(e);
        }
    }

    public static class Builder {

        private RxGraphQl rxGraphQl;

        public Builder() {
            rxGraphQl = new RxGraphQl();
        }

        public RxGraphQl build() {
            return rxGraphQl;
        }

        public Builder converter(Converter converter) {
            rxGraphQl.converter = converter;
            return this;
        }

        public Builder okClient(OkHttpClient okHttpClient) {
            rxGraphQl.okHttpClient = okHttpClient;
            return this;
        }

        public Builder baseUrl(String baseUrl) {
            rxGraphQl.baseUrl = baseUrl;
            return this;
        }
    }
}
