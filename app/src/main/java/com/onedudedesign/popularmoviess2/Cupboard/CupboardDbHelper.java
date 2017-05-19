package com.onedudedesign.popularmoviess2.Cupboard;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

/**
 * Created by clucier on 5/17/17.
 * Using Cupboard instead of a content provider to store the data in SQLLite for the favorites
 * for simplification it uses a POJO for the model similar to Retrofit for the JSON calls
 */

public class CupboardDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "FavoriteMovies.db";
    private static final int DATABASE_VERSION = 1;

    public CupboardDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    static {
        // register the POJO model
        cupboard().register(MovieFavorite.class);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create the tables
        cupboard().withDatabase(db).createTables();

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        cupboard().withDatabase(db).upgradeTables();
        // this will upgrade tables, adding columns and new tables.
        // Note that existing columns will not be converted
        // any migration work for future state can be done here

    }

}
