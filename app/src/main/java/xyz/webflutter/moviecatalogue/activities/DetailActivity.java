package xyz.webflutter.moviecatalogue.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sackcentury.shinebuttonlib.ShineButton;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import xyz.webflutter.moviecatalogue.R;
import xyz.webflutter.moviecatalogue.helper.database.movie.DatabaseMovie;
import xyz.webflutter.moviecatalogue.helper.database.movie.MovieHelper;
import xyz.webflutter.moviecatalogue.models.ResultMovie;

import static xyz.webflutter.moviecatalogue.helper.Contract.*;
import static xyz.webflutter.moviecatalogue.helper.Contract.MovieColumns.*;

public class DetailActivity extends AppCompatActivity {
    public static final String EXTRA_DATA = "movie_catalogue";
    private TextView title, overview, releasedDate, voteCount, popularity, language, voteAverage, overviewText;
    private String id, strTitle, strOverview, strReleaseDate, strVoteCount, strVoteAverage, strPopularity, strLanguage, strPoster;
    private ImageView poster;
    private RatingBar ratingBar;
    private View detailLayout;
    private CardView overviewLayout;
    private TableLayout tableLayout;
    private ProgressDialog progressDialog;
    private ShineButton favBtn;
    private SQLiteDatabase sqLiteDatabase;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.show();
        ResultMovie modelMovie = getIntent().getParcelableExtra(EXTRA_DATA);
        getItem();
        getView();
        initAnimation();
        MovieHelper helper = MovieHelper.getInstance(getApplicationContext());
        DatabaseMovie databaseMovie = new DatabaseMovie(this);
        sqLiteDatabase = databaseMovie.getWritableDatabase();
        sqLiteDatabase.isOpen();

        sharedPreferences = getApplicationContext().getSharedPreferences("ISFAVORITE", MODE_PRIVATE);
        boolean favorite = sharedPreferences.getBoolean("FAVORITE"+ id, false);
        if (favorite){
            favBtn.setChecked(true);
        } else {
            favBtn.setChecked(false);
        }
        favBtn.setOnCheckStateChangeListener(new ShineButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(View view, boolean checked) {
                SharedPreferences.Editor  edit = sharedPreferences.edit();
                if (checked) {
                    edit.putBoolean("FAVORITE"+ id,true);
                    edit.apply();
                    ContentValues values = new ContentValues();
                    values.put(ID, id);
                    values.put(ORIGINAL_TITLE, strTitle);
                    values.put(VOTE_COUNT, strVoteCount);
                    values.put(VOTE_AVERAGE, strVoteAverage);
                    values.put(ORIGINAL_LANGUAGE, strLanguage);
                    values.put(POPULARITY, strPopularity);
                    values.put(POSTER_PATH, strPoster);
                    values.put(OVERVIEW, strOverview);
                    values.put(RELEASE_DATE, strReleaseDate);
                    getContentResolver().insert(CONTENT_URI, values);
                    Toast.makeText(DetailActivity.this, getString(R.string.add_to_favorite), Toast.LENGTH_SHORT).show();
                } else {
                    edit.putBoolean("FAVORITE"+ id, false);
                    edit.apply();
                    deleteMovie();
                    favBtn.setChecked(false);
                }
            }
        });
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            progressDialog.dismiss();
        }
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
        overviewLayout = findViewById(R.id.overviewLayout);
        detailLayout = findViewById(R.id.detailLayout);
        overviewText = findViewById(R.id.overview);
        tableLayout = findViewById(R.id.table_view);
        favBtn = findViewById(R.id.btn_fav_movie);
    }

    private void getView() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();

                Intent intent = getIntent();
                id = intent.getStringExtra(INTENT_ID);
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
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String outputDate = date != null ? formatOut.format(date) : "";
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

    private void initAnimation() {
        Animation zoomingAnimation = AnimationUtils.loadAnimation(this, R.anim.zoom_animation);
        Animation swipeUp1 = AnimationUtils.loadAnimation(this, R.anim.swipe_animation1);
        Animation swipeUp2 = AnimationUtils.loadAnimation(this, R.anim.swipe_animation2);

        poster.startAnimation(zoomingAnimation);
        title.startAnimation(swipeUp1);
        overviewText.startAnimation(swipeUp1);
        overviewLayout.startAnimation(swipeUp2);
        detailLayout.startAnimation(swipeUp2);
        overview.startAnimation(swipeUp2);
        tableLayout.startAnimation(swipeUp2);
    }

    private void deleteMovie() {
        MovieHelper helper = MovieHelper.getInstance(getApplicationContext());
        helper.open();
        sqLiteDatabase.delete(TABLE_NAME, ID + " = '" + id + "'", null);
        Toast.makeText(this, getString(R.string.favorite_deleted), Toast.LENGTH_SHORT).show();
    }
}
