package com.onedudedesign.popularmoviess2.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.onedudedesign.popularmoviess2.Data.FavMovieContract.FavMovieEntry.TABLE_NAME;

/**
 * Created by clucier on 6/8/17.
 */

public class FavMovieContentProvider extends ContentProvider {

    public static final int FAVMOVIES = 100;
    public static final int FAVMOVIE_ID = 101;

    private static final UriMatcher sUriMatcher = BuildUriMatcher();

    public static UriMatcher BuildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(FavMovieContract.AUTHORITY,
                FavMovieContract.PATH_FAVMOVIES, FAVMOVIES);
        uriMatcher.addURI(FavMovieContract.AUTHORITY,
                FavMovieContract.PATH_FAVMOVIES + "/*",FAVMOVIE_ID);
        return uriMatcher;
    }

    private FavMovieDbHelper mFavMovieDbHelper;

    @Override
    public boolean onCreate() {

        Context context = getContext();
        mFavMovieDbHelper = new FavMovieDbHelper(context);
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        final SQLiteDatabase db = mFavMovieDbHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);

        Cursor retCursor;

        switch (match) {
            case FAVMOVIES:

                retCursor = db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        final SQLiteDatabase db = mFavMovieDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        Uri returnUri;

        switch (match) {
            case FAVMOVIES:

                long id = db.insert(TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(FavMovieContract.FavMovieEntry.CONTENT_URI, id);

                } else {
                    throw new android.database.SQLException("Failed to insert row into: " + uri);
                }

                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        final SQLiteDatabase db = mFavMovieDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        int moviesDeleted = 0;

        switch (match) {
            // Handle the single item case, recognized by the ID included in the URI path
            case FAVMOVIES:

                moviesDeleted = db.delete(FavMovieContract.FavMovieEntry.TABLE_NAME,selection,selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (moviesDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of tasks deleted
        return moviesDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        //implement to allow adding of information possibly
        return 0;
    }
}
