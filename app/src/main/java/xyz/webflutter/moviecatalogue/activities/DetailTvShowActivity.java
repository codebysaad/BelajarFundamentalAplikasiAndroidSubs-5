package xyz.webflutter.moviecatalogue.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sackcentury.shinebuttonlib.ShineButton;

import java.util.Objects;

import xyz.webflutter.moviecatalogue.R;
import xyz.webflutter.moviecatalogue.helper.database.tv.TvShowDbHelper;
import xyz.webflutter.moviecatalogue.models.ResultTvShow;

public class DetailTvShowActivity extends AppCompatActivity {
    public static final String EXTRA_TV_SHOW = "tv_show_catalogue";
    private String strName, strOverview, strLanguage, strVoteAverage, strVoteCount, strPopularity, strThumbnail;
    private TextView tvName;
    private TextView tvOverview;
    private TextView tvLanguages;
    private TextView tvVoteAverage;
    private TextView tvVoteCount;
    private TextView tvPopularity;
    private TextView tvOverviewText;
    private TextView detailViewTvShow;
    private ImageView ivThumbnail;
    private CardView cvOverviewTvShow;
    private CardView cvDetailTvShow;
    private ResultTvShow modelTvShow;
    private ShineButton btnFavShow;
    private TvShowDbHelper helper;
    private RatingBar ratingBar;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tv_show);
        initView();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.show();
        modelTvShow = getIntent().getParcelableExtra(EXTRA_TV_SHOW);
        helper = TvShowDbHelper.getTvShowDbHelper(getApplicationContext());
        getData();
        initializeAnimation();
        if (helper.tvShowDao().selectDetailTvShow(String.valueOf(modelTvShow.getId())) != null) {
            btnFavShow.setChecked(true);
        } else {
            btnFavShow.setChecked(false);
        }
        btnFavShow.setOnCheckStateChangeListener(new ShineButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(View view, boolean checked) {
                if (checked){
                    helper = TvShowDbHelper.getTvShowDbHelper(getApplicationContext());
                    insertTvShow(modelTvShow);
                }else {
                    helper = TvShowDbHelper.getTvShowDbHelper(getApplicationContext());
                    helper.tvShowDao().deleteTvShow(modelTvShow);
                    btnFavShow.setEnabled(false);
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            progressDialog.dismiss();
        }
    }

    private void initView() {
        tvName = findViewById(R.id.name_tv_show_detail);
        tvOverview = findViewById(R.id.overview_tv_show_detail);
        tvLanguages = findViewById(R.id.language_tv_show);
        tvVoteAverage = findViewById(R.id.vote_average_tv_show_detail);
        tvVoteCount = findViewById(R.id.vote_count_tv_show_detail);
        tvOverviewText = findViewById(R.id.overview_tv_show);
        detailViewTvShow = findViewById(R.id.detail_view_tv_show);
        tvPopularity = findViewById(R.id.popularity_tv_show_detail);
        ratingBar = findViewById(R.id.vote_rating_tv_show);
        ivThumbnail = findViewById(R.id.thumbnail_tv_show_detail);
        cvOverviewTvShow = findViewById(R.id.cv_overview_tv_show);
        cvDetailTvShow = findViewById(R.id.cv_detail_tv_show);
        btnFavShow = findViewById(R.id.btn_fav_tv_show);
    }

    private void getData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                ResultTvShow modelTvShow = getIntent().getParcelableExtra(EXTRA_TV_SHOW);
                strName = modelTvShow.getOriginalName();
                strOverview = modelTvShow.getOverview();
                strLanguage = modelTvShow.getOriginalLanguage();
                strVoteAverage = modelTvShow.getVoteAverage();
                strVoteCount = modelTvShow.getVoteCount();
                strPopularity = modelTvShow.getPopularity();
                strThumbnail = modelTvShow.getPosterPath();

                tvName.setText(strName);
                tvOverview.setText(strOverview);
                tvLanguages.setText(strLanguage);
                tvVoteAverage.setText(strVoteAverage);
                tvVoteCount.setText(strVoteCount);
                tvPopularity.setText(strPopularity);
                ratingBar.setRating(Float.parseFloat(strVoteAverage));
                Glide.with(getApplicationContext())
                        .load(strThumbnail)
                        .into(ivThumbnail);
                Objects.requireNonNull(getSupportActionBar()).setSubtitle(modelTvShow.getOriginalName());
            }
        }, 2000);
    }

    private void initializeAnimation() {
        Animation zoomingAnimation = AnimationUtils.loadAnimation(this, R.anim.zoom_animation);
        Animation swipeUp1 = AnimationUtils.loadAnimation(this, R.anim.swipe_animation1);
        Animation swipeUp2 = AnimationUtils.loadAnimation(this, R.anim.swipe_animation2);

        ivThumbnail.startAnimation(zoomingAnimation);
        tvName.startAnimation(swipeUp1);
        tvOverviewText.startAnimation(swipeUp1);
        detailViewTvShow.startAnimation(swipeUp1);
        cvOverviewTvShow.startAnimation(swipeUp2);
        cvDetailTvShow.startAnimation(swipeUp2);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @SuppressLint("StaticFieldLeak")
    private void insertTvShow(final ResultTvShow models){
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                return helper.tvShowDao().insertTvSHow(models);
            }

            @Override
            protected void onPostExecute(Long status) {
                Toast.makeText(DetailTvShowActivity.this, getString(R.string.add_to_favorite)+status, Toast.LENGTH_SHORT).show();
            }
        }.execute();
    }
}
