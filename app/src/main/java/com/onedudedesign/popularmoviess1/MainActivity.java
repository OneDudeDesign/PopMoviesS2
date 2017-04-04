package com.onedudedesign.popularmoviess1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private MovieAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        //set the Gridview Layout Manager with 2 columns
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        mAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        List<Movie> movies = new ArrayList<>();

        for (int i = 0; i < 25; i++) {
            movies.add(new Movie());
        }
        mAdapter.setMovieList(movies);
    }

    //The MovieViewHolder Class extending recyyclerview viewholder to hold
    // references to the image views in the layout

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;

        //the constructor
        public MovieViewHolder (View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.moviePosterImageView);
        }
    }
}
