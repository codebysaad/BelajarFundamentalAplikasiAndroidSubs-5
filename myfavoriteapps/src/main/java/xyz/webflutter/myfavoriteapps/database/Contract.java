package xyz.webflutter.myfavoriteapps.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public final class Contract {
    public static final String AUTHORITY = "xyz.webflutter.moviecatalogue";
    public static final String SCHEME = "content";

    public static final class MovieColumns implements BaseColumns {
        public static final String TABLE_NAME = "tbMovie";
        public static final String ID = "id";
        public static final String ORIGINAL_TITLE = "original_title";
        public static final String VOTE_COUNT = "vote_count";
        public static final String VOTE_AVERAGE = "vote_average";
        public static final String ORIGINAL_LANGUAGE = "original_language";
        public static final String POPULARITY = "popularity";
        public static final String POSTER_PATH = "poster_path";
        public static final String OVERVIEW = "overview";
        public static final String RELEASE_DATE = "release_date";
        public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build();
    }

    public static final String INTENT_ID = "id";
    public static final String INTENT_ORIGINAL_TITLE = "original_title";
    public static final String INTENT_VOTE_COUNT = "vote_count";
    public static final String INTENT_VOTE_AVERAGE = "vote_average";
    public static final String INTENT_ORIGINAL_LANGUAGE = "original_language";
    public static final String INTENT_POPULARITY = "popularity";
    public static final String INTENT_POSTER_PATH = "poster_path";
    public static final String INTENT_OVERVIEW = "overview";
    public static final String INTENT_RELEASE_DATE = "release_date";

    public static final String NOTIF_EXTRA_MESSAGE = "EXTRA_MESSAGE";
    public static final String NOTIF_TYPE_MESSAGE = "TYPE_MESSAGE";
    public static final String NOTIF_TYPE_REMINDER = "TYPE_REMINDER";
    public static final int NOTIF_ID_REMINDER  = 101;

    public static final String TAG_TASK_MOVIE_LOG = "Movie_Pop";


    private final static String PREF_NAME = "reminderMoviePreferences";
    private final static String KEY_REMINDER_MOVIE_TIME = "reminderTime";
    private final static String KEY_REMINDER_MOVIE_MESSAGE = "reminderMessage";
    private final static String KEY_FIELD_UPCOMING_REMINDER = "checkedPopular";
    private final static String KEY_FIELD_DAILY_REMINDER = "checkedDaily";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    public Contract(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
    }

    public void setReminderTime(String time) {
        editor.putString(KEY_REMINDER_MOVIE_TIME, time);
        editor.commit();
    }

    public String getReminderTime() {
        return sharedPreferences.getString(KEY_REMINDER_MOVIE_TIME, "");
    }

    public void setReminderMessage(String message) {
        editor.putString(KEY_REMINDER_MOVIE_MESSAGE, message);
        editor.commit();
    }

    public String getReminderMessage() {
        return sharedPreferences.getString(KEY_REMINDER_MOVIE_MESSAGE, "");
    }

    public void clear(){
        editor.clear();
        editor.commit();
    }

    public void setUpcomingStatus(Boolean status) {
        editor.putBoolean(KEY_FIELD_UPCOMING_REMINDER, status);
        editor.commit();
    }

    public void setDailytatus(Boolean status) {
        editor.putBoolean(KEY_FIELD_DAILY_REMINDER, status);
        editor.commit();
    }

    public Boolean getUpcomingStatus() {
        return sharedPreferences.getBoolean(KEY_FIELD_UPCOMING_REMINDER, false);
    }

    public Boolean getDailyStatus() {
        return sharedPreferences.getBoolean(KEY_FIELD_DAILY_REMINDER, false);
    }



    public static String getColomnString(Cursor cursor, String colomnName) {
        return cursor.getString(cursor.getColumnIndex(colomnName));
    }


    public static int getColomnInt(Cursor cursor, String colomnName) {
        return cursor.getInt(cursor.getColumnIndex(colomnName));
    }
}
