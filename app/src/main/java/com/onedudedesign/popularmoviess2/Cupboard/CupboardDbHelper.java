package com.onedudedesign.popularmoviess2.Cupboard;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

/**
 * Created by clucier on 5/17/17.
 */

public class CupboardDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "FavoriteMovies.db";
    private static final int DATABASE_VERSION = 1;

    public CupboardDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    static {
        // register our models
        cupboard().register(MovieFavorite.class);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // this will ensure that all tables are created
        cupboard().withDatabase(db).createTables();
        // add indexes and other database tweaks in this method if you want

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        cupboard().withDatabase(db).upgradeTables();
        // this will upgrade tables, adding columns and new tables.
        // Note that existing columns will not be converted
        // will need to use this are for migration work once released if columns change

    }

}
