package xyz.webflutter.moviecatalogue.helper.database.tv;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import xyz.webflutter.moviecatalogue.helper.local.TvShowDao;
import xyz.webflutter.moviecatalogue.models.ResultTvShow;

@Database(entities = {ResultTvShow.class}, version = 1)
public abstract class TvShowDbHelper extends RoomDatabase {
    public abstract TvShowDao tvShowDao();
    private static TvShowDbHelper tvShowDbHelper;

    public static  TvShowDbHelper getTvShowDbHelper(Context context){
        synchronized (TvShowDbHelper.class){
            if (tvShowDbHelper == null){
                tvShowDbHelper = Room.databaseBuilder(
                        context, TvShowDbHelper.class,
                        "dbTv"
                ).allowMainThreadQueries().build();
            }
        }return tvShowDbHelper;
    }
}
