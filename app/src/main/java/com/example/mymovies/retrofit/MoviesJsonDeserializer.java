package com.example.mymovies.retrofit;

import android.util.Log;

import com.example.mymovies.models.Movie;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

public class MoviesJsonDeserializer implements JsonDeserializer {
    private static String TAG = MoviesJsonDeserializer.class.getSimpleName();

    @Override
    public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        ArrayList<Movie> movies = null;
        try{
            JsonObject jsonObject = json.getAsJsonObject();
            JsonArray jsonArray = jsonObject.getAsJsonArray("results");
            movies = new ArrayList<>(jsonArray.size());
            for(int i=0; i<jsonArray.size(); i++){
                Movie dematerialized = context.deserialize(jsonArray.get(i), Movie.class);
                movies.add(dematerialized);
            }
        } catch (JsonParseException e){
            Log.e(TAG, String.format("Could not dematerialize Movie element: %s", json.toString()));
        }

        return movies;
    }
}
