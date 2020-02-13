package xyz.webflutter.myfavoriteapps;

import android.database.Cursor;

public interface LoadMovieCallback {

    void postExecute(Cursor notes);
}
