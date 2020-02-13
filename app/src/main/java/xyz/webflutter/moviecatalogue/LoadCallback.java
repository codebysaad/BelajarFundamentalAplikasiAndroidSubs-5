package xyz.webflutter.moviecatalogue;

import android.database.Cursor;

public interface LoadCallback {
    void preExecute();

    void postExecute(Cursor notes);
}
