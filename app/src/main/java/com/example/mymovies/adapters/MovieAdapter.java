package com.example.mymovies.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mymovies.R;
import com.example.mymovies.models.Movie;

import java.util.List;

import static com.example.mymovies.fragments.FragmentMain.genres;
import static com.example.mymovies.fragments.FragmentMovie.getReleaseDate;
import static com.example.mymovies.retrofit.ApiUtil.MOVIE_IMAGE_URL;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private final Context context;
    private Listener listener;

    private final List<Movie> movieList;

    public interface Listener{
        void onItemClicked(int position);
    }

    public void setListener(Listener listener){this.listener = listener;}

    public MovieAdapter(Context context, List<Movie> movies){
        this.context = context;
        this.movieList = movies;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movieList.get(position);

        holder.title.setText(movie.getTitle());

        Glide.with(context).load(MOVIE_IMAGE_URL + movie.getPosterPath()).into(holder.imageView);

        holder.releaseDate.setText(getReleaseDate(movie.getReleaseDate()));

        String sGenres = "";

        for(int i = 0; i< movie.getGenreIds().size(); i++){
            for(int j=0; j<genres.size(); j++){
                if (movie.getGenreIds().get(i).equals(genres.get(j).getId())){
                    sGenres += genres.get(j).getName()+", ";
                }
            }
        }

        sGenres = sGenres.substring(0, sGenres.length()-2);

        holder.movieGenres.setText(sGenres);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClicked(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title;
        TextView releaseDate;
        TextView movieGenres;
        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            releaseDate = itemView.findViewById(R.id.release_date);
            movieGenres = itemView.findViewById(R.id.genres);
        }
    }

}
