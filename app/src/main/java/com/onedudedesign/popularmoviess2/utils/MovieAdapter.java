package com.onedudedesign.popularmoviess2.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.onedudedesign.popularmoviess2.Models.Movie;
import com.onedudedesign.popularmoviess2.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by clucier on 4/4/17.
 * Adapter to feed the movie information to the Recyclerview
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> mMovieList;
    private LayoutInflater mInflater;
    private Context mContext;
    private ListItemClickListener mOnClickListener;

    /*
     * The interface that receives onClick messages.
     */
    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex, String movieID);
    }

    public MovieAdapter(Context context, ListItemClickListener listener) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        mOnClickListener = listener;
        this.mMovieList = new ArrayList<>();
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.movie_item_image, parent, false);
        MovieViewHolder viewHolder = new MovieViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Movie movie = mMovieList.get(position);

        //use Picasso to deal with loading and caching of the images
        //there is a placeholder value to just color the screen if the image cannot be loaded

        Picasso.with(mContext)
                .load(movie.getMoviePoster())
                .placeholder(R.color.greenScreenPlaceholder)
                .into(holder.imageView);

    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView imageView;

        //the constructor
        public MovieViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.moviePosterImageView);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            Movie movie = mMovieList.get(clickedPosition);
            String movieId = Integer.toString(movie.getMovieID());
            mOnClickListener.onListItemClick(clickedPosition, movieId);
        }

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
