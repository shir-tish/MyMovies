package com.example.mymovies.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mymovies.R;
import com.example.mymovies.models.Movie;

import java.util.List;

import static com.example.mymovies.fragments.FragmentMain.genres;
import static com.example.mymovies.fragments.FragmentMain.viewModel;
import static com.example.mymovies.retrofit.ApiUtil.MOVIE_IMAGE_URL;


public class FragmentMovie extends Fragment {

    private int MOVIE_POS = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie, container, false);
        init(view);
        return view;
    }

    private void init(View view){
        if (getArguments() != null){
            MOVIE_POS = getArguments().getInt("position");
        }

        viewModel.getMoviesList().observe(getViewLifecycleOwner(), new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                setVisuals(view, movies.get(MOVIE_POS));
                setMovieVisuals(view, movies.get(MOVIE_POS));
            }
        });

    }

    private void setVisuals(View view, Movie movie){
        ImageView star = view.findViewById(R.id.star);
        ImageView back = view.findViewById(R.id.back);

        if (movie.isLiked()){
            star.setColorFilter(getResources().getColor(R.color.yellow));
        }else {
            star.setColorFilter(getResources().getColor(R.color.white));
        }

        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (movie.isLiked()){
                    star.setColorFilter(getResources().getColor(R.color.white));
                    movie.setLiked(false);
                }else {
                    star.setColorFilter(getResources().getColor(R.color.yellow));
                    movie.setLiked(true);
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Navigation.findNavController(v).popBackStack();
            }
        });

    }

    private void setMovieVisuals(View view, Movie movie){
        ImageView background = view.findViewById(R.id.background);
        ImageView image = view.findViewById(R.id.image);
        TextView title = view.findViewById(R.id.title);
        TextView releaseDate = view.findViewById(R.id.release_date);
        TextView movieGenres = view.findViewById(R.id.genres);
        TextView overview = view.findViewById(R.id.overview);
        TextView rate = view.findViewById(R.id.rate);

        title.setText(movie.getTitle());
        releaseDate.setText(getReleaseDate(movie.getReleaseDate()));

        Glide.with(getContext()).load(MOVIE_IMAGE_URL + movie.getPosterPath()).into(image);
        Glide.with(getContext()).load(MOVIE_IMAGE_URL + movie.getPosterPath()).into(background);

        String sGenres = "";
        for(int i = 0; i< movie.getGenreIds().size(); i++){
            for(int j=0; j<genres.size(); j++){
                if (movie.getGenreIds().get(i).equals(genres.get(j).getId())){
                    sGenres += genres.get(j).getName()+", ";
                }
            }
        }
        sGenres = sGenres.substring(0, sGenres.length()-2);
        movieGenres.setText(sGenres);

        overview.setText(movie.getOverview());
        rate.setText((int)(movie.getVoteAverage()*10)+"%");
    }

    public static String getReleaseDate(String releaseDate){
        String[] date = releaseDate.split("-");
        return "Released on: "+date[2]+"/"+date[1]+"/"+date[0];
    }

}