package xyz.webflutter.moviecatalogue.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xyz.webflutter.moviecatalogue.BuildConfig;
import xyz.webflutter.moviecatalogue.ItemClickSupport;
import xyz.webflutter.moviecatalogue.R;
import xyz.webflutter.moviecatalogue.adapters.AdapterTvShow;
import xyz.webflutter.moviecatalogue.models.*;
import xyz.webflutter.moviecatalogue.rest.Client;

public class SearchActivityTvShow extends AppCompatActivity implements SearchView.OnQueryTextListener{
    private AdapterTvShow adapterTvShow;
    private ArrayList<ResultTvShow> resultTvShows = new ArrayList<>();
    private RecyclerView rvSearchAct;
    private ProgressBar pbSearchTvShow;
    private SearchView searchViewTvShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_tv_show);
        rvSearchAct = findViewById(R.id.rv_search_tv_show);
        pbSearchTvShow = findViewById(R.id.pb_search_tv_show);
        Toolbar toolbar = findViewById(R.id.toolbar_tv_show);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        searchViewTvShow = findViewById(R.id.sv_tv_show_act);

        searchViewTvShow.setIconified(false);
        searchViewTvShow.clearFocus();
        searchViewTvShow.setQueryHint(getString(R.string.search_tv_here));
        searchViewTvShow.setOnQueryTextListener(this);
        initRvSearch();
        clickTvShow();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Toast.makeText(getApplicationContext(),query, Toast.LENGTH_SHORT).show();
        searchViewTvShow.clearFocus();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        rvSearchAct.setVisibility(View.GONE);
        showLoading(true);
        Client.getInstanceRetrofit().searchTvShow(newText).enqueue(new Callback<ModelTvShow>() {
            @Override
            public void onResponse(@NotNull Call<ModelTvShow> call, @NotNull Response<ModelTvShow> response) {
                showLoading(false);
                rvSearchAct.setVisibility(View.VISIBLE);
                if ((response.body() != null ? response.body().getResultTvShows() : null) != null){
                    ModelTvShow modelTvShow = response.body();
                    resultTvShows = modelTvShow.getResultTvShows();
                    adapterTvShow = new AdapterTvShow(SearchActivityTvShow.this, resultTvShows);
                    rvSearchAct.setAdapter(adapterTvShow);
                }else{
                    showLoading(false);
                }
            }

            @Override
            public void onFailure(@NotNull Call<ModelTvShow> call, @NotNull Throwable t) {
                showLoading(false);
                Toast.makeText(SearchActivityTvShow.this, getString(R.string.fail_find_tv), Toast.LENGTH_SHORT).show();
            }
        });
        return false;
    }

    private void showLoading(Boolean state) {
        if (state) {
            pbSearchTvShow.setVisibility(View.VISIBLE);
        } else {
            pbSearchTvShow.setVisibility(View.GONE);
        }
    }

    private void initRvSearch(){
        if (adapterTvShow == null){
            adapterTvShow = new AdapterTvShow(this, resultTvShows);
            rvSearchAct.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
            rvSearchAct.setAdapter(adapterTvShow);
            rvSearchAct.setItemAnimator(new DefaultItemAnimator());
            rvSearchAct.setNestedScrollingEnabled(true);
        }else{
            adapterTvShow.notifyDataSetChanged();
        }
    }

    public void clickTvShow(){
        ItemClickSupport.addTo(rvSearchAct).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Intent intent = new Intent(getApplicationContext(), DetailTvShowActivity.class);
                try {
                    ResultTvShow modelTvShow = new ResultTvShow();
                    modelTvShow.setId(resultTvShows.get(position).getId());
                    modelTvShow.setOriginalName(resultTvShows.get(position).getOriginalName());
                    modelTvShow.setOriginalLanguage(resultTvShows.get(position).getOriginalLanguage());
                    modelTvShow.setOverview(resultTvShows.get(position).getOverview());
                    modelTvShow.setPopularity(resultTvShows.get(position).getPopularity());
                    modelTvShow.setVoteAverage(resultTvShows.get(position).getVoteAverage());
                    modelTvShow.setVoteCount(resultTvShows.get(position).getVoteCount());
                    modelTvShow.setPosterPath(BuildConfig.IMAGE + resultTvShows.get(position).getPosterPath());
                    intent.putExtra(DetailTvShowActivity.EXTRA_TV_SHOW, modelTvShow);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
