package xyz.webflutter.moviecatalogue.helper.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import xyz.webflutter.moviecatalogue.models.ResultTvShow;

@Dao
public interface TvShowDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertTvSHow(ResultTvShow tvSHow);

    @Query("SELECT * FROM tbTvShow")
    ResultTvShow[] readTvShow();

    @Update
    int updateTvShow(ResultTvShow tvSHow);

    @Delete
    void deleteTvShow(ResultTvShow tvSHow);

    @Query("SELECT * FROM tbTvShow WHERE id = :id LIMIT 1")
    ResultTvShow selectDetailTvShow(String id);
}
