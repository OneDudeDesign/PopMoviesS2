package com.onedudedesign.popularmoviess2.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.onedudedesign.popularmoviess2.Data.FavMovieContract.*;

/**
 * Created by clucier on 6/8/17.
 */

public class FavMovieDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favMovies.db";
    private static final int DATABASE_VERSION = 1;

    public FavMovieDbHelper (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_FAVMOVIE_TABLE = "CREATE TABLE " + FavMovieEntry.TABLE_NAME + " (" +
                FavMovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FavMovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                FavMovieEntry.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL, " +
                FavMovieEntry.COLUMN_FAVORITE + " BOOLEAN, " +
                FavMovieEntry.COLUMN_BACKDROP_PATH + " TEXT, " +
                FavMovieEntry.COLUMN_OVERVIEW + " TEXT, " +
                FavMovieEntry.COLUMN_POSTER_PATH + " TEXT, " +
                FavMovieEntry.COLUMN_RELEASE_DATE + " TEXT, " +
                FavMovieEntry.COLUMN_VOTE_AVERAGE + " TEXT" +
                "); ";

        db.execSQL(SQL_CREATE_FAVMOVIE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavMovieEntry.TABLE_NAME);
        onCreate(db);

    }
}
