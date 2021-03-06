package com.onedudedesign.popularmoviess2.Data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by clucier on 6/8/17.
 */

public class FavMovieContract {

    public static final String AUTHORITY = "com.onedudedesign.popularmoviess2";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_FAVMOVIES = "FavMovies";

    public final static class FavMovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FAVMOVIES).build();

        public static final String TABLE_NAME = "FavMovies";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";
        public static final String COLUMN_FAVORITE = "favorite";

    }
}
