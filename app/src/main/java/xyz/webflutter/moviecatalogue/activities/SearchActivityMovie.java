package xyz.webflutter.moviecatalogue.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import xyz.webflutter.moviecatalogue.adapters.AdapterMovie;
import xyz.webflutter.moviecatalogue.models.*;
import xyz.webflutter.moviecatalogue.rest.Client;

import static xyz.webflutter.moviecatalogue.helper.Contract.*;

public class SearchActivityMovie extends AppCompatActivity {
    private AdapterMovie adapterMovie;
    private ArrayList<ResultMovie> resultMovies = new ArrayList<>();
    private RecyclerView rvSearchAct;
    private ProgressBar pbSearchMovie;
    private SearchView searchViewMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_movie);
        rvSearchAct = findViewById(R.id.rv_search_movie);
        pbSearchMovie = findViewById(R.id.pb_search_movie);
        Toolbar toolbar = findViewById(R.id.toolbar_movie);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        searchViewMovie = findViewById(R.id.sv_movie_act);

        searchViewMovie.setIconified(false);
        searchViewMovie.clearFocus();
        searchViewMovie.setQueryHint(getString(R.string.search_movie_here));
        searchViewMovie.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(getApplicationContext(),query, Toast.LENGTH_SHORT).show();

                searchViewMovie.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                rvSearchAct.setVisibility(View.GONE);
                showLoading(true);
                Client.getInstanceRetrofit().searchMovie(newText).enqueue(new Callback<ModelMovie>() {
                    @Override
                    public void onResponse(@NotNull Call<ModelMovie> call, @NotNull Response<ModelMovie> response) {
                        showLoading(false);
                        rvSearchAct.setVisibility(View.VISIBLE);
                        if ((response.body() != null ? response.body().getResultMovies() : null) != null){
                            ModelMovie modelMovie = response.body();
                            resultMovies = modelMovie.getResultMovies();
                            adapterMovie = new AdapterMovie(SearchActivityMovie.this, resultMovies);
                            rvSearchAct.setAdapter(adapterMovie);
                        }else{
                            showLoading(false);
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ModelMovie> call, @NotNull Throwable t) {
                        showLoading(false);
                        Toast.makeText(SearchActivityMovie.this, getString(R.string.fail_find_movie), Toast.LENGTH_SHORT).show();
                    }
                });
                return false;
            }
        });
        initRvSearch();
        clickMovie();
    }

    private void showLoading(Boolean state) {
        if (state) {
            pbSearchMovie.setVisibility(View.VISIBLE);
        } else {
            pbSearchMovie.setVisibility(View.GONE);
        }
    }

    private void initRvSearch(){
        if (adapterMovie == null){
            adapterMovie = new AdapterMovie(this, resultMovies);
            rvSearchAct.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            rvSearchAct.setAdapter(adapterMovie);
            rvSearchAct.setItemAnimator(new DefaultItemAnimator());
            rvSearchAct.setNestedScrollingEnabled(true);
        }else{
            adapterMovie.notifyDataSetChanged();
        }
    }

    private void clickMovie(){
        ItemClickSupport.addTo(rvSearchAct).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                try {
                    intent.putExtra(INTENT_ID, resultMovies.get(position).getId());
                    intent.putExtra(INTENT_POSTER_PATH, BuildConfig.IMAGE + resultMovies.get(position).getPosterPath());
                    intent.putExtra(INTENT_ORIGINAL_TITLE, resultMovies.get(position).getOriginalTitle());
                    intent.putExtra(INTENT_OVERVIEW, resultMovies.get(position).getOverview());
                    intent.putExtra(INTENT_RELEASE_DATE, resultMovies.get(position).getReleaseDate());
                    intent.putExtra(INTENT_VOTE_COUNT, resultMovies.get(position).getVoteCount());
                    intent.putExtra(INTENT_VOTE_AVERAGE, resultMovies.get(position).getVoteAverage());
                    intent.putExtra(INTENT_POPULARITY, resultMovies.get(position).getPopularity());
                    intent.putExtra(INTENT_ORIGINAL_LANGUAGE, resultMovies.get(position).getOriginalLanguage());
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
