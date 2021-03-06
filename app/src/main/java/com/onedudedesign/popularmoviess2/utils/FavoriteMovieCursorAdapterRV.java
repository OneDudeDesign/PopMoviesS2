package com.onedudedesign.popularmoviess2.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.onedudedesign.popularmoviess2.Models.Movie;
import com.onedudedesign.popularmoviess2.R;
import com.squareup.picasso.Picasso;


/**
 * Created by clucier on 5/18/17.
 * This adapter was built to connect the cursor adapter to the recyclerview to display
 * the favorites set in the sql db
 * This is using the Cupboard library for simplification
 */

public class FavoriteMovieCursorAdapterRV
        extends RecyclerViewCursorAdapter<FavoriteMovieCursorAdapterRV.ImageViewHolder> {
    private static final String TAG = FavoriteMovieCursorAdapterRV.class.getSimpleName();
    private final Context mContext;
    private static ListItemClickListener mOnClickListener;



    public interface ListItemClickListener {
        //passing in the string for the movie id as it becomes important to track the TNDB id of 
        //the movie for proper loading from the network as the DB row ID's can become inconsistent
        //cannot rely on them to match later I will use this string to tag the imageview effectively
        //making the imageview remember the TMDB movie ID
        void onListItemClick(int clickedItemIndex, String movieID);
    }


    public FavoriteMovieCursorAdapterRV(Context context, ListItemClickListener listener) {
        super(null);
        mContext = context;
        mOnClickListener = listener;

    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_item_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(ImageViewHolder holder, Cursor cursor) {
        int columnIndexPoster = cursor.getColumnIndex(mContext.getString(R.string.databaseColumnPosterPath));
        int columnIndexMovieId = cursor.getColumnIndex(mContext.getString(R.string.databaseColumnMovieID));
        String imagePath = cursor.getString(columnIndexPoster);
        String movieId = cursor.getString(columnIndexMovieId);

        //setting the movieID as a tag OBJECT on the Imageview so it persists in the grid
        //to be used later by the intent
        holder.moviePoster.setTag(movieId);

        //Download image using picasso library
        //Look to store in db for no network not P2 requirement
        Picasso.with(mContext)
                .load(Movie.TMDB_IMAGE_PATH + imagePath)
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(holder.moviePoster);

    }


    public static class ImageViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        ImageView moviePoster;

        ImageViewHolder(View itemView) {
            super(itemView);
            moviePoster = (ImageView) itemView.findViewById(R.id.moviePosterImageView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            //getting the movieID tag back have to convert from object to string
            String mID = moviePoster.getTag().toString();
            mOnClickListener.onListItemClick(clickedPosition, mID);
        }
    }

}