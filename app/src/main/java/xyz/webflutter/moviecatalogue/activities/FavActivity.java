package xyz.webflutter.moviecatalogue.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.material.tabs.TabLayout;

import java.lang.ref.WeakReference;

import xyz.webflutter.moviecatalogue.LoadCallback;
import xyz.webflutter.moviecatalogue.R;
import xyz.webflutter.moviecatalogue.adapters.FragmentPageAdapter;

import static xyz.webflutter.moviecatalogue.helper.Contract.MovieColumns.CONTENT_URI;

public class FavActivity extends AppCompatActivity implements LoadCallback {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    ViewPager viewPager;
    FragmentPageAdapter pageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav);
        toolbar = findViewById(R.id.act_bar_fav);
        toolbar.setTitle(getResources().getString(R.string.favorite));
        setSupportActionBar(toolbar);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);

        pageAdapter = new FragmentPageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0) {
                    toolbar.setBackgroundColor(ContextCompat.getColor(FavActivity.this,
                            R.color.colorPrimary));
                    tabLayout.setBackgroundColor(ContextCompat.getColor(FavActivity.this,
                            R.color.colorPrimary));
                } else {
                    toolbar.setBackgroundColor(ContextCompat.getColor(FavActivity.this,
                            R.color.green_accent));
                    tabLayout.setBackgroundColor(ContextCompat.getColor(FavActivity.this,
                            R.color.green_accent));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void preExecute() {

    }

    @Override
    public void postExecute(Cursor movies) {

    }

    public static class LoadNoteAsync extends AsyncTask<Void, Void, Cursor> {

        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadCallback> weakCallback;

        public LoadNoteAsync(Context context, LoadCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            Context context = weakContext.get();
            return context.getContentResolver().query(CONTENT_URI, null, null, null, null);
        }

        @Override
        protected void onPostExecute(Cursor movies) {
            super.onPostExecute(movies);
            weakCallback.get().postExecute(movies);
        }
    }
    public static class DataObserver extends ContentObserver {

        final Context context;

        public DataObserver(Handler handler, Context context) {
            super(handler);
            this.context = context;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            new LoadNoteAsync(context, (FavActivity) context).execute();

        }
    }
}