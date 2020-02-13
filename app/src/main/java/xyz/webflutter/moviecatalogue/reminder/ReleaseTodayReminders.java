package xyz.webflutter.moviecatalogue.reminder;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xyz.webflutter.moviecatalogue.BuildConfig;
import xyz.webflutter.moviecatalogue.R;
import xyz.webflutter.moviecatalogue.activities.DetailActivity;
import xyz.webflutter.moviecatalogue.activities.UpcomingActivity;
import xyz.webflutter.moviecatalogue.models.ModelMovie;
import xyz.webflutter.moviecatalogue.models.ResultMovie;
import xyz.webflutter.moviecatalogue.rest.Client;

import static xyz.webflutter.moviecatalogue.helper.Contract.*;

public class ReleaseTodayReminders extends BroadcastReceiver {
    public final int NOTIF_ID_RELEASE = 21;
    int notifId = 3;
    int maxNotif = 3;
    private List<ResultMovie> resultMovies = new ArrayList<>();

    public ReleaseTodayReminders() {

    }
    @Override
    public void onReceive(Context context, Intent intent) {
        getUpComingMovie(context);
    }

    public void setReminderAlarm(Context context, String time, String message){
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReleaseTodayReminders.class);
        intent.putExtra(EXTRA_MESSAGE_RECEIVE,message);
        String timeArray[] = time.split(":");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND,0);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, NOTIF_ID_RELEASE,intent,0);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        Toast.makeText(context, context.getString(R.string.toast_reminder_created), Toast.LENGTH_SHORT).show();
    }

    public void cancelAlarm(Context context){
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReleaseTodayReminders.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, NOTIF_ID_RELEASE,intent,0);
        alarmManager.cancel(pendingIntent);
        Toast.makeText(context, context.getString(R.string.delete_reminder), Toast.LENGTH_SHORT).show();
    }

    private void getUpComingMovie(final Context context){
        Client.getInstanceRetrofit().getReleaseToday().enqueue(new Callback<ModelMovie>() {
            @Override
            public void onResponse(@NotNull Call<ModelMovie> call, @NotNull Response<ModelMovie> response) {
                if ((response.body() != null ? response.body().getResultMovies() : null) != null){
                    ModelMovie responseMovie = response.body();
                    assert responseMovie != null;
                    resultMovies.addAll(responseMovie.getResultMovies());

                    showAlarmNotification(context, resultMovies);

                }else {
                    Toast.makeText(context, "No Data", Toast.LENGTH_SHORT).show();
                    Log.d("getUpCommingMovie", "onFailure: ");
                }
            }

            @Override
            public void onFailure(@NotNull Call<ModelMovie> call, @NotNull Throwable t) {
                Toast.makeText(context, "No Data", Toast.LENGTH_SHORT).show();
                Log.d("getUpCommingMovie", "onFailure: " + t.toString());
            }
        });
    }

        private void showAlarmNotification(Context context, List<ResultMovie> item) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(
                    Context.NOTIFICATION_SERVICE);
            Intent i = new Intent(context, DetailActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            Intent intent = new Intent(context, UpcomingActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, NOTIF_ID_RELEASE, i,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            PendingIntent pendingSummary = PendingIntent.getActivity(context, NOTIF_ID_RELEASE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder mBuilder;

            i.putExtra(INTENT_ID, item.get(0).getId());
            i.putExtra(INTENT_ORIGINAL_TITLE, item.get(0).getOriginalTitle());
            i.putExtra(INTENT_POSTER_PATH, BuildConfig.IMAGE + item.get(0).getPosterPath());
            i.putExtra(INTENT_OVERVIEW, item.get(0).getOverview());
            i.putExtra(INTENT_RELEASE_DATE, item.get(0).getReleaseDate());
            i.putExtra(INTENT_ORIGINAL_LANGUAGE, item.get(0).getOriginalLanguage());
            i.putExtra(INTENT_VOTE_COUNT, item.get(0).getVoteCount());
            i.putExtra(INTENT_VOTE_AVERAGE, item.get(0).getVoteAverage());
            i.putExtra(INTENT_POPULARITY, item.get(0).getPopularity());

            Uri alarmRingtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            if (notifId < maxNotif) {
                    mBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle(item.get(0).getOriginalTitle())
                            .setContentText(context.getString(R.string.upcoming_reminder_message) + item.get(0).getReleaseDate())
                            .setContentIntent(pendingIntent)
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setAutoCancel(true)
                            .setGroup(GROUP_KEY_EMAILS)
                            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                            .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                            .setSound(alarmRingtone);
            } else {
                NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle()
                        .addLine(context.getString(R.string.release_today) + resultMovies.get(notifId).getOriginalTitle())
                        .addLine(context.getString(R.string.release_today) + resultMovies.get(notifId - 1).getOriginalTitle())
                        .addLine(context.getString(R.string.release_today) + resultMovies.get(notifId - 2).getOriginalTitle())
                        .addLine(context.getString(R.string.release_today) + resultMovies.get(notifId - 3).getOriginalTitle())
                        .addLine(context.getString(R.string.show_more))
                        .setBigContentTitle(resultMovies.size() + context.getString(R.string.movie_release))
                        .setSummaryText(context.getString(R.string.app_name));
                mBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(item.get(notifId).getOriginalTitle())
                        .setContentText(context.getString(R.string.upcoming_reminder_message) + item.get(notifId).getReleaseDate())
                        .setContentIntent(pendingSummary)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setAutoCancel(true)
                        .setGroup(GROUP_KEY_EMAILS)
                        .setGroupSummary(true)
                        .setStyle(inboxStyle)
                        .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                        .setSound(alarmRingtone);
            }

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.WHITE);
                notificationChannel.enableVibration(true);
                notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

                mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
                if (notificationManager != null) {
                    notificationManager.createNotificationChannel(notificationChannel);
                }
            }
            Notification notification = mBuilder.build();
            if (notificationManager != null) {
                notificationManager.notify(notifId, notification);
            }
        }
    }

