package xyz.webflutter.myfavoriteapps.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.HandlerThread;
import android.view.View;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import xyz.webflutter.myfavoriteapps.LoadMovieCallback;
import xyz.webflutter.myfavoriteapps.adapter.AdapterMovie;
import xyz.webflutter.myfavoriteapps.R;
import xyz.webflutter.myfavoriteapps.models.ModelMovie;

import static xyz.webflutter.myfavoriteapps.MappingHelper.mapCursorToArrayList;
import static xyz.webflutter.myfavoriteapps.database.Contract.MovieColumns.CONTENT_URI;

public class MainActivity extends AppCompatActivity implements LoadMovieCallback {
    private AdapterMovie adapterMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView rvMovie = findViewById(R.id.rv_movie);

        adapterMovie = new AdapterMovie(this);
        rvMovie.setLayoutManager(new LinearLayoutManager(this));
        rvMovie.setHasFixedSize(true);
        rvMovie.setAdapter(adapterMovie);
        HandlerThread handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        DataObserver myObserver = new DataObserver(handler, this);
        getContentResolver().registerContentObserver(CONTENT_URI, true, myObserver);
        new getData(this, this).execute();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exitApps();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapterMovie.notifyDataSetChanged();
    }

    private void exitApps(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle("Exit Aplication");
        alertDialogBuilder
                .setMessage("Do you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                            finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        exitApps();
    }

    @Override
    public void postExecute(Cursor notes) {
        ArrayList<ModelMovie> listNotes = mapCursorToArrayList(notes);
        if (listNotes.size() > 0) {
            adapterMovie.setListMovies(listNotes);
        } else {
            Toast.makeText(this, "Tidak Ada data saat ini", Toast.LENGTH_SHORT).show();
            adapterMovie.setListMovies(new ArrayList<ModelMovie>());
        }
    }

    static class DataObserver extends ContentObserver {

        final Context context;

        DataObserver(Handler handler, Context context) {
            super(handler);
            this.context = context;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            new getData(context, (MainActivity) context).execute();
        }
    }
    static class getData extends AsyncTask<Void, Void, Cursor> {
        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadMovieCallback> weakCallback;


        private getData(Context context, LoadMovieCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            return weakContext.get().getContentResolver().query(CONTENT_URI, null, null, null, null);
        }

        @Override
        protected void onPostExecute(Cursor data) {
            super.onPostExecute(data);
            weakCallback.get().postExecute(data);
        }
    }
}