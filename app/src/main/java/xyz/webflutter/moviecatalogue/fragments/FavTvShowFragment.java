package xyz.webflutter.moviecatalogue.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

import xyz.webflutter.moviecatalogue.ItemClickSupport;
import xyz.webflutter.moviecatalogue.R;
import xyz.webflutter.moviecatalogue.activities.DetailTvShowActivity;
import xyz.webflutter.moviecatalogue.adapters.FavShowAdapter;
import xyz.webflutter.moviecatalogue.helper.database.tv.TvShowDbHelper;
import xyz.webflutter.moviecatalogue.models.ResultTvShow;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavTvShowFragment extends Fragment {
    private RecyclerView rvFavTvShow;
    private ArrayList<ResultTvShow> favModels;


    public FavTvShowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_fav_tv_show, container, false);
        rvFavTvShow = view.findViewById(R.id.rv_fav_tv_show);
        TextView tvError = view.findViewById(R.id.error_fav_item_tv_show);
        favModels = new ArrayList<>();
        TvShowDbHelper helper = TvShowDbHelper.getTvShowDbHelper(getActivity());
        favModels.addAll(Arrays.asList(helper.tvShowDao().readTvShow()));
        rvFavTvShow.setHasFixedSize(true);
        if (favModels.isEmpty()){
            tvError.setVisibility(View.VISIBLE);
            Toast.makeText(getActivity(), getString(R.string.nothing_data), Toast.LENGTH_SHORT).show();
        }else {
            tvError.setVisibility(View.GONE);
            rvFavTvShow.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            FavShowAdapter adapter = new FavShowAdapter(favModels, getActivity());
            rvFavTvShow.setAdapter(adapter);
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        clickFavTvShow();
    }

    private void clickFavTvShow() {
        ItemClickSupport.addTo(rvFavTvShow).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Intent intent = new Intent(getActivity(), DetailTvShowActivity.class);
                try {
                    ResultTvShow models = new ResultTvShow();
                    models.setId(favModels.get(position).getId());
                    models.setOriginalName(favModels.get(position).getOriginalName());
                    models.setOriginalLanguage(favModels.get(position).getOriginalLanguage());
                    models.setFirstAirDate(favModels.get(position).getFirstAirDate());
                    models.setOverview(favModels.get(position).getOverview());
                    models.setPopularity(favModels.get(position).getPopularity());
                    models.setVoteAverage(favModels.get(position).getVoteAverage());
                    models.setVoteCount(favModels.get(position).getVoteCount());
                    models.setPosterPath(favModels.get(position).getPosterPath());
                    intent.putExtra(DetailTvShowActivity.EXTRA_TV_SHOW, models);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
