package xyz.webflutter.moviecatalogue.helper.database.movie;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import xyz.webflutter.moviecatalogue.helper.Contract;

public class DatabaseMovie extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "dbMovie";

    private static final int DATABASE_VERSION = 8;

    private static final String SQL_CREATE_TABLE = String.format("CREATE TABLE %s"
                    + " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL)",
            Contract.MovieColumns.TABLE_NAME,
            Contract.MovieColumns.ID,
            Contract.MovieColumns.ORIGINAL_TITLE,
            Contract.MovieColumns.VOTE_COUNT,
            Contract.MovieColumns.VOTE_AVERAGE,
            Contract.MovieColumns.ORIGINAL_LANGUAGE,
            Contract.MovieColumns.POPULARITY,
            Contract.MovieColumns.POSTER_PATH,
            Contract.MovieColumns.OVERVIEW,
            Contract.MovieColumns.RELEASE_DATE
    );

    public DatabaseMovie(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("Data", "onCreate: Table Movie");
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Contract.MovieColumns.TABLE_NAME);
        onCreate(db);
    }
}
