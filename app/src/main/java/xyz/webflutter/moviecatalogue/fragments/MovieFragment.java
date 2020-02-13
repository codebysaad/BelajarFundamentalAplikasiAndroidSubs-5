package xyz.webflutter.moviecatalogue.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import xyz.webflutter.moviecatalogue.BuildConfig;
import xyz.webflutter.moviecatalogue.ItemClickSupport;
import xyz.webflutter.moviecatalogue.activities.SearchActivityMovie;
import xyz.webflutter.moviecatalogue.models.ModelMovie;
import xyz.webflutter.moviecatalogue.viewmodel.MainViewModel;
import xyz.webflutter.moviecatalogue.R;
import xyz.webflutter.moviecatalogue.activities.DetailActivity;
import xyz.webflutter.moviecatalogue.adapters.AdapterMovie;
import xyz.webflutter.moviecatalogue.models.ResultMovie;

import static xyz.webflutter.moviecatalogue.helper.Contract.*;

public class MovieFragment extends Fragment {
    private RecyclerView rvMovie;
    private ArrayList<ResultMovie> listMovie = new ArrayList<>();
    private AdapterMovie adapter;
    private ProgressBar progressBar;
    private TextView tvLoading;

    public MovieFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvMovie = view.findViewById(R.id.rv_movie);
        progressBar = view.findViewById(R.id.progress_bar);
        tvLoading = view.findViewById(R.id.tv_loading_movie);
        TextView noConnection = view.findViewById(R.id.no_connection);
        ImageView ivyConnection = view.findViewById(R.id.ivy_no_connection);
        SearchView searchView = view.findViewById(R.id.search_movie);
        progressBar.showContextMenu();

        if (checkInternet()) {
            MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
            viewModel.initializeMovie();
            viewModel.getMoviesModel().observe(this, new Observer<ModelMovie>() {
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

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SearchActivityMovie.class));
            }
        });

    }

    private void initRv() {
        if (adapter == null) {
            adapter = new AdapterMovie(getActivity(), listMovie);
            rvMovie.setLayoutManager(new LinearLayoutManager(getActivity()));
            rvMovie.setAdapter(adapter);
            Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.recyclerview_animation);
            rvMovie.startAnimation(animation);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    private void clickMovie() {
        ItemClickSupport.addTo(rvMovie).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
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
        ConnectivityManager ConnectionManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = ConnectionManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected() == true) {
            connectStatus = true;
        } else {
            connectStatus = false;
        }
        return connectStatus;
    }
}
