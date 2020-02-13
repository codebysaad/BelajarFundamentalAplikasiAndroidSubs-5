package xyz.webflutter.myfavoriteapps.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import xyz.webflutter.myfavoriteapps.R;
import xyz.webflutter.myfavoriteapps.activities.DetailMovieActivity;
import xyz.webflutter.myfavoriteapps.models.ModelMovie;

import static xyz.webflutter.myfavoriteapps.database.Contract.*;

public class AdapterMovie extends RecyclerView.Adapter<AdapterMovie.ViewHolderMovie> {
    private Activity activity;
    private ArrayList<ModelMovie> listMovies = new ArrayList<>();

    public AdapterMovie(Activity activity){
        this.activity = activity;
    }

    public void setListMovies(ArrayList<ModelMovie> listMovies){
        this.listMovies.clear();
        this.listMovies.addAll(listMovies);
        notifyDataSetChanged();
    }

    public ArrayList<ModelMovie> getListMovies(){
        return listMovies;
    }

    @Override
    public AdapterMovie.ViewHolderMovie onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolderMovie(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false));
    }

    @Override
    public void onBindViewHolder(AdapterMovie.ViewHolderMovie holder, final int position) {
            holder.tvTitle.setText(getListMovies().get(position).getOriginalTitle());
            @SuppressLint("SimpleDateFormat") DateFormat formatIn = new SimpleDateFormat(activity.getString(R.string.FORMAT_IN));
            @SuppressLint("SimpleDateFormat") DateFormat formatOut = new SimpleDateFormat(activity.getString(R.string.FORMAT_OUT));
            String inputDate = getListMovies().get(position).getReleaseDate();
            Date date = null;
            try {
                date = formatIn.parse(inputDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String outputDate = formatOut.format(Objects.requireNonNull(date));
            holder.tvReleaseDate.setText(outputDate);
            holder.ratingBarMovie.setRating(Float.parseFloat(getListMovies().get(position).getVoteAverage()));

            Glide.with(activity)
                    .load(getListMovies().get(position).getPosterPath())
                    .into(holder.ivPoster);
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, DetailMovieActivity.class);
                intent.putExtra(INTENT_ID, listMovies.get(position).getId());
                intent.putExtra(INTENT_POSTER_PATH, listMovies.get(position).getPosterPath());
                intent.putExtra(INTENT_ORIGINAL_TITLE, listMovies.get(position).getOriginalTitle());
                intent.putExtra(INTENT_OVERVIEW, listMovies.get(position).getOverview());
                intent.putExtra(INTENT_RELEASE_DATE, listMovies.get(position).getReleaseDate());
                intent.putExtra(INTENT_VOTE_COUNT, listMovies.get(position).getVoteCount());
                intent.putExtra(INTENT_VOTE_AVERAGE, listMovies.get(position).getVoteAverage());
                intent.putExtra(INTENT_POPULARITY, listMovies.get(position).getPopularity());
                intent.putExtra(INTENT_ORIGINAL_LANGUAGE, listMovies.get(position).getOriginalLanguage());
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listMovies.size();
    }

    public class ViewHolderMovie extends RecyclerView.ViewHolder {
        private final TextView tvTitle;
        private final TextView tvReleaseDate;
        private RatingBar ratingBarMovie;
        private final ImageView ivPoster;
        private ConstraintLayout constraintLayout;
        public ViewHolderMovie(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.title_film_home);
            tvReleaseDate = itemView.findViewById(R.id.release_film_home);
            ratingBarMovie = itemView.findViewById(R.id.rating_movie_home);
            ivPoster = itemView.findViewById(R.id.poster_home);
            constraintLayout = itemView.findViewById(R.id.const_movie);
        }
    }
}
