package xyz.webflutter.moviecatalogue.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ModelMovie {
    @SerializedName("page")
    private int page;
    @SerializedName("total_results")
    private int total_result;
    @SerializedName("total_pages")
    private int total_pages;
    @SerializedName("results")
    private ArrayList<ResultMovie> resultMovies;

    public int getPage() {
        return page;
    }

    public ArrayList<ResultMovie> getResultMovies() {
        return resultMovies;
    }
}