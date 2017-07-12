package com.github.florent37.rxgraphql.sample;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by florentchampigny on 10/07/2017.
 */

public class MainApplication extends Application {

    private OkHttpClient okHttpClient;

    @Override
    public void onCreate() {
        super.onCreate();

        Stetho.initializeWithDefaults(getApplicationContext());
        okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new StethoInterceptor())
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }
}
