package com.onedudedesign.popularmoviess1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
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
