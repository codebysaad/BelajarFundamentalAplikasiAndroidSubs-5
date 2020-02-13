package xyz.webflutter.moviecatalogue.fragments;

import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.HandlerThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

import xyz.webflutter.moviecatalogue.LoadCallback;
import xyz.webflutter.moviecatalogue.R;
import xyz.webflutter.moviecatalogue.activities.FavActivity;
import xyz.webflutter.moviecatalogue.adapters.FavMovieAdapter;
import xyz.webflutter.moviecatalogue.models.ResultMovie;

import static xyz.webflutter.moviecatalogue.helper.Contract.MovieColumns.*;
import static xyz.webflutter.moviecatalogue.helper.MappingHelper.mapCursorToArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavFragment extends Fragment implements LoadCallback {
    private RecyclerView rvFavMovie;
    private FavMovieAdapter movieAdapter;
    private TextView errorText;
    private static final String EXTRA_STATE = "EXTRA_STATE";

    public FavFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fav_movie, container, false);
        rvFavMovie = view.findViewById(R.id.rv_fav_movie);
        errorText = view.findViewById(R.id.error_fav_item_movie);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        HandlerThread handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        FavActivity.DataObserver myObserver = new FavActivity.DataObserver(handler, getActivity());
        Objects.requireNonNull(getActivity()).getContentResolver().registerContentObserver(CONTENT_URI, true, myObserver);
        movieAdapter = new FavMovieAdapter(getActivity());
        rvFavMovie.setAdapter(movieAdapter);
        rvFavMovie.setHasFixedSize(true);
        rvFavMovie.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (savedInstanceState == null) {
            new FavActivity.LoadNoteAsync(getActivity(), this).execute();
        } else {
            ArrayList<ResultMovie> favModels = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if (favModels != null) {
                movieAdapter.setListNotes(favModels);
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE, movieAdapter.getListNotes());
    }

    @Override
    public void preExecute() {
        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                errorText.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void postExecute(Cursor notes) {
        errorText.setVisibility(View.GONE);
        ArrayList<ResultMovie> listNotes = mapCursorToArrayList(notes);
        if (listNotes.size() > 0) {
            movieAdapter.setListNotes(listNotes);
        } else {
            movieAdapter.setListNotes(new ArrayList<ResultMovie>());
            errorText.setVisibility(View.VISIBLE);
            Toast.makeText(getActivity(), getString(R.string.nothing_data), Toast.LENGTH_SHORT).show();
        }
    }
}