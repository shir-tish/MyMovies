package com.example.mymovies.retrofit;

import com.example.mymovies.models.Genre;
import com.example.mymovies.models.Movie;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;
import java.util.ArrayList;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiUtil {
    static String MOVIE_BASE_URL = "https://api.themoviedb.org/3/movie/";
    static String GENRE_BASE_URL = "https://api.themoviedb.org/3/genre/movie/";
    static final Type MOVIE_ARRAY_LIST_CLASS_TYPE = (new ArrayList<Movie>()).getClass();
    static final Type GENRE_ARRAY_LIST_CLASS_TYPE = (new ArrayList<Genre>()).getClass();

    public static final String MOVIE_IMAGE_URL = "https://image.tmdb.org/t/p/w300";

    public static Api getMoviesRetrofitApi(){
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(MOVIE_ARRAY_LIST_CLASS_TYPE, new MoviesJsonDeserializer())
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MOVIE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        Api api = retrofit.create(Api.class);

        return api;
    }

    public static Api getGenresRetrofitApi(){
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(GENRE_ARRAY_LIST_CLASS_TYPE, new GenresJsonDeserializer())
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GENRE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        Api api = retrofit.create(Api.class);

        return api;
    }
}
