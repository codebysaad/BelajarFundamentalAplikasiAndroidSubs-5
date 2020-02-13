package xyz.webflutter.moviecatalogue.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ModelTvShow {

    @SerializedName("page")
    private int page;
    @SerializedName("total_results")
    private int total_result;
    @SerializedName("total_pages")
    private int total_pages;
    @SerializedName("results")
    private ArrayList<ResultTvShow> resultTvShows;

    public int getPage() {
        return page;
    }

    public ArrayList<ResultTvShow> getResultTvShows() {
        return resultTvShows;
    }
}