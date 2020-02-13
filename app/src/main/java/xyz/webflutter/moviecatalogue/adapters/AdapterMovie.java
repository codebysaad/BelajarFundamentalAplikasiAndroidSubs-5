package xyz.webflutter.moviecatalogue.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import xyz.webflutter.moviecatalogue.BuildConfig;
import xyz.webflutter.moviecatalogue.R;
import xyz.webflutter.moviecatalogue.models.ResultMovie;

public class AdapterMovie extends RecyclerView.Adapter<AdapterMovie.AdapterViewHolder> {
    private final Context context;

    private final List<ResultMovie> list;

    public AdapterMovie(Context context, List<ResultMovie> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public AdapterMovie.AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_show_movie, parent, false);
        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterMovie.AdapterViewHolder holder, int position) {

        holder.tvTitle.setText(list.get(position).getOriginalTitle());
        @SuppressLint("SimpleDateFormat") DateFormat formatIn = new SimpleDateFormat(context.getString(R.string.FORMAT_IN));
        @SuppressLint("SimpleDateFormat") DateFormat formatOut = new SimpleDateFormat(context.getString(R.string.FORMAT_OUT));
        String inputDate = list.get(position).getReleaseDate();
        Date date = null;
        try {
            date = formatIn.parse(inputDate);
        }catch (ParseException e){
            e.printStackTrace();
        }
        String outputDate = date != null ? formatOut.format(date) : "";
        holder.tvReleaseDate.setText(outputDate);
        holder.ratingBarMovie.setRating(Float.parseFloat(list.get(position).getVoteAverage()));

        Glide.with(context)
                .load(BuildConfig.IMAGE + list.get(position).getPosterPath())
                .into(holder.ivPoster);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class AdapterViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTitle;
        private final TextView tvReleaseDate;
        private final RatingBar ratingBarMovie;
        private final ImageView ivPoster;

        AdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.title_film_home);
            tvReleaseDate = itemView.findViewById(R.id.release_film_home);
            ratingBarMovie = itemView.findViewById(R.id.rating_movie_home);
            ivPoster = itemView.findViewById(R.id.poster_home);
        }
    }
}
