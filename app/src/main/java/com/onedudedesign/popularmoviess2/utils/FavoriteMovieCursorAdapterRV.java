package com.onedudedesign.popularmoviess2.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.onedudedesign.popularmoviess2.Cupboard.CupboardDbHelper;
import com.onedudedesign.popularmoviess2.Cupboard.MovieFavorite;
import com.onedudedesign.popularmoviess2.Models.Movie;
import com.onedudedesign.popularmoviess2.R;
import com.squareup.picasso.Picasso;

import java.util.Random;

import static android.os.Build.VERSION_CODES.M;
import static nl.qbusict.cupboard.CupboardFactory.cupboard;

/**
 * Created by clucier on 5/18/17.
 */

public class FavoriteMovieCursorAdapterRV extends RecyclerViewCursorAdapter<FavoriteMovieCursorAdapterRV.ImageViewHolder>
{
    private static final String TAG = FavoriteMovieCursorAdapterRV.class.getSimpleName();
    private final Context mContext;
    private SQLiteDatabase mDB;
    private static ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex, String movieID);
    }


    public FavoriteMovieCursorAdapterRV(Context context, ListItemClickListener listener) {
        super(null);
        mContext = context;
        mOnClickListener = listener;

        // Get the cursor from Cupboard

        CupboardDbHelper dbHelper = new CupboardDbHelper(mContext);
        mDB = dbHelper.getWritableDatabase();
        Cursor cursor = cupboard().withDatabase(mDB).query(MovieFavorite.class).getCursor();

        swapCursor(cursor);
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_item_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(ImageViewHolder holder, Cursor cursor)
    {
        int columnIndexPoster = cursor.getColumnIndex("poster_path");
        int columnIndexMovieId = cursor.getColumnIndex("movie_id");
        String imagePath = cursor.getString(columnIndexPoster);
        String movieId = cursor.getString(columnIndexMovieId);

        holder.moviePoster.setTag(movieId);


        //Download image using picasso library
        Picasso.with(mContext)
                .load(Movie.TMDB_IMAGE_PATH + imagePath)
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(holder.moviePoster);

    }


    public static class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        ImageView moviePoster;

        ImageViewHolder(View itemView)
        {
            super(itemView);
            moviePoster = (ImageView) itemView.findViewById(R.id.moviePosterImageView);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            String mID = moviePoster.getTag().toString();
            Log.d("Tag", "MovieID " + mID);
            mOnClickListener.onListItemClick(clickedPosition, mID);
        }
    }
}