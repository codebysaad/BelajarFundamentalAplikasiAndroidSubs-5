package xyz.webflutter.moviecatalogue.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xyz.webflutter.moviecatalogue.models.ModelMovie;
import xyz.webflutter.moviecatalogue.rest.ApiServices;
import xyz.webflutter.moviecatalogue.rest.Client;

class Upcoming {
        private static Upcoming upcoming;
        private ApiServices apiServices;

        static Upcoming getInstance(){
            if (upcoming ==  null){
                upcoming = new Upcoming();
            }
            return upcoming;
        }
        private Upcoming(){
            apiServices = Client.getInstanceRetrofit();
        }

        MutableLiveData<ModelMovie> getUpcoming(){
            final MutableLiveData<ModelMovie> movieData = new MutableLiveData<>();
            apiServices.getReleaseToday().enqueue(new Callback<ModelMovie>() {
                @Override
                public void onResponse(@NonNull Call<ModelMovie> call, @NonNull Response<ModelMovie> response) {
                    if ((response.body() != null ? response.body().getPage() : 0) >0){
                        movieData.setValue(response.body());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ModelMovie> call, @NonNull Throwable t) {
                    movieData.setValue(null);
                }
            });
            return movieData;
        }
}
