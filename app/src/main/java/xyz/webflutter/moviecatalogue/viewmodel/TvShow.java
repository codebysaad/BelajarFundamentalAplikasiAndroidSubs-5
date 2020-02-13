package xyz.webflutter.moviecatalogue.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xyz.webflutter.moviecatalogue.models.ModelTvShow;
import xyz.webflutter.moviecatalogue.rest.ApiServices;
import xyz.webflutter.moviecatalogue.rest.Client;

class TvShow {
    private static TvShow tvShowClass;
    private ApiServices apiServices;

    static TvShow getInstanceShow(){
        if (tvShowClass ==  null){
            tvShowClass = new TvShow();
        }
        return tvShowClass;
    }
    private TvShow(){
        apiServices = Client.getInstanceRetrofit();
    }

    MutableLiveData<ModelTvShow> getDataTv(){
        final MutableLiveData<ModelTvShow> tvShowData = new MutableLiveData<>();
        apiServices.getTvShowData().enqueue(new Callback<ModelTvShow>() {
            @Override
            public void onResponse(@NonNull Call<ModelTvShow> call, @NonNull Response<ModelTvShow> response) {
                if ((response.body() != null ? response.body().getPage() : 0) >0){
                    tvShowData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ModelTvShow> call, @NonNull Throwable t) {
                tvShowData.setValue(null);
            }
        });
        return tvShowData;
    }
}
