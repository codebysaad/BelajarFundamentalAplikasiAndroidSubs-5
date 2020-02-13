package xyz.webflutter.moviecatalogue.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "tbTvShow")
public class ResultTvShow implements Parcelable {
    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerializedName("id")
    private int id;
    @ColumnInfo(name = "name")
    @SerializedName("name")
    private String name;
    @SerializedName("original_name")
    private String originalName;
    @ColumnInfo(name = "vote_count")
    @SerializedName("vote_count")
    private String voteCount;
    @ColumnInfo(name = "vote_average")
    @SerializedName("vote_average")
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
    @ColumnInfo(name = "first_air_date")
    @SerializedName("first_air_date")
    private String firstAirDate;
    @SerializedName("backdrop_path")
    private String backdropPath;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
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

    public String getFirstAirDate() {
        return firstAirDate;
    }

    public void setFirstAirDate(String firstAirDate) {
        this.firstAirDate = firstAirDate;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.originalName);
        dest.writeString(this.voteCount);
        dest.writeString(this.voteAverage);
        dest.writeString(this.originalLanguage);
        dest.writeString(this.posterPath);
        dest.writeString(this.popularity);
        dest.writeString(this.overview);
        dest.writeString(this.firstAirDate);
        dest.writeString(this.backdropPath);
    }

    public ResultTvShow() {
    }

    protected ResultTvShow(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.originalName = in.readString();
        this.voteCount = in.readString();
        this.voteAverage = in.readString();
        this.originalLanguage = in.readString();
        this.posterPath = in.readString();
        this.popularity = in.readString();
        this.overview = in.readString();
        this.firstAirDate = in.readString();
        this.backdropPath = in.readString();
    }

    public static final Creator<ResultTvShow> CREATOR = new Creator<ResultTvShow>() {
        @Override
        public ResultTvShow createFromParcel(Parcel source) {
            return new ResultTvShow(source);
        }

        @Override
        public ResultTvShow[] newArray(int size) {
            return new ResultTvShow[size];
        }
    };
}
