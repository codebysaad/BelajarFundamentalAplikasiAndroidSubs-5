package xyz.webflutter.moviecatalogue.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import xyz.webflutter.moviecatalogue.BuildConfig;
import xyz.webflutter.moviecatalogue.ItemClickSupport;
import xyz.webflutter.moviecatalogue.R;
import xyz.webflutter.moviecatalogue.adapters.AdapterMovie;
import xyz.webflutter.moviecatalogue.models.ModelMovie;
import xyz.webflutter.moviecatalogue.models.ResultMovie;
import xyz.webflutter.moviecatalogue.viewmodel.MainViewModel;

import static xyz.webflutter.moviecatalogue.helper.Contract.INTENT_ID;
import static xyz.webflutter.moviecatalogue.helper.Contract.INTENT_ORIGINAL_LANGUAGE;
import static xyz.webflutter.moviecatalogue.helper.Contract.INTENT_ORIGINAL_TITLE;
import static xyz.webflutter.moviecatalogue.helper.Contract.INTENT_OVERVIEW;
import static xyz.webflutter.moviecatalogue.helper.Contract.INTENT_POPULARITY;
import static xyz.webflutter.moviecatalogue.helper.Contract.INTENT_POSTER_PATH;
import static xyz.webflutter.moviecatalogue.helper.Contract.INTENT_RELEASE_DATE;
import static xyz.webflutter.moviecatalogue.helper.Contract.INTENT_VOTE_AVERAGE;
import static xyz.webflutter.moviecatalogue.helper.Contract.INTENT_VOTE_COUNT;

public class UpcomingActivity extends AppCompatActivity {
    private RecyclerView rvMovie;
    private ArrayList<ResultMovie> listMovie = new ArrayList<>();
    private AdapterMovie adapter;
    private ProgressBar progressBar;
    private TextView tvLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming);
        rvMovie = findViewById(R.id.rv_upcoming);
        progressBar = findViewById(R.id.progress_bar_upcoming);
        tvLoading = findViewById(R.id.tv_loading_upcoming);
        TextView noConnection = findViewById(R.id.no_connection_upcoming);
        ImageView ivyConnection = findViewById(R.id.ivy_no_connection_upcoming);
        progressBar.showContextMenu();

        if (checkInternet()) {
            MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
            viewModel.initUpcoming();
            viewModel.getUpcoming().observe(this, new Observer<ModelMovie>() {
                @Override
                public void onChanged(ModelMovie modelMovie) {
                    showLoading(false);
                    List<ResultMovie> resultMovies = modelMovie.getResultMovies();
                    listMovie.addAll(resultMovies);
                    adapter.notifyDataSetChanged();
                }
            });
        } else {
            showLoading(false);
            noConnection.setVisibility(View.VISIBLE);
            ivyConnection.setVisibility(View.VISIBLE);
        }
        initRv();
        clickMovie();
    }

    private void initRv() {
        if (adapter == null) {
            adapter = new AdapterMovie(getApplicationContext(), listMovie);
            rvMovie.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            rvMovie.setAdapter(adapter);
            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.recyclerview_animation);
            rvMovie.startAnimation(animation);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    private void clickMovie() {
        ItemClickSupport.addTo(rvMovie).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                try {
                    intent.putExtra(INTENT_ID, listMovie.get(position).getId());
                    intent.putExtra(INTENT_POSTER_PATH, BuildConfig.IMAGE + listMovie.get(position).getPosterPath());
                    intent.putExtra(INTENT_ORIGINAL_TITLE, listMovie.get(position).getOriginalTitle());
                    intent.putExtra(INTENT_OVERVIEW, listMovie.get(position).getOverview());
                    intent.putExtra(INTENT_RELEASE_DATE, listMovie.get(position).getReleaseDate());
                    intent.putExtra(INTENT_VOTE_COUNT, listMovie.get(position).getVoteCount());
                    intent.putExtra(INTENT_VOTE_AVERAGE, listMovie.get(position).getVoteAverage());
                    intent.putExtra(INTENT_POPULARITY, listMovie.get(position).getPopularity());
                    intent.putExtra(INTENT_ORIGINAL_LANGUAGE, listMovie.get(position).getOriginalLanguage());
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
            tvLoading.setVisibility(View.GONE);
        }
    }

    private boolean checkInternet() {
        boolean connectStatus = true;
        ConnectivityManager ConnectionManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = ConnectionManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected() == true) {
            connectStatus = true;
        } else {
            connectStatus = false;
        }
        return connectStatus;
    }
}
