package com.onedudedesign.popularmoviess1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by clucier on 4/4/17.
 * Adapter to feed the movie information to the Recyclerview
 */

public class MovieAdapter extends RecyclerView.Adapter<MainActivity.MovieViewHolder> {

    private List<Movie> mMovieList;
    private LayoutInflater mInflater;
    private Context mContext;

    public MovieAdapter (Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mMovieList = new ArrayList<>();
    }

    @Override
    public MainActivity.MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.movie_item_image, parent, false);
        MainActivity.MovieViewHolder viewHolder = new MainActivity.MovieViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MainActivity.MovieViewHolder holder, int position) {
        Movie movie = mMovieList.get(position);

        //use Picasso to deal with loading and caching of the images
        //there is a placeholder value to just color the screen if the image cannot be loaded

        Picasso.with(mContext)
                .load(movie.getMoviePoster())
                .placeholder(R.color.greenScreenPlaceholder)
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        if (mMovieList != null) {
            return mMovieList.size();
        }
        return 0;
    }

    //method to set the movie list and let the adapter know if the data has changed

    public void setMovieList(List<Movie> movies) {
        this.mMovieList.clear();
        this.mMovieList.addAll(movies);
        notifyDataSetChanged();
    }
}
