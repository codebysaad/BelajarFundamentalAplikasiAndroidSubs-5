package xyz.webflutter.moviecatalogue.providers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

import xyz.webflutter.moviecatalogue.activities.FavActivity;
import xyz.webflutter.moviecatalogue.helper.database.movie.MovieHelper;

import static xyz.webflutter.moviecatalogue.helper.Contract.AUTHORITY;
import static xyz.webflutter.moviecatalogue.helper.Contract.MovieColumns.*;

public class MovieContentProvider extends ContentProvider {
    private static final int MOVIE = 1;
    private static final int MOVIE_ID = 2;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private MovieHelper noteHelper;

    static {
        sUriMatcher.addURI(AUTHORITY, TABLE_NAME, MOVIE);
        sUriMatcher.addURI(AUTHORITY, TABLE_NAME + "/#", MOVIE_ID);
    }

    @Override
    public boolean onCreate() {
        noteHelper = MovieHelper.getInstance(getContext());

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        noteHelper.open();
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case MOVIE:
                cursor = noteHelper.queryProvider();
                break;
            case MOVIE_ID:
                cursor = noteHelper.queryByIdProvider(uri.getLastPathSegment());
                break;
            default:
                cursor = null;
                break;
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        noteHelper.open();
        long added;
        switch (sUriMatcher.match(uri)) {
            case MOVIE:
                added = noteHelper.insertProvider(values);
                break;
            default:
                added = 0;
                break;
        }

        Objects.requireNonNull(getContext()).getContentResolver().notifyChange(CONTENT_URI, new FavActivity.DataObserver(new Handler(), getContext()));

        return Uri.parse(CONTENT_URI + "/" + added);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        noteHelper.open();
        int deleted;
        switch (sUriMatcher.match(uri)) {
            case MOVIE_ID:
                deleted = noteHelper.deleteProvider(uri.getLastPathSegment());
                break;
            default:
                deleted = 0;
                break;
        }

        Objects.requireNonNull(getContext()).getContentResolver().notifyChange(CONTENT_URI, new FavActivity.DataObserver(new Handler(), getContext()));

        return deleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        noteHelper.open();
        int updated;
        switch (sUriMatcher.match(uri)) {
            case MOVIE_ID:
                updated = noteHelper.updateProvider(uri.getLastPathSegment(), values);
                break;
            default:
                updated = 0;
                break;
        }

        Objects.requireNonNull(getContext()).getContentResolver().notifyChange(CONTENT_URI, new FavActivity.DataObserver(new Handler(), getContext()));

        return updated;
    }
}