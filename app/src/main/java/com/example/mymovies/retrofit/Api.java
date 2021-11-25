package com.example.mymovies.retrofit;

import com.example.mymovies.models.Genre;
import com.example.mymovies.models.Movie;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {
    @GET("popular")
    Call<ArrayList<Movie>> getMovies(
            @Query("api_key") String apiKey
    );

    @GET("list")
    Call<ArrayList<Genre>> getGenres(
            @Query("api_key") String apiKey
    );
}
