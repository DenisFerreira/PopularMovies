package com.example.android.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lsitec205.ferreira on 21/11/17.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "movie.db";
    private static int DATABASE_VERSION = 4;

    public MovieDbHelper(Context context){
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIE_TABLE =
                "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +

                        MovieContract.MovieEntry._ID               + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        MovieContract.MovieEntry.COLUMN_MOVIE_ID       + " INTEGER NOT NULL, "              +
                        MovieContract.MovieEntry.COLUMN_TITLE + " TEXT NOT NULL,"                           +

                        MovieContract.MovieEntry.COLUMN_ORIGINAL_LANGUAGE   + " TEXT, "            +
                        MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE   + " TEXT, "               +

                        MovieContract.MovieEntry.COLUMN_OVERVIEW   + " TEXT NOT NULL, "                     +
                        MovieContract.MovieEntry.COLUMN_POPULARITY   + " REAL, "                   +
                        MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL, "                   +
                        MovieContract.MovieEntry.COLUMN_RELEASE_DATE    + " INTEGER NOT NULL, "             +

                        MovieContract.MovieEntry.COLUMN_VOTE_COUNT   + " INTEGER, "                +
                        MovieContract.MovieEntry.COLUMN_RUNTIME   + " INTEGER, "                   +

                        MovieContract.MovieEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, "                    +
                        " UNIQUE (" + MovieContract.MovieEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE);";
        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
