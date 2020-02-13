package xyz.webflutter.moviecatalogue.helper;

import android.database.Cursor;

import java.util.ArrayList;

import xyz.webflutter.moviecatalogue.models.ResultMovie;

import static xyz.webflutter.moviecatalogue.helper.Contract.MovieColumns.*;

public class MappingHelper {
    public static ArrayList<ResultMovie> mapCursorToArrayList(Cursor movieCursor) {
        ArrayList<ResultMovie> movieList = new ArrayList<>();
        while (movieCursor.moveToNext()) {
            int id = movieCursor.getInt(movieCursor.getColumnIndexOrThrow(ID));
            String originalTitle = movieCursor.getString(movieCursor.getColumnIndexOrThrow(ORIGINAL_TITLE));
            String voteCount = movieCursor.getString(movieCursor.getColumnIndexOrThrow(VOTE_COUNT));
            String voteAverage = movieCursor.getString(movieCursor.getColumnIndexOrThrow(VOTE_AVERAGE));
            String originalLanguage = movieCursor.getString(movieCursor.getColumnIndexOrThrow(ORIGINAL_LANGUAGE));
            String popularity = movieCursor.getString(movieCursor.getColumnIndexOrThrow(POPULARITY));
            String posterPath = movieCursor.getString(movieCursor.getColumnIndexOrThrow(POSTER_PATH));
            String overview = movieCursor.getString(movieCursor.getColumnIndexOrThrow(OVERVIEW));
            String releaseDate = movieCursor.getString(movieCursor.getColumnIndexOrThrow(RELEASE_DATE));
            movieList.add(new ResultMovie(id, originalTitle, voteCount, voteAverage, originalLanguage, popularity, posterPath, overview, releaseDate));
        }
        return movieList;
    }
}
