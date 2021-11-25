package com.example.mymovies.retrofit;

import android.util.Log;

import com.example.mymovies.models.Genre;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class GenresJsonDeserializer implements JsonDeserializer {
    private static String TAG = GenresJsonDeserializer.class.getSimpleName();

    @Override
    public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        ArrayList<Genre> genres = null;
        try{
            JsonObject jsonObject = json.getAsJsonObject();
            JsonArray jsonArray = jsonObject.getAsJsonArray("genres");
            genres = new ArrayList<>(jsonArray.size());
            for(int i=0; i<jsonArray.size(); i++){
                Genre dematerialized = context.deserialize(jsonArray.get(i), Genre.class);
                genres.add(dematerialized);
            }
        } catch (JsonParseException e){
            Log.e(TAG, String.format("Could not dematerialize Genre element: %s", json.toString()));
        }
        return genres;
    }
}
