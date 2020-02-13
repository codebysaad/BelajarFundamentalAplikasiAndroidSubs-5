package xyz.webflutter.myfavoriteapps;

import android.database.Cursor;

import java.util.ArrayList;

import xyz.webflutter.myfavoriteapps.models.ModelMovie;

import static xyz.webflutter.myfavoriteapps.database.Contract.MovieColumns.*;

public class MappingHelper {
    public static ArrayList<ModelMovie> mapCursorToArrayList(Cursor notesCursor) {
        ArrayList<ModelMovie> notesList = new ArrayList<>();
        while (notesCursor.moveToNext()) {
            int id = notesCursor.getInt(notesCursor.getColumnIndexOrThrow(ID));
            String originalTitle = notesCursor.getString(notesCursor.getColumnIndexOrThrow(ORIGINAL_TITLE));
            String voteCount = notesCursor.getString(notesCursor.getColumnIndexOrThrow(VOTE_COUNT));
            String voteAverage = notesCursor.getString(notesCursor.getColumnIndexOrThrow(VOTE_AVERAGE));
            String originalLanguage = notesCursor.getString(notesCursor.getColumnIndexOrThrow(ORIGINAL_LANGUAGE));
            String popularity = notesCursor.getString(notesCursor.getColumnIndexOrThrow(POPULARITY));
            String posterPath = notesCursor.getString(notesCursor.getColumnIndexOrThrow(POSTER_PATH));
            String overview = notesCursor.getString(notesCursor.getColumnIndexOrThrow(OVERVIEW));
            String releaseDate = notesCursor.getString(notesCursor.getColumnIndexOrThrow(RELEASE_DATE));
            notesList.add(new ModelMovie(id, originalTitle, voteCount, voteAverage, originalLanguage, popularity, posterPath, overview, releaseDate));
        }
        return notesList;
    }
}
