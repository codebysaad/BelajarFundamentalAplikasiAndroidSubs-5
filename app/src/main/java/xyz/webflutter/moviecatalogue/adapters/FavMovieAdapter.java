package xyz.webflutter.moviecatalogue.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import xyz.webflutter.moviecatalogue.R;
import xyz.webflutter.moviecatalogue.activities.DetailActivity;
import xyz.webflutter.moviecatalogue.helper.database.movie.MovieHelper;
import xyz.webflutter.moviecatalogue.models.ResultMovie;

import static xyz.webflutter.moviecatalogue.helper.Contract.*;

public class FavMovieAdapter extends RecyclerView.Adapter<FavMovieAdapter.ViewHolderMovie> {
    private ArrayList<ResultMovie> favModels = new ArrayList<>();
    private Context context;

    public FavMovieAdapter(Context context){
        this.context = context;
    }

    public ArrayList<ResultMovie> getListNotes() {
        return favModels;
    }

    public void setListNotes(ArrayList<ResultMovie> listNotes) {
        this.favModels.clear();
        this.favModels.addAll(listNotes);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FavMovieAdapter.ViewHolderMovie onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_show_movie, parent, false);
        return new ViewHolderMovie(v);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull final FavMovieAdapter.ViewHolderMovie holder, final int position) {
        holder.tvTitle.setText(favModels.get(position).getOriginalTitle());
        @SuppressLint("SimpleDateFormat") DateFormat formatIn = new SimpleDateFormat(context.getString(R.string.FORMAT_IN));
        @SuppressLint("SimpleDateFormat") DateFormat formatOut = new SimpleDateFormat(context.getString(R.string.FORMAT_OUT));
        String inputDate = favModels.get(position).getReleaseDate();
        Date date = null;
        try {
            date = formatIn.parse(inputDate);
        }catch (ParseException e){
            e.printStackTrace();
        }
        String outputDate = formatOut.format(date);
        holder.tvReleaseDate.setText(outputDate);
        holder.ratingBarMovie.setRating(Float.parseFloat(favModels.get(position).getVoteAverage()));

        Glide.with(context)
                .load(favModels.get(position).getPosterPath())
                .into(holder.ivPoster);
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra(INTENT_ID, favModels.get(position).getId());
                intent.putExtra(INTENT_POSTER_PATH, favModels.get(position).getPosterPath());
                intent.putExtra(INTENT_ORIGINAL_TITLE, favModels.get(position).getOriginalTitle());
                intent.putExtra(INTENT_OVERVIEW, favModels.get(position).getOverview());
                intent.putExtra(INTENT_RELEASE_DATE, favModels.get(position).getReleaseDate());
                intent.putExtra(INTENT_VOTE_COUNT, favModels.get(position).getVoteCount());
                intent.putExtra(INTENT_VOTE_AVERAGE, favModels.get(position).getVoteAverage());
                intent.putExtra(INTENT_POPULARITY, favModels.get(position).getPopularity());
                intent.putExtra(INTENT_ORIGINAL_LANGUAGE, favModels.get(position).getOriginalLanguage());
                context.startActivity(intent);
            }
        });
        holder.constraintLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                CharSequence[] choice = {context.getString(R.string.yes), context.getString(R.string.no)};
                AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext())
                        .setTitle(context.getString(R.string.message_del_favorite))
                        .setItems(choice, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case 0:
                                        deleteMovie(position);
                                        break;

                                    case 1:
                                        dialog.cancel();
                                        break;
                                }
                            }
                        });
                dialog.create();
                dialog.show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return favModels.size();
    }

    class ViewHolderMovie extends RecyclerView.ViewHolder {
        private final TextView tvTitle;
        private final TextView tvReleaseDate;
        private RatingBar ratingBarMovie;
        private final ImageView ivPoster;
        private ConstraintLayout constraintLayout;
        ViewHolderMovie(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.title_film_home);
            tvReleaseDate = itemView.findViewById(R.id.release_film_home);
            ratingBarMovie = itemView.findViewById(R.id.rating_movie_home);
            ivPoster = itemView.findViewById(R.id.poster_home);
            constraintLayout = itemView.findViewById(R.id.const_movie);
        }
    }

    private void deleteMovie(int position){
        MovieHelper helper = MovieHelper.getInstance(context);
        helper.open();
        helper.delete(favModels.get(position).getId());
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, favModels.size());
        favModels.remove(position);
        Toast.makeText(context, context.getString(R.string.favorite_deleted), Toast.LENGTH_SHORT).show();
    }
}
