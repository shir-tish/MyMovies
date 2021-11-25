package com.example.mymovies.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mymovies.retrofit.Api;
import com.example.mymovies.retrofit.ApiUtil;
import com.example.mymovies.models.Movie;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesViewModel extends ViewModel {
    private MutableLiveData<List<Movie>> moviesList;

    public LiveData<List<Movie>> getMoviesList(){
        if(moviesList == null){
            moviesList = new MutableLiveData<>();
            loadMovies();
        }
        return moviesList;
    }

    private void loadMovies(){
        Api api = ApiUtil.getMoviesRetrofitApi();

        Call<ArrayList<Movie>> call = api.getMovies("1e7c44a8832c61b4193ecb39e314a4d0");
        call.enqueue(new Callback<ArrayList<Movie>>() {
            @Override
            public void onResponse(Call<ArrayList<Movie>> call, Response<ArrayList<Movie>> response) {
                moviesList.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<Movie>> call, Throwable t) {
                Log.d("Error","");
            }
        });
    }



}
