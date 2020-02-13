package xyz.webflutter.moviecatalogue.models;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

import xyz.webflutter.moviecatalogue.helper.Contract;

import static xyz.webflutter.moviecatalogue.helper.Contract.MovieColumns.*;


@Entity(tableName = "tbMovie")
public class ResultMovie implements Parcelable {
    @SerializedName("id")
    public int id;
    @ColumnInfo(name = "title")
    @SerializedName("title")
    private String title;
    @ColumnInfo(name = "original_title")
    @SerializedName("original_title")
    private String originalTitle;
    @ColumnInfo(name = "vote_count")
    @SerializedName("vote_count")
    private String voteCount;
    @SerializedName("vote_average")
    @ColumnInfo(name = "vote_average")
    private String voteAverage;
    @ColumnInfo(name = "original_language")
    @SerializedName("original_language")
    private String originalLanguage;
    @ColumnInfo(name = "poster_path")
    @SerializedName("poster_path")
    private String posterPath;
    @ColumnInfo(name = "popularity")
    @SerializedName("popularity")
    private String popularity;
    @ColumnInfo(name = "overview")
    @SerializedName("overview")
    private String overview;
    @ColumnInfo(name = "release_date")
    @SerializedName("release_date")
    private String releaseDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(String voteCount) {
        this.voteCount = voteCount;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public static Creator<ResultMovie> getCREATOR() {
        return CREATOR;
    }

    public ResultMovie(){}

    public ResultMovie(Cursor cursor) {
        this.id = Contract.getColomnInt(cursor, ID);
        this.originalTitle = Contract.getColomnString(cursor, ORIGINAL_TITLE);
        this.voteCount = Contract.getColomnString(cursor, VOTE_COUNT);
        this.voteAverage = Contract.getColomnString(cursor, VOTE_AVERAGE);
        this.originalLanguage = Contract.getColomnString(cursor, ORIGINAL_LANGUAGE);
        this.popularity = Contract.getColomnString(cursor, POPULARITY);
        this.posterPath = Contract.getColomnString(cursor, POSTER_PATH);
        this.overview = Contract.getColomnString(cursor, OVERVIEW);
        this.releaseDate = Contract.getColomnString(cursor, RELEASE_DATE);
    }

    public ResultMovie(int id, String originalTitle, String voteCount, String voteAverage, String originalLanguage, String popularity, String posterPath, String overview, String releaseDate) {
        this.id = id;
        this.originalTitle = originalTitle;
        this.voteCount = voteCount;
        this.voteAverage = voteAverage;
        this.originalLanguage = originalLanguage;
        this.popularity = popularity;
        this.posterPath = posterPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.originalTitle);
        dest.writeString(this.voteCount);
        dest.writeString(this.voteAverage);
        dest.writeString(this.originalLanguage);
        dest.writeString(this.posterPath);
        dest.writeString(this.popularity);
        dest.writeString(this.overview);
        dest.writeString(this.releaseDate);
    }

    protected ResultMovie(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.originalTitle = in.readString();
        this.voteCount = in.readString();
        this.voteAverage = in.readString();
        this.originalLanguage = in.readString();
        this.posterPath = in.readString();
        this.popularity = in.readString();
        this.overview = in.readString();
        this.releaseDate = in.readString();
    }

    public static final Creator<ResultMovie> CREATOR = new Creator<ResultMovie>() {
        @Override
        public ResultMovie createFromParcel(Parcel source) {
            return new ResultMovie(source);
        }

        @Override
        public ResultMovie[] newArray(int size) {
            return new ResultMovie[size];
        }
    };
}
