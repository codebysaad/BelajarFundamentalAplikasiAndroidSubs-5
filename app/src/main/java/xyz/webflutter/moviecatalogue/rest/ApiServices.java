package xyz.webflutter.moviecatalogue.rest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import xyz.webflutter.moviecatalogue.BuildConfig;
import xyz.webflutter.moviecatalogue.models.ModelMovie;
import xyz.webflutter.moviecatalogue.models.ModelTvShow;

public interface ApiServices {
    @GET("discover/movie?api_key=" + BuildConfig.API_KEY + "&language=en-US")
    Call<ModelMovie> getDataMovie();

    @GET("discover/tv?api_key=" + BuildConfig.API_KEY + "&language=en-US")
    Call<ModelTvShow> getTvShowData();

    @GET("search/movie?api_key=" + BuildConfig.API_KEY + "&language=en-US")
    Call<ModelMovie> searchMovie(@Query("query") String movie);

    @GET("search/tv?api_key=" + BuildConfig.API_KEY + "&language=en-US")
    Call<ModelTvShow> searchTvShow(@Query("query") String tvshow);

    @GET("discover/movie?api_key=" + BuildConfig.API_KEY + "&primary_release_date.gte=2019-09-05&primary_release_date.lte=2019-09-05")
    Call<ModelMovie> getReleaseToday();
}