package xyz.webflutter.moviecatalogue.helper.database.movie;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import xyz.webflutter.moviecatalogue.models.ResultMovie;

import static xyz.webflutter.moviecatalogue.helper.Contract.MovieColumns.*;

public class MovieHelper {
    private static final String DATABASE_TABLE = TABLE_NAME;
    private final DatabaseMovie dataBaseHelper;
    private static MovieHelper INSTANCE;

    private SQLiteDatabase database;

    private MovieHelper(Context context) {
        dataBaseHelper = new DatabaseMovie(context);
    }

    public static MovieHelper getInstance(Context context) {
        if (INSTANCE == null){
            synchronized (SQLiteOpenHelper.class){
                if (INSTANCE == null){
                    INSTANCE = new MovieHelper(context);
                }
            }
        }
        return INSTANCE;
    }


    public void open() throws SQLException {
        database = dataBaseHelper.getWritableDatabase();
    }

    public ArrayList<ResultMovie> query() {
        ArrayList<ResultMovie> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE
                , null
                , null
                , null
                , null
                , null
                , ID + " DESC"
                , null);
        cursor.moveToFirst();
        ResultMovie movie;
        if (cursor.getCount() > 0) {
            do {
                movie = new ResultMovie();
                movie.setId(cursor.getInt(cursor.getColumnIndexOrThrow(ID)));
                movie.setOriginalTitle(cursor.getString(cursor.getColumnIndexOrThrow(ORIGINAL_TITLE)));
                movie.setVoteCount(cursor.getString(cursor.getColumnIndexOrThrow(VOTE_COUNT)));
                movie.setVoteAverage(cursor.getString(cursor.getColumnIndexOrThrow(VOTE_AVERAGE)));
                movie.setOriginalLanguage(cursor.getString(cursor.getColumnIndexOrThrow(ORIGINAL_LANGUAGE)));
                movie.setPopularity(cursor.getString(cursor.getColumnIndexOrThrow(POPULARITY)));
                movie.setPosterPath(cursor.getString(cursor.getColumnIndexOrThrow(POSTER_PATH)));
                movie.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(OVERVIEW)));
                movie.setReleaseDate(cursor.getString(cursor.getColumnIndexOrThrow(RELEASE_DATE)));

                arrayList.add(movie);
                cursor.moveToNext();

            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public long insert(ResultMovie movie) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(ID, movie.getId());
        initialValues.put(ORIGINAL_TITLE, movie.getOriginalTitle());
        initialValues.put(VOTE_COUNT, movie.getVoteCount());
        initialValues.put(VOTE_AVERAGE, movie.getVoteAverage());
        initialValues.put(ORIGINAL_LANGUAGE, movie.getOriginalLanguage());
        initialValues.put(POPULARITY, movie.getPopularity());
        initialValues.put(POSTER_PATH, movie.getPosterPath());
        initialValues.put(OVERVIEW, movie.getOverview());
        initialValues.put(RELEASE_DATE, movie.getReleaseDate());
        return database.insert(DATABASE_TABLE, null, initialValues);
    }

    public int delete(int id) {
        return database.delete(TABLE_NAME, ID + " = '" + id + "'", null);
    }

    public Cursor queryByIdProvider(String id) {
        return database.query(DATABASE_TABLE, null
                , ID + " = ?"
                , new String[]{id}
                , null
                , null
                , null
                , null);
    }

    public Cursor queryProvider() {
        return database.query(DATABASE_TABLE
                , null
                , null
                , null
                , null
                , null
                , ID + " ASC");
    }

    public long insertProvider(ContentValues values) {
        return database.insert(DATABASE_TABLE, null, values);
    }

    public int updateProvider(String id, ContentValues values) {
        return database.update(DATABASE_TABLE, values, ID + " = ?", new String[]{id});
    }

    public int deleteProvider(String id) {
        return database.delete(DATABASE_TABLE, ID + " = ?", new String[]{id});
    }
}
