package xyz.webflutter.moviecatalogue.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.concurrent.ExecutionException;

import xyz.webflutter.moviecatalogue.R;
import xyz.webflutter.moviecatalogue.models.ResultMovie;

import static xyz.webflutter.moviecatalogue.helper.Contract.MovieColumns.CONTENT_URI;

public class StackWidgetRemoteFacctory implements RemoteViewsService.RemoteViewsFactory {
    private final Context context;
    private Cursor cursor;

    StackWidgetRemoteFacctory(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        final long binder = Binder.clearCallingIdentity();
        cursor = context.getContentResolver().query(CONTENT_URI, null, null, null, null);
        Binder.restoreCallingIdentity(binder);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_item);
        if (cursor.moveToPosition(position)){
            ResultMovie movie = new ResultMovie(cursor);
            try {
                Bitmap img = Glide.with(context).asBitmap()
                        .load(movie.getPosterPath())
                        .apply(new RequestOptions().fitCenter())
                        .submit()
                        .get();
                remoteViews.setImageViewBitmap(R.id.ivy_item_widget, img);
            }catch (ExecutionException | InterruptedException e){
                e.printStackTrace();
            }
        }
        Bundle bundle = new Bundle();
        bundle.putInt(MovieWidget.EXTRA_ITEM, position);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        remoteViews.setOnClickFillInIntent(R.id.ivy_item_widget, intent);
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
