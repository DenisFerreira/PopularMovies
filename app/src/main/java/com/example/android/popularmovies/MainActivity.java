package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmovies.data.Movie;
import com.example.android.popularmovies.utilities.MoviesJsonUtils;
import com.example.android.popularmovies.utilities.NetworkUtils;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements FetchMovieTask.OnTaskCompleted {

    @Override
    public void onTaskStart() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onTaskCompleted(Movie[] result) {
        mProgressBar.setVisibility(View.INVISIBLE);
        if (result != null) {
            showDataView();
            mAdapter.setMovieData(result);
        } else {
            showErrorView();
        }

    }

    private RecyclerView mRecyclerView;
    private TextView mErrorMessageDisplay;
    private ProgressBar mProgressBar;
    private MovieAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.rc_movie_grid);
        mErrorMessageDisplay  = (TextView) findViewById(R.id.tv_error_message);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        GridLayoutManager layoutManager = new GridLayoutManager(MainActivity.this, numberOfColumns());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new MovieAdapter();
        mRecyclerView.setAdapter(mAdapter);
        loadMovieData(R.id.action_sort_most_popular);
    }

    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int widthDivider = 500;
        int nColumns = width / widthDivider;
        if (nColumns < 2) return 2;
        return nColumns;
    }

    private void showDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorView() {
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sort_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        loadMovieData(id);
        return super.onOptionsItemSelected(item);
    }

    private void loadMovieData(int id){
        if(id == R.id.action_sort_most_popular){
            setTitle(getString(R.string.most_popular_movies));
            new FetchMovieTask(this).execute(NetworkUtils.buildUrlMostPopular(getBaseContext()));
        }
        else if(id == R.id.action_sort_top_rated){
            setTitle((getString(R.string.top_rated_movies)));
            new FetchMovieTask(this).execute(NetworkUtils.buildUrlTopRated(getBaseContext()));
        }

    }
}
