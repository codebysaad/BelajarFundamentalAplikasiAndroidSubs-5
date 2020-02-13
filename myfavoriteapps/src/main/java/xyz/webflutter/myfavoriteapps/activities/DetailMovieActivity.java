package xyz.webflutter.myfavoriteapps.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import xyz.webflutter.myfavoriteapps.R;

import static xyz.webflutter.myfavoriteapps.database.Contract.*;

public class DetailMovieActivity extends AppCompatActivity {

    private TextView title;
    private TextView overview;
    private TextView releasedDate;
    private TextView voteCount;
    private TextView popularity;
    private TextView language;
    private TextView voteAverage;
    private String strTitle, strOverview, strReleaseDate, strVoteCount, strVoteAverage, strPopularity, strLanguage, strPoster;
    private ImageView poster;
    private RatingBar ratingBar;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.show();
        getItem();
        getView();
    }

    private void getItem() {
        title = findViewById(R.id.title_film);
        overview = findViewById(R.id.overview_film);
        releasedDate = findViewById(R.id.date_film);
        language = findViewById(R.id.language);
        voteCount = findViewById(R.id.vote_count);
        voteAverage = findViewById(R.id.vote_average);
        popularity = findViewById(R.id.popularity_film);
        poster = findViewById(R.id.poster);
        ratingBar = findViewById(R.id.vote_rating);
    }

    private void getView() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                Intent intent = getIntent();
                strTitle = intent.getStringExtra(INTENT_ORIGINAL_TITLE);
                strOverview = intent.getStringExtra(INTENT_OVERVIEW);
                strReleaseDate = intent.getStringExtra(INTENT_RELEASE_DATE);
                strLanguage = intent.getStringExtra(INTENT_ORIGINAL_LANGUAGE);
                strVoteAverage = intent.getStringExtra(INTENT_VOTE_AVERAGE);
                strVoteCount = intent.getStringExtra(INTENT_VOTE_COUNT);
                strPopularity = intent.getStringExtra(INTENT_POPULARITY);
                strPoster = intent.getStringExtra(INTENT_POSTER_PATH);

                title.setText(strTitle);
                overview.setText(strOverview);
                @SuppressLint("SimpleDateFormat") DateFormat formatIn = new SimpleDateFormat(getString(R.string.FORMAT_IN));
                @SuppressLint("SimpleDateFormat") DateFormat formatOut = new SimpleDateFormat(getString(R.string.FORMAT_OUT));

                Date date = null;
                try {
                    date = formatIn.parse(strReleaseDate);
                }catch (ParseException e){
                    e.printStackTrace();
                }
                String outputDate = formatOut.format(Objects.requireNonNull(date));
                releasedDate.setText(outputDate);
                language.setText(strLanguage);
                voteAverage.setText(strVoteAverage);
                voteCount.setText(strVoteCount);
                popularity.setText(strPopularity);
                ratingBar.setRating(Float.parseFloat(strVoteAverage));
                Glide.with(getApplicationContext())
                        .load(strPoster)
                        .into(poster);
                Objects.requireNonNull(getSupportActionBar()).setSubtitle(strTitle);
            }
        }, 2000);
    }
}
