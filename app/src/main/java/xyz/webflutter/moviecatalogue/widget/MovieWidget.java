package xyz.webflutter.moviecatalogue.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

import xyz.webflutter.moviecatalogue.R;

/**
 * Implementation of App Widget functionality.
 */
public class MovieWidget extends AppWidgetProvider {
    private static final String TOAST_ACTION = "xyz.webflutter.moviecatalogue.TOAST_ACTION";
    public static final String EXTRA_ITEM = "xyz.webflutter.moviecatalogue.EXTRA_ITEM";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Intent intent = new Intent(context,StackWidgetServices.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        RemoteViews rViews  = new RemoteViews(context.getPackageName(), R.layout.movie_widget);
        rViews.setRemoteAdapter(R.id.stack_view_movie, intent);
        rViews.setEmptyView(R.id.stack_view_movie,R.id.empty_view_movie);
        Intent toastIntent = new Intent(context, MovieWidget.class);
        toastIntent.setAction(MovieWidget.TOAST_ACTION);
        toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        rViews.setPendingIntentTemplate(R.id.stack_view_movie, pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetId,rViews);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.stack_view_movie);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction() != null){
            if (intent.getAction().equals(TOAST_ACTION)){
                int index = intent.getIntExtra(EXTRA_ITEM, 0);
                Toast.makeText(context, "Touch view" + index, Toast.LENGTH_SHORT).show();
            }
        }
    }
}

