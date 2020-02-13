package xyz.webflutter.moviecatalogue.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public final class Contract {
    public static final String AUTHORITY = "xyz.webflutter.moviecatalogue";
    private static final String SCHEME = "content";

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

    private final static String PREF_NAME = "reminderMoviePreferences";
    private final static String KEY_FIELD_UPCOMING_REMINDER = "checkedPopular";
    private final static String KEY_FIELD_DAILY_REMINDER = "checkedDaily";
    public static final String TYPE_REPEATING = "DailyReminders";
    public static final String EXTRA_MESSAGE = "message";
    public static final String EXTRA_MESSAGE_RECEIVE = "messageUpcoming";
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    public final static String GROUP_KEY_EMAILS = "group_key";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    public Contract(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
    }

    public void setUpcomingStatus(Boolean status) {
        editor.putBoolean(KEY_FIELD_UPCOMING_REMINDER, status);
        editor.commit();
    }

    public void setDailyStatus(Boolean status) {
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
