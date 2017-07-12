package com.github.florent37.okgraphql.converter;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class GsonConverter implements Converter {

    private final Gson gson;

    public GsonConverter(Gson gson) {
        this.gson = gson;
    }

    public GsonConverter() {
        this.gson = new Gson();
    }

    @Override
    public <T> BodyConverter<T> bodyConverter() {
        return new GsonBodyConverter<T>(gson);
    }

    public static class GsonBodyConverter<T> implements Converter.BodyConverter<T> {

        private final Gson gson;

        public GsonBodyConverter(Gson gson) {
            this.gson = gson;
        }

        @Override
        public T convert(String json, Class<T> classToCast, boolean toList) {
            String dataJSon = null;
            try {
                JSONObject toJson = new JSONObject(json);
                JSONObject data = toJson.getJSONObject("data");
                dataJSon = data.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (toList) {
                return null;
            } else {
                return (T) gson.fromJson(dataJSon, classToCast);
            }
        }

    }
}
