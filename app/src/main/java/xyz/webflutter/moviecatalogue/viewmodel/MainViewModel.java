package xyz.webflutter.moviecatalogue.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import xyz.webflutter.moviecatalogue.models.ModelMovie;
import xyz.webflutter.moviecatalogue.models.ModelTvShow;

public class MainViewModel extends ViewModel {
    private MutableLiveData<ModelMovie> responseMovie;
    private MutableLiveData<ModelTvShow> responseTvShow;
    private MutableLiveData<ModelMovie> upcoming;

    public void initializeMovie(){
        if (responseMovie != null){
            return;
        }
        Movie movie = Movie.getInstance();
        responseMovie = movie.getMovies();
    }

    public LiveData<ModelMovie> getMoviesModel(){
        return responseMovie;
    }

    public void initializeTvShow(){
        if (responseTvShow != null){
            return;
        }
        TvShow tvShow = TvShow.getInstanceShow();
        responseTvShow = tvShow.getDataTv();
    }

    public LiveData<ModelTvShow> getTvShowModel(){
        return responseTvShow;
    }

    public void initUpcoming(){
        if (upcoming != null){
            return;
        }
        Upcoming upcomingMovie = Upcoming.getInstance();
        upcoming = upcomingMovie.getUpcoming();
    }

    public LiveData<ModelMovie> getUpcoming(){
        return upcoming;
    }
}
