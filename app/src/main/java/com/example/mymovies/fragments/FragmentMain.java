package com.example.mymovies.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mymovies.R;
import com.example.mymovies.adapters.MovieAdapter;
import com.example.mymovies.retrofit.Api;
import com.example.mymovies.retrofit.ApiUtil;
import com.example.mymovies.models.Genre;
import com.example.mymovies.models.Movie;
import com.example.mymovies.viewmodels.MoviesViewModel;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentMain extends Fragment {

    public static ArrayList<Genre> genres;

    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter, likedMoviesAdapter;

    public static MoviesViewModel viewModel;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(MoviesViewModel.class);
        setGenres();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        init(view);
        return view;
    }

    private void init(View view){
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Popular"));
        tabLayout.addTab(tabLayout.newTab().setText("Newest"));
        tabLayout.addTab(tabLayout.newTab().setText("Favorites"));

        TextView noLikedMoviesTv = view.findViewById(R.id.no_liked_movies);
        noLikedMoviesTv.setVisibility(View.GONE);

        recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        viewModel.getMoviesList().observe(getViewLifecycleOwner(), new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                movieAdapter = new MovieAdapter(getContext(), movies);
                recyclerView.setAdapter(movieAdapter);

                Collections.sort(movies, Movie.byPopularity);

                movieAdapter.setListener(new MovieAdapter.Listener() {
                    @Override
                    public void onItemClicked(int position) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("position", position);
                        Navigation.findNavController(recyclerView).navigate(R.id.action_fragmentMain_to_fragmentMovie, bundle);
                    }
                });

                tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        switch (tab.getPosition()){
                            case 0:
                                Collections.sort(movies, Movie.byPopularity);
                                recyclerView.setAdapter(movieAdapter);
                                noLikedMoviesTv.setVisibility(View.GONE);
                                break;
                            case 1:
                                Collections.sort(movies, Movie.byDate);
                                recyclerView.setAdapter(movieAdapter);
                                noLikedMoviesTv.setVisibility(View.GONE);
                                break;
                            case 2:
                                List<Movie> likedMovies = new ArrayList<>();
                                for(Movie movie : movies){
                                    if(movie.isLiked())
                                        likedMovies.add(movie);
                                }
                                if (likedMovies.size() == 0){
                                    noLikedMoviesTv.setVisibility(View.VISIBLE);
                                }else{
                                    noLikedMoviesTv.setVisibility(View.GONE);
                                }
                                likedMoviesAdapter = new MovieAdapter(getContext(), likedMovies);
                                recyclerView.setAdapter(likedMoviesAdapter);
                                break;
                        }
                        movieAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });
            }
        });
    }

    private void setGenres(){
        genres = new ArrayList<>();

        Api api = ApiUtil.getGenresRetrofitApi();

        Call<ArrayList<Genre>> call = api.getGenres("1e7c44a8832c61b4193ecb39e314a4d0");
        call.enqueue(new Callback<ArrayList<Genre>>() {
            @Override
            public void onResponse(Call<ArrayList<Genre>> call, Response<ArrayList<Genre>> response) {
                genres = response.body();
            }

            @Override
            public void onFailure(Call<ArrayList<Genre>> call, Throwable t) {

            }
        });
    }

}